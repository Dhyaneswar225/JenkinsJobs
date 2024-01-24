package com.shutterfly.services.example.dao.impl;
import com.shutterfly.services.example.dao.api.IpifyDao;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Repository
public class IpifyDaoImpl implements IpifyDao {

    private static final String API_URL = "https://api.ipify.org";

    @Override
    public String getIpAddress() {
        try {
            // Create a RestTemplate with SSL verification disabled
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(getRequestFactoryWithDisabledSSLVerification());

            // Make the request
            return restTemplate.getForObject(API_URL, String.class);
        } catch (Exception e) {
            // Handle exceptions
            return "Error: " + e.getMessage();
        }
    }

    private ClientHttpRequestFactory getRequestFactoryWithDisabledSSLVerification() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException, IOException {
                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                    ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                }
                super.prepareConnection(connection, httpMethod);
            }
        };
    }
}
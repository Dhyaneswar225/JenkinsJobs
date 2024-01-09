package com.shutterfly.services.example.functionalTest;

import com.shutterfly.test.user.models.User;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;


import static com.shutterfly.test.matchers.HttpStatusMatchers.methodNotAllowed;
import static com.shutterfly.test.matchers.HttpStatusMatchers.notFound;
import static io.restassured.RestAssured.given;

public class SpringboardExampleServiceFullIT extends BaseSpringboardExampleServiceIT {

    private static final String SFLY_APIKEY_HEADER = "SFLY-apikey";

    @Test
    public void testRetrieveNonExistingUser() throws Exception {
        final User newUser = generateNewUser();
        final String token = generateUserToken(newUser);
        given()
                .auth().preemptive().oauth2(token)
                .header(SFLY_APIKEY_HEADER, getClientId())
                .when()
                .get(getUserUrl(), "999")
                .then()
                .statusCode(notFound());

        // Response Code: 404 - NOT FOUND - which means the path doesn't exist
        // and since for all existing users, we can append the userId to the URL
        // to get the User Information, this error implies that the User doesn't
        // exist.
    }

    @Test
    public void testDeleteUser() throws Exception {
        final User newUser = generateNewUser();
        final String newUserId = newUser.getId();
        final String token = generateUserToken(newUser);
        // Trying to delete User at a URL that only accepts GET requests.
        given()
                .auth().preemptive().oauth2(token)
                .header(SFLY_APIKEY_HEADER, getClientId())
                .when()
                .delete(getUserUrl(), newUserId)
                .then()
                .statusCode(methodNotAllowed());
    }

    @Test
    public void testPostUserData() throws Exception {
        final User newUser = generateNewUser();
        final String token = generateUserToken(newUser);
        final com.shutterfly.services.example.model.User postData = new com.shutterfly.services.example.model.User();
        postData.setUserID("000020450980");
        postData.setUserName("g");
        postData.setUserEmail("gl060502@shutterfly.com");

        //Trying to post Test User Data at a URL that only accepts GET requests.
        given()
                .auth().preemptive().oauth2(token)
                .header(SFLY_APIKEY_HEADER, getClientId())
                .contentType(ContentType.JSON)
                .body(postData)
                .when()
                .post(getBaseUrl())
                .then()
                .statusCode(methodNotAllowed());
    }
}

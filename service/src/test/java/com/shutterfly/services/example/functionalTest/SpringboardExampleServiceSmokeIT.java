package com.shutterfly.services.example.functionalTest;

import com.shutterfly.test.user.models.User;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.shutterfly.test.matchers.HttpStatusMatchers.ok;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpringboardExampleServiceSmokeIT extends BaseSpringboardExampleServiceIT {

    private static final String SFLY_APIKEY_HEADER = "SFLY-apikey";

    @Test
    public void retrieveCreatedUser() throws Exception {
        final User newUser = generateNewUser();
        final String token = generateUserToken(newUser);
        final String newUserId = newUser.getId();

        given()
                .auth().preemptive().oauth2(token).header(SFLY_APIKEY_HEADER, getClientId())
                .when()
                .get(getUserUrl(), newUserId)
                .then()
                .statusCode(ok())
                .body("userID", is(newUserId));
    }

    @Test
    public void retrieveSize() throws Exception {
        final User newUser = generateNewUser();
        final String token = generateUserToken(newUser);

        final String body = given()
                .auth().preemptive().oauth2(token).header(SFLY_APIKEY_HEADER, getClientId())
                .when()
                .get(getBaseUrl())
                .then()
                .statusCode(ok())
                .extract()
                .body().asString();
        final List<?> root = new JsonPath(body).getList("$");
        assertThat("Number of users retrieved doesn't match", root, hasSize(5));
    }

    @Test
    public void retrieveSpecificUserContents() throws Exception {
        final User newUser = generateNewUser();

        final String newUserId = newUser.getId();
        final String newUserEmail = newUser.getEmailId();
        final String newUserName = newUser.getFirstName();
        final String token = generateUserToken(newUser);

        given()
                .auth().preemptive().oauth2(token).header(SFLY_APIKEY_HEADER, getClientId())
                .when()
                .get(getUserUrl(), newUserId)
                .then()
                .statusCode(ok())
                .body("userID", is(newUserId))
                .body("userEmail", is(equalToIgnoringCase(newUserEmail)))
                .body("userName", is(newUserName));
    }
}

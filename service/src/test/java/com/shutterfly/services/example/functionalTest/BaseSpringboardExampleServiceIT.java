package com.shutterfly.services.example.functionalTest;

import com.shutterfly.test.endpoint.EndpointBaseTest;
import com.shutterfly.test.endpoint.LoadPropertiesRule;
import com.shutterfly.test.endpoint.TestValue;
import com.shutterfly.test.security.TokenService;
import com.shutterfly.test.user.UserService;
import com.shutterfly.test.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseSpringboardExampleServiceIT extends EndpointBaseTest {
    /**
     * This rule will populate the @TestValue fields with properties from
     * application.yml according to the environment.
     */
    @Rule
    public LoadPropertiesRule loadPropertiesRule =
        LoadPropertiesRule.builder()
                .target(BaseSpringboardExampleServiceIT.class)
                .build();

    @TestValue("client.id")
    private String clientId;

    @TestValue("client.secret")
    private String clientSecret;

    @TestValue("base.url")
    private String baseUrl;

    private static final String PASSWORD_TEST_USER = "Test1234";

    protected String getBaseUrl() {
        return this.baseUrl;
    }

    protected String getUserUrl() {
        return this.baseUrl + "/{userId}";
    }

    protected String getClientId() { return this.clientId; }

    protected User generateNewUser() {
        return generateNewUser(getUserService());
    }

    public static User generateNewUser(UserService userService) {
        return userService.createNewUserViaUserService("test-service-example", "shutterfoo.com");
    }

    protected String getGenerateNewUserId() {
        return generateNewUser().getId();
    }

    protected String generateUserToken(User user) {
        return generateUserToken(getTokenService(),user);
    }

    public static String generateUserToken(TokenService tokenService, User user) {
        return tokenService.generateUserToken(user.getEmailId(),PASSWORD_TEST_USER);
    }
}

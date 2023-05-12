import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import user.Credentials;
import user.User;
import user.UserData;
import user.UserMethods;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class AuthorizationTest {
    private User user;
    private UserMethods userMethods;
    private String accessToken;

    @Before
    public void setUp() {
        userMethods = new UserMethods();
        user = UserData.defaultUser();
        accessToken = userMethods.create(user).extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя с корректно заполненными данными")
    public void authorizationUserTest() {
        ValidatableResponse response = userMethods.login(Credentials.from(user));
        boolean isAuthorizationPassed = response.extract().path("success");
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_OK, actualStatus);
        assertTrue(isAuthorizationPassed);
    }


    @Test
    @DisplayName("Авторизация пользователя без некорркетно заполненным паролем")
    public void authIncorrectPasswordTest() {
        user.setPassword("MissOranjeDiscoDancer");
        ValidatableResponse response = userMethods.login(Credentials.from(user));
        boolean isAuthorizationPassed = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        String expectedMessage = "email or password are incorrect";
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, actualStatus);
        assertFalse(isAuthorizationPassed);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Авторизация пользователя без некорркетно заполненным Email")
    public void authIncorrectEmailTest() {
        user.setEmail("TequilaSunrise@ya.ru");
        ValidatableResponse response = userMethods.login(Credentials.from(user));
        boolean isAuthorizationPassed = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        String expectedMessage = "email or password are incorrect";
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED, actualStatus);
        assertFalse(isAuthorizationPassed);
        assertEquals(expectedMessage, actualMessage);
    }

}

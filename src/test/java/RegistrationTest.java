import io.qameta.allure.junit4.DisplayName;
import user.*;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class RegistrationTest {
    private User user;
    private UserMethods userMethods;
    private String accessToken;
    @Before
    public void setUp() {
        userMethods = new UserMethods();
        user = UserData.defaultUser();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userMethods.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Регистрация пользователя с корректными данными")
    public void registrationUserTest() {
        ValidatableResponse response = userMethods.create(user);
        accessToken = response.extract().path("accessToken");
        boolean isRegistrationPassed = response.extract().path("success");
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_OK, actualStatus);
        assertTrue(isRegistrationPassed);
    }


    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    public void registrationSameUserTest() {
        accessToken = userMethods.create(user).extract().path("accessToken");
        ValidatableResponse responseCreateDouble = userMethods.create(user);
        boolean isRegistrationPassed = responseCreateDouble.extract().path("success");
        int actualStatus = responseCreateDouble.extract().statusCode();
        String expectedMessage = "User already exists";
        String actualMessage = responseCreateDouble.extract().path("message");
        assertEquals(SC_FORBIDDEN, actualStatus);
        assertFalse(isRegistrationPassed);
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    @DisplayName("Регистрация пользователя без заполненного поля email")
    public void registrationWithoutEmailTest() {
        user = UserData.userWithoutEmail();
        ValidatableResponse response = userMethods.create(user);
        String expectedMessage = "Email, password and name are required fields";
        String actualMessage = response.extract().path("message");
        boolean isRegistrationPassed = response.extract().path("success");
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, actualStatus);
        assertFalse(isRegistrationPassed);
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    @DisplayName("Регистрация пользователя без заполненного поля Имя")
    public void registrationWithoutNameTest() {
        user = UserData.userWithoutName();
        ValidatableResponse response = userMethods.create(user);
        String expectedMessage = "Email, password and name are required fields";
        String actualMessage = response.extract().path("message");
        boolean isRegistrationPassed = response.extract().path("success");
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, actualStatus);
        assertFalse(isRegistrationPassed);
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    @DisplayName("Регистрация пользователя без заполненного поля Пароль")
    public void registrationWithoutPasswordTest() {
        user = UserData.userWithoutPassword();
        ValidatableResponse response = userMethods.create(user);
        String expectedMessage = "Email, password and name are required fields";
        String actualMessage = response.extract().path("message");
        boolean isRegistrationPassed = response.extract().path("success");
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN, actualStatus);
        assertFalse(isRegistrationPassed);
        assertEquals(expectedMessage, actualMessage);
    }

}
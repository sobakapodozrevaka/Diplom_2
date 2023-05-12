import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserData;
import user.UserMethods;


import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class DataUpdateTest {

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
        userMethods.delete(accessToken);
    }

    @Test
    @DisplayName("Изменение поля Email неавторизованного пользователя")
    public void updatePasswordUnauthorizedTest() {
        user.setEmail("TequilaSunrise@ya.ru");
        ValidatableResponse updateResponse = userMethods.updateUnauthorizedUser(user);
        boolean isChanged = updateResponse.extract().path("success");
        String actualMessage = updateResponse.extract().path("message");
        String expectedMessage = "You should be authorised";
        int actualStatus = updateResponse.extract().statusCode();
        assertFalse(isChanged);
        assertEquals(SC_UNAUTHORIZED, actualStatus);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Изменение поля Имя неавторизованного пользователя")
    public void updateNameUnauthorizedTest() {
        user.setName("Costeau");
        ValidatableResponse updateResponse = userMethods.updateUnauthorizedUser(user);
        boolean isChanged = updateResponse.extract().path("success");
        String actualMessage = updateResponse.extract().path("message");
        String expectedMessage = "You should be authorised";
        int actualStatus = updateResponse.extract().statusCode();
        assertFalse(isChanged);
        assertEquals(SC_UNAUTHORIZED, actualStatus);
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    @DisplayName("Изменение поля Имя авторизованного пользователя")
    public void updateNameAuthorizedTest() {
        user.setName("Costeau");
        ValidatableResponse updateResponse = userMethods.updateAuthorizedUser(user, accessToken);
        ValidatableResponse getDataResponse = userMethods.getData(accessToken);
        boolean isChanged = updateResponse.extract().path("success");
        String newNameUser = getDataResponse.extract().path("user.name");
        int actualStatus = updateResponse.extract().statusCode();
        assertEquals(SC_OK, actualStatus);
        assertTrue(isChanged);
        assertEquals(user.getName(), newNameUser);
        System.out.println(newNameUser);
    }


    @Test
    @DisplayName("Изменение поля Email авторизованного пользователя")
    public void updateEmailAuthorizedTest() {
        user.setEmail("tequilasunrise@ya.ru");
        ValidatableResponse updateResponse = userMethods.updateAuthorizedUser(user, accessToken);
        ValidatableResponse getDataResponse = userMethods.getData(accessToken);
        boolean isChanged = updateResponse.extract().path("success");
        String newEmailUser = getDataResponse.extract().path("user.email");
        int actualStatus = updateResponse.extract().statusCode();
        assertEquals(SC_OK, actualStatus);
        assertTrue(isChanged);
        assertEquals(user.getEmail(), newEmailUser);
        System.out.println(newEmailUser);
    }

}

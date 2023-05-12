package user;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserMethods {

    private final static String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private final static String REGISTER_PATH = "/api/auth/register";
    private final static String LOGIN_PATH = "/api/auth/login";
    private final static String USER_PATH = "/api/auth/user";

    public UserMethods() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Запрос на создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(REGISTER_PATH)
                .then();
    }

    @Step("Запрос на авторизацию пользователя")
    public ValidatableResponse login(Credentials user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(LOGIN_PATH)
                .then().log().status();
    }

    @Step("Запрос на удаление пользователя")
    public ValidatableResponse delete(String token) {
        return given()
                .header("Authorization", token)
                .delete(USER_PATH)
                .then();
    }


    @Step("Запрос на изменение данных авторизованного пользователя")
    public ValidatableResponse updateAuthorizedUser(User user, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(user)
                .patch(USER_PATH)
                .then().log().status();
    }

    @Step("Запрос на изменение данных неавторизованного пользователя")
    public ValidatableResponse updateUnauthorizedUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .patch(USER_PATH)
                .then().log().status();
    }

    @Step("Запрос на получение информации о пользователе")
    public ValidatableResponse getData(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .get(USER_PATH)
                .then().log().status();
    }

}
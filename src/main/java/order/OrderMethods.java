package order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import user.User;

import static io.restassured.RestAssured.given;

public class OrderMethods{

    private final static String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String ORDER_PATH = "/api/orders/";

    public OrderMethods() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Запрос на создание заказа после авторизации")
    public ValidatableResponse createOrderAfterAuth(Order order, String token){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().status();
    }

    @Step("Запрос на создание заказа до авторизации")
    public ValidatableResponse createOrderBeforeAuth(Order order){
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().status();
    }


    @Step("Запрос на получение заказа авторизованного пользователя")
    public ValidatableResponse getOrderAuthorizedUser(String token){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .get(ORDER_PATH)
                .then().log().status();
    }

    @Step("Запрос на получение заказа невторизованного пользователя")
    public ValidatableResponse getOrderUnauthorizedUser(){
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_PATH)
                .then().log().status();
    }
}
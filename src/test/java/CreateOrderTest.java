import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateOrderTest {
    private Order order;
    private OrderMethods orderMethods;
    private String accessToken;
    private User user;
    private UserMethods userMethods;
    private List<String> orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa7a"));

    @Before
    public void setUp() {
        order = new Order();
        userMethods = new UserMethods();
        orderMethods = new OrderMethods();
        user = UserData.defaultUser();
        ValidatableResponse responseCreate = userMethods.create(user);
        accessToken = responseCreate.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userMethods.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа после авторизаризации пользователя")
    public void createOrderAfterAuthTest() {
        order.setIngredients(orderList);
        ValidatableResponse createOrder = orderMethods.createOrderAfterAuth(order, accessToken);
        int actualStatus = createOrder.extract().statusCode();
        boolean isCreated = createOrder.extract().path("success");
        assertEquals(SC_OK, actualStatus);
        assertTrue(isCreated);
        System.out.println(order.getIngredients());
    }


    @Test
    @DisplayName("Создание заказа до авторизации пользователя")
    public void createOrderBeforeAuthTest() {
        order.setIngredients(orderList);
        ValidatableResponse createOrder = orderMethods.createOrderBeforeAuth(order);
        int actualStatus = createOrder.extract().statusCode();
        boolean isCreated = createOrder.extract().path("success");
        assertEquals(SC_OK, actualStatus);
        assertTrue(isCreated);
        System.out.println(order.getIngredients());
    }


    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ValidatableResponse createOrder = orderMethods.createOrderAfterAuth(order, accessToken);
        int actualStatus = createOrder.extract().statusCode();
        boolean isCreated = createOrder.extract().path("success");
        String actualMessage = createOrder.extract().path("message");
        String expectedMessage = "Ingredient ids must be provided";
        assertEquals(SC_BAD_REQUEST, actualStatus);
        assertFalse(isCreated);
        assertEquals(expectedMessage, actualMessage);
        System.out.println(order.getIngredients());
    }


    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectHashTest() {
        List<String> orderList = new ArrayList<>(Arrays.asList("0123456789qwerty"));
        order.setIngredients(orderList);
        ValidatableResponse response = orderMethods.createOrderAfterAuth(order, accessToken);
        int actualStatus = response.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, actualStatus);
    }
}
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GetOrderTest {
    private User user;
    private Order order;
    private UserMethods userMethods;
    private OrderMethods orderMethods;
    private String accessToken;
    private List<String> orderList = new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa74", "61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa7a"));

    @Before
    public void setUp() {
        order = new Order(orderList);
        orderMethods = new OrderMethods();
        userMethods = new UserMethods();
        user = UserData.defaultUser();
        accessToken = userMethods.create(user).extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userMethods.delete(accessToken);
    }

    @Test
    @DisplayName("Получение заказа авторизированного пользователя")
    public void getOrderAuthorizedUserTest() {
        orderMethods.createOrderAfterAuth(order, accessToken);
        ValidatableResponse getOrder = orderMethods.getOrderAuthorizedUser(accessToken);
        boolean isAppeared = getOrder.extract().path("success");
        int actualStatus = getOrder.extract().statusCode();
        assertEquals(SC_OK, actualStatus);
        assertTrue(isAppeared);
    }


    @Test
    @DisplayName("Check the response and the status code of the unauthorized user's order list")
    public void getOrdersUnauthorizedUserTest() {
        orderMethods.createOrderBeforeAuth(order);
        ValidatableResponse getOrder = orderMethods.getOrderUnauthorizedUser();
        int actualStatus = getOrder.extract().statusCode();
        boolean isAppeared = getOrder.extract().path("success");
        String actualMessage = getOrder.extract().path("message");
        assertEquals(SC_UNAUTHORIZED, actualStatus);
        assertFalse(isAppeared);
        assertEquals("You should be authorised", actualMessage);
    }

}
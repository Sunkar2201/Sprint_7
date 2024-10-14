package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.responses.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class OrderTest {
    public CourierApi courierApi = new CourierApi();
    public OrderApi orderApi = new OrderApi();

    public static final String LOGIN = "SunkarSunkarCourier";
    public static final  String PASSWORD = "12345";

    public static final String FIRST_NAME = "Sunkar";
    public static final String LAST_NAME = "Sunkarov";
    public static final String ADDRESS = "Гагарина 135И";
    public static final int METRO_STATION = 12;
    public static final String PHONE = "+7 747 144 21 10";
    public static final int RENT_TIME = 5;
    public static final String DELIVERY_DATE = "2024-10-30";
    public static final String COMMENT = "Тест коммент";
    public static final List<String> COLOR = List.of("BLACK");



    @DisplayName("Создание заказа с цветами 'BLACK', 'GRAY' и без цветов.")
    @ParameterizedTest()
    @CsvSource({
            "BLACK",
            "GRAY",
            "BLACK and GRAY"
    })
    @Step("Тест создания заказа с двумя цветами.")
    @Description("Этот тест проверяет создание заказа с двумя цветами.")
    @EmptySource
    public void testCreateOrderWithTwoColors(String colorElement) {
        List<String> color = new ArrayList<>();
        if (colorElement != null && !colorElement.isEmpty()) {
            String[] colors = colorElement.split("and");
            color.addAll(Arrays.asList(colors));
        }
        Response response = orderApi.createOrder(
                FIRST_NAME,
                LAST_NAME,
                ADDRESS,
                METRO_STATION,
                PHONE,
                RENT_TIME,
                DELIVERY_DATE,
                COMMENT,
                COLOR
        );
        assertEquals(201, response.statusCode());

        OrdersCreateResponse responseOrder = response.then().extract().as(OrdersCreateResponse.class);
        assertNotNull(responseOrder.getTrack());
    }

    @Test
    @Step("Тест получения списка заказов.")
    @Description("Этот тест проверяет получение списка заказов.")
    public void testGetOrderList() {
        // Создание курьера
        Response responseCourier = courierApi.createCourier(LOGIN, PASSWORD, FIRST_NAME);
        assertEquals(201, responseCourier.statusCode());

        DefaultSuccessResponse courier = responseCourier.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(courier.isOk());

        // Логин курьера и получение его id
        Response responseLogin = courierApi.loginCourier(LOGIN, PASSWORD);
        assertEquals(200, responseLogin.statusCode());

        CourierLoginResponse courierLoginResponse = responseLogin.then().extract().body().as(CourierLoginResponse.class);
        Long courierId = courierLoginResponse.getId();
        assertNotNull(courierId);

        // Создание заказа
        Response responseOrder = orderApi.createOrder(
                FIRST_NAME,
                LAST_NAME,
                ADDRESS,
                METRO_STATION,
                PHONE,
                RENT_TIME,
                DELIVERY_DATE,
                COMMENT,
                COLOR
        );
        assertEquals(201, responseOrder.statusCode());

        OrdersCreateResponse order = responseOrder.then().extract().as(OrdersCreateResponse.class);
        Long orderId = order.getTrack();
        assertNotNull(orderId);

        // Приемка заказа курьером
        Response responseAcceptOrder = orderApi.acceptOrder(orderId, courierId);
        assertEquals(200, responseAcceptOrder.statusCode());

        DefaultSuccessResponse acceptOrder = responseAcceptOrder.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(acceptOrder.isOk());

        // Получение списка заказов по курьеру
        Response responseOrderList = orderApi.getOrders(courierId);
        assertEquals(200, responseOrderList.statusCode());

        OrdersGetListResponse orderList = responseOrderList.then().extract().body().as(OrdersGetListResponse.class);
        assertNotNull(orderList.getOrders());
    }

    @AfterEach
    @Step("tearDown")
    void tearDown(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("testGetOrderList()")) {
            CourierLoginResponse loginResponse = courierApi
                    .loginCourier(LOGIN, PASSWORD)
                    .then().extract().body().as(CourierLoginResponse.class);
            Long courierId = loginResponse.getId();

            DefaultSuccessResponse deleteResponse = courierApi
                    .deleteCourier(Long.toString(courierId))
                    .then().extract().body().as(DefaultSuccessResponse.class);

            assertTrue(deleteResponse.isOk());
        }
    }
}

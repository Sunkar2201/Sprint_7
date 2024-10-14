package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.requests.OrderCreateRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderApi extends BaseHttpClient {

    private final String apiPath = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(
            String firstName,
            String lastName,
            String address,
            int metroStation,
            String phone,
            int rentTime,
            String deliveryDate,
            String comment,
            List<String> color
    ) {
        OrderCreateRequest request = new OrderCreateRequest(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color
        );

        return doPostRequest(apiPath, request);
    }

    @Step("Приемка заказа курьером")
    public Response acceptOrder(Long orderId, Long courierId) {
        Map<String, Object> params = new HashMap<>();
        params.put("courierId", courierId);
        return doPutRequest(apiPath + "/accept/" + orderId, params);
    }

    @Step("Получение всех заказов по id заказа")
    public Response getOrders(Long courierId) {
        Map<String, Object> params = new HashMap<>();
        params.put("courierId", courierId);
        return doGetRequest(apiPath, params);
    }
}

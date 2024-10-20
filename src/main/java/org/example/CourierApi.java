package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.requests.CourierCreateRequest;
import org.example.models.requests.CourierLoginRequest;

public class CourierApi extends BaseHttpClient{

    private final String apiPath = "/api/v1/courier";

    @Step("Авторизация курьера - {login}")
    public Response loginCourier(String login, String password) {
        CourierLoginRequest request = new CourierLoginRequest(login, password);
        return doPostRequest(apiPath + "/login", request);
    }

    @Step("Создаем курьера: логин - {login}, имя - {firstName}")
    public Response createCourier(String login, String password, String firstName) {
        CourierCreateRequest request = new CourierCreateRequest(login, password, firstName);
        return doPostRequest(apiPath, request);
    }

    @Step("Удаляем курьера: id - {id}")
    public Response deleteCourier(String id) {
        return doDeleteRequest(apiPath + "/" + id);
    }
}

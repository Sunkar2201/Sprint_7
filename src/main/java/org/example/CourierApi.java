package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.requests.CourierCreateRequest;
import org.example.models.requests.CourierLoginRequest;

public class CourierApi extends BaseHttpClient{

    private final String apiPath = "/api/v1/courier";

    @Step("Login курьера")
    public Response loginCourier(String login, String password) {
        CourierLoginRequest request = new CourierLoginRequest(login, password);
        System.out.println("Вызов метода loginCourier");
        return doPostRequest(apiPath + "/login", request);
    }

    @Step("Создание курьера")
    public Response createCourier(String login, String password, String firstName) {
        CourierCreateRequest request = new CourierCreateRequest(login, password, firstName);
        return doPostRequest(apiPath, request);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(String id) {
        return doDeleteRequest(apiPath + "/" + id);
    }
}

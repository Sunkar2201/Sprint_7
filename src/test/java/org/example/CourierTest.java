package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.responses.DefaultSuccessResponse;
import org.example.models.responses.CourierLoginResponse;
import org.example.models.responses.ErrorResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class CourierTest
{

    public CourierApi courierApi = new CourierApi();

    public static final String LOGIN = "SunkarSunkarCourier";
    public static final  String PASSWORD = "12345";
    public static final String FIRST_NAME = "Sunkar";

    static Stream<Arguments> provideCourierParams() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("", PASSWORD, FIRST_NAME),
                org.junit.jupiter.params.provider.Arguments.of(LOGIN, "", FIRST_NAME)
        );
    }

    @Test
    @Step("Тест создания курьера.")
    @Description("Этот тест создает курьера.")
    public void testCreateCourier() {
        // Тест создания курьера.
        Response response = courierApi.createCourier(LOGIN, PASSWORD, FIRST_NAME);
        assertEquals(201, response.statusCode());

        DefaultSuccessResponse courierResponse = response.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(courierResponse.isOk());
    }

    @Test
    @Step("Тест ошибки создания двух одинаковых курьеров.")
    @Description("Этот тест проверяет ошибку создания двух одинаковых курьеров.")
    public void testCreateTwoIdenticalCourier() {
        // Тест ошибки создания двух одинаковых курьеров.
        Response response = courierApi.createCourier(LOGIN, PASSWORD, FIRST_NAME);
        assertEquals(201, response.statusCode());

        DefaultSuccessResponse courierResponse = response.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(courierResponse.isOk());

        Response responseTwo = courierApi.createCourier(LOGIN, PASSWORD, FIRST_NAME);
        assertEquals(409, responseTwo.statusCode());

        ErrorResponse errorResponse = responseTwo.then().extract().body().as(ErrorResponse.class);
        assertEquals("Этот логин уже используется. Попробуйте другой.", errorResponse.getMessage());
    }

    @DisplayName("Validating a POST /courier without a parameter")
    @ParameterizedTest(name = "{index}: login - ''{0}'' | password - ''{1}'' | firstName - ''{2}''.")
    @MethodSource("provideCourierParams")
    @Step("Тест создания курьера без параметров.")
    @Description("Этот тест проверяет создание курьера без параметров.")
    public void testCreateCourierWithoutParams(String login, String password, String firstName) {
        Response response = courierApi.createCourier(login, password, firstName);
        assertEquals(400, response.statusCode());

        ErrorResponse errorResponse = response.then().extract().body().as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", errorResponse.getMessage());
    }

    @Test
    @Step("Тест авторизации курьера.")
    @Description("Этот тест проверяет авторизацию курьера.")
    public void testCourierLogin() {
        Response responseCourier = courierApi.createCourier(LOGIN, PASSWORD, FIRST_NAME);
        assertEquals(201, responseCourier.statusCode());

        DefaultSuccessResponse courierResponse = responseCourier.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(courierResponse.isOk());

        Response responseLogin = courierApi.loginCourier(LOGIN, PASSWORD);
        assertEquals(200, responseLogin.statusCode());

        CourierLoginResponse courierLoginResponse = responseLogin.then().extract().body().as(CourierLoginResponse.class);
        assertNotNull(courierLoginResponse.getId());
    }

    @DisplayName("Validating a POST /courier/login without a parameter")
    @ParameterizedTest(name = "{index}: login - ''{0}'' | password - ''{1}''.")
    @MethodSource("provideCourierParams")
    @Step("Тест авторизации курьера без параметров.")
    @Description("Этот тест проверяет авторизацию курьера без параметров.")
    public void testLoginCourierWithoutParams(String login, String password) {
        Response responseCourier = courierApi.createCourier(CourierTest.LOGIN, CourierTest.PASSWORD, FIRST_NAME);
        assertEquals(201, responseCourier.statusCode());

        DefaultSuccessResponse courierResponse = responseCourier.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(courierResponse.isOk());

        Response response = courierApi.loginCourier(login, password);
        assertEquals(400, response.statusCode());

        ErrorResponse errorResponse = response.then().extract().body().as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
    }

    @DisplayName("Checking for non-existent fields")
    @ParameterizedTest(name = "{index}: login - ''{0}'' | password - ''{1}''.")
    @CsvSource({
            CourierTest.LOGIN + ", 123456",
            "InvalidLogin , " + CourierTest.PASSWORD
    })
    @Step("Тест авторизации с неверными полями.")
    @Description("Этот тест проверяет авторизацию курьера с неверными полями.")
    public void testInvalidLoginFields(String login, String password) {
        Response responseCourier = courierApi.createCourier(CourierTest.LOGIN, CourierTest.PASSWORD, FIRST_NAME);
        assertEquals(201, responseCourier.statusCode());

        DefaultSuccessResponse courierResponse = responseCourier.then().extract().body().as(DefaultSuccessResponse.class);
        assertTrue(courierResponse.isOk());

        Response response = courierApi.loginCourier(login, password);
        assertEquals(404, response.statusCode());

        ErrorResponse errorResponse = response.then().extract().body().as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
    }

    @AfterEach
    @Step("tearDown")
    public void tearDown() {
        Response response = courierApi.loginCourier(LOGIN, PASSWORD);

        if (response.statusCode() == 200) {
            CourierLoginResponse loginResponse = response.then().extract().body().as(CourierLoginResponse.class);
            Long courierId = loginResponse.getId();

            DefaultSuccessResponse deleteResponse = courierApi
                    .deleteCourier(Long.toString(courierId))
                    .then().extract().body().as(DefaultSuccessResponse.class);

            assertTrue(deleteResponse.isOk());
        }
    }

}

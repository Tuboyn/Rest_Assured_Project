package requests;

import enums.ApiEndpoint;
import io.restassured.specification.ResponseSpecification;
import model.RegisterData;
import model.RegisterResponseData;
import model.ResourceData;
import model.UserData;
import specification.Specification;

import static io.restassured.RestAssured.given;

import java.util.List;

public class ApiRequestHelper {
    public static List<UserData> getUsers() {
        Specification.installSpecification(Specification.responseOK(),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        return given()
                .when()
                .get(ApiEndpoint.USERS_URL.getEndpoint())
                .then()
                .extract().body().jsonPath().getList("data", UserData.class);
    }

    public static RegisterResponseData register(RegisterData regData, ResponseSpecification specification) {
        Specification.installSpecification(specification,
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        return given()
                .body(regData)
                .when()
                .post(ApiEndpoint.REG_URL.getEndpoint())
                .then()
                .extract().as(RegisterResponseData.class);
    }

    public static List<ResourceData> getResources() {
        Specification.installSpecification(Specification.responseOK(),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        return given()
                .when()
                .get(ApiEndpoint.RESOURCE_URL.getEndpoint())
                .then()
                .extract().body().jsonPath().getList("data", ResourceData.class);
    }

    public static void deleteUser() {
        Specification.installSpecification(Specification.responseUnique(204),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        given()
                .when()
                .delete(ApiEndpoint.DELETE_URL.getEndpoint());
    }
}
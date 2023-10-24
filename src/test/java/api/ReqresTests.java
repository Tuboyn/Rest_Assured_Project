package api;

import annotations.PropertyValue;
import specification.Specification;
import model.RegisterData;
import model.RegisterResponseData;
import model.ResourceData;
import model.UserData;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PropertyLoader;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTests {
    private static final String BASE_URL = "https://reqres.in";
    private static final String USERS_URL = "api/users?page=2";
    private static final String REG_URL = "api/register";
    private static final String RESOURCE_URL = "api/unknown";
    private static final String DELETE_URL = "api/users/2";

    @PropertyValue("email")
    private String email;

    @PropertyValue("password")
    private String password;
    @PropertyValue("wrongEmail")
    private String wrongEmail;


    public ReqresTests() {
        PropertyLoader.loadProperties(this);
    }

    @Test
    public void checkAvatarAndIdTest() {
        Specification.installSpecification(Specification.responseOK(), Specification.requestSpecification(BASE_URL));

        List<UserData> data = given()
                .when()
                .get(USERS_URL)
                .then()
                .extract().body().jsonPath().getList("data", UserData.class);

        data.forEach(user -> Assert.assertTrue(user.getAvatar().contains(String.valueOf(user.getId()))));
        data.forEach(user -> Assert.assertTrue(user.getEmail().endsWith("reqres.in")));
    }

    @Test
    public void successRegister() {
        Specification.installSpecification(Specification.responseOK(), Specification.requestSpecification(BASE_URL));
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        RegisterData regData = new RegisterData(email, password);
        RegisterResponseData responseData = given()
                .body(regData)
                .when()
                .post(REG_URL)
                .then()
                .extract().as(RegisterResponseData.class);
        Assert.assertEquals(responseData.getId(), id);
        Assert.assertEquals(responseData.getToken(), token);
    }

    @Test
    public void unsuccessfulRegister() {
        Specification.installSpecification(Specification.responseError(), Specification.requestSpecification(BASE_URL));
        RegisterData regData = new RegisterData(wrongEmail, null);
        RegisterResponseData responseData = given()
                .body(regData)
                .when()
                .post(REG_URL)
                .then()
                .extract().as(RegisterResponseData.class);
        Assert.assertNotNull(responseData.getError());
        Assert.assertEquals("Missing password", responseData.getError());
    }

    @Test
    public void sortedYearsTest() {
        Specification.installSpecification(Specification.responseOK(), Specification.requestSpecification(BASE_URL));
        List<ResourceData> resourceData = given()
                .when()
                .get(RESOURCE_URL)
                .then()
                .extract().body().jsonPath().getList("data", ResourceData.class);
        List<Integer> years = resourceData.stream().map(ResourceData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(years, sortedYears);
    }

    @Test
    public void deleteUserTest() {
        Specification.installSpecification(Specification.responseUnique(204), Specification.requestSpecification(BASE_URL));
        given()
                .when()
                .delete(DELETE_URL);
    }
}
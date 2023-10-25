package api;

import enums.ApiEndpoint;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.BeforeMethod;
import specification.Specification;
import model.RegisterData;
import model.RegisterResponseData;
import model.ResourceData;
import model.UserData;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.MyConfig;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTests {

    private MyConfig config;

    @BeforeMethod
    public void setUp() {
        config = ConfigFactory.create(MyConfig.class);
    }

    @Test
    public void checkAvatarAndIdTest() {
        Specification.installSpecification(Specification.responseOK(),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));

        List<UserData> data = given()
                .when()
                .get(ApiEndpoint.USERS_URL.getEndpoint())
                .then()
                .extract().body().jsonPath().getList("data", UserData.class);

        data.forEach(user -> Assert.assertTrue(user.getAvatar().contains(String.valueOf(user.getId()))));
        data.forEach(user -> Assert.assertTrue(user.getEmail().endsWith("reqres.in")));
    }

    @Test
    public void successRegister() {
        Specification.installSpecification(Specification.responseOK(),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        int id = 4;
        RegisterData regData = new RegisterData(config.email(), config.password());
        RegisterResponseData responseData = given()
                .body(regData)
                .when()
                .post(ApiEndpoint.REG_URL.getEndpoint())
                .then()
                .extract().as(RegisterResponseData.class);
        Assert.assertEquals(responseData.getId(), id);
        Assert.assertEquals(responseData.getToken(), config.token());
    }

    @Test
    public void unsuccessfulRegister() {
        Specification.installSpecification(Specification.responseError(),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        RegisterData regData = new RegisterData(config.wrongEmail(), null);
        RegisterResponseData responseData = given()
                .body(regData)
                .when()
                .post(ApiEndpoint.REG_URL.getEndpoint())
                .then()
                .extract().as(RegisterResponseData.class);
        Assert.assertNotNull(responseData.getError());
        Assert.assertEquals("Missing password", responseData.getError());
    }

    @Test
    public void sortedYearsTest() {
        Specification.installSpecification(Specification.responseOK(),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        List<ResourceData> resourceData = given()
                .when()
                .get(ApiEndpoint.RESOURCE_URL.getEndpoint())
                .then()
                .extract().body().jsonPath().getList("data", ResourceData.class);
        List<Integer> years = resourceData.stream().map(ResourceData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(years, sortedYears);
    }

    @Test
    public void deleteUserTest() {
        Specification.installSpecification(Specification.responseUnique(204),
                Specification.requestSpecification(ApiEndpoint.BASE_URL.getEndpoint()));
        given()
                .when()
                .delete(ApiEndpoint.DELETE_URL.getEndpoint());
    }
}
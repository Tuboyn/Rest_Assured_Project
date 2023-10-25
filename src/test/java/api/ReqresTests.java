package api;

import model.RegisterData;
import model.RegisterResponseData;
import model.ResourceData;
import model.UserData;
import org.aeonbits.owner.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import requests.ApiRequestHelper;
import specification.Specification;
import utils.MyConfig;

import java.util.List;
import java.util.stream.Collectors;

public class ReqresTests {

    private final MyConfig config = ConfigFactory.create(MyConfig.class);

    @Test
    public void checkAvatarAndIdTest() {
        List<UserData> data = ApiRequestHelper.getUsers();

        data.forEach(user -> Assert.assertTrue(user.getAvatar().contains(String.valueOf(user.getId()))));
        data.forEach(user -> Assert.assertTrue(user.getEmail().endsWith("reqres.in")));
    }

    @Test
    public void successRegister() {
        RegisterData regData = new RegisterData(config.email(), config.password());
        RegisterResponseData responseData = ApiRequestHelper.register(regData, Specification.responseOK());

        Assert.assertEquals(responseData.getId(), 4);
        Assert.assertEquals(responseData.getToken(), config.token());
    }

    @Test
    public void unsuccessfulRegister() {
        RegisterData regData = new RegisterData(config.wrongEmail(), null);
        RegisterResponseData responseData = ApiRequestHelper.register(regData, Specification.responseError());
        Assert.assertNotNull(responseData.getError());
        Assert.assertEquals("Missing password", responseData.getError());
    }

    @Test
    public void sortedYearsTest() {
        List<ResourceData> resourceData = ApiRequestHelper.getResources();
        List<Integer> years = resourceData.stream().map(ResourceData::getYear).collect(Collectors.toList());
        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());

        Assert.assertEquals(years, sortedYears);
    }

    @Test
    public void deleteUserTest() {
        ApiRequestHelper.deleteUser();
    }
}
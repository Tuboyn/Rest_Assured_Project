package enums;

import lombok.Getter;

@Getter
public enum ApiEndpoint {
    BASE_URL("https://reqres.in"),
    USERS_URL("api/users?page=2"),
    REG_URL("api/register"),
    RESOURCE_URL("api/unknown"),
    DELETE_URL("api/users/2");

    private final String endpoint;

    ApiEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}

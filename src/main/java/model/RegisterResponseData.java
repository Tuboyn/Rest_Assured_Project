package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponseData {
    private Integer id;
    private String token;
    private String error;
}
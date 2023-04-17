package it.epicode.jwt.login;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoginResponse {
    private String token;
    private final String type = "Bearer";
    private String username;
    private List<String> roles;
    private Date expirationDate;

    public LoginResponse(String token, String username, List<String> roles, Date expirationDate) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.expirationDate = expirationDate;
    }
}

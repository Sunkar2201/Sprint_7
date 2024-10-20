package org.example.models.requests;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CourierLoginRequest {
    private String login;
    private String password;
}

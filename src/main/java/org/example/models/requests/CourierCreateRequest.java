package org.example.models.requests;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CourierCreateRequest {
    private String login;
    private String password;
    private String firstName;
}

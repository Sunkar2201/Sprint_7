package org.example.models.responses;

import lombok.Data;

import java.util.List;


@Data
public class OrdersTrack {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private int track;
    private int status;
    private List<String> color;
    private String comment;
    private boolean cancelled;
    private boolean finished;
    private boolean inDelivery;
    private String courierFirstName;
    private String createdAt;
    private String updatedAt;
}

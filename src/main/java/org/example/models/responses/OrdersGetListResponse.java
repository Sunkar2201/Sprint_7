package org.example.models.responses;

import lombok.Data;

import java.util.List;

@Data
public class OrdersGetListResponse {
    private List<Order> orders;
    private PageInfo pageInfo;
    private List<Station> availableStations;

    @Data
    public static class Order {
        private Long id;
        private Integer courierId;
        private String firstName;
        private String lastName;
        private String address;
        private String metroStation;
        private String phone;
        private int rentTime;
        private String deliveryDate;
        private int track;
        private List<String> color;
        private String comment;
        private String createdAt;
        private String updatedAt;
        private int status;
    }

    @Data
    public static class PageInfo {
        private int page;
        private int total;
        private int limit;
    }

    @Data
    public static class Station {
        private String name;
        private String number;
        private String color;
    }
}
package com.longnh.mobile.mininow.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Store implements Serializable {

    private long id;
    private String uid;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String imgURL;
    private String bannerURL;
    private LocalDateTime registerTime;
    private Double latitude;
    private Double longitude;
    private String distance;

}

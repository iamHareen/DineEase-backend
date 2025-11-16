package com.hareendev.dineease.dto.request;

import com.hareendev.dineease.model.ContactInformation;
import lombok.Data;

import java.util.List;

@Data
public class CreateRestaurantRequest {
    private long id;
    private String name;
    private String description;
    private String cuisineType;
    private String address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
}

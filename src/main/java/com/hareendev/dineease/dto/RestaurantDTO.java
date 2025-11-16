package com.hareendev.dineease.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class RestaurantDTO {
    private String title;

    @Column
    @ElementCollection
    private List<String> images;

    private String description;
    private Long id;
}

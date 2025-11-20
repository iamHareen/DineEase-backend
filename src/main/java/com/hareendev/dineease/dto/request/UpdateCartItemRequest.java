package com.hareendev.dineease.dto.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private long cartItemId;
    private int quantity;
}

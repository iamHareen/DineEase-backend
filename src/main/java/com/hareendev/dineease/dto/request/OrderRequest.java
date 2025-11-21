package com.hareendev.dineease.dto.request;

import com.hareendev.dineease.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long requestId;
    private Address deliveryAddress;
}

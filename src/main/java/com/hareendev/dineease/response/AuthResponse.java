package com.hareendev.dineease.response;

import com.hareendev.dineease.model.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}

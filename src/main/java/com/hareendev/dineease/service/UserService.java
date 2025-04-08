package com.hareendev.dineease.service;

import com.hareendev.dineease.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}

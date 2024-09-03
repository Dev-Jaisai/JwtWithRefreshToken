package com.Jwt.service;


import com.Jwt.dtos.UserRequest;
import com.Jwt.dtos.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse saveUser(UserRequest userRequest);

    UserResponse getUser();

    List<UserResponse> getAllUser();


}
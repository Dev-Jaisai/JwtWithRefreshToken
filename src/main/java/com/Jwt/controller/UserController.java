package com.Jwt.controller;

import com.Jwt.dtos.*;
import com.Jwt.entity.RefreshToken;
import com.Jwt.service.RefreshTokenService;
import com.Jwt.service.UserService;
import com.Jwt.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing user-related operations, including authentication and token handling.
 *
 * @author Jaisai
 * @date Created on 2024-09-03
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Endpoint to save a new user.
     *
     * @param userRequest the user data to save
     * @return a ResponseEntity containing the saved user's response data
     */
    @PostMapping(value = "/save")
    public ResponseEntity saveUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse userResponse = userService.saveUser(userRequest);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint to retrieve all users.
     *
     * @return a ResponseEntity containing a list of all users
     */
    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        try {
            List<UserResponse> userResponses = userService.getAllUser();
            return ResponseEntity.ok(userResponses);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint to get the profile of the currently authenticated user.
     *
     * @return a ResponseEntity containing the current user's profile data
     */
    @PostMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        try {
            UserResponse userResponse = userService.getUser();
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Test endpoint that requires the 'ROLE_ADMIN' authority.
     *
     * @return a welcome message if the user has the 'ROLE_ADMIN' authority
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint to authenticate a user and generate a JWT token.
     *
     * @param authRequestDTO contains the username and password for authentication
     * @return a JwtResponseDTO containing the access token and refresh token
     */
    @PostMapping("/login")
    public JwtResponseDTO authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken()).build();
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }

    /**
     * Endpoint to refresh an access token using a refresh token.
     *
     * @param refreshTokenRequestDTO contains the refresh token
     * @return a JwtResponseDTO containing the new access token and the same refresh token
     */
    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
    }

}

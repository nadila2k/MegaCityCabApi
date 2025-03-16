package com.nadila.MegaCityCab.controller;

import com.nadila.MegaCityCab.InBuildUseObjects.UserData;
import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.exception.AlreadyExistsException;

import com.nadila.MegaCityCab.requests.PassangerRequest;
import com.nadila.MegaCityCab.response.JwtResponse;

import com.nadila.MegaCityCab.service.AuthService.LoginService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private AuthController authController;

    private PassangerRequest passangerRequest;


    @Test
    void testPassengerSignupSuccess() {
        passangerRequest = new PassangerRequest(
                "test@example.com",
                "password123",
                Roles.PASSENGER,
                "John",
                "Doe",
                "123 Street, City",
                "1234567890"
        );
        JwtResponse mockResponse = new JwtResponse(new UserData(),"mock-jwt-token");
        when(loginService.passengerSignup(any(PassangerRequest.class))).thenReturn(mockResponse);

        JwtResponse response = loginService.passengerSignup(passangerRequest);

        assertNotNull(response);
        System.out.println(response);
        assertEquals("mock-jwt-token", response.getToken());
        verify(loginService, times(1)).passengerSignup(any(PassangerRequest.class));
    }

    @Test
    void testPassengerSignupAlreadyExists() {
        when(loginService.passengerSignup(any(PassangerRequest.class)))
                .thenThrow(new AlreadyExistsException("User already exists"));

        Exception exception = assertThrows(AlreadyExistsException.class, () -> {
            loginService.passengerSignup(passangerRequest);
        });

        assertEquals("User already exists", exception.getMessage());
        verify(loginService, times(1)).passengerSignup(any(PassangerRequest.class));
    }
}
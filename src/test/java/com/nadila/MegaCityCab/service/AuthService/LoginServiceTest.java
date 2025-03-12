package com.nadila.MegaCityCab.service.AuthService;

import com.nadila.MegaCityCab.enums.Roles;
import com.nadila.MegaCityCab.model.CabUser;
import com.nadila.MegaCityCab.repository.CabUserRepository;
import com.nadila.MegaCityCab.repository.PassengerRepository;
import com.nadila.MegaCityCab.requests.LoginRequest;
import com.nadila.MegaCityCab.requests.PassangerRequest;
import com.nadila.MegaCityCab.response.JwtResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private JwtResponse jwtResponse;

    @Mock
    LoginService loginService;

    @Test
    void passengerSignup() {
        PassangerRequest passengerRequest = new PassangerRequest();
        passengerRequest.setEmail("john.doe@example.com");
        passengerRequest.setPassword("password123");
        passengerRequest.setFirstName("John");
        passengerRequest.setLastName("Doe");
        passengerRequest.setAddress("123 Main St, Springfield");
        passengerRequest.setMobileNumber("123-456-7890");
        passengerRequest.setRoles(Roles.PASSENGER);


        // Mocking the expected return
        when(loginService.passengerSignup(passengerRequest)).thenReturn(jwtResponse);

        // Act
        JwtResponse response = loginService.passengerSignup(passengerRequest);

        // Assert
        assertNotNull(response); // Ensure the response is not null
        assertEquals(jwtResponse, response); // Ensure the response is the mocked JwtResponse

    }

    @Test
    void signin() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");


        when(loginService.signin(loginRequest)).thenReturn(jwtResponse);

        // Act
        JwtResponse response = loginService.signin(loginRequest);

        // Assert
        assertNotNull(response); // Ensure the response is not null
        assertEquals(jwtResponse, response); // Ensure the response is the mocked JwtResponse
        System.out.println(response.getToken());
    }

    @Test
    void signinInvalidAuth() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@exa23mle.com");
        loginRequest.setPassword("passwd3223123");

        // Mock the signin method to throw an exception for invalid credentials


        // Try to sign in and expect an exception
        try {
            loginService.signin(loginRequest); // This should throw the exception
            fail("Expected an AuthenticationException to be thrown"); // Fail if no exception is thrown
        } catch (AuthenticationException e) {
            // Expected exception, test should pass if this block is entered
            assertEquals("Invalid credentials", e.getMessage(), "The exception message should match the expected message");
        }
    }
}


package com.nadila.MegaCityCab.service.Drivers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    DriverService driverService;
    @Test
    void deleteDriver() {
        driverService.deleteDriver("james.smith@example.com", "james.smith");
    }
}
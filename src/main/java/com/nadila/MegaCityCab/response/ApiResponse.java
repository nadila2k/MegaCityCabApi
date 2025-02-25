package com.nadila.MegaCityCab.response;

import com.nadila.MegaCityCab.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private ResponseStatus status;
    private String message;
    private Object data;
}

package com.jr.security_no_guide.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username","message","jwt","status"})
public record AuthResponse(String username,
                           String message,
                           String jwt,
                           Boolean status) {
}

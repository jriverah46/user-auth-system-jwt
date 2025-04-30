package com.jr.security_no_guide.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthSignUpRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}

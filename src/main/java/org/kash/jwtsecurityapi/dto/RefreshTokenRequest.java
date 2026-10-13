package org.kash.jwtsecurityapi.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * RefreshTokenRequest
 */
public record RefreshTokenRequest(
    @NotBlank(message = "token is required")
    String refreshToken
) {}

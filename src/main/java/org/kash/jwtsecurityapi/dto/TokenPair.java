package org.kash.jwtsecurityapi.dto;

/**
 * TokenPair
 */
public record TokenPair(
String accessToken,
String refreshToken
) {}

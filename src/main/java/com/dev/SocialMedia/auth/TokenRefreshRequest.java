package com.dev.SocialMedia.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}

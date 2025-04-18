package com.mitocode.dto;

import java.util.Date;

public record LoginResponseDTO(
        String token,
        Date expiration
) {
}

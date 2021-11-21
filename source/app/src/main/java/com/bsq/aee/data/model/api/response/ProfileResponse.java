package com.bsq.aee.data.model.api.response;

import lombok.Data;

@Data
public class ProfileResponse {
    private String token;
    private String fullName;
    private String avatarPath;
}

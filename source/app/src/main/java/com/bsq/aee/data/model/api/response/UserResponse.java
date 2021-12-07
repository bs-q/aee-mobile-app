package com.bsq.aee.data.model.api.response;

import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String email;
    private String avatarPath;
}

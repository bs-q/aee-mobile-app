package com.bsq.aee.data.model.api.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class LoginResponse extends BaseResponse{
    private String token;
    private String fullName;
    private String avatarPath;
}

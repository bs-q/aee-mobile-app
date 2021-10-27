package com.bsq.aee.data.model.api.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class LoginRequest extends BaseRequest{
    private String email;
    private String password;
}

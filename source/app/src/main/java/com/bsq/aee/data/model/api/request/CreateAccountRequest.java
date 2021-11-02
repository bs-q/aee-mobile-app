package com.bsq.aee.data.model.api.request;

import lombok.Data;

@Data
public class CreateAccountRequest extends BaseRequest{
    private String firebaseToken;
    private String firebaseUserId;
    private String email;
    private String password;
}

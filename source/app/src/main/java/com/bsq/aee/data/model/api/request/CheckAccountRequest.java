package com.bsq.aee.data.model.api.request;

import lombok.Data;

@Data
public class CheckAccountRequest extends BaseRequest{
    private String firebaseToken;
    private String firebaseUserId;
}

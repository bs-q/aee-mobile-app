package com.bsq.aee.data.model.api.request;

import lombok.Data;

@Data
public class LoginWithGoogleRequest extends BaseRequest {
    private String uid;
}

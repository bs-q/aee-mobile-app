package com.hq.remview.data.model.api.request;

import lombok.Data;

@Data
public class VerifyQRCodeRequest extends BaseRequest{
    private String qrcode;
    private String deviceId;
}

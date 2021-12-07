package com.bsq.aee.data.model.api.response;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyResponse {
    private String content;
    private UserResponse user;
    private Date createdDate;
}

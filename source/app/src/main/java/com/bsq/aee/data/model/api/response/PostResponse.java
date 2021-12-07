package com.bsq.aee.data.model.api.response;

import java.util.Date;

import lombok.Data;

@Data
public class PostResponse{
    private Date createdDate;
    private String title;
    private String content;
    private Long id;
    private UserResponse user;
}

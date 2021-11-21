package com.bsq.aee.data.model.api.request;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String content;
    private String title;
}

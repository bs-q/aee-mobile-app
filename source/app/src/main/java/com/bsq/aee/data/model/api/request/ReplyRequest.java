package com.bsq.aee.data.model.api.request;

import lombok.Data;

@Data
public class ReplyRequest {
    private String content;
    private Long postId;
}


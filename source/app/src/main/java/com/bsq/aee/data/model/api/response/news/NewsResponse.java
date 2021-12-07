package com.bsq.aee.data.model.api.response.news;

import com.bsq.aee.data.model.api.response.BaseResponse;

import lombok.Data;

@Data
public class NewsResponse extends BaseResponse {

    private Long id;

    private Integer type;

    private String titles;

    private String content;

    private String url;

    private String tag;

    private String thumbnail;
}

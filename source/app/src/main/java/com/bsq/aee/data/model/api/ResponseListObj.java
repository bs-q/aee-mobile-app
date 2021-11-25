package com.bsq.aee.data.model.api;

import java.util.List;

import lombok.Data;

@Data
public class ResponseListObj<T> {
    private List<T> data;
    private Integer currentPage = 0;
    private Integer totalPages = 0;
    private Integer totalItems = 0 ;
    private Integer oldIndex = -1;

    public boolean hasNext() {
        return totalPages - 1 > currentPage;
    }

    public Integer getNext() {
        return currentPage + 1;
    }

    public void copy(ResponseListObj<T> data) {
        this.currentPage = data.currentPage;
        this.totalPages = data.totalPages;
        this.totalItems = data.totalItems;
        if (data.data.isEmpty()){
            this.oldIndex = -1;
        } else {
            this.oldIndex = this.data.size() - 1;
        }
        this.data.addAll(data.data);
    }
}

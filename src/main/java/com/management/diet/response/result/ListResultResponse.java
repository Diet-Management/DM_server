package com.management.diet.response.result;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResultResponse<T> extends CommonResultResponse{
    private List<T> list;
    public ListResultResponse(CommonResultResponse commonResultResponse, List<T> list) {
        super(commonResultResponse.isSuccess(), commonResultResponse.getMsg(), commonResultResponse.getStatus());
        this.list=list;
    }
}

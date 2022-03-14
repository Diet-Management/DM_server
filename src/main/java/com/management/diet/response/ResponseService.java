package com.management.diet.response;

import com.management.diet.response.result.CommonResultResponse;
import com.management.diet.response.result.ListResultResponse;
import com.management.diet.response.result.SingleResultResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    @Getter
    @AllArgsConstructor
    public enum CommonResponse{
        SUCCESS(true, "성공하였습니다", 200);
        private boolean success;
        private String msg;
        private int status;
    }
    //성공 결과 처리
    public CommonResultResponse getSuccessResult(){
        return getCommonResultResponse();
    }
    //단일 결과 처리
    public <T>SingleResultResponse<T> getSingleResult(T data){
        return new SingleResultResponse<>(getCommonResultResponse(), data);
    }
    //다중 결과 처리
    public <T> ListResultResponse<T> getListResult(List<T> list){
        return new ListResultResponse<>(getCommonResultResponse(), list);
    }

    private CommonResultResponse getCommonResultResponse() {
        return new CommonResultResponse(CommonResponse.SUCCESS.isSuccess(), CommonResponse.SUCCESS.getMsg(), CommonResponse.SUCCESS.getStatus());
    }
}

package com.management.diet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(500, "Unknown Error", ErrorClassification.SERVER + "-ERR-500"),
    BAD_REQUEST(400, "Bad Request", ErrorClassification.COMMON+"-ERR-400"),
    UNAUTHORIZED(401, "Unauthorized", ErrorClassification.COMMON+"-ERR-401"),
    FORBIDDEN(403, "Forbidden", ErrorClassification.COMMON+"-ERR-403"),
    ACCESS_TOKEN_EXPIRED(403, "AccessToken is expired", ErrorClassification.MEMBER+"-ERR403"),
    MEMBER_NOT_FIND(404, "Member can't find", ErrorClassification.MEMBER+"-ERR-404"),
    PASSWORD_NOT_CORRECT(404, "Password is not correct", ErrorClassification.MEMBER+"-ERR-404"),
    NOT_COINCIDE_API(400, "This request is not coincide API spec", ErrorClassification.COMMON+"-ERR-400"),
    FILE_NOT_EXISTS(400, "File doesn't exists", ErrorClassification.COMMON+"-ERR-400"),
    WRONG_PATH(400, "Path isn't right", ErrorClassification.COMMON+"-ERR-400"),
    PROFILE_NOT_EXISTS(404, "Profile Picture can't find", ErrorClassification.COMMON+"-ERR-404"),
    POSTING_NOT_FIND(404, "Posting can't find exception", ErrorClassification.COMMON+"-ERR-404"),
    WRITER_NOT_SAME(401, "Writer isn't same", ErrorClassification.MEMBER+"-ERR-401"),
    ;

    private int status;
    private String msg;
    private String details;
}

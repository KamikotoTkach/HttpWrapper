package ru.cwcode.tkach.httpWrapper;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Map;

public class BaseResponse<R extends BaseResponse<R>> {
  protected final R r = (R) this;
  
  @Getter
  boolean success;
  @Getter
  int code;
  @Getter(AccessLevel.PROTECTED)
  Map<String, Object> response;
  @Getter
  BaseRequest<?, ?> request;
  
  public BaseResponse(boolean success, int code, BaseRequest<?, ?> request, Map<String, Object> response) {
    this.success = success;
    this.code = code;
    this.request = request;
    this.response = response;
  }
}

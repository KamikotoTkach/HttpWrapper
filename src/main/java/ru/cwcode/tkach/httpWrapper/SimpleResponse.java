package ru.cwcode.tkach.httpWrapper;

import lombok.Getter;

import java.util.Map;

@Getter
public class SimpleResponse<R extends BaseResponse<R>> extends BaseResponse<R> {
  SimpleResponseHandlerChain<R> handler = new SimpleResponseHandlerChain<>(r);
  
  public SimpleResponse(boolean success, int code, BaseRequest<?, ?> request, Map<String, Object> response) {
    super(success, code, request, response);
  }
}

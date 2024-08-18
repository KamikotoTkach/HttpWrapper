package ru.cwcode.tkach;

import java.util.Map;

public class SimpleResponse extends BaseResponse<SimpleResponse> {
  public SimpleResponse(boolean success, int code, BaseRequest<?, ?> request, Map<String, Object> response) {
    super(success, code, request, response);
  }
}

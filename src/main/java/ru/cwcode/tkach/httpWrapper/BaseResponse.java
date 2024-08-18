package ru.cwcode.tkach.httpWrapper;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class BaseResponse<R extends BaseResponse<R>> {
  private final R r = (R) this;
  
  @Getter
  boolean success;
  @Getter
  int code;
  @Getter(AccessLevel.PROTECTED)
  Map<String, Object> response;
  
  BaseRequest<?, ?> request;
  
  public BaseResponse(boolean success, int code, BaseRequest<?, ?> request, Map<String, Object> response) {
    this.success = success;
    this.code = code;
    this.request = request;
    this.response = response;
  }
  
  public R ifSuccess(Consumer<R> onSuccess) {
    if (success) {
      onSuccess.accept(r);
    }
    
    return r;
  }
  
  public R ifFail(Consumer<R> onFail) {
    if (!success) {
      onFail.accept(r);
    }
    
    return r;
  }
  
  public <M extends ModelObject> R ifMatch(int code, Class<M> responseType, Consumer<M> ifMatch) {
    if (code != this.code) return r;
    
    Class<? extends ModelObject> type = request.responseModels.get(code);
    if (type == null) return r;
    
    ModelObject response = createResponse(type);
    
    if (!responseType.isInstance(response)) return r;
    
    ifMatch.accept(responseType.cast(response));
    
    return r;
  }
  
  private <O extends ModelObject> @Nullable O createResponse(Class<O> type) {
    try {
      return type.getDeclaredConstructor(Map.class)
                 .newInstance(getResponse());
    } catch (Exception e) {
      return null;
    }
  }
}

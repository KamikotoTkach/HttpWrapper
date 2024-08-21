package ru.cwcode.tkach.httpWrapper;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class ResponseHandlerChain<R extends BaseResponse<R>, H extends ResponseHandlerChain<R, H>> {
  private final H h = (H) this;
  
  R original;
  volatile boolean handled = false;
  
  public ResponseHandlerChain(R original) {
    this.original = original;
  }
  
  public H ifSuccess(Consumer<R> onSuccess) {
    if (handled) return h;
    
    if (original.isSuccess()) {
      onSuccess.accept(original);
      handled = true;
    }
    
    return h;
  }
  
  public H ifFail(Consumer<R> onFail) {
    if (handled) return h;
    
    if (!original.isSuccess()) {
      onFail.accept(original);
      handled = true;
    }
    
    return h;
  }
  
  public <M extends ModelObject> H ifMatch(int code, Class<M> responseType, Consumer<M> ifMatch) {
    if (handled) return h;
    if (code != original.getCode()) return h;
    
    Class<? extends ModelObject> type = original.getRequest().getResponseModels().get(code);
    if (type == null) return h;
    
    ModelObject response = createResponse(type);
    
    if (!responseType.isInstance(response)) return h;
    
    ifMatch.accept(responseType.cast(response));
    
    handled = true;
    return h;
  }
  
  private <O extends ModelObject> @Nullable O createResponse(Class<O> type) {
    try {
      return type.getDeclaredConstructor(Map.class)
                 .newInstance(original.getResponse());
    } catch (Exception e) {
      return null;
    }
  }
}

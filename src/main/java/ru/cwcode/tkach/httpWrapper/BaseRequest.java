package ru.cwcode.tkach.httpWrapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class BaseRequest<T extends BaseRequest<T, R>, R extends BaseResponse<R>> implements ModelSerializeable {
  protected final Class<? extends R> responseClass;
  protected final T t = (T) this;
  
  protected final String method;
  protected final String url;
  
  protected final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
  protected final LinkedHashMap<String, String> headers = new LinkedHashMap<>();
  protected final LinkedHashMap<String, Object> body = new LinkedHashMap<>();
  
  protected final LinkedHashMap<Integer, Class<? extends ModelObject>> responseModels = new LinkedHashMap<>();
  
  public BaseRequest(String method, String url, Class<? extends R> responseClass) {
    this.method = method;
    this.url = url;
    this.responseClass = responseClass;
  }
  
  protected T addResponseModel(int code, Class<? extends ModelObject> responseClass) {
    responseModels.put(code, responseClass);
    return t;
  }
  
  protected T addParameter(String name, String value) {
    parameters.put(name, value);
    return t;
  }
  
  protected T addHeader(String name, String value) {
    headers.put(name, value);
    return t;
  }
  
  protected T addBody(String name, Object value) {
    body.put(name, value);
    return t;
  }
  
  @Override
  public Map<String, Object> serialize() {
    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    
    body.forEach((key, object) -> {
      if (object instanceof ModelSerializeable inner) {
        result.putAll(inner.serialize());
      } else {
        result.put(key, object);
      }
    });
    
    return result;
  }
}

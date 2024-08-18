package ru.cwcode.tkach.httpWrapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServiceClient {
  public static final MediaType JSON_UTF8 = MediaType.parse("application/json; charset=utf-8");
  private static final Type RESPONSE_TYPE = new TypeToken<LinkedHashMap<String, Object>>() {}.getType();
  protected final OkHttpClient okHttpClient;
  protected final Gson gson;
  protected final String baseUrl;
  protected final LinkedHashMap<String, String> headers = new LinkedHashMap<>();
  
  public ServiceClient(OkHttpClient okHttpClient, Gson gson, @Pattern(".*/$") String baseUrl) {
    this.okHttpClient = okHttpClient;
    this.gson = gson;
    this.baseUrl = baseUrl;
  }
  
  public void addHeader(String key, String value) {
    headers.put(key, value);
  }
  
  public <T extends BaseRequest<T, R>, R extends BaseResponse<R>> CompletableFuture<R> sendRequest(T request) {
    CompletableFuture<R> future = new CompletableFuture<>();
    
    okHttpClient.newCall(buildRequest(request)).enqueue(new Callback() {
      
      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        future.complete(parseResponse(request, response));
      }
      
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        future.completeExceptionally(e);
      }
    });
    
    return future;
  }
  
  private <R extends BaseResponse<R>, T extends BaseRequest<T, R>> R parseResponse(T request, Response response) throws IOException {
    ResponseBody body = response.body();
    if (body == null) throw new IOException("Response body is empty");
    
    String string = body.string();
    
    boolean successful = response.isSuccessful();
    int code = response.code();
    LinkedHashMap<String, Object> bodyData = gson.fromJson(string, RESPONSE_TYPE);
    
    return createResponseInstance(request, createConstructor(request), successful, code, bodyData);
  }
  
  private <T extends BaseRequest<T, R>, R extends BaseResponse<R>> Request buildRequest(T request) {
    Request.Builder builder = new Request.Builder();
    builder.setMethod$okhttp(request.method);
    
    {//build url, add parameters
      HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + request.url)
                                          .newBuilder();
      request.parameters.forEach(urlBuilder::addQueryParameter);
      builder.url(urlBuilder.build());
    }
    
    {//add headers
      this.headers.forEach(builder::addHeader);
      request.headers.forEach(builder::addHeader);
    }
    
    {//add body
      String json = gson.toJson(request.serialize());
      builder.setBody$okhttp(RequestBody.create(json, JSON_UTF8));
    }
    
    return builder.build();
  }
  
  private static <R extends BaseResponse<R>, T extends BaseRequest<T, R>> @NotNull
    R createResponseInstance(T request,
                             Constructor<? extends R> constructor,
                             boolean successful,
                             int code, LinkedHashMap<String, Object> bodyData)
    throws IOException {
    try {
      return constructor.newInstance(successful, code, request, bodyData);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new IOException("Cannot construct response instance", e);
    }
  }
  
  private static <R extends BaseResponse<R>, T extends BaseRequest<T, R>> @NotNull
    Constructor<? extends R> createConstructor(T request)
    throws IOException {
    try {
      return request.responseClass.getDeclaredConstructor(boolean.class, int.class, BaseRequest.class, Map.class);
    } catch (NoSuchMethodException e) {
      throw new IOException("Cannot get response constructor (boolean, int, Map)", e);
    }
  }
}

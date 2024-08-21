package ru.cwcode.tkach.httpWrapper;

public class SimpleResponseHandlerChain<R extends BaseResponse<R>> extends ResponseHandlerChain<R, SimpleResponseHandlerChain<R>> {
  public SimpleResponseHandlerChain(R original) {
    super(original);
  }
}

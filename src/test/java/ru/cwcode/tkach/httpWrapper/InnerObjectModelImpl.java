package ru.cwcode.tkach.httpWrapper;

import lombok.Getter;

import java.util.Map;

@Getter
public class InnerObjectModelImpl extends ModelObject {
  ModelField<String> string = new ModelField<>("string", String.class, this);
  
  public InnerObjectModelImpl() {
  }
  
  public InnerObjectModelImpl(Map<String, Object> data) {
    super(data);
  }
}

package ru.cwcode.tkach.httpWrapper;

import java.util.Optional;

public class ModelField<T> {
  protected String key;
  protected Class<T> type;
  protected ModelObject modelObject;
  protected boolean required = false;
  
  public ModelField(String key, Class<T> type, ModelObject modelObject, boolean required) {
    this(key, type, modelObject);
    this.required = required;
  }
  
  public ModelField(String key, Class<T> type, ModelObject modelObject) {
    this.key = key;
    this.type = type;
    this.modelObject = modelObject;
    this.modelObject.registerField(this);
  }
  
  public T get() {
    return (T) modelObject.get(key);
  }
  
  public void set(T value) {
    modelObject.set(key, value);
  }
  
  public Optional<T> getSafe() {
    if (has()) {
      return Optional.of(get());
    }
    
    return Optional.empty();
  }
  
  public boolean has() {
    Object value;
    return (value = get()) != null && type.isInstance(value);
  }
  
  public boolean isSatisfied() {
    return !required || has();
  }
}

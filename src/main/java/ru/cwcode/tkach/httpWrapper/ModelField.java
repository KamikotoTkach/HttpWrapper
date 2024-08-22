package ru.cwcode.tkach.httpWrapper;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
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
    return map(modelObject.get(key), type);
  }
  
  protected @Nullable <O> O map(Object object, Class<O> type) {
    if (object == null) return null;
    
    if (object instanceof Map<?,?> map && ModelObject.class.isAssignableFrom(type)) {
      return constructModelObject(map, type);
    }
    
    if (type.isInstance(object)) {
      return type.cast(object);
    }
    
    if (Integer.class.isAssignableFrom(type) && object instanceof Double doubleValue) {
      return type.cast(doubleValue.intValue());
    }
    
    if (object instanceof List<?> list) {
      if (type.isArray()) {
          return type.cast(list.stream()
                               .map(x->map(x, type.componentType()))
                               .toArray(x -> (O[]) Array.newInstance(type.componentType(), x)));
      }
      
      return type.cast(list.toArray());
    }
    
    return null;
  }
  
  
  private <C> C constructModelObject(Map<?, ?> map, Class<C> type) {
    try {
      return type.getDeclaredConstructor(Map.class).newInstance(map);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void set(T value) {
    if (type.isInstance(value)) {
      modelObject.set(key, value);
    } else {
      throw new RuntimeException("Can't set value of type " + type.getName() + " to " + value.getClass().getName());
    }
  }
  
  public Optional<T> getSafe() {
    try {
      return Optional.ofNullable(get());
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}

package ru.cwcode.tkach.httpWrapper;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModelObject implements ModelSerializable {
  protected final Map<String, Object> data;
  protected final Map<String, ModelField<?>> fields = new LinkedHashMap<>();
  
  public ModelObject() {
    this.data = new LinkedHashMap<>();
  }
  
  public ModelObject(Map<String, Object> data) {
    this.data = data;
  }
  
  protected @Nullable Object get(String key) {
    return data.get(key);
  }
  
  protected void set(String key, Object value) {
    data.put(key, value);
  }
  
  void registerField(ModelField<?> modelField) {
    fields.put(modelField.key, modelField);
  }
  
  @Override
  public LinkedHashMap<String, Object> serialize() {
    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    
    for (ModelField<?> field : fields.values()) {
      field.getSafe().ifPresentOrElse(value -> {
        if (value instanceof ModelSerializable inner) {
          result.put(field.key, inner.serialize());
        } else {
          if (value.getClass().isArray()) {
            result.put(field.key, Arrays.toString((Object[]) value));
          } else {
            result.put(field.key, value); //todo add custom type serialization
          }
        }
      }, () -> result.put(field.key, null));
    }
    
    return result;
  }
  
  @Override
  public String toString() {
    return serialize().toString();
  }
}

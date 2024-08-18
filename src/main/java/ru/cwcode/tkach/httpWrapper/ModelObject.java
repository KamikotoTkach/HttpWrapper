package ru.cwcode.tkach.httpWrapper;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelObject implements ModelSerializeable {
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
  
  public boolean isComplete() {
    return fields.values().stream().allMatch(ModelField::isSatisfied);
  }
  
  @Override
  public LinkedHashMap<String, Object> serialize() {
    LinkedHashMap<String, Object> result = new LinkedHashMap<>();
    
    for (ModelField<?> field : fields.values()) {
      field.getSafe().ifPresent(value -> {
        if (value instanceof ModelSerializeable inner) {
          result.putAll(inner.serialize());
        } else {
          result.put(field.key, value); //todo add custom type serialization
        }
      });
    }
    
    return result;
  }
  
  @Override
  public String toString() {
    return data.toString();
  }
}

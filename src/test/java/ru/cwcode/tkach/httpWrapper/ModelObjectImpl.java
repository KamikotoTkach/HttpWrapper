package ru.cwcode.tkach.httpWrapper;

import lombok.Getter;

import java.util.Map;

@Getter
public class ModelObjectImpl extends ModelObject {
  ModelField<String> stringModelField = new ModelField<>("string", String.class,this);
  ModelField<Integer> integerModelField = new ModelField<>("integer", Integer.class, this);
  ModelField<Double> doubleModelField = new ModelField<>("double", Double.class, this);
  ModelField<Boolean> booleanModelField = new ModelField<>("boolean", Boolean.class, this);
  ModelField<InnerObjectModelImpl> innerObjectModelField = new ModelField<>("innerObject", InnerObjectModelImpl.class, this);
  
  ModelField<String[]> stringModelArrField = new ModelField<>("stringArr", String[].class, this);
  ModelField<Integer[]> integerArrModelField = new ModelField<>("integerArr", Integer[].class, this);
  ModelField<Double[]> doubleArrModelField = new ModelField<>("doubleArr", Double[].class, this);
  ModelField<Boolean[]> booleanArrModelField = new ModelField<>("booleanArr", Boolean[].class, this);
  ModelField<InnerObjectModelImpl[]> innerObjectArrModelField = new ModelField<>("innerObjectArr", InnerObjectModelImpl[].class, this);
  
  public ModelObjectImpl() {
  }
  
  public ModelObjectImpl(Map<String, Object> data) {
    super(data);
  }
}

package ru.cwcode.tkach.httpWrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelObjectTest {
  ModelObjectImpl model;
  
  @BeforeEach
  void setUp() {
    model = new ModelObjectImpl(Map.of("string", "str",
                                       "integer", 1,
                                       "double", 2.0,
                                       "boolean", true,
                                       "innerObject", Map.of("string", "str"),
                                       
                                       "stringArr", List.of("a", "b", "c"),
                                       "integerArr", List.of(1, 2, 3),
                                       "doubleArr", List.of(1.0, 2.0, 3.0),
                                       "booleanArr", List.of(true, false),
                                       "innerObjectArr", List.of(Map.of("string", "one"),
                                                                 Map.of("string", "two"))
    ));
  }
  
  @Test
  void stringTest() {
    assertEquals("str", model.getStringModelField().get());
  }
  
  @Test
  void stringArrTest() {
    assertArrayEquals(new String[]{"a", "b", "c"}, model.getStringModelArrField().get());
  }
  
  @Test
  void integerTest() {
    assertEquals(1, model.getIntegerModelField().get());
  }
  
  @Test
  void integerArrTest() {
    assertArrayEquals(new Integer[]{1, 2, 3}, model.getIntegerArrModelField().get());
  }
  
  @Test
  void doubleTest() {
    assertEquals(2.0, model.getDoubleModelField().get());
  }
  
  @Test
  void doubleArrTest() {
    assertArrayEquals(new Double[]{1.0, 2.0, 3.0}, model.getDoubleArrModelField().get());
  }
  
  @Test
  void booleanTest() {
    assertEquals(true, model.getBooleanModelField().get());
  }
  
  @Test
  void booleanArrTest() {
    assertArrayEquals(new Boolean[]{true, false}, model.getBooleanArrModelField().get());
  }
  
  @Test
  void innerObjectTest() {
    assertEquals("str", model.getInnerObjectModelField().get().getString().get());
  }
  
  @Test
  void innerObjectArrTest() {
    String[] array = Arrays.stream(model.getInnerObjectArrModelField().get())
                           .map(x -> x.getString().get())
                           .toArray(String[]::new);
    
    assertArrayEquals(new String[]{"one", "two"}, array);
  }
}
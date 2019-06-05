package com.swagger.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
public class PolymorphicStructureTest extends AbstractTest{
  @Test
  public void serializationTest() {
    Map<String, PolymorphicStructure.PropertyValue<?>> properties = new HashMap<>();
    properties.put("integerValue", new PolymorphicStructure.IntegerValue(42));
    properties.put("stringValue", new PolymorphicStructure.StringValue("String value"));
    properties.put("booleanValue", new PolymorphicStructure.BooleanValue(true));

    List<String> list = Arrays.asList("Item 1", "Item 2", "Item 3");
    properties.put("listValue", new PolymorphicStructure.ListValue(list));
    properties.put("credentialsValue", new PolymorphicStructure.CredentialsValue("admin", "123"));

    PolymorphicStructure model = new PolymorphicStructure();
    model.setProperties(properties);

    String json = getGson().toJson(model);

    assertTrue(json.contains("\"stringValue\": {\n      \"value\": \"String value\"\n    }"));
    assertTrue(json.contains("\"integerValue\": {\n      \"value\": 42\n    }"));
    assertTrue(json.contains("\"booleanValue\": {\n      \"value\": true\n    }"));
    assertTrue(json.contains("\"credentialsValue\": {\n      \"value\": {\n        \"user\": \"admin\",\n        \"password\": \"123\"\n      }\n    }"));
    assertTrue(json.contains("\"listValue\": {\n      \"value\": [\n        \"Item 1\",\n        \"Item 2\",\n        \"Item 3\"\n      ]\n    }"));
  }

  @Test
  public void deserializationTest() {
    String json = "{\n" +
            "  \"properties\": {\n" +
            "    \"listValue\": {\n" +
            "      \"value\": [\n" +
            "        \"Item 1\",\n" +
            "        \"Item 2\",\n" +
            "        \"Item 3\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"stringValue\": {\n" +
            "      \"value\": \"String value\"\n" +
            "    },\n" +
            "    \"integerValue\": {\n" +
            "      \"value\": 42\n" +
            "    },\n" +
            "    \"booleanValue\": {\n" +
            "      \"value\": true\n" +
            "    },\n" +
            "    \"credentialsValue\": {\n" +
            "      \"value\": {\n" +
            "        \"user\": \"admin\",\n" +
            "        \"password\": \"123\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    PolymorphicStructure model = getGson().fromJson(json, PolymorphicStructure.class);

    assertEquals(42, model.getProperties().get("integerValue").getValue());
    assertEquals("String value", model.getProperties().get("stringValue").getValue());
    assertTrue((Boolean) model.getProperties().get("booleanValue").getValue());
    assertEquals("admin", ((PolymorphicStructure.CredentialsValue.Payload)model.getProperties().get("credentialsValue").getValue()).getUser());
    assertEquals("123", ((PolymorphicStructure.CredentialsValue.Payload)model.getProperties().get("credentialsValue").getValue()).getPassword());
    assertEquals("Item 1", ((List<?>)model.getProperties().get("listValue").getValue()).get(0));
    assertEquals("Item 2", ((List<?>)model.getProperties().get("listValue").getValue()).get(1));
    assertEquals("Item 3", ((List<?>)model.getProperties().get("listValue").getValue()).get(2));
  }
}

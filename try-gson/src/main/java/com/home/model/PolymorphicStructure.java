package com.home.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor
@Data
public class PolymorphicStructure {
  @SerializedName("properties")
  private Map<String, PropertyValue<?>> properties;

  @JsonAdapter(GsonPropertyValueTypeAdapter.class)
  public interface PropertyValue<T> {
    T getValue();
  }

  @Value
  public static class ListValue implements PropertyValue<List<String>> {
    @SerializedName("value")
    private List<String> value;
  }

  @Value
  public static class StringValue implements PropertyValue<String> {
    @SerializedName("value")
    private String value;
  }

  @Value
  public static class IntegerValue implements PropertyValue<Integer> {
    @SerializedName("value")
    private Integer value;
  }

  @Value
  public static class BooleanValue implements PropertyValue<Boolean> {
    @SerializedName("value")
    private Boolean value;
  }

  @Value
  public static class CredentialsValue implements PropertyValue<CredentialsValue.Payload> {
    @SerializedName("value")
    private Payload value;

    public CredentialsValue(String user, String password) {
      this.value = new Payload(user, password);
    }

    @Value
    public static class Payload {
      @SerializedName("user")
      private String user;
      @SerializedName("password")
      private String password;
    }
  }

  public static class GsonPropertyValueTypeAdapter
          implements JsonDeserializer<PropertyValue<?>>, JsonSerializer<PropertyValue<?>> {

    @Override
    public JsonElement serialize(
            PropertyValue<?> propertyValue,
            Type type,
            JsonSerializationContext jsonSerializationContext) {

      JsonObject json = new JsonObject();
      if (propertyValue instanceof StringValue) {
        json.addProperty("value", ((StringValue) propertyValue).getValue());
      } else if (propertyValue instanceof BooleanValue) {
        json.addProperty("value", ((BooleanValue) propertyValue).getValue());
      } else if (propertyValue instanceof IntegerValue) {
        json.addProperty("value", ((IntegerValue) propertyValue).getValue());
      } else if (propertyValue instanceof ListValue) {
        JsonArray jsonArray = new JsonArray();
        ((ListValue) propertyValue).getValue().forEach(jsonArray::add);

        json.add("value", jsonArray);
      } else {
        JsonObject nestedJson = new JsonObject();
        if (propertyValue instanceof CredentialsValue) {
          CredentialsValue.Payload value = ((CredentialsValue) propertyValue).getValue();

          nestedJson.addProperty("user", value.getUser());
          nestedJson.addProperty("password", value.getPassword());
        } else {
          throw new JsonParseException("Unsupported value type");
        }
        json.add("value", nestedJson);
      }
      return json;
    }

    @Override
    public PropertyValue<?> deserialize(
            JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
      JsonObject json = jsonElement.getAsJsonObject();
      JsonElement element = json.get("value");

      if (element == null) {
        throw new JsonParseException("Unable to find value node");
      }

      if (element.isJsonObject()) {
        JsonObject object = element.getAsJsonObject();

        if (object.keySet().contains("user")) {
          JsonElement user = object.get("user");
          JsonElement password = object.get("password");

          return new CredentialsValue(user.getAsString(), password.getAsString());
        }

        throw new JsonParseException("Unsupported nested value type");
      }

      if (element.isJsonArray()) {
        JsonArray array = element.getAsJsonArray();

        List<String> stringList =
                StreamSupport.stream(array.spliterator(), false)
                        .map(JsonElement::getAsString)
                        .collect(Collectors.toList());

        return new ListValue(stringList);
      }

      JsonPrimitive primitive = element.getAsJsonPrimitive();
      if (primitive.isBoolean()) {
        return new BooleanValue(primitive.getAsBoolean());
      }

      if (primitive.isNumber()) {
        return new IntegerValue(primitive.getAsInt());
      }

      return new StringValue(primitive.getAsString());
    }
  }
}

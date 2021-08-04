package au.org.ala.custom.marshalling;



import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Gson Serializer/Deserializer for Classes.
 *
 * @author Doug Palmer &lt;Doug.Palmer@csiro.au&gt;
 * @copyright Copyright (c) 2017 CSIRO
 */
public class ClassSerializer { // implements JsonSerializer<Class>, JsonDeserializer<Class> {
//    @Override
//    public Class deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
////        return new JsonPrimitive(aClass.getName());
//        return null
//    }
//
//    @Override
//    public void serialize(Class value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//
//    }
    //    @Override
//    public JsonElement serialize(Class aClass, Type type, JsonSerializationContext jsonSerializationContext) {
//        return new JsonPrimitive(aClass.getName());
//    }
//
//    @Override
//    public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        try {
//            return Class.forName(jsonElement.getAsString());
//        } catch (ClassNotFoundException ex) {
//            throw new JsonParseException("Unable to decode class name from " + jsonElement, ex);
//        }
//    }
}

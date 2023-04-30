package org.example.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import java.io.IOException;

public class CustomAuthenticationDeserializer extends JsonDeserializer<Authentication> {

    @Override
    public Authentication deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        // 这里，我们只处理 UsernamePasswordAuthenticationToken 类型的 Authentication 对象
        // 您可以根据需要扩展这个方法，以处理其他类型的 Authentication 对象
        String json = jsonParser.readValueAsTree().toString();
        UsernamePasswordAuthenticationToken authentication = new ObjectMapper().readValue(json, UsernamePasswordAuthenticationToken.class);
        return authentication;
    }
}

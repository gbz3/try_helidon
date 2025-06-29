package io.helidon.examples;

import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/greet") 
@RequestScoped 
public class GreetResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Map.of());
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() { 
        return JSON.createObjectBuilder()
                .add("message", "Hello World")
                .build(); 
    }

}

package io.helidon.examples;

import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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
    private final GreetingProvider greetingProvider;

    @Inject
    public GreetResource(GreetingProvider greetingProvider) {
        this.greetingProvider = greetingProvider;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() { 
        return createResponse("World");
    }

    private JsonObject createResponse(String who) {
        var message = String.format("%s, %s!", greetingProvider.getMessage(), who);
        return JSON.createObjectBuilder()
                .add("message", message)
                .build();
    }

}

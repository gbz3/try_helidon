package io.helidon.examples;

import java.util.Map;

import org.eclipse.microprofile.metrics.annotation.Timed;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @Timed
    public JsonObject getDefaultMessage() { 
        return createResponse("World");
    }

    private JsonObject createResponse(String who) {
        var message = String.format("%s, %s!", greetingProvider.getMessage(), who);
        return JSON.createObjectBuilder()
                .add("message", message)
                .build();
    }

    @Path("/{name}")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public JsonObject getMessage(@PathParam("name") String name) {
        return createResponse(name);
    }

    @Path("/greeting")
    @PUT
    @Produces(value = MediaType.APPLICATION_JSON)
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response updateGreeting(JsonObject jsonObject) {
        if (!jsonObject.containsKey("greeting")) {
            var entity = JSON.createObjectBuilder()
                    .add("error", "Missing 'greeting' property")
                    .build();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(entity)
                    .build();
        }

        var greeting = jsonObject.getString("greeting");
        greetingProvider.setMessage(greeting);
        return Response.status(Response.Status.NO_CONTENT)
                .build();
    }

}

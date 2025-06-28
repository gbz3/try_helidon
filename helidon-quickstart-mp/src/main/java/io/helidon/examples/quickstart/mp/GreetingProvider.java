
package io.helidon.examples.quickstart.mp;

import java.util.concurrent.atomic.AtomicReference;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

//import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.helidon.config.Config;

/**
 * Provider for greeting message.
 */
@ApplicationScoped
public class GreetingProvider {
    private final AtomicReference<String> message = new AtomicReference<>();

    /**
     * Create a new greeting provider, reading the message from configuration.
     *
     * @param message greeting to use
     */
//    public GreetingProvider(@ConfigProperty(name = "app.greeting") String message) {
    @Inject
    public GreetingProvider(Config config) {
        var message = config.get("app.greeting.xxx").asString().orElse("Hello from MP Config");
        this.message.set(message);
    }

    String getMessage() {
        return message.get();
    }

    void setMessage(String message) {
        this.message.set(message);
    }
}

package io.helidon.examples;

import java.io.IOException;

import io.helidon.microprofile.server.Server;

public final class Main {

    private Main() {
    } 

    public static void main(final String[] args) throws IOException {
        Server server = startServer();
        System.out.println("http://localhost:" + server.port() + "/greet");
    }

    static Server startServer() {
        return Server.create().start(); 
    }

}

package com.example.app;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.webserver.WebServer;
import jakarta.ws.rs.client.Client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class MyHelidonIntegrationTest {

    private static WebServer webServer;
    private static Client client;

    // PostgreSQLContainer を定義
    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void startServer() {
        // Testcontainers が起動した DB の接続情報を使って Helidon の設定を上書き
        System.setProperty("db.my-persistence-unit.hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("db.my-persistence-unit.hibernate.connection.username", postgres.getUsername());
        System.setProperty("db.my-persistence-unit.hibernate.connection.password", postgres.getPassword());
        System.setProperty("db.my-persistence-unit.hibernate.connection.driver_class", postgres.getDriverClassName());

        // Helidon Main クラスの main メソッドを呼び出してアプリケーションを起動
        // ここでは直接 WebServer を構築して起動しています
        Config config = Config.builder()
                .addSource(ConfigSources.classpath("application.yaml"))
                .build();

        // 結合テスト用に application.yaml のポートを上書きしたい場合
        // 例: ConfigValue<Integer> port = config.get("server.port").asInt().orElse(8080);
        // webServer = WebServer.builder().config(config.get("server")).port(port).build().start().toCompletableFuture().join();

        webServer = WebServer.builder()
            .config(config.get("server"))
            .routing(rb -> Main.routing(rb, null)) // Main.java の routing メソッドを利用
            .build();

        webServer = WebServer.builder()
                .config(config.get("server"))
                //.routing(Main::routing) // Main.java の routing メソッドを利用
                .build()
                .start()
                .toCompletableFuture().join();

        client = Http1Client.builder()
                .baseUri("http://localhost:" + webServer.port())
                .build();

        System.out.println("Helidon Server started on port: " + webServer.port());
    }

    @AfterAll
    static void stopServer() {
        if (webServer != null) {
            webServer.shutdown()
                    .toCompletableFuture()
                    .join();
        }
        // Main クラスで開いた EntityManagerFactory もクローズする
        Main.stop();

        // システムプロパティをクリーンアップ
        System.clearProperty("db.my-persistence-unit.hibernate.connection.url");
        System.clearProperty("db.my-persistence-unit.hibernate.connection.username");
        System.clearProperty("db.my-persistence-unit.hibernate.connection.password");
        System.clearProperty("db.my-persistence-unit.hibernate.connection.driver_class");
    }

    @Test
    void testHelloEndpoint() {
        String response = client.get("/hello").request(String.class).toCompletableFuture().join();
        assertEquals("Hello World!", response);
    }

    @Test
    void testEntitySaveAndFetch() {
        // POST でエンティティを保存
        String postResponse = client.post("/entities").request(String.class).toCompletableFuture().join();
        System.out.println("POST Response: " + postResponse);
        assertTrue(postResponse.contains("Entity saved: MyEntity"));

        // GET で保存されたエンティティを確認
        String getResponse = client.get("/entities").request(String.class).toCompletableFuture().join();
        System.out.println("GET Response: " + getResponse);
        assertTrue(getResponse.contains("MyEntity{id="));
        assertTrue(getResponse.contains("name='TestName'"));
        assertTrue(getResponse.contains("description='TestDescription'"));
    }
}

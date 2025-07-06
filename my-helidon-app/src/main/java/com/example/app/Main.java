package com.example.app;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;

import java.util.Map;

import com.example.data.entity.MyEntity;
import com.example.data.repository.MyRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        // Helidon Configuration
        Config config = Config.builder()
                .addSource(ConfigSources.classpath("application.yaml"))
                .build();

        // JPA EntityManagerFactory の初期化
        // application.yaml で定義されたデータソース設定がここで利用されます
        emf = Persistence.createEntityManagerFactory("my-persistence-unit", config.get("db").asMap().orElse(Map.of()));

        // Repository のインスタンス化
        // 実際のアプリケーションでは、CDI/Jakarta EE DI を使用して注入します
        MyRepository myRepository = new MyRepository(emf);

        WebServer.builder()
                .config(config.get("server"))
                .routing(rb -> routing(rb, myRepository))
                .build()
                .start()
                ;
                // .whenComplete((ws, throwable) -> {
                //     if (throwable == null) {
                //         System.out.println("WEB server is up! http://localhost:" + ws.port());
                //         ws.probes()
                //                 .addReadinessCheck(() -> true)
                //                 .addLivenessCheck(() -> true);
                //     } else {
                //         System.err.println("Web server failed to start: " + throwable.getMessage());
                //         throwable.printStackTrace();
                //         // エラー時に EntityManagerFactory をクローズ
                //         if (emf != null && emf.isOpen()) {
                //             emf.close();
                //         }
                //     }
                // });
    }

    private static void routing(HttpRouting.Builder rules, MyRepository myRepository) {
        rules.get("/hello", (req, res) -> res.send("Hello World!"))
             .get("/entities", (req, res) -> {
                 // 実際のアプリケーションでは非同期に処理すべき
                 try {
                     res.send(myRepository.findAll().toString());
                 } catch (Exception e) {
                     res.status(500).send("Error fetching entities: " + e.getMessage());
                 }
             })
             .post("/entities", (req, res) -> {
                 // 実際のアプリケーションではリクエストボディからエンティティを構築
                 // 例として適当なエンティティを作成
                 MyEntity newEntity = new MyEntity("TestName", "TestDescription");
                 try {
                     MyEntity savedEntity = myRepository.save(newEntity);
                     res.send("Entity saved: " + savedEntity);
                 } catch (Exception e) {
                     res.status(500).send("Error saving entity: " + e.getMessage());
                 }
             });
    }

    public static void stop() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

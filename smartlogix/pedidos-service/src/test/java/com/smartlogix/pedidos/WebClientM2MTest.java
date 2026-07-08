package com.smartlogix.pedidos;

import com.smartlogix.pedidos.config.WebClientConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Import(WebClientConfig.class)
public class WebClientM2MTest implements CommandLineRunner {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public static void main(String[] args) {
        System.setProperty("spring.kafka.consumer.auto-startup", "false");
        SpringApplication.run(WebClientM2MTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========== INICIANDO TEST M2M ==========");
        try {
            WebClient webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
            String result = webClient.get()
                .uri("/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();
            System.out.println("Resultado: " + result);
        } catch (Exception e) {
            System.out.println("========== FALLO DETECTADO ==========");
            e.printStackTrace();
        }
        System.out.println("========== FIN DEL TEST ==========");
        System.exit(0);
    }
}

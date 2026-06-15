package se.lexicon.subscriptionapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SubscriptionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionApiApplication.class, args);
    }

}

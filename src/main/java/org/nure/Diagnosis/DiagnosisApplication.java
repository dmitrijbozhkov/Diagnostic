package org.nure.Diagnosis;

import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.drivers.bolt.driver.BoltDriver;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SpringBootApplication
@ComponentScan("org.nure")
@EnableNeo4jRepositories("org.nure.Diagnosis.Repositories")
public class DiagnosisApplication {

    @Value("${database.uri}")
    private String uri;

    public static void main(String[] args) {
        SpringApplication.run(DiagnosisApplication.class, args);
    }


    @Bean
    public Configuration configuration() {
        Configuration configuration = new Configuration.Builder()
                .uri(this.uri)
                .build();
        return configuration;
    }

    @Bean
    public SessionFactory sessionFactory(Driver driver) {
        return new SessionFactory(driver, "org.nure.Diagnosis.Models");
    }

    @Bean
    public Driver driver() {
        return new BoltDriver();
    }
}

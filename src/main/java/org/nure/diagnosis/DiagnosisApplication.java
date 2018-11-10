package org.nure.diagnosis;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

@SpringBootApplication
@ComponentScan("org.nure.diagnosis")
@EnableNeo4jRepositories("org.nure.diagnosis.repositories")
public class DiagnosisApplication {

    @Value("${database.uri}")
    private String uri;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(DiagnosisApplication.class, args);
    }

    @Bean
    public Configuration configuration() {
        Configuration configuration = new Configuration.Builder()
                .uri(this.uri)
                .credentials(this.username, this.password)
                .build();
        return configuration;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(configuration(), "org.nure.diagnosis.models");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() throws Exception {
        return new Neo4jTransactionManager(sessionFactory());
    }
}

package iti.jets.misk.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer() {
        MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.36")
                .withDatabaseName("misk_test")
                .withUsername("testuser")
                .withPassword("testpass")
                .withInitScript("init.sql");
        return container;
    }
}
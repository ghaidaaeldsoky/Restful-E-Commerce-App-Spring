package iti.jets.misk.config;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class TestConfig {



    @PostConstruct
    public void init() {
        // Initialization logic here
        System.out.println("TestConfig initialized");
    }


    @PreDestroy
    public void destroy() {
        // Initialization logic here
        System.out.println("TestCo nfig initialized");
    }
}

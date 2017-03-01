package ru.mike.felix2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by mkopylov on 27.02.17.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"ru.mike.felix2.config"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}


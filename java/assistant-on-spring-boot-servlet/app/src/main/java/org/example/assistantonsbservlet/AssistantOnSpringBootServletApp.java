package org.example.assistantonsbservlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
    HibernateJpaAutoConfiguration.class,
    DataSourceAutoConfiguration.class
})
public class AssistantOnSpringBootServletApp {
    public static void main(String[] args) {
        SpringApplication.run(AssistantOnSpringBootServletApp.class, args);
    }
}

package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.example.model.db.Task;
import org.example.manager.taskManager.TaskRepository;

@SpringBootTest
@Testcontainers
class DemoApplicationTests {

        @Autowired
        private TaskRepository repository;

        @Test
        void contextLoads() {
        }

        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
                        .withDatabaseName("task_database")
                        .withUsername("myuser")
                        .withPassword("secret");

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {

                registry.add("spring.datasource.url",
                                postgres::getJdbcUrl);

                registry.add("spring.datasource.username",
                                postgres::getUsername);

                registry.add("spring.datasource.password",
                                postgres::getPassword);
        }

        @Test
        void shouldCreateTask() {

                Task task = new Task();

                task.setTitle("Docker");
                task.setAuthor("Syeda");
                task.setStatus("Pending");
                task.setDescription("Integration Test");

                Task saved = repository.save(task);

                assertNotNull(saved.getId());

                assertEquals("Docker", saved.getTitle());

        }

}

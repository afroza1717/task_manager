package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.manager.taskManager.TaskApi;
import org.example.manager.taskManager.TaskRepository;
import org.example.model.db.Task;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@WebMvcTest(TaskApi.class)
class TaskApiTests {

        @Autowired
        MockMvc mockMvc;

        // @Autowired
        // ObjectMapper objectMapper;

        @MockitoBean
        TaskRepository taskRepository;

        @Test
        void shouldCreateTask() throws Exception {
                final ObjectMapper objectMapper = new ObjectMapper();

                Task task = new Task();
                task.setId(1L);
                task.setTitle("Docker");
                task.setAuthor("Syeda");
                task.setDescription("Testing");
                task.setStatus("Pending");

                when(taskRepository.save(any(Task.class)))
                                .thenReturn(task);

                mockMvc.perform(post("/create_task")
                                .contentType(MediaType.APPLICATION_JSON)
                                // .content("{\"title\":\"Docker\",\"author\":\"Syeda\",\"description\":\"Testing\",\"status\":\"Pending\"}"))
                                .content(objectMapper.writeValueAsString(task)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Docker"))
                                .andExpect(jsonPath("$.author").value("Syeda"));

                verify(taskRepository).save(any(Task.class));
        }

        @Test
        void shouldReturnTaskById() throws Exception {
                Task task = new Task();
                task.setId(1L);
                task.setTitle("Spring Boot");

                when(taskRepository.findById(1L))
                                .thenReturn(Optional.of(task));

                mockMvc.perform(get("/task_list/1/"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Spring Boot"));

                verify(taskRepository).findById(1L);
        }

        @Test
        void shouldReturnAllTasks() throws Exception {
                Task t1 = new Task();
                t1.setId(1L);
                t1.setTitle("Task 1");

                Task t2 = new Task();
                t2.setId(2L);
                t2.setTitle("Task 2");

                when(taskRepository.findAll())
                                .thenReturn(List.of(t1, t2));

                mockMvc.perform(get("/task_list/"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2));

                verify(taskRepository).findAll();
        }

        ///partial_update/{taskId}/
        @Test
        void shouldUpdateTask() throws Exception {

                Task task = new Task();
                task.setId(1L);
                task.setTitle("Updated");
                task.setStatus("In Progress");
                task.setDescription("Testing Updated");


                when(taskRepository.findById(1L))
                                .thenReturn(Optional.of(task));

                when(taskRepository.save(any(Task.class)))
                                .thenReturn(task);

                mockMvc.perform(put("/partial_update/1/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                  "title":"Updated",
                                                  "status":"Sone",
                                                  "author":"Syeda",
                                                  "description":"Testing Updated"
                                                }
                                                """))
                                .andDo(result -> {
                                        System.out.println("STATUS: " + result.getResponse().getStatus());
                                        System.out.println("BODY: " + result.getResponse().getContentAsString());
                                        System.out.println("ERROR: " + result.getResolvedException());
                                })
                                .andExpect(status().isOk());

                verify(taskRepository).save(any(Task.class));
        }
}
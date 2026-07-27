package org.example.manager.taskManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.model.db.Task;
import org.example.util.HelperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TaskApi {
    @Autowired
    TaskRepository taskRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskApi.class);

    @GetMapping(path = "/task_list/")
    @ResponseBody
    public List<Task> getAll() {
        Timestamp startTime = HelperUtils.nowTimestamp();
        Timestamp endTime = HelperUtils.nowTimestamp();
        logger.info("Getting all records takes: " + Long.valueOf(endTime.getTime() - startTime.getTime()).toString()
                + " milli seconds. ");

        List<Task> taskList = taskRepository.findAll();

        return taskList;
    }

    @GetMapping(value = "/task_list/{taskId}/")
    @ResponseBody
    public Optional<Task> getSingleTask(@PathVariable(value = "taskId") Long taskId) {
        Timestamp startTime = HelperUtils.nowTimestamp();
        Timestamp endTime = HelperUtils.nowTimestamp();
        logger.info("Getting all records takes getAllParent: "
                + Long.valueOf(endTime.getTime() - startTime.getTime()).toString()
                + " milli seconds. ");

        logger.info("task id:{} ", taskId);

        return taskRepository.findById(taskId);
    }

    @PostMapping(value = "/create_task", consumes = MediaType.APPLICATION_JSON_VALUE) 
    public @ResponseBody Task createTasks(@RequestBody Task task) throws IOException {
        // Create a new Task object and set the fields
   
        logger.info("Creating task with title: " + task.getTitle() + ", author: " + task.getAuthor() + ", paragraph: " + 
             task.getDescription() + ", is_publish: " + task.getStatus());
         
        Task savedTask = taskRepository.save(task);
        return savedTask;
    }

    @PutMapping(value = "/partial_update/{taskId}/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Task updateTask(@PathVariable(value = "taskId") Long taskId, 
                @RequestBody Task task) {
            
        Task updatedTask = null;
        Optional<Task> taskToEdit = taskRepository.findById(taskId);
        try {

            if (taskToEdit.isPresent()) {
                updatedTask = taskToEdit.get();
                updatedTask.setTitle(task.getTitle());
                updatedTask.setAuthor(task.getAuthor());

                updatedTask = taskRepository.save(updatedTask);
                logger.info("task updated!");

                return updatedTask;
            }
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        }

        return updatedTask;
    }

    @DeleteMapping(value = "/delete/{taskId}/")
    @ResponseBody
    public String deleteItem(@PathVariable(value = "taskId") Long taskId) {
        Timestamp startTime = HelperUtils.nowTimestamp();
        taskRepository.deleteById(taskId);
        Timestamp endTime = HelperUtils.nowTimestamp();
        logger.info("Deleting data takes: " + Long.valueOf(endTime.getTime() - startTime.getTime()).toString()
                + " milli seconds. ");

        return HelperUtils.SUCCESS_MESSAGE;
    }

}

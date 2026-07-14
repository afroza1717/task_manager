package org.example.manager.taskManager;

import org.example.model.db.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("taskRepository")
public interface TaskRepository extends JpaRepository<Task, Long> {

}

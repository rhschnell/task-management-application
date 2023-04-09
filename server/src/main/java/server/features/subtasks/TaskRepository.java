package server.features.subtasks;

import commons.Task;
import org.springframework.stereotype.Repository;
import server.features.CustomRepository;

@Repository
public interface TaskRepository extends CustomRepository<Task, Long> {
}

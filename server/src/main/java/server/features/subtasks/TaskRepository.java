package server.features.subtasks;

import commons.Task;
import server.features.CustomRepository;

@Repository
public interface TaskRepository extends CustomRepository<Task, Long> {
}

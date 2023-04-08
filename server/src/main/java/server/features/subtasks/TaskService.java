package server.features.subtasks;

import commons.Task;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TaskService implements RepositoryService<Task, Long> {
    private final TaskRepository repo;

    /**
     * Constructor for the TaskService
     * @param repo a TaskRepository instance
     */
    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts given task into repository
     *
     * @param task the task to be inserted
     */
    @Override
    public Task insert(Task task) {
        if (task == null) {
            throw new IllegalArgumentException();
        }
        return repo.save(task);
    }

    /**
     * Deletes task with given ID from repository
     *
     * @param id id of task to be deleted
     */
    @Override
    public void delete(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        repo.deleteById(id);
    }

    /**
     * Finds and returns task with given ID from repository
     *
     * @param id id of entity to be found and returned
     * @return task corresponding to given id
     */
    @Override
    public Task getByID(Long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException();
        }
        return repo.getById(id);
    }

    /**
     * Returns all tasks from repository
     *
     * @return list of all tasks from repository
     */
    @Override
    public List<Task> getAll() {
        return repo.findAll();
    }
}

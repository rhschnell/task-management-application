package server.features.subtasks;

import commons.Route;
import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(Route.TASK)
public class TaskController {

    private final TaskService service;

    /**
     * Creates a new TaskController
     * @param service Instance of task repository
     */
    public TaskController(TaskService service) {
        this.service = service;
    }


    /**
     * Adds a task to the database
     * @param task The task list to add
     * @return ResponseEntity with code 200 if successful or occurring error code
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Void> insert(@RequestBody Task task) {
        try {
            service.insert(task);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a task from the database
     * @param id The id of the task to delete
     * @return ResponseEntity with code 200 if successful or occurring error code
     *
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets a specific task from the database
     * @param id The id of the board
     * @return ResponseEntity with code 200, containing the requested task, if successful or occurring error code
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        try {
            Task returnTask = service.getByID(id);
            return ResponseEntity.ok(returnTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Gets all tasks from the database
     * @return ResponseEntity containing a list of all tasks
     */
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}

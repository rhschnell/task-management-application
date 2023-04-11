package client.serverUtils;

import commons.Route;
import commons.Task;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class TaskUtils {
    private final ServerUtils serverUtils;

    /**
     * Creates a new TaskUtils object
     *
     * @param serverUtils The ServerUtils object (injected) to use in requests.
     */
    @Inject
    public TaskUtils(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    /**
     * Sends a get request to the server to get all cards from the database
     *
     * @return All cards in the database
     */
    public List<Task> getTasks() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TASK)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Sends a post request to the server to add a Task to the database
     *
     * @param card The card to add to the database
     */
    public void insertTask(Task card) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TASK)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Task.class);
    }

    /**
     * Sends a request to the server to delete a certain card from the database
     *
     * @param id the id of the card to delete
     */
    public void deleteTask(long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TASK + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(Task.class);
    }

    /**
     * Sends a request to the server to delete a certain card from the database
     *
     * @param id the id of the card to delete
     * @return The task with the given ID
     */
    public Task getTask(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverUtils.getServer()).path(Route.TASK + "/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(Task.class);
    }
}

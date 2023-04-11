package client.serverUtils;

import commons.Route;
import commons.Tag;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.Mockito.*;


class TagUtilsTest {

    private ServerUtils serverUtils;
    private TagUtils tagUtils;

    private HTTPMocker mocker;

    @BeforeEach
    void setup() {
        serverUtils = Mockito.mock(ServerUtils.class);
        tagUtils = new TagUtils(serverUtils);
        mocker = new HTTPMocker(Tag.class);
        tagUtils.setClient(mocker.clientMock);
        when(serverUtils.getServer()).thenReturn("http://nonexisting:123/");
    }

    @Test
    void addTagToCard() {
        Tag tag = new Tag();
        long cardId = 1;

        tagUtils.addTagToCard(tag, cardId);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path("api/tagToCard");
        verify(mocker.targetMock).queryParam("cardId", cardId);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
        verify(serverUtils).getServer();
    }

    @Test
    void insertTag() {
        Tag tag = new Tag();
        tagUtils.insertTag(tag);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.TAG);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
        verify(serverUtils).getServer();
    }

    @Test
    void deleteTag() {
        long id = 1;
        tagUtils.deleteTag(id);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.TAG + '/' + id);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).delete(Response.class);
        verify(serverUtils).getServer();
    }

    @Test
    void getBoardTags() {
        String key = "someKey";
        tagUtils.getBoardTags(key);
        verify(mocker.clientMock).target("http://nonexisting:123/");
        verify(mocker.targetMock).path(Route.BOARD + "/getBoardTags/" + key);
        verify(mocker.targetMock).request(APPLICATION_JSON);
        verify(mocker.builderMock).accept(APPLICATION_JSON);
        verify(mocker.builderMock).get(new GenericType<List<Tag>>() {
        });
        verify(serverUtils).getServer();
    }
}

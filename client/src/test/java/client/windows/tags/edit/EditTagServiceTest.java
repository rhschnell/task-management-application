package client.windows.tags.edit;

import client.serverUtils.TagUtils;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class EditTagServiceTest {
    private EditTagService editTagService;
    private TagUtils server;

    @BeforeEach
    public void setup(){
        server = mock(TagUtils.class);
        editTagService = new EditTagService(server);
    }

    @Test
    public void constructorTest(){
        assertNotNull(editTagService);
    }

    @Test
    public void insertBoardTest(){
        Tag tag = new Tag("New Test Tag", "#FFFFFF");
        editTagService.insertTag(tag);

        verify(server, times(1)).insertTag(tag);
    }

}

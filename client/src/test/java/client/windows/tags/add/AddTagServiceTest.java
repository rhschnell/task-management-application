package client.windows.tags.add;

import client.serverUtils.BoardUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class AddTagServiceTest {
    private AddTagService addTagService;
    private BoardUtils server;

    @BeforeEach
    public void setup(){
        server = mock(BoardUtils.class);
        addTagService = new AddTagService(server);
    }

    @Test
    public void constructorTest(){
        assertNotNull(addTagService);
    }

}

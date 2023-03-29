package client.windows.tags.add;

import static org.mockito.Mockito.*;

import client.serverUtils.BoardUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void insertBoardTest(){
        Board b = new Board("Test Key", "Test Board", null, null);
        addTagService.insertBoard(b);

        verify(server, times(1)).insertBoard(b);
    }

}

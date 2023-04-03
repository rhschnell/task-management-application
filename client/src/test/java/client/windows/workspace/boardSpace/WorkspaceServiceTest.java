package client.windows.workspace.boardSpace;

import client.serverUtils.BoardUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class WorkspaceServiceTest {
  private WorkspaceService workspaceService;

  @Mock
  private BoardUtils boardUtils;

  @BeforeEach
  public void setup() {
      MockitoAnnotations.initMocks(this);
      workspaceService = new WorkspaceService(boardUtils);
  }

  @Test
  public void insertBoard() {
      Board board = new Board();
      workspaceService.insertBoard(board);
      verify(boardUtils, times(1)).insertBoard(board);
  }

  @Test
  public void getBoard() {
      String key = "test board";
      workspaceService.getBoard(key);
      verify(boardUtils, times(1)).getBoard(key);
  }

  @Test
  public void deleteBoard() {
      Board board = new Board();
      workspaceService.deleteBoard(board);
      verify(boardUtils, times(1)).deleteBoard(board.getKey());
  }
}
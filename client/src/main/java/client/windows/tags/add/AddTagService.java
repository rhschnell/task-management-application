package client.windows.tags.add;

import client.serverUtils.BoardUtils;
import client.windows.workspace.boardSpace.WorkspaceCtrl;
import com.google.inject.Inject;
import commons.Board;

public class AddTagService {
    private final BoardUtils server;

    /**
     * Constructor for AddTagService
     * @param server
     */
    @Inject AddTagService(BoardUtils server){
        this.server = server;
    }

    public void insertBoard(Board board){
        server.insertBoard(board);
    }
}

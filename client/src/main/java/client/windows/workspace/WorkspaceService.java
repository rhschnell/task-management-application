package client.windows.workspace;

import client.serverUtils.BoardUtils;
import client.utils.HelperMethods;
import client.utils.Scenes;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

public class WorkspaceService {
    private final HelperMethods hm;
    private final BoardUtils server;

    private Board shownBoard;

    @Inject
    public WorkspaceService(HelperMethods hm, BoardUtils server) {
        this.hm = hm;
        this.server = server;
    }

    public void disconnect() {
        hm.setScene(Scenes.USER);
    }

    public void loadBoard(String targetKey, ) {
        try {
            shownBoard = server.getBoard(targetKey);
        } catch (NotFoundException | BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null);
            server.insertBoard(shownBoard);
        }

        displayBoard();
    }

    public void displayBoard() {

    }
}

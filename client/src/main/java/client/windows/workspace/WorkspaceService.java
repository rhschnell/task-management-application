package client.windows.workspace;

import client.utils.HelperMethods;
import client.utils.Scenes;
import com.google.inject.Inject;

public class WorkspaceService {
    private final HelperMethods hm;

    @Inject
    public WorkspaceService(HelperMethods hm) {
        this.hm = hm;
    }

    public void disconnect() {
        hm.setScene(Scenes.USER);
    }

    public void loadBoard(String targetKey) {
        try {
            shownBoard = server.getBoard(targetKey);
        } catch (NotFoundException | BadRequestException e) {
            shownBoard = new Board(targetKey, targetKey, null);
            server.insertBoard(shownBoard);
        }

        displayBoard();
    }
}

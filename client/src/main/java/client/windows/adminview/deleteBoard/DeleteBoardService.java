package client.windows.adminview.deleteBoard;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;

public class DeleteBoardService {
    private final BoardUtils server;

    @Inject
    public DeleteBoardService(BoardUtils server) {
        this.server = server;
    }


    public void deleteBoard(String key) {
        server.deleteBoard(key);
    }
}

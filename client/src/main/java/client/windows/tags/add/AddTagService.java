package client.windows.tags.add;

import client.serverUtils.BoardUtils;
import com.google.inject.Inject;
import commons.Tag;

public class AddTagService {
    private final BoardUtils server;

    /**
     * Constructor for AddTagService
     * @param server Server that handles boards
     */
    @Inject AddTagService(BoardUtils server){
        this.server = server;
    }

    /**
     * Inserts the tag into the server
     * @param key the key of the board where we need to insert the tag
     * @param tag the tag to be inserted
     */
    public void insertTag(String key, Tag tag){
        server.insertNewTag(key, tag);
    }
}

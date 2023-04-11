package client.windows.tags.edit;

import client.serverUtils.TagUtils;
import com.google.inject.Inject;
import commons.Tag;

public class EditTagService {
    private final TagUtils server;

    /**
     * Constructor for the EditTagService
     *
     * @param server The corresponding server
     */
    @Inject
    public EditTagService(TagUtils server) {
        this.server = server;
    }

    /**
     * Method to insert a new tag into the database
     *
     * @param tag The tag to be inserted
     */
    public void insertTag(Tag tag) {
        server.insertTag(tag);
    }

}

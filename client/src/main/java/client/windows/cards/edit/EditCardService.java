package client.windows.cards.edit;

import client.serverUtils.CardUtils;
import client.serverUtils.TagUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;
import commons.Task;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class EditCardService {
    private final CardUtils server;
    private final TagUtils tagUtils;

    private List<Tag> appliedTags;
    private Card card;

    private String boardKey;

    @Inject
    public EditCardService(CardUtils server, TagUtils tagUtils) {
        this.server = server;
        this.tagUtils=tagUtils;
        appliedTags = new ArrayList<>();
    }

    /**
     * Sets the card that needs to be stored in the edit-card pop-up
     * @param card the card that needs to be stored
     */
    public void setCard(Card card) {
        this.card = card;
        appliedTags=card.getTags();
        if(appliedTags ==null)
            appliedTags=new ArrayList<>();
    }

    /**
     * Returns the card stored in the editCard pop-up
     * @return the card that needs to be returned
     */
    public Card getCard() {
        return card;
    }

    /**
     * Returns the available tags of the card (the tags that have not been applied yet)
     * @return the list of tags the that have not been applied to the card yet
     */
    public List<Tag> getAvailableTags()
    {
        List<Tag> availableTags = new ArrayList<>(tagUtils.getBoardTags(boardKey));
        availableTags.removeAll(appliedTags);
        return availableTags;
    }
    public void insertCard(Card card) {
        server.insertCard(card);
    }


    /**
     * Returns the tags from the service
     * //TODO get the tags from the board
     * @return the list of tags of the board
     */
    public List<Tag> getTags()
    {
        return tagUtils.getBoardTags(boardKey);
    }

    /**
     * Adds a tag to the list of the applied tags of the card
     * @param tag the tag that needs to be added to the card
     */
    public void applyTag(Tag tag)
    {
        if(appliedTags!=null && !appliedTags.contains(tag))
            appliedTags.add(tag);
    }

    /**
     * Returns a list of tags that are applied on this card
     * @return Applied tags for this card
     */
    public List<Tag> getAppliedTags() {
        return appliedTags;
    }



    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    /**
     * Updates the card to reflect a drag-and-drop action, where tasks are reordered. Their
     * priorities are reordered and reflected in the card.
     * @param draggedTask The task to insert at a new place
     * @param newIndex The index of the new place to insert the dragged task
     * @throws NotFoundException if the task to drag is not part of the provided list
     */
    public void dragAndDropDB(Task draggedTask, int newIndex) {
        // Reflect the priority shift on a list

        // Check if the draggedTask is inside the list
        if (!card.getSubTasks().contains(draggedTask)) throw new NotFoundException();

        // Shift the tasks around (implicitly done by addSubTask)
        card.deleteSubTask(draggedTask);
        card.addSubTask(newIndex, draggedTask);
    }
}

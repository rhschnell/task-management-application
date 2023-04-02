package client.windows.cards.add;

import client.serverUtils.CardListUtils;
import client.serverUtils.TagUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import commons.Tag;
import commons.Task;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AddCardService {
    private final CardListUtils server;
    private final TagUtils tagUtils;
    private List<Tag> appliedTags;

    private CardList cardList;
    private String boardKey;

    @Inject
    public AddCardService(CardListUtils server, TagUtils tagUtils) {

        this.server = server;
        this.tagUtils = tagUtils;
        appliedTags = new ArrayList<>();
    }

    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    /**
     * Sets the cardList that will need to be updated with the new card
     *
     * @param cardList
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
    }

    /**
     * Inserts an updated cardList into the server
     */
    public void insertCardList() {
        server.insertCardList(cardList);
    }

    /**
     * Returns the CardList from where the AddCard method was called
     *
     * @return the cardList
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * Returns the tags from the service
     * //TODO get the tags from the board
     *
     * @return the list of tags of the board
     */
    public List<Tag> getTags() {
        return tagUtils.getBoardTags(boardKey);
    }

    /**
     * Returns the tags that have been already applied to the card
     *
     * @return the list of cards applied
     */
    public List<Tag> getAppliedTags() {
        return appliedTags;
    }

    /**
     * Sets the appliedTags of the card
     *
     * @param newTagList the tags that have been applied to the card
     */
    public void setAppliedTags(List<Tag> newTagList) {
        this.appliedTags = newTagList;
    }

    /**
     * Adds a tag to the list of the applied tags of the card
     *
     * @param tag the tag that needs to be added to the card
     */
    public void applyTag(Tag tag) {
        if (!appliedTags.contains(tag))
            appliedTags.add(tag);
    }


    /**
     * Returns the available tags of the card (the tags that have not been applied yet)
     *
     * @return the list of tags the that have not been applied to the card yet
     */
    public List<Tag> getAvailableTags() {
        List<Tag> availableTags = new ArrayList<>();
        availableTags.addAll(tagUtils.getBoardTags(boardKey));
        availableTags.removeAll(appliedTags);
        return availableTags;
    }

    /**
     * Adds a new card to the cardList that needs to be updated
     *
     * @param card the card that needs to be added
     */
    public void addCard(Card card) {
        cardList.addCard(card);
    }

    /**
     * In-place algorithm to swap tasks around and update their priorities
     * @param draggedTask The task to insert at a new place
     * @param newIndex The index of the new place to insert the dragged task
     * @param subtasks The list in which the inserting should take place
     * @throws NotFoundException if the task to drag is not part of the provided list
     */
    public void reorderTasks(Task draggedTask, int newIndex, List<Task> subtasks) {
        // Reflect the priority shift on a list

        // Check if the draggedTask is inside the list
        if (!subtasks.contains(draggedTask)) throw new NotFoundException();

        // Shift the tasks around
        int oldIndex = subtasks.indexOf(draggedTask);
        long priorityOfPredecessor = subtasks.get(newIndex).getPriority();

        subtasks.remove(draggedTask);
        subtasks.add(newIndex, draggedTask);

        // Assign the priority of the newly inserted task
        draggedTask.setPriority(priorityOfPredecessor + 1);

        // All tasks that are after the inserted one need to update their priorities
        for (int i = newIndex + 1; i < subtasks.size(); i++){
            long oldPriority = subtasks.get(i).getPriority();
            subtasks.get(i).setPriority(oldPriority + 1);
        }
    }
}


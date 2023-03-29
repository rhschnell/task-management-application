package client.windows.cards.edit;

import client.serverUtils.CardUtils;
import client.serverUtils.TagUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.Tag;

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
        List<Tag> availableTags = new ArrayList<>();
        availableTags.addAll(getTags());
        availableTags.removeAll(card.getTags());
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
     * Returns the tags that have been already applied to the card
     * @return the list of cards applied
     */
    public List<Tag> getAppliedTags()
    {
        return appliedTags;
    }

    /**
     * Sets the appliedTags of the card
     * @param newTagList the tags that have been applied to the card
     */
    public void setAppliedTags(List<Tag> newTagList)
    {
        this.appliedTags=newTagList;
    }

    /**
     * Adds a tag to the list of the applied tags of the card
     * @param tag the tag that needs to be added to the card
     */
    public void applyTag(Tag tag)
    {
        appliedTags.add(tag);
    }

    /**
     * Removes a tag from the list of the applied tags of the card
     * @param tag the tag that needs to be removed
     */
    public void removeAppliedTag(Tag tag)
    {
        appliedTags.remove(tag);
    }

    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }
}

package client.windows.cards.add;

import client.serverUtils.CardListUtils;
import client.serverUtils.TagUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.CardList;
import commons.Tag;

import java.util.ArrayList;
import java.util.List;

public class AddCardService {
    private final CardListUtils server;
    private final TagUtils tagUtils;
    private List<Tag> appliedTags;

    private CardList cardList;

    @Inject
    public AddCardService(CardListUtils server, TagUtils tagUtils) {

        this.server = server;
        this.tagUtils = tagUtils;
        appliedTags = new ArrayList<>();
    }

    /**
     * Sets the cardList that will need to be updated with the new card
     * @param cardList
     */
    public void setCardList(CardList cardList)
    {
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
     * @return the cardList
     */
    public CardList getCardList() {
        return cardList;
    }

    /**
     * Returns the tags from the service
     * //TODO get the tags from the board
     * @return the list of tags of the board
     */
    public List<Tag> getTags()
    {
        return tagUtils.getTags();
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

    /**
     * Returns the available tags of the card (the tags that have not been applied yet)
     * @return the list of tags the that have not been applied to the card yet
     */
    public List<Tag> getAvailableTags()
    {
        List<Tag> availableTags = new ArrayList<>();
        availableTags.addAll(tagUtils.getTags());
        availableTags.removeAll(appliedTags);
        return availableTags;
    }

    /**
     * Adds a new card to the cardList that needs to be updated
     * @param card the card that needs to be added
     */
    public void addCard(Card card)
    {
        cardList.addCard(card);
    }
}

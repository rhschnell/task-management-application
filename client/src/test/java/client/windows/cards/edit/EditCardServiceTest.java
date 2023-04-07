package client.windows.cards.edit;

import client.serverUtils.CardUtils;
import client.serverUtils.TagUtils;
import commons.Card;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EditCardServiceTest {
    private EditCardService editCardService;
    private CardUtils server;

    private TagUtils tagUtils;

    @BeforeEach
    void setUp() {
        server = Mockito.mock(CardUtils.class);
        tagUtils = Mockito.mock(TagUtils.class);
        when(tagUtils.getBoardTags(anyString())).thenReturn(List.of(
                new Tag("Tag1", "000000"),
                new Tag("Tag2", "00CF00")
        ));
        editCardService = new EditCardService(server, tagUtils);
    }

    @Test
    void setCard() {
        Card card = new Card();
        editCardService.setCard(card);
        assertEquals(card, editCardService.getCard());
    }

    @Test
    void getCard() {
        assertNull(editCardService.getCard());
    }

    @Test
    void getAvailableTags() {
        List<Tag> boardTags = tagUtils.getBoardTags("some key");
        editCardService.setBoardKey("some key");
        editCardService.applyTag(boardTags.get(0));
        assertEquals(boardTags.subList(1, boardTags.size()), editCardService.getAvailableTags());
    }

    @Test
    void insertCard() {
        Card card = new Card();
        editCardService.insertCard(card);
        verify(server).insertCard(card);
    }

    @Test
    void getTags() {
        editCardService.setBoardKey("key");
        List<Tag> tags = editCardService.getTags();
        verify(tagUtils).getBoardTags("key");
        assertEquals(List.of(
                new Tag("Tag1", "000000"),
                new Tag("Tag2", "00CF00")
        ), tags);


    }

    @Test
    void applyTag() {
        Tag tag = new Tag();
        editCardService.applyTag(tag);
        assertEquals(List.of(tag), editCardService.getAppliedTags());
    }

    @Test
    void applyTagDuplicate() {
        Tag tag = new Tag();
        editCardService.applyTag(tag);
        editCardService.applyTag(tag); // Apply it twice should still result in one applied tag
        assertEquals(List.of(tag), editCardService.getAppliedTags());
    }

    @Test
    void getAppliedTags(){
        assertEquals(new ArrayList<Tag>(), editCardService.getAppliedTags());
    }

    @Test
    void getBoardKey() {
        assertNull(editCardService.getBoardKey());
    }

    @Test
    void setBoardKey() {
        editCardService.setBoardKey("Key");
        assertEquals("Key", editCardService.getBoardKey());
    }
}
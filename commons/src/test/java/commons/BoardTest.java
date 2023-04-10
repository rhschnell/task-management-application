package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    private Board board2;

    private CardList cardList;

    @BeforeEach
    void setUp() {
        board = new Board("My Board", null, new ArrayList<>(), null);
        cardList = new CardList();
        Card card = new Card(
                "My Card",
                "Text",
                null,
                null,
                new ArrayList<>()
        );
        cardList.addCard(card);
        board2 = new Board("My Board", null, null, null);
        board2.addList(cardList);
    }

    @Test
    void notEmptyConstructorTest() {
        assertNotNull(board);
    }

    @Test
    void emptyConstructorTest() {
        assertNotNull(new Board());
    }

    @Test
    void customConstructorTest() {
        Board board = new Board("key", "title", null, null, null);
        // When a null value is passed for these fields, new lists should be created instead of
        // assigning null to the corresponding fields
        assertNotNull(board.getCardLists());
        assertNotNull(board.getTagList());
        assertNotNull(board.getPresetList());
        assertEquals("", board.getPassword());
    }

    @Test
    void addNewList() {
        CardList cardList = new CardList();
        board.addList();
        assertEquals(cardList, board.getCardLists().get(0));
    }

    @Test
    void testAddMadeList() {
        CardList cardList = new CardList();
        Card card = new Card(
                "My Card",
                "Text",
                null,
                null,
                new ArrayList<>()
        );
        cardList.addCard(card);
        board.addList(cardList);
        assertEquals(cardList, board.getCardLists().get(0));
    }

    @Test
    void removeList() {
        board2.removeList(cardList);
        assertEquals(new ArrayList<>(), board2.getCardLists());
    }

    @Test
    void removeListByIndex() {
        board2.removeListByIndex(0);
        assertEquals(new ArrayList<>(), board2.getCardLists());
    }

    @Test
    void getAmountList() {
        assertEquals(1, board2.getAmountList());
    }

    @Test
    void getKey() {
        assertEquals(null, board.getKey());
    }

    @Test
    void getTitle() {
        assertEquals("My Board", board.getTitle());
    }

    @Test
    void getCardLists() {
        assertEquals(new ArrayList<>(), board.getCardLists());
    }

    @Test
    void setKey() {
        board.setKey("000002");
        assertEquals("000002", board.getKey());
    }

    @Test
    void setTitle() {
        board.setTitle("New Title");
        assertEquals("New Title", board.getTitle());
    }

    @Test
    void getTagList() {
        assertNotNull(board.getTagList());
    }

    @Test
    void setTagList() {
        ArrayList<Tag> list = new ArrayList<>();
        board.setTagList(list);
        assertEquals(list, board.getTagList());
    }

    @Test
    void setPassword() {
        assertEquals("", board.getPassword());
    }

    @Test
    void getPassword() {
        board.setPassword("password");
        assertEquals("password", board.getPassword());
    }

    @Test
    void verifyPasswordNoPassword() {
        assertTrue(board.verifyPassword("anything here should work blabla"));
    }

    @Test
    void verifyPasswordWithSetPassword() {
        board.setPassword("!oopp-23.passw0rd");
        assertTrue(board.verifyPassword("!oopp-23.passw0rd"));
    }

    @Test
    void getPresetList() {
        assertEquals(new ArrayList<>(), board.getPresetList());
    }

    @Test
    void setPresetList() {
        List<CardColorPreset> presetList = List.of(new CardColorPreset(), new CardColorPreset());
        board.setPresetList(presetList);
        assertEquals(presetList, board.getPresetList());
    }


    @Test
    void setProtected() {
        board.setProtected(true);
        assertTrue(board.isProtected());
    }

    @Test
    void getProtected() {
        assertFalse(board.isProtected());
    }


    @Test
    void addTag() {
        Tag tag = new Tag();
        board.addTag(tag);
        assertEquals(tag, board.getTagList().get(0));
    }

    @Test
    void removeTag() {
        Tag tag = new Tag();
        board.addTag(tag);
        board.removeTag(tag);
        assertEquals(0, board.getTagList().size());
    }

    @Test
    void addPreset() {
        CardColorPreset preset = new CardColorPreset();
        assertEquals(new ArrayList<>(), board.getPresetList());
        board.addPreset(preset);
        assertEquals(List.of(preset), board.getPresetList());
    }

    @Test
    void removePreset() {
        CardColorPreset preset = new CardColorPreset();

        board.addPreset(preset);
        assertEquals(List.of(preset), board.getPresetList());

        board.removePreset(preset);
        assertEquals(new ArrayList<>(), board.getPresetList());

    }

    @Test
    void equalsSameObject() {
        assertEquals(board, board);
    }

    @Test
    void equalsSameFields() {
        Board board1 = new Board("Board key", "Title", null, null, null);
        Board board2 = new Board("Board key", "Title", null, null, null);
        assertEquals(board1, board2);
    }

    @Test
    void equalsNull() {
        assertNotEquals(null, board);
    }

    @Test
    void equalsDifferentFields() {
        Board board1 = new Board("Board key", "Title", null, null, null);
        Board board2 = new Board("Board key", "Title", null, null, null);
        board2.addTag(new Tag("This makes board 2 different from board 1", "White"));
        assertNotEquals(board1, board2);
    }

    @Test
    void testHashCode() {
        Board board1 = new Board();
        Board board2 = new Board();
        assertEquals(board1.hashCode(), board2.hashCode());
    }
}


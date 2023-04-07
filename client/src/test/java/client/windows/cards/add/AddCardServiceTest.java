package client.windows.cards.add;

import client.serverUtils.CardColorPresetUtils;
import client.serverUtils.CardListUtils;
import client.serverUtils.TagUtils;
import commons.Card;
import commons.CardList;
import commons.Tag;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddCardServiceTest {

    private AddCardService addCardService;
    private CardListUtils server;
    private TagUtils tagUtils;
    private CardColorPresetUtils cardColorPresetUtils;

    @BeforeEach
    void setUp() {
        server = Mockito.mock(CardListUtils.class);
        tagUtils = Mockito.mock(TagUtils.class);
        cardColorPresetUtils = Mockito.mock(CardColorPresetUtils.class);

        when(tagUtils.getBoardTags(anyString())).thenReturn(List.of(
                new Tag("Testing", "FFFFFF"),
                new Tag("BugFixing", "000000"),
                new Tag("Refactoring", "00FF00")
        ));

        addCardService = new AddCardService(server, tagUtils, cardColorPresetUtils);
    }

    @Test
    void getBoardKey() {
        assertNull(addCardService.getBoardKey());
    }

    @Test
    void setBoardKey() {
        addCardService.setBoardKey("New key");
        assertEquals("New key", addCardService.getBoardKey());
    }

    @Test
    void getCardList() {
        assertNull(addCardService.getCardList());
    }

    @Test
    void setCardList() {
        Card card1 = new Card();
        Card card2 = new Card();
        CardList cardList = new CardList();
        cardList.addCard(card1);
        cardList.addCard(card2);
        addCardService.setCardList(cardList);
        assertEquals(cardList, addCardService.getCardList());
    }

    @Test
    void insertCardList() {
        CardList toInsert = new CardList();
        addCardService.setCardList(toInsert);
        addCardService.insertCardList();
        verify(server).insertCardList(toInsert);
    }

    @Test
    void getTags() {
        assertEquals(new ArrayList<Tag>(), addCardService.getTags());
    }

    @Test
    void getAppliedTags() {
        assertEquals(new ArrayList<Tag>(), addCardService.getAppliedTags());
    }

    @Test
    void setAppliedTags() {
        List<Tag> applied = List.of(
                new Tag("Tag 1", "FFFFFF"), new Tag("Tag 2", "000000"));
        addCardService.setAppliedTags(applied);
        assertEquals(applied, addCardService.getAppliedTags());
    }

    @Test
    void applyTag() {
        Tag toApply = new Tag("Some title", "20CE00");
        addCardService.applyTag(toApply);
        assertEquals(List.of(toApply), addCardService.getAppliedTags());
    }

    @Test
    void applyTagDuplicate() {
        Tag toApply = new Tag("Some title", "20CE00");
        addCardService.applyTag(toApply);
        assertEquals(List.of(toApply), addCardService.getAppliedTags());

        addCardService.applyTag(toApply);
        assertEquals(List.of(toApply), addCardService.getAppliedTags());
    }

    @Test
    void getAvailableTagsEmpty() {
        List<Tag> available = addCardService.getAvailableTags();
        verify(tagUtils).getBoardTags(addCardService.getBoardKey());
        assertEquals(new ArrayList<Tag>(), available);
    }

    @Test
    void getAvailableTags() {
        List<Tag> boardTags = tagUtils.getBoardTags("some key");
        addCardService.setBoardKey("some key");
        addCardService.applyTag(boardTags.get(0));
        assertEquals(boardTags.subList(1, boardTags.size()), addCardService.getAvailableTags());
    }


    @Test
    void addCard() {
        Card card = new Card();
        CardList cardListMock = Mockito.mock(CardList.class);
        addCardService.setCardList(cardListMock);
        addCardService.addCard(card);
        verify(cardListMock).addCard(card);
    }

    @Test
    void dragAndDrop() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();

        task1.setPriority(1);
        task2.setPriority(2);
        task3.setPriority(3);

        Card card = new Card();
        List<Task> taskList = Stream.of(task1, task2, task3).collect(Collectors.toList());
        card.setSubTasks(taskList);

        addCardService.reorderTasks(task1, 2, taskList);
        List<Task> expectedOrder = List.of(task2, task3, task1);
        assertEquals(expectedOrder, taskList);

        List<Long> expectedPriorities =
                LongStream.range(1, (taskList.size() + 1)).boxed().collect(Collectors.toList());
        assertEquals(expectedOrder.stream().map(Task::getPriority).collect(Collectors.toList()),
                expectedPriorities);


    }
}
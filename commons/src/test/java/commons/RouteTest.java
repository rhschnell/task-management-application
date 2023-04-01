package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RouteTest {

    @Test
    void testBoardRoute() {
        assertEquals("api/boards", Route.BOARD);
    }

    @Test
    void testCardListRoute() {
        assertEquals("api/card_lists", Route.CARD_LIST);
    }

    @Test
    void testCardRoute() {
        assertEquals("api/cards", Route.CARD);
    }

    @Test
    void testTaskRoute() {
        assertEquals("api/tasks", Route.TASK);
    }

    @Test
    void testTagRoute() {
        assertEquals("api/tags", Route.TAG);
    }

    @Test
    void testAdminRoute() {
        assertEquals("api/admin", Route.ADMIN);
    }
}


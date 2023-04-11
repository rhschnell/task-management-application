package client.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScenesTest {

    @Test
    public void testScenesEnumValues() {
        Scenes[] expectedScenes = {Scenes.STARTUP, Scenes.WORKSPACE, Scenes.ADMINVIEW, Scenes.ADMIN, Scenes.USER};
        assertArrayEquals(expectedScenes, Scenes.values());
    }

    @Test
    public void testScenesEnumValueOf() {
        assertEquals(Scenes.STARTUP, Scenes.valueOf("STARTUP"));
        assertEquals(Scenes.WORKSPACE, Scenes.valueOf("WORKSPACE"));
        assertEquals(Scenes.ADMINVIEW, Scenes.valueOf("ADMINVIEW"));
        assertEquals(Scenes.ADMIN, Scenes.valueOf("ADMIN"));
        assertEquals(Scenes.USER, Scenes.valueOf("USER"));
    }
}

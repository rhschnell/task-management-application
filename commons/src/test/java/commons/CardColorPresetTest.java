package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardColorPresetTest {
    private CardColorPreset preset;

    @BeforeEach
    void setup(){
        preset = new CardColorPreset("New Preset", "0xDEEDE7FF", "0x000000FF");
    }

    @Test
    void notEmptyConstructorTest() {
        assertNotNull(preset);
    }

    @Test
    void emptyConstructorTest(){
        assertNotNull(new CardColorPreset());
    }

    @Test
    void getName(){
        assertEquals("New Preset", preset.getName());
    }

    @Test
    void getBackgroundColor(){
        assertEquals("0xDEEDE7FF", preset.getBackgroundColor());
    }

    @Test
    void getFontColor(){
        assertEquals("0x000000FF", preset.getFontColor());
    }

    @Test
    void isDefault(){
        assertFalse(preset.isDefault());
    }

    @Test
    void setName(){
        preset.setName("New Name");
        assertEquals("New Name", preset.getName());
    }

    @Test
    void setBackgroundColor(){
        preset.setBackgroundColor("0x121212FF");
        assertEquals("0x121212FF", preset.getBackgroundColor());
    }

    @Test
    void setFontColor(){
        preset.setFontColor("0xDEEDE7FF");
        assertEquals("0xDEEDE7FF", preset.getFontColor());
    }

    @Test
    void setDefault(){
        preset.setDefault(true);
        assertTrue(preset.isDefault());
    }

    @Test
    void hashCodeTest() {
        CardColorPreset p = new CardColorPreset("New Preset", "0xDEEDE7FF", "0x000000FF");
        assertEquals(preset.hashCode(), p.hashCode());
    }

    @Test
    void toStringTest() {
        assertEquals( "CardColorPreset(id=0, cards=null, name=New Preset, " +
                "backgroundColor=0xDEEDE7FF, fontColor=0x000000FF, " +
                "isDefault=false)", preset.toString());
    }
}

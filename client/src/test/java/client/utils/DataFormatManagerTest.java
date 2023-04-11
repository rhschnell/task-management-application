package client.utils;

import javafx.scene.input.DataFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataFormatManagerTest {

    private DataFormatManager dataFormatManager;

    @BeforeEach
    void setup(){
        this.dataFormatManager = new DataFormatManager();
    }

    @Test
    void testExistingDataFormats(){
        // Create a new dataFormatManager to test if the code can handle already existing formats
        DataFormatManager dataFormatManager1 = new DataFormatManager();
        assertEquals(DataFormat.lookupMimeType("card"), dataFormatManager1.getCardFormat());
        assertEquals(DataFormat.lookupMimeType("task"), dataFormatManager1.getSubtaskFormat());
    }

    @Test
    void getCardFormat() {
        assertEquals(DataFormat.lookupMimeType("card"), this.dataFormatManager.getCardFormat());
    }

    @Test
    void getSubtaskFormat() {
        assertEquals(DataFormat.lookupMimeType("task"), this.dataFormatManager.getSubtaskFormat());
    }
}
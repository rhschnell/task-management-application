package client.utils;

import javafx.scene.input.DataFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DataFormatManagerTest {
    private DataFormatManager dataFormatManager;

    @BeforeEach
    public void setup() {
        dataFormatManager = new DataFormatManager();
    }

    @Test
    public void getCardFormat() {
        DataFormat cardFormat = dataFormatManager.getCardFormat();
        assertNotNull(cardFormat, "Card format should not be null");
    }

    @Test
    public void getSubtaskFormat() {
        DataFormat subtaskFormat = dataFormatManager.getSubtaskFormat();
        assertNotNull(subtaskFormat, "Subtask format should not be null");
    }
}


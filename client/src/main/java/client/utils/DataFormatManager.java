package client.utils;

import javafx.scene.input.DataFormat;

public class DataFormatManager {
    private final DataFormat cardFormat;
    private final DataFormat subtaskFormat;


    /**
     * Empty constructor for injection
     */
    public DataFormatManager() {
        DataFormat task = DataFormat.lookupMimeType("task");
        DataFormat card = DataFormat.lookupMimeType("card");

        if (task == null) {
            this.subtaskFormat = new DataFormat("task");
        } else {
            this.subtaskFormat = task;
        }

        if (card == null) {
            this.cardFormat = new DataFormat("card");
        } else {
            this.cardFormat = card;
        }

    }

    public DataFormat getCardFormat() {
        return cardFormat;
    }

    public DataFormat getSubtaskFormat() {
        return subtaskFormat;
    }
}

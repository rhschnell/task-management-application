package client.windows.customize.cards.edit;

import client.serverUtils.CardColorPresetUtils;
import com.google.inject.Inject;
import commons.CardColorPreset;

public class EditCardPresetService {
    private final CardColorPresetUtils server;

    /**
     * Constructor for the EditCardPresetService
     * @param server The corresponding server
     */
    @Inject
    public EditCardPresetService(CardColorPresetUtils server){
        this.server = server;
    }

    /**
     * Method to insert a new preset into the database
     * @param preset The preset to be inserted
     */
    public void insertPreset(CardColorPreset preset){
        server.insertPreset(preset);
    }
}

package client.windows.customize;

import client.serverUtils.BoardUtils;
import client.serverUtils.CardColorPresetUtils;
import client.serverUtils.CardListUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardColorPreset;
import commons.CardList;

public class CustomizeService {
    private final BoardUtils boardUtils;
    private final CardColorPresetUtils cardColorPresetUtils;
    private final CardListUtils cardListUtils;

    /**
     * Constructor for CustomizeService
     *
     * @param boardUtils           The BoardUtils server
     * @param cardColorPresetUtils The CardColorPresetUtils server
     * @param cardListUtils        The CardListUtils server
     */
    @Inject
    public CustomizeService(BoardUtils boardUtils, CardColorPresetUtils cardColorPresetUtils,
                            CardListUtils cardListUtils) {
        this.boardUtils = boardUtils;
        this.cardColorPresetUtils = cardColorPresetUtils;
        this.cardListUtils = cardListUtils;
    }

    /**
     * Inserts the board to the server, or updates it
     *
     * @param board The board to be inserted or updated
     */
    public void insertBoard(Board board) {
        boardUtils.insertBoard(board);
    }

    /**
     * Gets the board
     *
     * @param boardKey
     * @return the board
     */
    public Board getBoard(String boardKey) {
        return boardUtils.getBoard(boardKey);
    }


    /**
     * Inserts the cardlist to the server, or updates it
     *
     * @param list The cardlist to be inserted or updated
     */
    public void insertCardList(CardList list) {
        cardListUtils.insertCardList(list);
    }

    /**
     * Inserts the preset to the server, or updates it
     *
     * @param preset The preset to be inserted or updated
     */
    public void insertPreset(CardColorPreset preset) {
        cardColorPresetUtils.insertPreset(preset);
    }

    /**
     * Deletes the preset from the server
     *
     * @param preset The preset to be deleted
     */
    public void deletePreset(CardColorPreset preset) {
        cardColorPresetUtils.deletePreset(preset.getId());
    }
}

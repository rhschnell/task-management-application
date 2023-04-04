package client.windows.workspace.boardSpace;

public class CardInListPosition {
    private int cardIndex;
    private int listIndex;

    public CardInListPosition(int cardIndex, int listIndex) {
        this.cardIndex = cardIndex;
        this.listIndex = listIndex;
    }

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }
}

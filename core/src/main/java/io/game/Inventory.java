package io.game;

public class Inventory {
    private Item[] items;
    private int selectedHotbarIndex;


    public Inventory(int size) {
        items = new Item[size];
    }

    public void setSelectedHotbarIndex(int index) {
        this.selectedHotbarIndex = index;
    }

    public int getSelectedHotbarIndex() {
        return selectedHotbarIndex;
    }

    public Item getItem(int index) {
        return items[index];
    }

    public void setItem(int index, Item item) {
        items[index] = item;
    }
}

package ru.ha3ap.seabattleweb.model.ships;

public class OneDeck extends Ship {

    public OneDeck() {
        this.deck = 1;
        this.decks = 1;
        this.shipPlace = new int[deck][2];
    }
}

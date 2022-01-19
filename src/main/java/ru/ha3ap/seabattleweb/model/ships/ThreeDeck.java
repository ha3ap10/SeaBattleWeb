package ru.ha3ap.seabattleweb.model.ships;

public class ThreeDeck extends Ship {

    public ThreeDeck() {
        this.deck = 3;
        this.decks = 3;
        this.shipPlace = new int[deck][2];
    }
}

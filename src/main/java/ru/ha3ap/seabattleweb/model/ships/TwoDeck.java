package ru.ha3ap.seabattleweb.model.ships;

public class TwoDeck extends Ship {

    public TwoDeck() {
        this.deck = 2;
        this.decks = 2;
        this.shipPlace = new int[deck][2];
    }
}

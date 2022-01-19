package ru.ha3ap.seabattleweb.model.ships;

public class FourDeck extends Ship {

    public FourDeck() {
        this.deck = 4;
        this.decks = 4;
        this.shipPlace = new int[deck][2];
    }
}

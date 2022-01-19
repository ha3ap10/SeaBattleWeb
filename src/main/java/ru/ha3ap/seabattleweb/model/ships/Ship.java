package ru.ha3ap.seabattleweb.model.ships;

import java.util.Arrays;

public abstract class Ship {

    protected int deck;
    protected boolean isVertical;
    protected int decks;
    protected int[][] shipPlace = new int[deck][2];

    public int getDecks() {
        return decks;
    }

    public void setDecks() {
        decks--;
    }

    public void setShipPlace(int i, int x, int y) {
        shipPlace[Math.abs(i)][0] = x;
        shipPlace[Math.abs(i)][1] = y;
    }

    public boolean getShipPlace(int i, int x, int y) {
        return shipPlace[i][0] == x && shipPlace[i][1] == y;
    }

    public int getX(int i) {
        return shipPlace[i][0];
    }

    public int getY(int i) {
        return shipPlace[i][1];
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
    }

    public boolean getVertical() {
        return isVertical;
    }

    public int getDeck() {
        return deck;
    }

    public boolean isShip(int x, int y) {
        for (int i = 0; i < deck; i++) {
            if (shipPlace[i][0] == x && shipPlace[i][1] == y) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < deck; i++) {
            sb
                    .append(getX(i))
                    .append(" - ")
                    .append(getY(i))
                    .append("\n");
        }
        return "Ship{" +
                "deck=" + deck +
                ", shipPlace:\n" + sb +
                '}';
    }
}

package ru.ha3ap.seabattleweb.model;

import java.util.*;

public class Coords {
    private static Coords coords;
    private final List<String> list = Arrays.asList(" ", "а", "б", "в", "г", "д", "е", "ж", "з", "и", "к");

    private Coords() {}

    public static Coords getInstance() {
        if (coords == null) coords = new Coords();
        return coords;
    }

    public int getNumber(String letter) {
        return list.indexOf(letter);
    }

    public String getLetter(int i) {
        return list.get(i).toUpperCase();
    }

    public List<String> getList() {
        return list;
    }
}

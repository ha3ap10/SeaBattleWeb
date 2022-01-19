package ru.ha3ap.seabattleweb;

//import ru.ha3ap.seabattleweb.ships.*;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

//import static constants.Colors.*;
//import static constants.ColorsBackground.*;

public class Main {



/*
    public static final int SIZE = 10;
    public static final char EMPTY = ' ';
    public static final char SHIP = '#';
    public static final char DEAD = 'X';
    public static final char MISS = '\u00B0';*//*'o' '\u02D1'*//*
    public static boolean enemyShot;
    public static int xEnemyShot;
    public static int yEnemyShot;
    public static int xFirstShot;
    public static int yFirstShot;
    public static boolean repeatEnemy;
    public static boolean repeatMy;
    public static final String DELIMITER = "\n" + BLUE_BACKGROUND + "--------------------------------------------" + RESET +
            "     " + BLUE_BACKGROUND + "--------------------------------------------" + RESET;

    public static void main(String[] args) {
        WarField warFieldNew = new WarField();
        Coords coords = Coords.getInstance();

        List<Ship> myShips = warFieldNew.getMyShips();
        List<Ship> enemyShips = warFieldNew.getEnemyShips();


        char[][] enemyField = warFieldNew.getFieldEnemy();
        char[][] myField = warFieldNew.getFieldMy();

        ru.ha3ap.seabattleweb.model.Turns enemyTurn = new ru.ha3ap.seabattleweb.model.Turns(myField, myShips);
        ru.ha3ap.seabattleweb.model.Turns muTurn = new ru.ha3ap.seabattleweb.model.Turns(enemyField, enemyShips);

//        for (char[] row : warField)
//            Arrays.fill(row, EMPTY);
//
//        for (char[] row : warFieldMy)
//            Arrays.fill(row, EMPTY);
//
//        FourDeck[] fourDeck = {
//                new FourDeck()
//        };
//        ThreeDeck[] threeDeck = {
//                new ThreeDeck(),
//                new ThreeDeck()
//        };
//        TwoDeck[] twoDeck = {
//                new TwoDeck(),
//                new TwoDeck(),
//                new TwoDeck()
//        };
//        OneDeck[] oneDeck = {
//                new OneDeck(),
//                new OneDeck(),
//                new OneDeck(),
//                new OneDeck()
//        };
//
//        FourDeck[] fourDeckMy = {
//                new FourDeck()
//        };
//        ThreeDeck[] threeDeckMy = {
//                new ThreeDeck(),
//                new ThreeDeck()
//        };
//        TwoDeck[] twoDeckMy = {
//                new TwoDeck(),
//                new TwoDeck(),
//                new TwoDeck()
//        };
//        OneDeck[] oneDeckMy = {
//                new OneDeck(),
//                new OneDeck(),
//                new OneDeck(),
//                new OneDeck()
//        };
//
//        //заполнение вражеского поля
//        for (Ship ship : fourDeck) {
//            setShips(warField, ship);
//        }
//        for (Ship ship : threeDeck) {
//            setShips(warField, ship);
//        }
//        for (Ship ship : twoDeck) {
//            setShips(warField, ship);
//        }
//        for (Ship ship : oneDeck) {
//            setShips(warField, ship);
//        }
//
//        //заполнение моего поля
//        for (Ship ship : fourDeckMy) {
//            setShips(warFieldMy, ship);
//        }
//        for (Ship ship : threeDeckMy) {
//            setShips(warFieldMy, ship);
//        }
//        for (Ship ship : twoDeckMy) {
//            setShips(warFieldMy, ship);
//        }
//        for (Ship ship : oneDeckMy) {
//            setShips(warFieldMy, ship);
//        }

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        //Орёл - true, решка - false
        boolean coin = random.nextBoolean();
        System.out.println("\nОрёл (0) или Решка (1)???");
        String headsOrTails = scanner.nextLine();
        if (coin) {
            System.out.print("\nВыпал Орёл!\n\n");
            if (headsOrTails.equals("0")) {
                repeatMy = true;
            }
        } else {
            System.out.print("\nВыпала Решка!\n\n");
            if (headsOrTails.equals("1")) {
                repeatMy = true;
            }
        }

        int shipsEnemy = enemyShips.size();
        int shipsMy = myShips.size();

        warFieldNew.printFields();

        if (repeatMy) {
            System.out.println("\nВы ходите первым!");
        } else {
            System.out.println("\nПротивник ходит первым!");
        }

        int moves = 0;
        int countMoves = 0;

        //Главный цикл
        while (shipsEnemy > 0) {
            if (shipsMy == 0)
                break;
//            moves++;

            //ход противника
            while (shipsMy > 0) {
                if (repeatMy) {
                    break;
                }
                int x1 = -1;
                int y1 = -1;

                countMoves++;
                if (enemyShot && countMoves > 30) {
                    xEnemyShot = xFirstShot;
                    yEnemyShot = yFirstShot;
                    countMoves = 0;
                }

                int xPlus = 0;
                int yPlus = 0;

                if (!enemyShot) {
                    x1 = random.nextInt(SIZE);
                    y1 = random.nextInt(SIZE);
                } else {
                    boolean isByX = random.nextBoolean();
                    boolean isPos = random.nextBoolean();


                    if (isByX && isPos) {
                        xPlus = 1;
                        yPlus = 0;
                    }
                    if (isByX && !isPos) {
                        xPlus = -1;
                        yPlus = 0;
                    }
                    if (!isByX && isPos) {
                        xPlus = 0;
                        yPlus = 1;
                    }
                    if (!isByX && !isPos) {
                        xPlus = 0;
                        yPlus = -1;
                    }

                    if (xEnemyShot - xPlus < 0 || xEnemyShot - xPlus >= SIZE) {
                        moves--;
                        continue;
                    }
                    if (yEnemyShot - yPlus < 0 || yEnemyShot - yPlus >= SIZE) {
                        moves--;
                        continue;
                    }
                    x1 = xEnemyShot - xPlus;
                    y1 = yEnemyShot - yPlus;
                }

                if ((x1 < 0 || x1 >= SIZE) || (y1 < 0 || y1 >= SIZE)) {
                    moves--;
                    continue;
                }

                if (myField[x1][y1] == DEAD || myField[x1][y1] == MISS) {
                    moves--;
                    continue;
                }
                if (myField[x1][y1] == SHIP) {

                    System.out.println("Ход противника:   " + (coords.getLetter(x1 + 1)) + (y1 + 1));
                    System.out.printf("%-18s", " ");
                    shipsMy = shotEnemy(myField, myShips, x1, y1, shipsMy);
                    myField[x1][y1] = DEAD;

                    if (isInRange(x1, 1) && isInRange(y1, 1)) {
                        myField[x1 - 1][y1 - 1] = MISS;
                    }
                    if (isInRange(x1, 1) && isInRange(y1, -1)) {
                        myField[x1 - 1][y1 + 1] = MISS;
                    }
                    if (isInRange(x1, -1) && isInRange(y1, 1)) {
                        myField[x1 + 1][y1 - 1] = MISS;
                    }
                    if (isInRange(x1, -1) && isInRange(y1, -1)) {
                        myField[x1 + 1][y1 + 1] = MISS;
                    }

                } else if (myField[x1][y1] == EMPTY) {
                    System.out.printf("\n%-18s%-1s%-2d\n", "Ход противника:", coords.getLetter(x1 + 1), y1 + 1);
                    myField[x1][y1] = MISS;
                    System.out.printf("%23s\n", "Мимо!");
                    repeatEnemy = false;
                    warFieldNew.printFields();
                    break;
                }
            }

            //мой ход
            int x = -1;
            int y = -1;

            while (shipsEnemy > 0) {
                if (repeatEnemy) {
                    break;
                }
                System.out.printf("\n%-18s", "Ваш ход:");
                String input = scanner.nextLine();

                try {
                    x = coords.getNumber(input.charAt(0)) - 1;
                    y = Integer.parseInt(String.valueOf(input.charAt(1))) - 1;
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Нужно ввести две координаты через пробел\n");
                    moves--;
                    continue;
                }

                if ((x < 0 || x >= SIZE) || (y < 0 || y >= SIZE)) {
                    System.out.println("Вернись на поле боя!\n");
                    moves--;
                    continue;
                }

                if (enemyField[x][y] == DEAD || enemyField[x][y] == MISS) {
                    System.out.println("Ну видно же, что нельзя сюда!\n");
                    moves--;
                    continue;
                }

                if (!repeatEnemy && x != -1) {
                    if (enemyField[x][y] == SHIP) {
                        System.out.printf("%-18s", "");
                        shipsEnemy = shot(enemyField, enemyShips, x, y, shipsEnemy);
                        enemyField[x][y] = DEAD;
                        warFieldNew.printFields();
                        if (shipsEnemy == 0) {
                            break;
                        }
                    } else if (enemyField[x][y] == EMPTY) {
                        enemyField[x][y] = MISS;
                        System.out.printf("%23s\n", "Мимо!");
                        repeatMy = false;
                        warFieldNew.printFields();
                        break;
                    }
                }
            }
            countMoves = 0;

            // System.out.printf("%-28s%4d\n", "Осталось вражеских кораблей", shipsEnemy);
            // System.out.printf("%-28s%4d\n\n", "Осталось своих кораблей", shipsMy);
        }
        System.out.println("Game over!");
        if (shipsEnemy == 0) {
            System.out.println("Вы победили!");
        } else {
            System.out.println("Вы проиграли...");
        }
        warFieldNew.printFields();
        // System.out.printf("%-18s%4d\n", "Ходов сделано", moves);
        if (shipsEnemy > 0)
            System.out.printf("%-28s%4d\n", "Осталось вражеских кораблей", shipsEnemy);
        if (shipsMy > 0)
            System.out.printf("%-28s%4d\n\n", "Осталось своих кораблей", shipsMy);
    }

    public static void enemyTurn() {

    }

    public static void myTurn() {

    }

    public static boolean isInRange(int shipPlace, int i) {
        if (shipPlace - i >= 0 && shipPlace - i < SIZE) {
            return true;
        }
        return false;
    }

    public static void kill(char[][] field, Ship ship) {
        for (int i = 0; i < ship.getDeck(); i++) {
            for (int x = -1; x <= 1; x += 2) {
                for (int y = -1; y <= 1; y += 2) {

                    int shipX = ship.getX(i);
                    int shipY = ship.getY(i);

                    if (isInRange(shipX, x) && isInRange(shipY, y) && field[shipX - x][shipY - y] == EMPTY) {
                        field[shipX - x][shipY - y] = MISS;
                    }
                }
            }
        }



//        for (int i = ship.getDeck() - 1; i <= 0; i++) {
//            for (int m = -1; m <= 1; m++) {
//                for (int n = -1; n <= 1; n++) {
//                    if ((isInRange(ship.getX(Math.abs(i)), m) && isInRange(ship.getY(Math.abs(i)), n)) && field[ship.getX(Math.abs(i)) - m][ship.getY(Math.abs(i)) - n] == EMPTY) {
//                        field[ship.getX(Math.abs(i)) - m][ship.getY(Math.abs(i)) - n] = MISS;
//                    }
//                }
//            }
//        }
    }

    public static int shot(char[][] field, List<Ship> attacked, int x, int y, int ships) {
        for (Ship ship : attacked) {
            for (int i = 0; i < ship.getDeck(); i++) {
                if (ship.getShipPlace(i, x, y) && ship.getDecks() > 1) {
                    ship.setDecks();
                    System.out.println("Ранен");
                } else if (ship.getShipPlace(i, x, y) && ship.getDecks() == 1) {
                    ship.setDecks();
                    System.out.println("Убит");
                    kill(field, ship);
                    ships--;
                }
            }
        }
        repeatMy = true;
        return ships;
    }

    public static int shotEnemy(char[][] field, List<Ship> attacked, int x, int y, int ships) {
        for (Ship ship : attacked) {
            for (int i = 0; i < ship.getDeck(); i++) {
                if (ship.getShipPlace(i, x, y)) {
                    if (ship.getDecks() > 1) {
                        if (ship.getDecks() == ship.getDeck()) {
                            enemyShot = true;
                            xFirstShot = x;
                            yFirstShot = y;
                            ship.setDecks();
                            System.out.println("Ранен");
                            xEnemyShot = x;
                            yEnemyShot = y;
                        } else if (ship.getDecks() != ship.getDeck()) {
                            enemyShot = true;
                            ship.setDecks();
                            System.out.println("Ранен");
                            xEnemyShot = x;
                            yEnemyShot = y;
                        }
                    } else if (ship.getDecks() == 1) {
                        enemyShot = false;
                        ship.setDecks();
                        System.out.println("Убит");
                        kill(field, ship);
                        ships--;
                        xEnemyShot = -1;
                        yEnemyShot = -1;
                    }
                }
            }
        }
        repeatEnemy = true;
        return ships;
    }*/
}
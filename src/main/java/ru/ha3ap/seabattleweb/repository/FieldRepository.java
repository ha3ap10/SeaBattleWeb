package ru.ha3ap.seabattleweb.repository;

import org.springframework.stereotype.Repository;
import ru.ha3ap.seabattleweb.model.Coords;
import ru.ha3ap.seabattleweb.model.ships.*;

import java.util.*;

@Repository
public class FieldRepository {

    private static final int SIZE = 10;
    private static final char EMPTY = ' ';
    private static final char SHIP = '#';
    private static final char DEAD = 'X';
    private static final char MISS = '\u00B0';

    private static final String MISS_MSG = "Мимо!";
    private static final String HURT_MSG = "Ранен!";
    private static final String KILLED_MSG = "Убит!";
    private static final String AGAIN_MSG = "Уже нельзя сюда!";


    private int oneDecks = 4;
    private int twoDecks = 3;
    private int threeDecks = 2;
    private int fourDecks = 1;
    private boolean printAll = false;

    private char[][] fieldMy = new char[SIZE][SIZE];
    private char[][] fieldEnemy = new char[SIZE][SIZE];

    private List<Ship> enemyShips = new ArrayList<>();
    private List<Ship> myShips = new ArrayList<>();

    private final Coords coords = Coords.getInstance();
    private final Random random = new Random();

    public char[][] getFieldMy() {
        return fieldMy;
    }

    public char[][] getFieldEnemy() {
        return fieldEnemy;
    }

    public List<Ship> getEnemyShips() {
        return enemyShips;
    }

    public List<Ship> getMyShips() {
        return myShips;
    }

    public FieldRepository() {
        for (char[] row : fieldMy)
            Arrays.fill(row, EMPTY);

        for (char[] row : fieldEnemy)
            Arrays.fill(row, EMPTY);

        addShips4(fourDecks);
        addShips3(threeDecks);
        addShips2(twoDecks);
        addShips1(oneDecks);

        for (Ship enemyShip : enemyShips) {
            setShips(fieldEnemy, enemyShip);
        }

        for (Ship myShip : myShips) {
            setShips(fieldMy, myShip);
        }
    }

    public void reset() {
//        this = new FieldRepository();
    }

    private void addShips(int count, Ship ship) {
        for (int i = 0; i < count; i++) {
            enemyShips.add(ship);
            myShips.add(ship);
        }
    }

    private void addShips1(int count) {
        for (int i = 0; i < count; i++) {
            enemyShips.add(new OneDeck());
            myShips.add(new OneDeck());
        }
    }

    private void addShips2(int count) {
        for (int i = 0; i < count; i++) {
            enemyShips.add(new TwoDeck());
            myShips.add(new TwoDeck());
        }
    }

    private void addShips3(int count) {
        for (int i = 0; i < count; i++) {
            enemyShips.add(new ThreeDeck());
            myShips.add(new ThreeDeck());
        }
    }

    private void addShips4(int count) {
        for (int i = 0; i < count; i++) {
            enemyShips.add(new FourDeck());
            myShips.add(new FourDeck());
        }
    }

    private void setShips(char[][] warField, Ship ship) {

        int shipPlaceX = random.nextInt(SIZE);
        int shipPlaceY = random.nextInt(SIZE);
        ship.setVertical(random.nextBoolean());

        boolean busy = false;
        if (ship.getVertical()) {
            for (int n = -ship.getDeck() - 1; n <= 1; n++) {
                for (int m = -1; m <= 1; m++) {
                    if ((isInRange(shipPlaceX, m) && isInRange(shipPlaceY, n)) &&
                            warField[shipPlaceX - m][shipPlaceY - n] == SHIP) {
                        busy = true;
                    }
                }
            }
        } else {
            for (int m = -ship.getDeck() - 1; m <= 1; m++) {
                for (int n = -1; n <= 1; n++) {
                    if ((isInRange(shipPlaceX, m) && isInRange(shipPlaceY, n)) &&
                            warField[shipPlaceX - m][shipPlaceY - n] == SHIP) {
                        busy = true;
                    }
                }
            }
        }

        if (!busy) {
            if (ship.getVertical()) {
                if ((isInRange(shipPlaceX, 0) && isInRange(shipPlaceY, -ship.getDeck()))) {
                    for (int i = 0; i > -ship.getDeck(); i--) {
                        warField[shipPlaceX][shipPlaceY - i] = SHIP;
                        ship.setShipPlace(i, shipPlaceX, shipPlaceY - i);
                    }
                } else {
                    setShips(warField, ship);
                }
            } else {
                if ((isInRange(shipPlaceX, -ship.getDeck()) && isInRange(shipPlaceY, 0))) {
                    for (int i = 0; i > -ship.getDeck(); i--) {
                        warField[shipPlaceX - i][shipPlaceY] = SHIP;
                        ship.setShipPlace(i, shipPlaceX - i, shipPlaceY);
                    }
                } else {
                    setShips(warField, ship);
                }
            }
        } else {
            setShips(warField, ship);
        }
    }

    public boolean isInRange(int shipPlace, int i) {
        return shipPlace - i >= 0 && shipPlace - i < SIZE;
    }

    public String myShot(char[][] field, List<Ship> ships, int x, int y) {
        String status = new String();
//        try {
        if (field[x][y] == MISS || field[x][y] == DEAD) {
            status = AGAIN_MSG;
        }

        if (field[x][y] == EMPTY) {
            field[x][y] = MISS;
            status = MISS_MSG;
            enemyShot();
        }
        if (field[x][y] == SHIP) {
            Iterator<Ship> iterator = ships.iterator();
            while (iterator.hasNext()) {
//            for (Ship ship : ships) {
                Ship ship = iterator.next();
                if (ship.isShip(x, y)) {
                    System.out.println(ship);
                    field[x][y] = DEAD;
                    status = HURT_MSG;
                    diagonalStrike(field, x, y);
                    if (ship.getDecks() <= 1) {
                        killed(field, ship);
                        status = KILLED_MSG;
                        iterator.remove();
                    } else {
                        ship.setDecks();
                    }
                }
            }
        }
        return status;
    }

    private boolean enemyShot;
    private int xEnemyShot;
    private int yEnemyShot;
    private int xFirstShot;
    private int yFirstShot;

    public void enemyShot() {
        int countMoves = 0;

        while (myShips.size() > 0) {
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
                    continue;
                }
                if (yEnemyShot - yPlus < 0 || yEnemyShot - yPlus >= SIZE) {
                    continue;
                }
                x1 = xEnemyShot - xPlus;
                y1 = yEnemyShot - yPlus;
            }

            if ((x1 < 0 || x1 >= SIZE) || (y1 < 0 || y1 >= SIZE)) {
                continue;
            }

            if (fieldMy[x1][y1] == DEAD || fieldMy[x1][y1] == MISS) {
//                enemyShot = true;
                continue;
            }

            if (fieldMy[x1][y1] == EMPTY) {
//                enemyShot = false;
                fieldMy[x1][y1] = MISS;
                break;
            }

            if (fieldMy[x1][y1] == SHIP) {
                fieldMy[x1][y1] = DEAD;

//                xFirstShot = xEnemyShot;
//                yFirstShot = yEnemyShot;

                Iterator<Ship> iterator = myShips.iterator();
                while (iterator.hasNext()) {
//            for (Ship ship : ships) {
                    Ship ship = iterator.next();
                    if (ship.isShip(x1, y1)) {
                        System.out.println("x: " + x1 + " y: " + y1);
                        System.out.println("FirstShot x: " + xFirstShot + " y: " + yFirstShot);
                        System.out.println("EnemyShot x: " + xEnemyShot + " y: " + yEnemyShot);
                        fieldMy[x1][y1] = DEAD;
                        enemyShot = true;
                        if (ship.getDecks() > 1 && ship.getDecks() == ship.getDeck()) {
                            xFirstShot = x1;
                            yFirstShot = y1;
                        }
                        xEnemyShot = x1;
                        yEnemyShot = y1;
//                        status = HURT_MSG;
                        diagonalStrike(fieldMy, x1, y1);
                        if (ship.getDecks() <= 1) {
                            killed(fieldMy, ship);
//                            status = KILLED_MSG;
                            iterator.remove();
                            enemyShot = false;
                            xFirstShot = -1;
                            yFirstShot = -1;
                            xEnemyShot = -1;
                            yEnemyShot = -1;
                        } else {
                            ship.setDecks();
                        }
                    }
                }
                continue;
            }
        }
    }

    private void diagonalStrike(char[][] field, int x, int y) {
        try {
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    if ((isInRange(x, i) && isInRange(y, j)) && field[x - i][y - j] == EMPTY) field[x - i][y - j] = MISS;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("diagonalStrike: " + e.getMessage());
        }
    }

    private void killed(char[][] field, Ship ship) {
        try {
            for (int deck = 0; deck < ship.getDeck(); deck++) {
                int x = ship.getX(deck);
                int y = ship.getY(deck);

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if ((isInRange(x, i) && isInRange(y, j)) && field[x - i][y - j] == EMPTY) field[x - i][y - j] = MISS;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("killed: " + ex.getMessage());
        }
    }
}

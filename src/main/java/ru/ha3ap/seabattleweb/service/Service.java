package ru.ha3ap.seabattleweb.service;

import ru.ha3ap.seabattleweb.model.Coords;
import ru.ha3ap.seabattleweb.repository.FieldRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@org.springframework.stereotype.Service
public class Service {
    private FieldRepository repository;
    private Coords coords = Coords.getInstance();

    private static final String RESOURCES = "src/main/resources";

    private final Path missFile = Path.of(RESOURCES, "miss.png");
    private final Path deadFile = Path.of(RESOURCES, "dead.png");
    private final Path shipFile = Path.of(RESOURCES, "ship.png");
    private final Path emptyFile = Path.of(RESOURCES, "empty.png");

    private static final char EMPTY = ' ';
    private static final char SHIP = '#';
    private static final char DEAD = 'X';
    private static final char MISS = '\u00B0';

    private static final String ID = "attack=";
    private static final String COIN = "coin=";

    private final Path seabattlePath = Path.of(RESOURCES, "html", "seabattle.html");
    private final Path startPath = Path.of(RESOURCES, "html", "start.html");

    public Service(FieldRepository repository) {
        this.repository = repository;
    }

    public String attack(String attack) throws IOException {
        String[] coord = attack.substring(ID.length()).strip().split("\\+");
        int x = coords.getNumber(URLDecoder.decode(coord[0], StandardCharsets.UTF_8).toLowerCase()) - 1;
        int y = Integer.parseInt(coord[1]) - 1;

        return printField(repository.myShot(repository.getFieldEnemy(), repository.getEnemyShips(), x, y));
    }

    public byte[] getFile(String file) throws IOException {
        final var filePath = Path.of(RESOURCES, file);
        InputStream in = Files.newInputStream(filePath);
        return in.readAllBytes();
    }

    public String getCoin() throws IOException {
        repository.reset();
        return Files.readString(startPath);
    }

    public String getStart(String userChoice) throws IOException {
        Random random = new Random();
        final var choice = userChoice.substring(COIN.length());
        //Орёл - true, решка - false
        boolean coin = random.nextBoolean();
        StringBuilder status = new StringBuilder();
        if (coin) {
            status.append("Выпал орёл!\n");
            if (choice.equals("0")) {
                //my turn
                status.append("Вы ходите первым!");
            } else {
                status.append("Противник ходит первым!");
                repository.enemyShot();
            }
        } else {
            status.append("Выпала решка!\n");
            if (choice.equals("1")) {
                status.append("Вы ходите первым!");
                //my turn
            } else {
                status.append("Противник ходит первым!");
                repository.enemyShot();
            }
        }
        return printField(status.toString());
    }

    public String printField(String status) throws IOException {
        final var template = Files.readString(seabattlePath);
        if (repository.getEnemyShips().size() == 0) status = "ПОБЕДА!";
        if (repository.getMyShips().size() == 0) status = "ПОРАЖЕНИЕ!";
        var content = template.replace("{status}", status);
        content = content.replace("{myShips}", repository.getMyShips().size() + "");
        content = content.replace("{enemyShips}", repository.getEnemyShips().size() + "");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String pathMy = new String();
                String pathEnemy = new String();
                if (repository.getFieldMy()[i][j] == MISS) pathMy = missFile.toString();
                if (repository.getFieldMy()[i][j] == DEAD) pathMy = deadFile.toString();
                if (repository.getFieldMy()[i][j] == SHIP) pathMy = shipFile.toString();
                if (repository.getFieldMy()[i][j] == EMPTY) pathMy = emptyFile.toString();

                if (repository.getFieldEnemy()[i][j] == MISS) pathEnemy = missFile.toString();
                if (repository.getFieldEnemy()[i][j] == DEAD) pathEnemy = deadFile.toString();
                if (repository.getFieldEnemy()[i][j] == SHIP) pathEnemy = emptyFile.toString();
                if (repository.getFieldEnemy()[i][j] == EMPTY) pathEnemy = emptyFile.toString();

                content = content.replace("{" + (i + 1) + "my" + (j + 1) + "}", pathMy);
                content = content.replace("{" + (i + 1) + "enemy" + (j + 1) + "}", pathEnemy);
            }
        }
        return content;
    }

    public String restart() {
        return "In construction";
    }
}

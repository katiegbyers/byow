package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private Point current = new Point(0, 0);
    private Random RANDOM;
    private long SEED;
    private int currmX;
    private int currmY;
    private String moves = "";
    private String name;
    private boolean containsQ = false;
    private boolean load = false;
    private int lives = 10;
    private boolean win = false;
    private boolean lost = false;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        initializeTERender();

        popUpMain(); //main menu pop up
        String seedInput = handleNLQ(); //handling N, L, :Q
        if (seedInput.equals("Q")) {
            return;
        }

        TETile[][] world = interactWithInputString(seedInput); //Call interact With Input String
        if (!load) {
            name = pickName();
        }

        popUpDirections();
        handleC();

        if (!containsQ) {
            StdDraw.clear();
            ter.renderFrame(world);
            moves(world); //Moves
        }

    }

    private void popUpDirections() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setFont(new Font("Bebas", Font.BOLD, 40));
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "DIRECTIONS: ");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "1. Find your way to the treasure (yellow box) before losing all your lives");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "2. If you hit a pink box, you gain a life");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "3. If you hit a green box, you lose a life");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 9, "Press C to continue");
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
    }

    private void handleC() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                if (c == 'C') {
                    return;
                }
            }
        }
    }

    private void initializeTERender() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 4, 0, 4);
    }

    private void moves(TETile[][] world) {
        while (true) {
            ter.renderFrame(world);
            checkMouse(world);
            if(lives <= 0){
                gameOver();
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                moves = moves + c;
                switch (c) {
                    case 'W':
                        moveUp(world);
                        break;
                    case 'S':
                        moveDown(world);
                        break;
                    case 'A':
                        moveLeft(world);
                        break;
                    case 'D':
                        moveRight(world);
                        break;
                    case ':':
                        System.out.println("In :");
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                c = StdDraw.nextKeyTyped();
                                System.out.println("In :, In Q");
                                if (c == 'Q') {
                                    saveWorld();
                                    quit();
                                } else {
                                    break;
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void gameOver(){
        lost = true;
        StdDraw.clear();
        while(true) {
            StdDraw.setFont(new Font("Bebas", Font.BOLD, 70));
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "GAME   OVER");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Quit  (Press   Q)");
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                if (c == 'Q') {
                    saveWorld();
                    quit();
                }
            }
            StdDraw.show();
        }
    }

    private void winGame(){
        win = true;
        StdDraw.clear();
        while(true) {
            StdDraw.setFont(new Font("Bebas", Font.BOLD, 70));
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "CONGRATS!!  YOU   WON");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Quit  (Press   Q)");
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                if (c == 'Q') {
                    saveWorld();
                    quit();
                }
            }
            StdDraw.show();
        }
    }

    private void loseLife(){
        lives -= 1;
        ReturnWorld r = new ReturnWorld(name);
    }

    private void gainLife(){
        lives += 1;
        GainWorld g = new GainWorld(name);
    }

    private void moveRight(TETile[][] world) {
        if (current.x + 1 >= 0 && current.x + 1 < WIDTH) {
            TETile tileFour = world[current.x + 1][current.y];
            if (tileFour.equals(Tileset.FLOOR)) {
                world[current.x][current.y] = Tileset.FLOOR;
                current.x = current.x + 1;
                world[current.x][current.y] = Tileset.AVATAR;
            } else if ((tileFour.equals(Tileset.TREE))) {
                loseLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.x = current.x + 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            } else if (tileFour.equals(Tileset.GRASS)) {
                world[current.x + 1][current.y] = Tileset.TREE;
            } else if(tileFour.equals(Tileset.TREASURE)){
                winGame();
            } else if(tileFour.equals(Tileset.GAIN)){
                gainLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.x = current.x + 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            }
        }
    }

    private void moveLeft(TETile[][] world) {
        if (current.x - 1 >= 0 && current.x - 1 < WIDTH) {
            TETile tileThree = world[current.x - 1][current.y];
            if (tileThree.equals(Tileset.FLOOR)) {
                world[current.x][current.y] = Tileset.FLOOR;
                current.x = current.x - 1;
                world[current.x][current.y] = Tileset.AVATAR;
            } else if ((tileThree.equals(Tileset.TREE))) {
                loseLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.x = current.x - 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            } else if (tileThree.equals(Tileset.GRASS)) {
                world[current.x - 1][current.y] = Tileset.TREE;
            } else if(tileThree.equals(Tileset.TREASURE)){
                winGame();
            } else if(tileThree.equals(Tileset.GAIN)){
                gainLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.x = current.x - 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            }
        }
    }

    private void moveDown(TETile[][] world) {
        if (current.y - 1 >= 0 && current.y - 1 < HEIGHT) {
            TETile tileTwo = world[current.x][current.y - 1];
            if (tileTwo.equals(Tileset.FLOOR)) {
                world[current.x][current.y] = Tileset.FLOOR;
                current.y = current.y - 1;
                world[current.x][current.y] = Tileset.AVATAR;
            } else if ((tileTwo.equals(Tileset.TREE))) {
                loseLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.y = current.y - 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            } else if (tileTwo.equals(Tileset.GRASS)) {
                world[current.x][current.y - 1] = Tileset.TREE;
            } else if(tileTwo.equals(Tileset.TREASURE)){
                winGame();
            } else if(tileTwo.equals(Tileset.GAIN)){
                gainLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.y = current.y - 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            }
        }
    }

    private void moveUp(TETile[][] world) {
        if (current.y + 1 >= 0 && current.y + 1 < HEIGHT) {
            TETile tile = world[current.x][current.y + 1];
            if (tile.equals(Tileset.FLOOR)) {
                world[current.x][current.y] = Tileset.FLOOR;
                current.y = current.y + 1;
                world[current.x][current.y] = Tileset.AVATAR;
            } else if ((tile.equals(Tileset.TREE))) {
                loseLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.y = current.y + 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            } else if (tile.equals(Tileset.GRASS)) {
                world[current.x][current.y + 1] = Tileset.TREE;
            } else if(tile.equals(Tileset.TREASURE)){
                winGame();
            } else if(tile.equals(Tileset.GAIN)){
                gainLife();
                world[current.x][current.y] = Tileset.FLOOR;
                current.y = current.y + 1;
                world[current.x][current.y] = Tileset.AVATAR;
                initializeTERender();
            }
        }
    }

    private void checkMouse(TETile[][] world) {

        currmX = (int) StdDraw.mouseX();
        currmY = (int) StdDraw.mouseY() - 4;

        if (currmX < 0 || currmX >= WIDTH || currmY < 0 || currmY >= HEIGHT) {
            return;
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(WIDTH / 2, 2, world[currmX][currmY].description());
        StdDraw.text(10, 2, name);
        StdDraw.text(WIDTH - 10, 2, "Lives: " + lives);
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
    }

    private void popUpMain() {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 70));
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "WELCOME TO CS61B: THE GAME");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 35));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game(Press N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Load Game(Press L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Quit(Press Q)");

        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
    }

    private String handleNLQ() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                if (c == 'N') {
                    return "N" + promptForSeed();
                } else if (c == 'L') {
                    return "L";
                } else if (c == 'Q') {
                    return quit();
                } else {
                    return quit();
                }
            }
        }
    }

    private String quit() {
        Runtime.getRuntime().halt(0);
        return "Q";
    }

    private String promptForSeed() {
        String seedBuild = "";
        int seed = 0;
        boolean startedSeed = false;

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                if (c == 'S' && !startedSeed) {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Must enter seed");
                } else if (c == 'S') {
                    return seedBuild;
                } else if (Character.isDigit(c)) {
                    startedSeed = true;
                    seed = seed * 10 + Character.getNumericValue(c);
                    seedBuild = seedBuild + c;
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, seedBuild);
                }
            }

            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 70));
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "CS61B: THE GAME");
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 35));
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Enter SEED");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Press S to stop:");
            if (startedSeed) {
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, seedBuild);
            }
            StdDraw.show();
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        }
    }

    private void saveWorld() {
        try {
            FileOutputStream f = new FileOutputStream(new File("CurrentWorld.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(lost);
            o.writeObject(win);
            o.writeObject(name);
            o.writeObject(moves);
            o.writeObject(SEED);

            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    private void loadWorld() {
        try {
            FileInputStream f = new FileInputStream(new File("CurrentWorld.txt"));
            ObjectInputStream o = new ObjectInputStream(f);

            lost = (Boolean) o.readObject();
            if(lost){
                gameOver();
            }
            win = (Boolean) o.readObject();
            if(win){
                winGame();
            }
            name = (String) o.readObject();
            moves = (String) o.readObject();
            SEED = (Long) o.readObject();
            RANDOM = new Random(SEED);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123 sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     * @source: https://gamedevelopment.tutsplus.com/tutorials/
     * how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
     */
    public TETile[][] interactWithInputString(String input) {
        if (input.equals("")) {
            saveWorld();
            return null;
        }
        input = input.toLowerCase();
        String newSeed = "";
        load = false;
        char first = input.charAt(0);
        if (first == 'l') {
            load = true;
            loadWorld();
            int index = 1;
            while (index < input.length()) {
                char num = input.charAt(index);
                if (num == ':') {
                    if (index + 1 < input.length() && input.charAt(index + 1) == 'q') {
                        saveWorld();
                        containsQ = true;
                        break;
                    }
                }
                moves += num;
                index++;
            }
        } else if ((first == ':')) {
            if (input.length() >= 2 && input.charAt(1) == 'q') {
                saveWorld();
                containsQ = true;
            }
        } else if (first == 'n') {
            boolean foundS = false;
            for (int i = 1; i < input.length(); i++) {
                char num = input.charAt(i);
                if (num != 's' && Character.isDigit(num) && !foundS) {
                    newSeed += num;
                } else if (num == 's' && !foundS) {
                    foundS = true;
                } else if (num == ':') {
                    if (i + 1 < input.length() && input.charAt(i + 1) == 'q') {
                        saveWorld();
                        containsQ = true;
                        break;
                    }
                } else if (foundS && Character.isAlphabetic(num)) {
                    moves += num;
                }

                SEED = Long.parseLong(newSeed);
            }
        } else {
            saveWorld();
        }
        RANDOM = new Random(SEED);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        initializeWorld(finalWorldFrame);
        ArrayList<Leaf> listOfLeaves = partitionWorld(finalWorldFrame, RANDOM);
        fillWithRooms(listOfLeaves, finalWorldFrame, RANDOM);
        generateEntities(finalWorldFrame, RANDOM);
        generateAvatar(finalWorldFrame, RANDOM);
        generateTreasure(finalWorldFrame, RANDOM);
        generateGainPoints(finalWorldFrame, RANDOM);

        loadedMoves(finalWorldFrame);

        return finalWorldFrame;
    }

    private void initializeWorld(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.WALL;
            }
        }
        //top horizontal row
        for (int i = 1; i < WIDTH - 1; i++) {
            world[i][HEIGHT - 2] = Tileset.FLOOR;
        }
        //bottom horizontal row
        for (int i = 1; i < WIDTH - 1; i++) {
            world[i][1] = Tileset.FLOOR;
        }
        //left vertical column
        for (int i = 1; i < HEIGHT - 1; i++) {
            world[1][i] = Tileset.FLOOR;
        }
        //right vertical column
        for (int i = 1; i < HEIGHT - 1; i++) {
            world[WIDTH - 2][i] = Tileset.FLOOR;
        }
    }


    private String pickName() {
        String seedBuild = "";
        boolean startedSeed = false;
        int index = 0;
        while (index < 10) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == '!' && !startedSeed) {
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Must enter seed");
                } else if (c == '!') {
                    name = seedBuild;
                    break;
                } else if (Character.isAlphabetic(c)) {
                    index++;
                    startedSeed = true;
                    seedBuild = seedBuild + c;
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, seedBuild);
                }
            }
            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 70));
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "CS61B: THE GAME");
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 35));
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Enter a name for your avatar (Max 10)");
            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Press '!' to STOP");
            if (startedSeed) {
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, seedBuild);
            }
            StdDraw.show();
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        }

        return seedBuild;
    }

    private void loadedMoves(TETile[][] world) {
        int index = 0;
        while (index < moves.length()) {
            char c = moves.charAt(index);
            c = Character.toUpperCase(c);
            switch (c) {
                case 'W':
                    if (current.y + 1 >= 0 && current.y + 1 < HEIGHT) {
                        TETile tile = world[current.x][current.y + 1];
                        if (tile.equals(Tileset.FLOOR) || (tile.equals(Tileset.TREE))) {
                            world[current.x][current.y] = Tileset.FLOOR;
                            current.y = current.y + 1;
                            world[current.x][current.y] = Tileset.AVATAR;
                        } else if (tile.equals(Tileset.GRASS)) {
                            world[current.x][current.y + 1] = Tileset.TREE;
                        }
                    }
                    break;
                case 'S':
                    if (current.y - 1 >= 0 && current.y - 1 < HEIGHT) {
                        TETile tileTwo = world[current.x][current.y - 1];
                        if (tileTwo.equals(Tileset.FLOOR) || (tileTwo.equals(Tileset.TREE))) {
                            world[current.x][current.y] = Tileset.FLOOR;
                            current.y = current.y - 1;
                            world[current.x][current.y] = Tileset.AVATAR;
                        } else if (tileTwo.equals(Tileset.GRASS)) {
                            world[current.x][current.y - 1] = Tileset.TREE;
                        }
                    }
                    break;
                case 'A':
                    if (current.x - 1 >= 0 && current.x - 1 < WIDTH) {
                        TETile tileThree = world[current.x - 1][current.y];
                        if (tileThree.equals(Tileset.FLOOR) || (tileThree.equals(Tileset.TREE))) {
                            world[current.x][current.y] = Tileset.FLOOR;
                            current.x = current.x - 1;
                            world[current.x][current.y] = Tileset.AVATAR;
                        } else if (tileThree.equals(Tileset.GRASS)) {
                            world[current.x - 1][current.y] = Tileset.TREE;
                        }
                    }
                    break;
                case 'D':
                    if (current.x + 1 >= 0 && current.x + 1 < WIDTH) {
                        TETile tileFour = world[current.x + 1][current.y];
                        if (tileFour.equals(Tileset.FLOOR) || (tileFour.equals(Tileset.TREE))) {
                            world[current.x][current.y] = Tileset.FLOOR;
                            current.x = current.x + 1;
                            world[current.x][current.y] = Tileset.AVATAR;
                        } else if (tileFour.equals(Tileset.GRASS)) {
                            world[current.x + 1][current.y] = Tileset.TREE;
                        }
                    }
                    break;
                case ':':
                    if (index + 1 < moves.length() && moves.charAt(index + 1) == 'Q') {
                        saveWorld();
                        return;
                    }
                    break;
                default:
                    break;
            }
            index++;
        }
    }

    public void generateGainPoints(TETile[][] world, Random random) {
        double numEntities = RandomUtils.uniform(random, 1, (WIDTH * HEIGHT) / 50);
        untilValidSpot(world, random, numEntities, Tileset.GAIN);
    }

    public void generateTreasure(TETile[][] world, Random random) {
        untilValidSpot(world, random, 1, Tileset.TREASURE);
    }

    public void generateEntities(TETile[][] world, Random random) {
        double numEntities = RandomUtils.uniform(random, 1, (WIDTH * HEIGHT) / 50);
        untilValidSpot(world, random, numEntities, Tileset.GRASS);
    }

    public void generateAvatar(TETile[][] world, Random random) {
        current = untilValidSpot(world, random, 1, Tileset.AVATAR);
    }

    public Point untilValidSpot(TETile[][] world, Random random, double totalNum, TETile tile) {
        int index = 0;
        Point currentSpot = null;
        while (index < totalNum) {
            int randX = (int) (RandomUtils.uniform(random, WIDTH));
            int randY = (int) (RandomUtils.uniform(random, HEIGHT));
            currentSpot = new Point(randX, randY);
            while (!world[randX][randY].equals(Tileset.FLOOR)) {
                randX = (int) (RandomUtils.uniform(random, WIDTH));
                randY = (int) (RandomUtils.uniform(random, HEIGHT));
                currentSpot = new Point(randX, randY);
            }
            world[randX][randY] = tile;
            index++;
        }
        return currentSpot;
    }

    public ArrayList<Leaf> partitionWorld(TETile[][] world, Random random) {

        ArrayList<Leaf> listOfLeaves = new ArrayList();

        int id;
        if (WIDTH > HEIGHT) {
            id = 1;
        } else {
            id = -1;
        }

        Leaf root = new Leaf(0, 0, WIDTH, HEIGHT, id);
        listOfLeaves.add(root);

        boolean allSplit = true;

        while (allSplit) {
            allSplit = false;
            for (int i = 0; i < listOfLeaves.size(); i++) {
                Leaf l = listOfLeaves.get(i);
                Leaf left = l.getLeftChild();
                Leaf right = l.getRightChild();
                if (left == null && right == null && l.split(world, random)) {
                    listOfLeaves.add(l.getLeftChild());
                    listOfLeaves.add(l.getRightChild());
                    allSplit = true;
                }
            }
        }
        return listOfLeaves;
    }

    public void fillWithRooms(ArrayList<Leaf> listOfLeaves, TETile[][] world, Random random) {
        int rooms = 0;
        for (int i = 0; i < listOfLeaves.size(); i++) {
            createRoom(listOfLeaves.get(i), world, random);
        }
    }

    public void createRoom(Leaf l, TETile[][] world, Random random) {
        Leaf left = l.getLeftChild();
        Leaf right = l.getRightChild();

        if (left != null || right != null) {
            return;
        }

        int maxWidth = l.getWidth();
        int maxHeight = l.getHeight();
        int roomWidth = RandomUtils.uniform(random, maxWidth);
        int roomHeight = RandomUtils.uniform(random, maxHeight);

        for (int j = l.getX(); j < l.getX() + roomWidth; j++) {
            if (j > 0 && j < WIDTH - 1) {
                for (int i = l.getY(); i < l.getY() + roomHeight; i++) {
                    if (i > 0 && i < HEIGHT - 1) {
                        world[j][i] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

}

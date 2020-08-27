package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

public class ReturnWorld {
    private Point current = new Point(0, 0);
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    private int currmX;
    private int currmY;
    private String name;
    TERenderer ter;

    public ReturnWorld(String name) {
        this.name = name;
        ter = new TERenderer();
        ter.initialize(40, 44, 0, 4);
        TETile[][] popUp = new TETile[40][40];

        popUpDirections();
        handleC();

        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                popUp[i][j] = Tileset.SAND;
            }
        }
        for (int i = 15; i < 25; i++) {
            for (int j = 15; j < 25; j++) {
                popUp[i][j] = Tileset.FLOWER;
            }
        }
        popUp[current.x][current.y] = Tileset.AVATAR;
        ter.renderFrame(popUp);
        moves(popUp);
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

    private void popUpDirections() {
        StdDraw.setFont(new Font("Bebas", Font.BOLD, 40));
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "YOU   LOST   A   LIFE");
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Directions: Go the flower bed");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Press C to continue");
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
    }

    private void moves(TETile[][] world) {
        while (true) {
            ter.renderFrame(world);
            checkMouse(world);
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                switch (c) {
                    case 'W':
                        if (current.y + 1 >= 0 && current.y + 1 < HEIGHT) {
                            TETile tile = world[current.x][current.y + 1];
                            if (tile.equals(Tileset.SAND)) {
                                world[current.x][current.y] = Tileset.FLOOR;
                                current.y = current.y + 1;
                                world[current.x][current.y] = Tileset.AVATAR;
                            } else if (tile.equals(Tileset.FLOWER)) {
                                return;
                            }
                        }
                        break;
                    case 'S':
                        if (current.y - 1 >= 0 && current.y - 1 < HEIGHT) {
                            TETile tileTwo = world[current.x][current.y - 1];
                            if (tileTwo.equals(Tileset.SAND)) {
                                world[current.x][current.y] = Tileset.FLOOR;
                                current.y = current.y - 1;
                                world[current.x][current.y] = Tileset.AVATAR;
                            } else if (tileTwo.equals(Tileset.FLOWER)) {
                                return;
                            }
                        }
                        break;
                    case 'A':
                        if (current.x - 1 >= 0 && current.x - 1 < WIDTH) {
                            TETile tileThree = world[current.x - 1][current.y];
                            if (tileThree.equals(Tileset.SAND)) {
                                world[current.x][current.y] = Tileset.FLOOR;
                                current.x = current.x - 1;
                                world[current.x][current.y] = Tileset.AVATAR;
                            } else if (tileThree.equals(Tileset.FLOWER)) {
                                return;
                            }
                        }
                        break;
                    case 'D':
                        if (current.x + 1 >= 0 && current.x + 1 < WIDTH) {
                            TETile tileFour = world[current.x + 1][current.y];
                            if (tileFour.equals(Tileset.SAND)) {
                                world[current.x][current.y] = Tileset.FLOOR;
                                current.x = current.x + 1;
                                world[current.x][current.y] = Tileset.AVATAR;
                            } else if (tileFour.equals(Tileset.FLOWER)) {
                                return;
                            }
                        }
                        break;
                    case ':':
                        while (true) {
                            if (StdDraw.hasNextKeyTyped()) {
                                if (StdDraw.nextKeyTyped() == 'Q') {
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

    private void checkMouse(TETile[][] world) {

        currmX = (int) StdDraw.mouseX();
        currmY = (int) StdDraw.mouseY() - 4;

        if (currmX < 0 || currmX >= WIDTH || currmY < 0 || currmY >= HEIGHT) {
            return;
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
        StdDraw.text(5, 2, world[currmX][currmY].description());
        StdDraw.text(WIDTH - 20, 2, name + ", GO TO THE FLOWER BED");
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
    }

    private String quit() {
        Runtime.getRuntime().halt(0);
        return "Q";
    }

}

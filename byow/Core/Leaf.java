package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Leaf {
    private static final int MIN_LEAF_SIZE = 5;
    private int x;
    private int y;
    private int width;
    private int height;
    private Leaf leftChild, rightChild = null;
    private int id;


    public Leaf(int X, int Y, int width, int height, int id) {
        this.x = X;
        this.y = Y;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    public boolean split(TETile[][] world, Random random) {
        if (leftChild != null || rightChild != null) {
            return false;
        }

        boolean horizontal;

        if (id > 0) {
            horizontal = false;
        } else {
            horizontal = true;
        }

        int maxTileSizePossible;
        if (horizontal) {
            maxTileSizePossible = height - MIN_LEAF_SIZE;
        } else {
            maxTileSizePossible = width - MIN_LEAF_SIZE;
        }
        if (maxTileSizePossible <= MIN_LEAF_SIZE) {
            return false;
        }
        int divider = RandomUtils.uniform(random, MIN_LEAF_SIZE, maxTileSizePossible);
        if (horizontal) {
            leftChild = new Leaf(x, y, width, divider, -1 * id);
            rightChild = new Leaf(x, y + divider, width, height - divider, -1 * id);
            createHallways(world, horizontal, x, y + divider);
        } else {
            leftChild = new Leaf(x, y, divider, height, -1 * id);
            rightChild = new Leaf(x + divider, y, width - divider, height, -1 * id);
            createHallways(world, horizontal, x + divider, y);
        }
        return true;
    }

    private void createHallways(TETile[][] world, boolean horizontal, int xC, int yC) {
        int ww = world.length;
        int wh = world[0].length;
        if (horizontal) {
            while (xC < ww && xC >= 0 && !world[xC][yC].equals(Tileset.FLOOR)) {
                world[xC][yC] = Tileset.FLOOR;
                xC++;
            }
        } else {
            while (yC < wh && yC >= 0 && !world[xC][yC].equals(Tileset.FLOOR)) {
                world[xC][yC] = Tileset.FLOOR;
                yC++;
            }
        }
    }

    public Leaf getLeftChild() {
        return this.leftChild;
    }

    public Leaf getRightChild() {
        return this.rightChild;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

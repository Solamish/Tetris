package model;

import java.awt.image.BufferedImage;


public class Cell {
    /**
     * @param row 行
     * @param col 列
     * @param color 格子的颜色
     */

    private int row;
    private int col;
    private BufferedImage color;

    public Cell() {
    }

    public Cell(int row, int col, BufferedImage color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public void moveRight() {
        col++;
    }


    public void moveLeft() {
        col--;
    }

    void moveDown() {
        row++;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public BufferedImage getColor() {
        return color;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setColor(BufferedImage color) {
        this.color = color;
    }
}

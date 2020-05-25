package model;

import view.Tetris;

import java.util.Random;

public class Tetromino extends Cell {

    // 一个俄罗斯方块由四个格子组成
   public Cell[] cells = new Cell[4];

    @Override
    public void moveDown() {
        for (int i = 0; i < cells.length; i++) {
            cells[i].moveDown();
        }
    }

    @Override
    public void moveRight() {
        for (int i = 0; i <cells.length ; i++) {
            cells[i].moveRight();
        }
    }

    @Override
    public void moveLeft() {
        for (int i = 0; i <cells.length ; i++) {
            cells[i].moveLeft();
        }
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < cells.length; i++){
            str = str + cells[i].getRow()+" "+cells[i].getCol()+"\n";
        }
        return str;
    }

    public Cell[] spin() {
        Cell[] iCells = new Cell[4];
        int iRow = this.cells[2].getRow();
        int iCol = this.cells[2].getCol();
        for (int i = 0; i < this.cells.length; i++) {
            int nRow = this.cells[i].getRow();
            int nCol = this.cells[i].getCol();
            iCells[i] = new Cell(iRow-iCol+nCol, iRow+iCol-nRow, this.cells[i].getColor());
        }
        return iCells;
    }

    public static Tetromino ranShape() {
        Random random = new Random();
        int index = random.nextInt(7);
        switch (index) {
            case 0:return new J();
            case 1:return new L();
            case 2:return new O();
            case 3:return new Z();
            case 4:return new S();
            case 5:return new I();
            case 6:return new T();
        }
        return null;
    }

}

class J extends Tetromino {
    public J() {
        cells[0] = new Cell(2, 5, Tetris.J);
        cells[1] = new Cell(0, 6, Tetris.J);
        cells[2] = new Cell(2, 6, Tetris.J);
        cells[3] = new Cell(1, 6, Tetris.J);
    }
}
class L extends Tetromino {

    public L() {
        cells[0] = new Cell(2, 6, Tetris.L);
        cells[1] = new Cell(0, 5, Tetris.L);
        cells[2] = new Cell(2, 5, Tetris.L);
        cells[3] = new Cell(1, 5, Tetris.L);
    }
}
class O extends Tetromino {
    public O() {
        cells[0] = new Cell(0, 5, Tetris.O);
        cells[1] = new Cell(0, 6, Tetris.O);
        cells[2] = new Cell(1, 5, Tetris.O);
        cells[3] = new Cell(1, 6, Tetris.O);
    }
}
class Z extends Tetromino {

    public Z() {
        cells[0] = new Cell(0, 4, Tetris.Z);
        cells[1] = new Cell(0, 5, Tetris.Z);
        cells[2] = new Cell(1, 5, Tetris.Z);
        cells[3] = new Cell(1, 6, Tetris.Z);
    }
}
class S extends Tetromino {
    public S() {
        cells[0] = new Cell(1, 4, Tetris.S);
        cells[1] = new Cell(1, 5, Tetris.S);
        cells[2] = new Cell(0, 5, Tetris.S);
        cells[3] = new Cell(0, 6, Tetris.S);
    }
}
class I extends Tetromino {
    public I() {
        cells[0] = new Cell(0, 5, Tetris.I);
        cells[1] = new Cell(1, 5, Tetris.I);
        cells[2] = new Cell(2, 5, Tetris.I);
        cells[3] = new Cell(3, 5, Tetris.I);
    }
}
class T extends Tetromino {
    public T() {
        cells[0] = new Cell(0, 4, Tetris.T);
        cells[1] = new Cell(0, 6, Tetris.T);
        cells[2] = new Cell(0, 5, Tetris.T);
        cells[3] = new Cell(1, 5, Tetris.T);
    }
}


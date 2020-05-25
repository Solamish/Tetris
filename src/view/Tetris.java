package view;

import model.Cell;
import model.Tetromino;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import java.util.Timer;

public class Tetris extends JPanel {

    private static final int ROWS = 20;
    private static final int COLS = 10;

    private int score = 0;
    private int lines = 0;
    private int level = 6;

    private Cell[][] wall = new Cell[ROWS][COLS];
    private Tetromino tetromino;  // 当前方块
    private Tetromino nextone;  // 下一个方块

    private boolean STATE = true;
    private int BGI = 0;

    public static final int CELL_SIZE = 26;
    public static BufferedImage Z;
    public static BufferedImage S;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage I;
    public static BufferedImage T;
    public static BufferedImage pause;
    public static BufferedImage tetris;
    public static BufferedImage gameover;
    public static BufferedImage bgi;

    static {
        try {
            Z = ImageIO.read(Tetris.class.getResource("/image/Z.png"));
            S = ImageIO.read(Tetris.class.getResource("/image/S.png"));
            J = ImageIO.read(Tetris.class.getResource("/image/J.png"));
            T = ImageIO.read(Tetris.class.getResource("/image/T.png"));
            O = ImageIO.read(Tetris.class.getResource("/image/O.png"));
            I = ImageIO.read(Tetris.class.getResource("/image/I.png"));
            L = ImageIO.read(Tetris.class.getResource("/image/L.png"));
            pause = ImageIO.read(Tetris.class.getResource("/image/pause.png"));
            tetris = ImageIO.read(Tetris.class.getResource("/image/tetris1.png"));
            gameover = ImageIO.read(Tetris.class.getResource("/image/gameover.png"));
            bgi = ImageIO.read(Tetris.class.getResource("/image/background.png"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void action() {
        tetromino = Tetromino.ranShape();
        nextone = Tetromino.ranShape();

        KeyListener kl = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                keyMoveAction(code);
                repaint();
            }
        };
        this.addKeyListener(kl);
        this.setFocusable(true);
        this.requestFocus();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int moveIndex = 0;
            int bgiIndex = 0;
            int speed = 6 * level;

            @Override
            public void run() {
                if (STATE) {
                    if (moveIndex % speed == 0) {
                        moveDownAction();
                        moveIndex = 0;
                    }
                }
                moveIndex++;
                bgiIndex++;
                repaint();
            }
        };
        timer.schedule(task, 10, 20);
    }

    public void keyMoveAction(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_RIGHT:
                moveRightAction();
                break;
            case KeyEvent.VK_LEFT:
                moveLeftAction();
                break;
            case KeyEvent.VK_DOWN:
                moveDownAction();
                break;
            case KeyEvent.VK_UP:
                spinCellAction();
                break;
            case KeyEvent.VK_P:
                STATE = !STATE;
                break;// 暂停
            case KeyEvent.VK_SPACE:
                moveToBottomAction();
                break;
            case KeyEvent.VK_R:
                moveInitAction();
                break; // 重玩
            case KeyEvent.VK_E:
                System.exit(0);
                break; // 退出
        }
    }

    public void moveInitAction() {
        //STATE = false;
        wall = new Cell[ROWS][COLS];
        tetromino = Tetromino.ranShape();
        nextone = Tetromino.ranShape();
        score = 0;
        lines = 0;
        level = 6;
    }

    public void moveToBottomAction() {
        if (tetromino == null) return;
        while (!isBottom()) {
            tetromino.moveDown();
        }
    }

    public void moveLeftAction() {
        if (canLeftMove() && !isBottom()) {
            tetromino.moveLeft();
        }
    }

    public void moveRightAction() {
        if (canRightMove() && !isBottom()) {
            tetromino.moveRight();
        }
    }

    public void moveDownAction() {
        if (tetromino == null) return;
        if (!isBottom()) {
            tetromino.moveDown();
        }
    }

    public void spinCellAction() {
        Cell[] nCells = tetromino.spin();
        if (nCells == null) return;
        for (int i = 0; i < nCells.length; i++) {
            int nRow = nCells[i].getRow();
            int nCol = nCells[i].getCol();
            if (nRow < 0 || nRow >= ROWS || nCol < 0 || nCol >= COLS || wall[nRow][nCol] != null) return;
        }
        tetromino.cells = nCells;
    }

    /**
     * 判断当前方块是否可以右移
     *
     * @return boolean
     */
    public boolean canRightMove() {
        if (tetromino == null) return false;
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell c = cells[i];
            int row = c.getRow();
            int col = c.getCol();
            if ((col + 1) == COLS || wall[row][col + 1] != null) return false;
        }
        return true;
    }

    /**
     * 判断当前方块是否可以左移
     *
     * @return boolean
     */
    public boolean canLeftMove() {
        if (tetromino == null) return false;
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell c = cells[i];
            int row = c.getRow();
            int col = c.getCol();
            if (col == 0 || wall[row][col - 1] != null) return false;
        }
        return true;
    }

    // 方块是否到底，即是否还能下落
    public boolean isBottom() {
        if (tetromino == null) return false;
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell c = cells[i];
            int col = c.getCol();
            int row = c.getRow();
            if ((row + 1) == ROWS || wall[row + 1][col] != null) {
                for (int j = 0; j < cells.length; j++) {
                    Cell cell = cells[j];
                    int col1 = cell.getCol();
                    int row1 = cell.getRow();
                    wall[row1][col1] = cell;
                }
                removeLine();
                tetromino = nextone;
                nextone = Tetromino.ranShape();
                score += 10;
                return true;
            }
        }
        return false;
    }

    // 消除行
    public void removeLine() {
        boolean flag = true;
        int rowStart = 20;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (wall[row][col] == null) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (int col = 0; col < COLS; col++) {
                    wall[row][col] = null;
                }
                rowStart = row;
                lines++;                 // 消除的行数+1
                score += 100;
                level = lines % 10 == 0 ? (level == 1 ? level : level - 1) : level;
                // 方块下落
                for (int row1 = rowStart; row1 > 0; row1--) {
                    for (int col1 = 0; col1 < COLS; col1++) {
                        wall[row1][col1] = wall[row1 - 1][col1];
                    }
                }
            } else {
                flag = true;
            }
        }

    }

    public void paint(Graphics g) {

        g.drawImage(bgi, 0, 0, null);
        g.drawImage(tetris, 0, 0, null);
        g.translate(15, 15);


        paintWall(g);

        paintTetromino(g);
        paintNextone(g);

        paintTabs(g);

        paintGamePause(g);

        paintGameOver(g);
    }

    /**
     * @return
     */
    public boolean isGameOver() {
        for (int col = 0; col < COLS; col++) {
            if (wall[0][col] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param g
     */
    public void paintGameOver(Graphics g) {
        if (isGameOver()) {
            tetromino = null;
            g.drawImage(gameover, -15, -15, null);
            Color color = new Color(0, 71, 157);
            g.setColor(color);
            Font font = new Font(Font.SERIF, Font.BOLD, 30);
            g.setFont(font);
            g.drawString("" + score, 260, 207);
            g.drawString("" + lines, 260, 253);
            g.drawString("" + (7 - level), 260, 300);
            STATE = false;
        }
    }

    /**
     * @param g
     */
    public void paintGamePause(Graphics g) {
        if (!STATE && !isGameOver()) {
            g.drawImage(pause, -15, -15, null);
        }
    }

    /**
     * @param g
     */
    public void paintTabs(Graphics g) {

        int x = 410;
        int y = 160;

        Color color = new Color(0, 0, 0, 253);
        g.setColor(color);

        Font f = new Font(Font.SERIF, Font.BOLD, 30);
        g.setFont(f);
        g.drawString("" + score, x, y);
        y += 56;
        g.drawString("" + lines, x, y);
        y += 56;
        g.drawString("" + (7 - level), x, y);
    }

    /**
     * Nextone
     *
     * @param g
     */
    public void paintNextone(Graphics g) {
        if (nextone == null) return;
        Cell[] cells = nextone.cells;
        for (int i = 0; i < cells.length; i++) {
            Cell c = cells[i];
            int row = c.getRow();
            int col = c.getCol() + 9;
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;
            g.drawImage(c.getColor(), x, y, null);
        }
    }

    /**
     * @param g
     */
    public void paintTetromino(Graphics g) {

        if (tetromino == null) return;

        //System.out.println(tetromino == null);
        Cell[] cells = tetromino.cells;
        for (int i = 0; i < cells.length; i++) {

            Cell c = cells[i];
            int col = c.getCol();
            int row = c.getRow();
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;
            g.drawImage(c.getColor(), x, y, null);
        }
    }

    /**
     * @param g
     */
    public void paintWall(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = wall[row][col];
                int rows = row * CELL_SIZE;
                int cols = col * CELL_SIZE;
                //g.drawImage(O, rows, cols, null);
                if (cell == null) {
                    //System.out.println(0);
                } else {
                    g.drawImage(cell.getColor(), cols, rows, null);
                }
            }
        }
    }

    public static void startTetris() {
        JFrame frame = new JFrame("俄罗斯方块");

        Tetris tetris = new Tetris();

        frame.add(tetris);
        frame.setSize(525, 600);
        frame.setLocationRelativeTo(null);
        //frame.setUndecorated(true);
        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);


        frame.setVisible(true);

        tetris.action();
    }
}

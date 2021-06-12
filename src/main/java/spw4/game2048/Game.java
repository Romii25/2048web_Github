package spw4.game2048;

import java.util.Random;


public class Game {
    private final int size = 4;
    private int score;
    private boolean isWon = false;
    private boolean isOver = true;
    public int moves = 0;
    public int[][] board = new int[size][size];



    public Game() {
        this.score = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
    }

    private void setRandomValue() {
        Random rand = new Random();
        boolean initialized = false;

        while (!initialized) {
            int i = rand.nextInt(4);
            int j = rand.nextInt(4);
            if (board[i][j] == 0) {
                int nr = rand.nextInt(10);
                if (nr == 9) {
                    board[i][j] = 4;
                } else {
                    board[i][j] = 2;
                }
                initialized = true;
            }
        }
    }

    public void initialize() {
        setRandomValue();
        setRandomValue();
    }

    public int getScore() {
        return score;
    }

    public int getMoves() {return moves;}

    public int getValueAt(int i, int j) { return board[i][j];}


    public boolean isOver() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    isOver = false;
                }
            }
        }
        return isOver;
    }

    public boolean isWon() {
        return isWon;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Moves: %d   Score: %d", moves, score));
        s.append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    s.append(".     ");
                } else {
                    s.append(String.format("%-6d",board[i][j]));
                }

            }
            s.append("\n");
        }
        return s.toString();
    }

    private void merge(int[] row){
        boolean[] merged = new boolean[] {false,false,false,false};

        for(int i = 1; i < 4; i++){
            if(row[i] != 0){
                int j = i;
                while(j > 0 && (row[j-1] == 0||row[j-1] == row[j]) && !merged[j-1] && !merged[j]){
                    if (row[j-1] == row[j]) {
                        row[j-1] = row[j-1] * 2;
                        score += row[j-1];
                        merged[j-1] = true;
                        if (row[j-1] == 2048) {
                            isWon = true;
                        }
                    } else {
                        row[j-1] = row[j];
                    }
                    row[j] = 0;
                    j--;
                }
            }
        }
    }

    private void reverse(int[] row) {
        int tmp = row[0];
        row[0] = row[3];
        row[3] = tmp;

        tmp = row[1];
        row[1] = row[2];
        row[2] = tmp;
    }


    public void move(Direction direction) {
        switch (direction) {
            case left:
                for (int r = 0; r < 4; r++) {
                    merge(board[r]);
                }
                break;
            case right:
                for (int r = 0; r < 4; r++) {
                    reverse(board[r]);
                    merge(board[r]);
                    reverse(board[r]);
                }
                break;
            case up:
                int[] column = new int[size];
                for (int c = 0; c < 4; c++) {
                    for (int i = 0; i < size; i++) {
                        column[i] = board[i][c];

                    }
                    merge(column);
                    for (int i = 0; i < size; i++) {
                        board[i][c] = column[i];
                    }
                }

                break;
            case down:
                for (int c = 0; c < 4; c++) {
                    column = new int[size];
                    for (int i = 0; i < size; i++) {
                        column[i] = board[i][c];
                    }
                    reverse(column);
                    merge(column);
                    reverse(column);
                    for (int i = 0; i < size; i++) {
                        board[i][c] = column[i];
                    }
                }
                break;
        }
        moves++;
        if (!isOver()) {
           setRandomValue();
        }
    }
}

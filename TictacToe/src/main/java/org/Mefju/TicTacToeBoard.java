package org.Mefju;

public class TicTacToeBoard {
    private int[][] board = new int[3][3];
    private int currentPlayer = 1;

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void printBoard(){
        System.out.println(" A B C");
        for(int i = 0;i<3;i++)
        {
            System.out.print((i+1)+" ");
            for(int j =0;j<3;j++){
                char symbol = switch (board[i][j]){
                    case 1->'X';
                    case 2->'O';
                    default -> '.';
                };
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }
    public void switchPlayer(){
        this.currentPlayer = (currentPlayer ==1)?2:1;
    }
    public int checkWinner(){
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 &&
                    board[i][0] == board[i][1] &&
                    board[i][1] == board[i][2]) return board[i][0];

            if (board[0][i] != 0 &&
                    board[0][i] == board[1][i] &&
                    board[1][i] == board[2][i]) return board[0][i];
        }

        if (board[0][0] != 0 &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) return board[0][0];

        if (board[0][2] != 0 &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) return board[0][2];

        boolean full = true;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) full = false;
            }
        }

        return full ? 3 : 0;
    }
    public boolean makeMove(int row, int col,int idPlayer){
        if(board[row][col] ==0){
            board[row][col]= idPlayer;
            return true;
        }
        return false;
    }
    public int[][] getBoard() {
        return board;
    }
}

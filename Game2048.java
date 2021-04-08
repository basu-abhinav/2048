package Game;
import Game.Tile;
import java.util.Random;

public class Game2048
{
    enum State
    {
        start, won, running, over
    }

    final static int target = 2048;

    static int highest;
    static int score;

    private Random rand = new Random();

    private Tile[][] tiles;
    private int side = 4;
    private State gamestate = State.start;
    private boolean checkingAvailableMoves;

    public Game2048()
    {
        startGame();
        this.checkingAvailableMoves = false;
    }

    private void startGame()
    {
        if (gamestate != State.running)
        {
            this.score = 0;
            this.highest = 0;
            this.gamestate = State.running;
            this.tiles = new Tile[side][side];
            addRandomTile(true);
            addRandomTile(true);
        }
    }

    void addRandomTile(boolean moved)
    {
        if(!moved)
        {
            return;
        }
        int pos = 0;
        int row, col;
        do {
            pos = rand.nextInt(side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);

        int val = rand.nextInt(2) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
    }

    private boolean move(int countDownFrom, int yIncr, int xIncr)
    {
        boolean moved = false;

        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);

            int r = j / side;
            int c = j % side;

            if (tiles[r][c] == null)
                continue;

            int nextR = r + yIncr;
            int nextC = c + xIncr;

            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {

                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];

                if (next == null) {

                    if (checkingAvailableMoves)
                        return true;

                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;

                } else if (next.canMergeWith(curr)) {

                    if (checkingAvailableMoves)
                        return true;

                    int value = next.mergeWith(curr);
                    if (value > highest)
                        highest = value;
                    score += value;
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }

        if (moved) {
            if (highest < target) {
                clearMerged();
                //addRandomTile();
                if (!movesAvailable()) {
                    gamestate = State.over;
                }
            } else if (highest == target)
                gamestate = State.won;
        }

        return moved;
    }

    boolean moveUp()
    {
        return move(0, -1, 0);
    }

    boolean moveDown()
    {
        return move(side * side - 1, 1, 0);
    }

    boolean moveLeft()
    {
        return move(0, 0, -1);
    }

    boolean moveRight()
    {
        return move(side * side - 1, 0, 1);
    }

    void clearMerged()
    {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }

    private boolean movesAvailable()
    {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }

    void printBoard()
    {
        System.out.println("- - - - - - - - -");
        for(int i=0;i<this.side;i++)
        {
            for(int j=0;j<this.side;j++)
            {
                if(this.tiles[i][j] != null)
                {
                    System.out.print("| "+this.tiles[i][j].getValue()+" ");
                }
                else
                {
                    System.out.print("|   ");
                }
            }
            System.out.print("|");
            System.out.println();
            System.out.println("- - - - - - - - -");
        }
    }

    Tile[][] getBoard()
    {
        final Tile[][] clone = this.tiles.clone();
        return clone;
    }

    void setBoard(Tile[][] board)
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                this.tiles[i][j] = board[i][j];
            }
        }
    }

    int getSum()
    {
        int sum = 0;
        for(int i=0;i<this.side;i++)
        {
            for(int j=0;j<this.side;j++)
            {
                if(this.tiles[i][j] != null)
                {
                    sum += this.tiles[i][j].getValue();
                }
            }
        }
        return sum;
    }

    public static void main(String[] args)
    {
        Game2048 newGame = new Game2048();
        newGame.printBoard();

        newGame.addRandomTile(newGame.moveDown());
        System.out.println("Down");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveRight());
        System.out.println("Right");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveDown());
        System.out.println("Down");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveRight());
        System.out.println("Right");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveDown());
        System.out.println("Down");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveRight());
        System.out.println("Right");
        newGame.printBoard();

        Tile[][] newBoard = new Tile[4][4];
        newBoard[3][0] = new Tile(4);
        newBoard[3][1] = new Tile(4);
        newBoard[3][2] = new Tile(4);
        newBoard[3][3] = new Tile(4);
        newGame.setBoard(newBoard);

        System.out.println("Board Changed");

        newGame.addRandomTile(newGame.moveDown());
        System.out.println("Down");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveRight());
        System.out.println("Right");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveDown());
        System.out.println("Down");
        newGame.printBoard();
        newGame.addRandomTile(newGame.moveRight());
        System.out.println("Right");
        newGame.printBoard();


    }
}

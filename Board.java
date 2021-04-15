package Game;
import java.util.Random;

public class Board {

    private Tile[][] tiles; 
    private static final int DIMENSION = 4; 
    private boolean checkingAvailableMoves;
    private GameState gameState;
    private Random rand = new Random();

    public Board()
    {
        initializeBoard();
        this.checkingAvailableMoves = false;
        this.gameState = GameState.START;
    }

    public Board(Board board){
        this.tiles = board.getTiles().clone();
        this.gameState = board.getGameState();
        checkingAvailableMoves = false;
    }

    public void printBoard()
    {
        System.out.println("- - - - - - - - -");
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
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

    private void initializeBoard()
    {
        if (gameState != GameState.RUNNING)
        {
            this.gameState = GameState.RUNNING;
            this.tiles = new Tile[DIMENSION][DIMENSION];
            addRandomTile(true);
            addRandomTile(true);
        }
    }

    private boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = false;
        for(Direction direction: Direction.values()){
            hasMoves = hasMoves || moveCommand(Operation.ADD, direction);
            if(hasMoves){
                break;
            }
        }
        checkingAvailableMoves = false;
        return hasMoves;
    }

    private void addRandomTile(){
        int pos = 0;
        int row, col;
        do {
            pos = rand.nextInt(DIMENSION * DIMENSION);
            row = pos / DIMENSION;
            col = pos % DIMENSION;
        } while (tiles[row][col] != null);

        int val = rand.nextInt(2) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
        System.out.println("addRandomTile()");
    }

    private void addRandomTile(boolean moved)
    {
        if(!moved)
        {
            return;
        }
        int pos = 0;
        int row, col;
        do {
            pos = rand.nextInt(DIMENSION * DIMENSION);
            row = pos / DIMENSION;
            col = pos % DIMENSION;
        } while (tiles[row][col] != null);

        int val = rand.nextInt(2) == 0 ? 4 : 2;
        tiles[row][col] = new Tile(val);
        System.out.println("addRandomTile(boolean moved)");
    }

    private void clearMerged()
    {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }

    private boolean move(int countDownFrom, int yIncr, int xIncr, Operation operation) {
        
        boolean moved = false;

        for (int i = 0; i < DIMENSION * DIMENSION; i++) {
            int j = Math.abs(countDownFrom - i);
 
            int r = j / DIMENSION;
            int c = j % DIMENSION;
 
            if (tiles[r][c] == null)
                continue;
 
            int nextR = r + yIncr;
            int nextC = c + xIncr;
 
            while (nextR >= 0 && nextR < DIMENSION && nextC >= 0 && nextC < DIMENSION) {
 
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
 
                } 
                else if (next.canMergeWith(curr)) {
 
                    if (checkingAvailableMoves)
                        return true;
 
                    next.mergeWith(curr,operation);
                    if(operation.equals(Operation.SUBTRACT)){
                        tiles[nextR][nextC]=null;
                    }
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } 
                else
                    break;
            }
        }
 
        if (moved) {
            clearMerged();
            addRandomTile();
            if (!movesAvailable())
            {
                gameState = GameState.GAME_OVER;
            }
        }
        return moved;
    }
  
    public boolean checkMoveCommand(Operation operation, Direction direction)
    {
        Board boardClone = new Board(this);
        boardClone.moveCommand(operation, direction);
        return !boardClone.getGameState().equals(GameState.GAME_OVER);
    }

    public boolean moveCommand(Operation operation, Direction direction)
    {
        int countDownFrom = 0 , yIncr = 0, xIncr = 0;
        switch (direction){
            case LEFT:{
                countDownFrom = 0;
                yIncr = 0;
                xIncr =-1;
                break;
            }
            case RIGHT:{
                countDownFrom = DIMENSION * DIMENSION - 1;
                yIncr = 0;
                xIncr =1;
                break;
            }
            case UP:{
                countDownFrom = 0;
                yIncr = -1;
                xIncr =0;
                break;
            }
            case DOWN:{
                countDownFrom = DIMENSION*DIMENSION-1;
                yIncr = 1;
                xIncr =0;
                break;
            }
        }
        return move(countDownFrom,yIncr,xIncr,operation);
    }

    public boolean moveCommand(Direction direction)
    {
        int countDownFrom = 0 , yIncr = 0, xIncr = 0;
        switch (direction){
            case LEFT:{
                countDownFrom = 0;
                yIncr = 0;
                xIncr =-1;
                break;
            }
            case RIGHT:{
                countDownFrom = DIMENSION * DIMENSION - 1;
                yIncr = 0;
                xIncr =1;
                break;
            }
            case UP:{
                countDownFrom = 0;
                yIncr = -1;
                xIncr =0;
                break;
            }
            case DOWN:{
                countDownFrom = DIMENSION*DIMENSION-1;
                yIncr = 1;
                xIncr =0;
                break;
            }
        }
        return move(countDownFrom,yIncr,xIncr,Operation.ADD);
    }

    private Tile[][] getTiles()
    {
        return tiles;
    }

    private void setTiles(Tile[][] tiles)
    {
        this.tiles = tiles;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    private void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }

    public boolean assign(int x, int y, int value)
    {
        if(x>1 && x<=DIMENSION && y>0 && y<=DIMENSION)
        {
            tiles[x-1][y-1] = new Tile(value);
            return true;
        }
        return false;
    }

    public boolean var(int x,int y, String name)
    {
        if(x>0 && x<=DIMENSION && y>0 && y<=DIMENSION && tiles[x-1][y-1] != null)
        {
            tiles[x-1][y-1].addName(name);
            return true;
        }
        return false;
    }

    public int getTileValue(int x, int y)
    {
        if(x>1 && x<=DIMENSION && y>0 && y<=DIMENSION && tiles[x-1][y-1] != null)
        {
            return tiles[x-1][y-1].getValue();
        }
        return -1;
    }

    public String toString()
    {
        String str = "";
        for(Tile[] tileArray: tiles){
            for(Tile tile: tileArray)
            {
                try
                {
                    str = str + tile.getValueString() + " ";
                }
                catch(NullPointerException e)
                {
                    str = str + "0 ";
                }
            }
        }

        for(int i=0;i<DIMENSION;i++){
            for(int j=0;j<DIMENSION;j++){
                if(tiles[i][j] != null && tiles[i][j].isNamed()){
                    str = str +" "+Integer.toString(i) + ',' + Integer.toString(j) + tiles[i][j].getNameString();
                }
            }
        }

        return str;
    }

}

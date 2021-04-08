import Game.*;
import java.util.Random;

public class Driver
{
    Tile[][] prevState;

    public Driver()
    {
        this.prevState = new Tile[4][4];
    }

    void printBoard(Tile[][] tiles)
    {
        System.out.println("- - - - - - - - -");
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
            {
                if(tiles[i][j] != null)
                {
                    System.out.print("| "+tiles[i][j].getValue()+" ");
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

    public static void main(String args[])
    {
        Game2048 newGame = new Game2048();
        System.out.println("Game begins");
        newGame.printBoard();
        Driver driver = new Driver();
        Random rand = new Random();
        while(newGame.getSum()<8)
        {
            //store previous state
            for(int i=0;i<4;i++)
            {
                for(int j=0;j<4;j++)
                {
                    driver.prevState[i][j] = newGame.getBoard()[i][j];
                }
            }

            //move
            int move = rand.nextInt(4);
            boolean moved = false;
            switch(move)
            {
                case 0:
                    System.out.println("Left Move");
                    newGame.addRandomTile(newGame.moveLeft());
                    break;
                case 1:
                    System.out.println("Right Move");
                    newGame.addRandomTile(newGame.moveRight());
                    break;
                case 2:
                    System.out.println("Up Move");
                    newGame.addRandomTile(newGame.moveUp());
                    break;
                case 3:
                    System.out.println("Down Move");
                    newGame.addRandomTile(newGame.moveDown());
                    break;
            }

            if(newGame.getSum() == 8)
            {
                newGame.printBoard();
                System.out.println("Sum = 8\nGame ends");
                break;
            }
            if(newGame.getSum() < 8)
            {
                newGame.printBoard();
            }
            if(newGame.getSum() > 8)
            {
                newGame.printBoard();//discarded state
                System.out.println("Sum exceeded 8\nDiscarding the above state.");
                System.out.println("Going back to: ");
                driver.printBoard(driver.prevState);
                newGame.setBoard(driver.prevState);
            }
        }
    }
}

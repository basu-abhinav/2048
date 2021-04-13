package Game;
import Game.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

public class Driver
{
    Tile[][] prevState;

    private ArrayList<String> moveOperations;
    private ArrayList<String> moveDirections;
    private ArrayList<String> commands;

    public Driver()
    {
        this.prevState = new Tile[4][4];
        this.moveOperations = new ArrayList<String>(Arrays.asList("ADD","SUBTRACT","MULTIPLY","DIVIDE"));
        this.moveDirections = new ArrayList<String>(Arrays.asList("LEFT","RIGHT","UP","DOWN"));
        this.commands = new ArrayList<String>(Arrays.asList("ASSIGN","VAR","VALUE"));
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

    private Operation getOperation(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        String operation = keywords.nextToken();

        if(operation.compareTo("ADD") == 0)
            return Operation.ADD;
        if(operation.compareTo("SUBTRACT") == 0)
            return Operation.SUBTRACT;
        if(operation.compareTo("MULTIPLY") == 0)
            return Operation.MULTIPLY;
        if(operation.compareTo("DIVIDE") == 0)
            return Operation.DIVIDE;
        if(operation.compareTo("ASSIGN") == 0)
            return Operation.ASSIGN;
        if(operation.compareTo("VAR") == 0)
            return Operation.VAR;
        if(operation.compareTo("VALUE") == 0)
            return Operation.VALUE;
        return null;

    }

    private boolean isValidCommand(String command)
    {



        return false;
    }

    private boolean isValidMove(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        if(keywords.countTokens() != 2)
            return false;
        if(!this.moveOperations.contains(keywords.nextToken()))
            return false;
        if(!this.moveDirections.contains(keywords.nextToken()))
            return false;
        return true;
    }

    private boolean isValidOperation(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        if(keywords.countTokens() > 4 || keywords.countTokens() < 3)
            return false;
        String operation = keywords.nextToken();
        if(!this.commands.contains(operation))
            return false;
        
        if(operation.compareTo("ASSIGN") == 0)
        {
            int value = Integer.parseInt(keywords.nextToken());
            if(keywords.nextToken().compareTo("TO") != 0)
                return false;
            
        }
    }

    private boolean endsWithFullStop(String command)
    {
        if(command.endsWith("."))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private String removeFullStop(String command)
    {
        String keywords[] = command.split(".");
        return keywords[0];
    }

    

    private Error getError(Operation operation)
    {
        return Error.VARIABLENAME;
    }

    private String getErrorMessage(Error error)
    {
        switch (error)
        {
            case FULLSTOP:
                return "You need to end a command with a full stop.";
            case WRONGINDEX:
                return "There is no tile like that. The tile co-ordinates must be in the range 1,2,3,4";
            case VARIABLENAME:
                return "No, a keyword cannot be a variable name.";
            case INCORRECTVALUE:
                return "Please enter a positive value.";
            default:
                return "Sorry, I don’t understand that.";
        }
    }

    public static void main(String args[])
    {
        
    }
}

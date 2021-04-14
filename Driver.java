package Game;
import Game.Tile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

public class Driver
{
    private ArrayList<String> moveOperations;
    private ArrayList<String> moveDirections;
    private ArrayList<String> commands;
    private ArrayList<String> otherKeywords;

    public Driver()
    {
        this.moveOperations = new ArrayList<String>(Arrays.asList("ADD","SUBTRACT","MULTIPLY","DIVIDE"));
        this.moveDirections = new ArrayList<String>(Arrays.asList("LEFT","RIGHT","UP","DOWN"));
        this.commands = new ArrayList<String>(Arrays.asList("ASSIGN","VAR","VALUE"));
        this.otherKeywords = new ArrayList<String>(Arrays.asList("IS","TO","IN"));
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

    private Direction getDirection(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        String operation = keywords.nextToken();
        String direction = keywords.nextToken();

        if(direction.compareTo("LEFT") == 0)
            return Direction.LEFT;
        if(direction.compareTo("RIGHT") == 0)
            return Direction.RIGHT;
        if(direction.compareTo("UP") == 0)
            return Direction.UP;
        if(direction.compareTo("DOWN") == 0)
            return Direction.DOWN;
        return null;
    }

    private boolean isValidCommand(String command)
    {
        StringTokenizer tokens = new StringTokenizer(command," ");
        int numTokens = tokens.countTokens();
        if(numTokens > 4 || numTokens < 2)
            return false;
        String firstToken = tokens.nextToken();
        if(this.moveOperations.contains(firstToken))
            return isValidMove(command);
        if(this.commands.contains(firstToken))
            return isValidOperation(command);
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
        int numTokens = keywords.countTokens();
        if(numTokens > 4 || numTokens < 3)
            return false;
        String operation = keywords.nextToken();
        if(!this.commands.contains(operation))
            return false;
        
        try
        {
            if(operation.compareTo("ASSIGN") == 0 && numTokens == 4)
            {
                int value = Integer.parseInt(keywords.nextToken());
                if(value < 0)
                    return false;

                if(keywords.nextToken().compareTo("TO") != 0)
                    return false;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return false;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return false;
                
                return true;
            }

            if(operation.compareTo("VAR") == 0 && numTokens == 4)
            {
                String name = keywords.nextToken();
                if(this.moveDirections.contains(name) || this.moveOperations.contains(name) || this.commands.contains(name) || this.otherKeywords.contains(name))
                    return false;
                if(keywords.nextToken().compareTo("IS") != 0)
                    return false;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return false;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return false;
                
                return true;
            }

            if(operation.compareTo("VALUE") == 0 && numTokens == 3)
            {
                if(keywords.nextToken().compareTo("IN") != 0)
                    return false;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return false;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return false;
                
                return true;
            }
        }
        catch(NumberFormatException e)
        {
            return false;
        }

        return false;
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

    private Error getError(String command)
    {
        StringTokenizer tokens = new StringTokenizer(command," ");
        int numTokens = tokens.countTokens();
        if(numTokens > 4 || numTokens < 2)
            return Error.INVALIDCOMMAND;
        String firstToken = tokens.nextToken();
        if(this.moveOperations.contains(firstToken))
            return getErrorMove(command);
        if(this.commands.contains(firstToken))
            return getErrorOperation(command);
        return Error.INVALIDCOMMAND;
    }

    private Error getErrorMove(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        if(keywords.countTokens() != 2)
            return Error.INVALIDCOMMAND;
        if(!this.moveOperations.contains(keywords.nextToken()))
            return Error.INVALIDCOMMAND;
        if(!this.moveDirections.contains(keywords.nextToken()))
            return Error.INVALIDCOMMAND;
        return Error.INVALIDCOMMAND;
    }

    private Error getErrorOperation(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        int numTokens = keywords.countTokens();
        if(numTokens > 4 || numTokens < 3)
            return Error.INVALIDCOMMAND;
        String operation = keywords.nextToken();
        if(!this.commands.contains(operation))
            return Error.INVALIDCOMMAND;
        
        try
        {
            if(operation.compareTo("ASSIGN") == 0 && numTokens == 4)
            {
                int value = Integer.parseInt(keywords.nextToken());
                if(value < 0)
                    return Error.INCORRECTVALUE;

                if(keywords.nextToken().compareTo("TO") != 0)
                    return Error.INVALIDCOMMAND;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return Error.INVALIDCOMMAND;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return Error.WRONGINDEX;
                
                return Error.INVALIDCOMMAND;
            }

            if(operation.compareTo("VAR") == 0 && numTokens == 4)
            {
                String name = keywords.nextToken();
                if(this.moveDirections.contains(name) || this.moveOperations.contains(name) || this.commands.contains(name) || this.otherKeywords.contains(name))
                    return Error.VARIABLENAME;
                if(keywords.nextToken().compareTo("IS") != 0)
                    return Error.INVALIDCOMMAND;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return Error.INVALIDCOMMAND;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return Error.WRONGINDEX;
                
                return Error.INVALIDCOMMAND;
            }

            if(operation.compareTo("VALUE") == 0 && numTokens == 3)
            {
                if(keywords.nextToken().compareTo("IN") != 0)
                    return Error.INVALIDCOMMAND;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return Error.INVALIDCOMMAND;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return Error.WRONGINDEX;
                
                return Error.INVALIDCOMMAND;
            }
        }
        catch(NumberFormatException e)
        {
            return Error.INVALIDCOMMAND;
        }

        return Error.INVALIDCOMMAND;
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

    private boolean executeMove(Board board,String command)
    {
        return board.moveCommand(this.getOperation(command), this.getDirection(command));
    }

    private boolean executeOperation(Board board,String command)
    {
        Operation oper = this.getOperation(command);
        StringTokenizer tokens = null,indices = null;
        String index = null;
        int x=0,y=0;
        switch(oper)
        {
            case ASSIGN:
                tokens = new StringTokenizer(command," ");
                String assign = tokens.nextToken();
                int value = Integer.parseInt(tokens.nextToken());
                String to = tokens.nextToken();
                index = tokens.nextToken();
                indices = new StringTokenizer(index,",");
                x = Integer.parseInt(indices.nextToken());
                y = Integer.parseInt(indices.nextToken());
                return board.assign(x, y, value);
            case VAR:
                tokens = new StringTokenizer(command," ");
                String var = tokens.nextToken();
                String name = tokens.nextToken();
                String is = tokens.nextToken();
                index = tokens.nextToken();
                indices = new StringTokenizer(index,",");
                x = Integer.parseInt(indices.nextToken());
                y = Integer.parseInt(indices.nextToken());
                return board.var(x, y, name);                
            default:
                return false;
        }
    }

    private int executeOperationValue(Board board,String command)
    {
        StringTokenizer tokens = new StringTokenizer(command," ");
        String val = tokens.nextToken();
        String in = tokens.nextToken();
        String index = tokens.nextToken();
        StringTokenizer indices = new StringTokenizer(index,",");
        int x = Integer.parseInt(indices.nextToken());
        int y = Integer.parseInt(indices.nextToken());
        return board.getTileValue(x, y);
    }

    public static void main(String args[])
    {
        System.out.println("Welcome to the 2048 Game Engine!");
    }
}

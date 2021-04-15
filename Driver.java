package Game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Random;

public class Driver
{
    private ArrayList<String> moveOperations;
    private ArrayList<String> moveDirections;
    private ArrayList<String> commands;
    private ArrayList<String> otherKeywords;
    private ArrayList<Operation> supportedMoves;
    //private ArrayList<Direction> supportedDirections;
    private ArrayList<Operation> supportedOperations;
    private Random rand = new Random();

    public Driver()
    {
        this.moveOperations = new ArrayList<String>(Arrays.asList("ADD","SUBTRACT","MULTIPLY","DIVIDE"));
        this.moveDirections = new ArrayList<String>(Arrays.asList("LEFT","RIGHT","UP","DOWN"));
        this.commands = new ArrayList<String>(Arrays.asList("ASSIGN","VAR","VALUE"));
        this.otherKeywords = new ArrayList<String>(Arrays.asList("IS","TO","IN"));
        this.supportedMoves = new ArrayList<Operation>(Arrays.asList(Operation.ADD,Operation.SUBTRACT,Operation.MULTIPLY,Operation.DIVIDE));
        //this.supportedDirections = new ArrayList<Direction>(Arrays.asList(Direction.UP,Direction.DOWN,Direction.LEFT,Direction.RIGHT));
        this.supportedOperations = new ArrayList<Operation>(Arrays.asList(Operation.ASSIGN,Operation.VAR,Operation.VALUE));
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
        return command.substring(0,command.length()-1);
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
        if(this.checkCase(firstToken))
            return Error.SYNTAXERROR;
        return Error.INVALIDCOMMAND;
    }

    private Error getErrorMove(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        if(keywords.countTokens() != 2)
            return Error.SYNTAXERROR;

        
        String firstToken = keywords.nextToken();
        if(!this.moveOperations.contains(firstToken))
            return Error.INVALIDCOMMAND;


        String secondToken = keywords.nextToken();
        if(!this.moveDirections.contains(secondToken))
        {
            if(this.checkCase(secondToken))
                return Error.SYNTAXERROR;
            else
                return Error.INVALIDCOMMAND;
        }        
        return Error.INVALIDCOMMAND;
    }

    private Error getErrorOperation(String command)
    {
        StringTokenizer keywords = new StringTokenizer(command," ");
        int numTokens = keywords.countTokens();
        if(numTokens > 4 || numTokens < 3)
            return Error.SYNTAXERROR;
        String operation = keywords.nextToken();
        
        try
        {
            if(operation.compareTo("ASSIGN") == 0 && numTokens == 4)
            {
                int value = Integer.parseInt(keywords.nextToken());
                if(value < 0)
                    return Error.INCORRECTVALUE;
                String to = keywords.nextToken();
                if(to.compareTo("TO") != 0)
                    return Error.SYNTAXERROR;                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return Error.SYNTAXERROR;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return Error.WRONGINDEX;
                
                return Error.SYNTAXERROR;
            }

            if(operation.compareTo("VAR") == 0 && numTokens == 4)
            {
                String name = keywords.nextToken();
                if(this.moveDirections.contains(name) || this.moveOperations.contains(name) || this.commands.contains(name) || this.otherKeywords.contains(name))
                    return Error.VARIABLENAME;
                String is = keywords.nextToken();
                if(is.compareTo("IS") != 0)
                    return Error.SYNTAXERROR;                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return Error.SYNTAXERROR;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return Error.WRONGINDEX;
                
                return Error.SYNTAXERROR;
            }

            if(operation.compareTo("VALUE") == 0 && numTokens == 3)
            {
                if(keywords.nextToken().compareTo("IN") != 0)
                    return Error.SYNTAXERROR;
                
                StringTokenizer indexValues = new StringTokenizer(keywords.nextToken(),",");
                if(indexValues.countTokens() != 2)
                    return Error.SYNTAXERROR;
                int x = Integer.parseInt(indexValues.nextToken());
                int y = Integer.parseInt(indexValues.nextToken());

                if(x < 1 || x > 4 || y < 1 || y > 4)
                    return Error.WRONGINDEX;
                
                return Error.SYNTAXERROR;
            }
        }
        catch(NumberFormatException e)
        {
            return Error.SYNTAXERROR;
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
                return "There is no tile like that. The tile co-ordinates must be in the range 1,2,3,4.";
            case VARIABLENAME:
                return "No, a keyword cannot be a variable name.";
            case INCORRECTVALUE:
                return "Please enter a positive value.";
            case SYNTAXERROR:
                return "Syntax Error.";
            case NONUNIQUENAME:
                return "Same name cannot be assigned again to a Tile.";
            case NULLNAMEASSIGN:
                return "Name cannot be assigned to an empty Tile.";
            default:
                return "Sorry, I don’t understand that.";
        }
    }

    private boolean executeMove(Board board,String command,int nextTile)
    {
        return board.moveCommand(this.getOperation(command), this.getDirection(command),nextTile);
        //return true;
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
                int returnVal = board.var(x, y, name);
                if(returnVal == -1)
                    return false;
                else if(returnVal == 0)
                {
                    System.out.println(getErrorMessage(Error.NONUNIQUENAME));
                    return false;
                }
                else if(returnVal == -2)
                {
                    System.out.println(getErrorMessage(Error.NULLNAMEASSIGN));
                    return false;
                }
                return true;
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
        //return 1;
    }

    private boolean checkCase(String token)
    {
        for(String x : this.moveOperations)
        {
            if(token.equalsIgnoreCase(x))
                return true;
        }
        for(String x : this.commands)
        {
            if(token.equalsIgnoreCase(x))
                return true;
        }
        for(String x : this.moveDirections)
        {
            if(token.equalsIgnoreCase(x))
                return true;
        }
        for(String x : this.otherKeywords)
        {
            if(token.equalsIgnoreCase(x))
                return true;
        }
        return false;
    }
    
    public static void main(String args[])
    {
        Driver driver =  new Driver();
        System.out.println("Welcome to the 2048 Game Engine!");
        Board game = new Board();
        System.out.println("The Start state is :");
        game.printBoard();
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            if(game.getGameState().equals(GameState.GAME_OVER))
            {
                System.out.println("Game Over. Thanks for Playing!");
                break;
            }
            if(!game.movesAvailable())
            {
                System.out.println("Game Over. Thanks for Playing!");
                break;
            }
            System.out.println("Please type a command:");
            String command = sc.nextLine().trim();
            if(!driver.endsWithFullStop(command))
            {
                System.out.println(driver.getErrorMessage(Error.FULLSTOP));
                int err = -1;
                System.err.println(err);
                continue;
            }
            else
            {
                command = driver.removeFullStop(command);
            }


            if(!driver.isValidCommand(command))
            {
                System.out.println(driver.getErrorMessage(driver.getError(command)));
                int err = -1;
                System.err.println(err);
                continue;
            }
            else
            {
                //command is valid
                //get operation


                Operation oper = driver.getOperation(command);

                //execute operation
                if(driver.supportedMoves.contains(oper))
                {
                    //check if move leads to end of game
                    int val = driver.rand.nextInt(2) == 0 ? 4 : 2;
                    if(!game.checkMoveCommand(oper, driver.getDirection(command),val))
                    {
                        System.out.println("WARNING, executing this move will end the game!");
                        System.out.print("Do you want to continue? Enter N to retry, Enter any other key to continue : ");
                        String response = sc.nextLine().trim();
                        System.out.println();
                        if(response.equalsIgnoreCase("N"))
                        {
                            continue;
                        }
                    }
                    
                    if(driver.executeMove(game, command,val))
                    {
                        System.out.println("Move Successful, random tile added");
                        System.out.println("The current state is:");
                        game.printBoard();
                        System.err.println(game);
                    }
                    else
                    {
                        System.out.println("Move not possible, try other moves.");
                        System.out.println("The current state is:");
                        game.printBoard();
                        System.err.println(game);

                    }
                }

                if(driver.supportedOperations.contains(oper))
                {
                    if(oper.equals(Operation.VALUE))
                    {
                        int x = driver.executeOperationValue(game, command);
                        System.out.println("Operation Successful.");
                        System.out.println("The value at the specified position is "+x);
                    }
                    else
                    {
                        if(driver.executeOperation(game, command))
                        {
                            System.out.println("Operation Successful.");
                            System.out.println("The current state is:");
                            game.printBoard();
                            System.err.println(game);
                        }
                        else
                        {
                            System.out.println("Operation failed to execute. Please try again.");
                            int err = -1;
                            System.err.println(err);
                        }
                    }
                }
            }
        }
    }
}

package Game;
enum Operation
{
    ADD, SUBTRACT, MULTIPLY, DIVIDE, ASSIGN, VAR, VALUE
}

enum Direction
{
    LEFT, RIGHT, UP, DOWN
}

enum GameState
{
    RUNNING,GAME_OVER
}

enum Error
{
    FULLSTOP, WRONGINDEX, VARIABLENAME, INCORRECTVALUE, INVALIDCOMMAND
}
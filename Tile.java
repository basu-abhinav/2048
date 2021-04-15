package Game;
import java.util.ArrayList;

class Tile
{
    private int value;
    private ArrayList<String> names;
    private boolean isMerged;
    
    public Tile(int value)
    {
        this.value = value;
        this.names = new ArrayList<>();
        this.isMerged = false;
    }
    
    public Tile(Tile tile)
    {
        this.value = tile.getValue();
        this.names = new ArrayList<String>(tile.getNames());
        this.isMerged = tile.isMerged();
    }

    public int getValue()
    {
        return this.value;
    }

    public ArrayList<String> getNames()
    {
        return this.names;
    }

    public void addName(String name)
    {
        names.add(name);
    }

    public void addName(ArrayList<String> newNames)
    {
        names.addAll(newNames);
    }

    public boolean isMerged()
    {
        return isMerged;
    }

    public void setMerged(boolean isMerged)
    {
        this.isMerged = isMerged;
    }

    public boolean canMergeWith(Tile other)
    {
        return !isMerged && other != null && !other.isMerged() && value == other.getValue();
    }

    public boolean mergeWith(Tile other, Operation operation)
    {
        if(canMergeWith(other))
        {
            switch (operation)
            {
                case ADD:
                {
                    value += value;
                    break;
                }
                case SUBTRACT:
                {
                    value -= value;
                    break;
                }
                case MULTIPLY:
                {
                    value *= value;
                    break;
                }
                case DIVIDE:
                {
                    value /= value;
                    break;
                }
            }     
            this.addName(other.getNames());  
            this.setMerged(true);
            return true;
        }
        return false;
    }

    public String getValueString()
    {
        return String.valueOf(value);
    }

    public boolean isNamed()
    {
        return !names.isEmpty();
    } 

    public String getNameString()
    {
        String str="";
        for(String name: names)
        {
            str += name + ",";
        }
        str = str.substring(0,str.length()-1);
        return str;
    }

}
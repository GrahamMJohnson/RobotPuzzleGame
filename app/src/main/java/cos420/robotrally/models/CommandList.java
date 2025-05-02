package cos420.robotrally.models;

import java.util.LinkedList;

import cos420.robotrally.commands.*;
import cos420.robotrally.enumerations.SubroutineType;

/**
 * Class that stores a list of commands and deals with editing them
 */
public class CommandList {

    /** Linked list to store the commands */
    private final LinkedList<ICommand> script;
    /** The position in script of the selected command*/
    private int select;
    /** Tracks if the roomba crashed or not */
    public boolean didWeDriveSafe;

    /// Constructor

    /**
     * Constructor<br>
     * Creates an empty linked list for the script
     */
    public CommandList()
    {
        script = new LinkedList<>();
        select = 0;
    }

    /// Public methods

    /**
     * Method to add a new command to the list
     * @param command  ICommand - the command to be added to the list
     */
    public void addCommand(ICommand command)
    {
        if(script.isEmpty()) { //list is empty, so command that's added is selected
            script.add(command);
        } else { //adds command in list at position
            select = select + 1; //selects command to the right
            script.add(select, command);
        }
    }

    /**
     * Method to add a subroutine to a script<br> used at runtime to expand out the subroutines
     * @param commands The list of commands to be added
     * @param subroutineType Enum for which subroutine is being added
     */
    public void addSubroutine(CommandList commands, SubroutineType subroutineType)
    {
        for (int i = 0; i < commands.size(); i++) {
            ICommand c = commands.get(i);
            ICommand newCommand;

            if (c instanceof Up) {
                newCommand = new Up(c.getGameBoard());
            }
            else if (c instanceof Down) {
                newCommand = new Down(c.getGameBoard());
            }
            else if (c instanceof Left) {
                newCommand = new Left(c.getGameBoard());
            }
            else if (c instanceof Right){
                newCommand = new Right(c.getGameBoard());
            }
            else {
                // Command could be subroutine B so if it didn't match anything above that is what it is.
                // We cannot have an A command inside of a subroutine, so don't have to account for that
                newCommand = new B(c.getGameBoard());
                subroutineType = SubroutineType.AB;
            }

            newCommand.setSubroutine(subroutineType);
            addCommand(newCommand);
        }
    }

    /**
     * Method to remove the selected command of the list
     * @throws Exception if the list is empty
     */
    public void remove() throws Exception {
        if (!script.isEmpty())
        {
            script.remove(select);
            //selects command to left
            if(select > 0 )
            {
                select = select - 1;
            }
        }
        else
        {
            throw new Exception("Cannot remove from empty list");
        }
    }

    /**
     * Getter for select
     */
    public int getSelect() {
        return select;
    }

    /**
     * Setter for select
     */
    public void setSelect(int s) {
        // If s is already selected, and not at the end, selects the end instead
        // resets to default position of the end
        if(select == s && script.size() - 1 != s) {
            select = script.size() - 1; //the end
        }else {
            select = s; //selects
        }
    }

    /**
     * Method to get the current size of the script
     * @return the number of commands in the script
     */
    public int size()
    {
        return script.size();
    }

    /**
     * Gets the element at specified index
     * @param index index of element to return
     * @return the element at the specified index
     */
    public ICommand get(int index)
    {
        return script.get(index);
    }

    /**
     * Method to count how many instances of B subroutine exist script
     * @return The number of occurrences of B in the script
     */
    public int containsSubroutineB() {
        int count = 0;
        for (ICommand c : script) {
            if (c instanceof B) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method to get if we crashed or not
     * @return the opposite of didWeDriveSafe, whether or not we crashed during execution
     */
    public boolean didWeCrash() {
        return !didWeDriveSafe;
    }

    /**
     * Executes command at a specified index
     * @param index of command to execute
     */
    public void executeCommand(int index) {
        didWeDriveSafe = script.get(index).execute();
    }
}

package cos420.robotrally.models;

import java.util.Collections;
import java.util.LinkedList;

import cos420.robotrally.commands.ICommand;

public class CommandList {

    /** Linked list to store the commands */
    private LinkedList<ICommand> script;

    /**
     * Constructor<br>
     * Creates an empty linked list for the script
     */
    public CommandList()
    {
        script = new LinkedList<ICommand>();
    }

    /**
     * Method to add a new command to the list
     * @param command  ICommand - the command to be added to the list
     */
    public void addCommand(ICommand command)
    {
        script.add(command);
    }

    /**
     * Method to remove the last command of the list
     * @return the command removed
     */
    public void removeLastCommand(){
        script.removeLast();
    }

    /**
     * Switches a command from position "from" to position "to"
     * @param from previous position of command
     * @param to future position of command
     */
    public void switchMove(int from, int to) { Collections.swap(script, from, to); }

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
     * iterates through the script and executes each command
     */
    public void execute()
    {
        for (ICommand command : script)
        {
            command.execute();
        }
    }

}

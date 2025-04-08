package cos420.robotrally.models;

import android.util.Log;
import android.widget.RemoteViews;

import java.util.Collections;
import java.util.LinkedList;

import cos420.robotrally.commands.ICommand;

// TODO javadoc for the class itself
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
     * @throws Exception if the list is empty
     */
    public void removeLastCommand() throws Exception {
        if (!script.isEmpty())
        {
            script.removeLast();
        }
        else
        {
            throw new Exception("Cannot remove from empty list");
        }
    }

    /**
     * Method to clear the list of all its commands
     */
    public void clearList()
    {
        script.clear();
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
     * @return true if every command was successfully executed, and false if not
     * (which indicates roomba crash)
     */
    public boolean execute()
    {
        for (ICommand command : script)
        {
            boolean didWeDriveSafe = command.execute();
            if(!didWeDriveSafe) {
                // We crashed, quit running script
                return false;
            }
        }
        // Every command executed without a crash
        return true;
    }

}

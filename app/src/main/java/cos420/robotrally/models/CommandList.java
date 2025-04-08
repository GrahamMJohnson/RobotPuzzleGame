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
    /** The position in script of the selected command*/
    private int select;

    /**
     * Constructor<br>
     * Creates an empty linked list for the script
     */
    public CommandList()
    {
        script = new LinkedList<ICommand>();
        select = 0;
    }

    /**
     * Method to add a new command to the list
     * @param command  ICommand - the command to be added to the list
     */
    public void addCommand(ICommand command)
    {
        if(script.isEmpty()) { //list is empty, so command that's added is selected
            script.add(command);
        }else { //adds command in list at position
            select = select + 1; //selects command to the right
            script.add(select, command);
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
            if(select > 0 ) { select = select - 1;}


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
        select = 0;
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

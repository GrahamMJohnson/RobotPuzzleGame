package cos420.robotrally.models;

import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Collections;
import java.util.LinkedList;

import cos420.robotrally.commands.*;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.enumerations.LocationInSubroutine;

/**
 * Class that stores a list of commands and deals with editing them
 */
public class CommandList {

    /** Linked list to store the commands */
    private final LinkedList<ICommand> script;
    /** The position in script of the selected command*/
    private int select;

    public boolean didWeDriveSafe;


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
     * Method to add a subroutine of commands to the list
     * @param commands The list of commands to add
     */
    public void addSubroutine(CommandList commands)
    {
        // Can only add a subroutine to another script if there at least 2 commands in the subroutine
        if (commands.size() < 2) {
            return;
        }
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
            else {
                newCommand = new Right(c.getGameBoard());
            }

                // Check if it is first command
            if(i == 0) {
                newCommand.setLocationInSubroutine(LocationInSubroutine.START);
            }
            // Check if it is last command
            else if (i == commands.size() - 1) {
                newCommand.setLocationInSubroutine(LocationInSubroutine.END);
            }
            // Command is in middle
            else {
                newCommand.setLocationInSubroutine(LocationInSubroutine.MIDDLE);
            }
            addCommand(newCommand);
        }
    }

    /**
     * Method to remove the selected command/subroutine of the list
     * @return int - The number of commands that were removed
     * @throws Exception if the list is empty
     */
    public int remove() throws Exception {
        int count = 0;
        if (!script.isEmpty())
        {
            int inSubroutine = 0;

            // Keep deleting as long as we are in a subroutine
            while (true) {
                ICommand c = script.get(select);
                LocationInSubroutine l = c.getLocationInSubroutine();

                script.remove(select);
                //selects command to left
                if (select > 0) {
                    select--;
                }
                count++;

                if(l == null || l == LocationInSubroutine.START) {
                    inSubroutine--;
                    if (inSubroutine <= 0) {
                        break;
                    }
                }
                else if (l == LocationInSubroutine.END) {
                    inSubroutine++;
                }
            }
        }
        else
        {
            throw new Exception("Cannot remove from empty list");
        }
        return count;
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

        // Clicking on command in subroutine selects last command of subroutine
        LocationInSubroutine l = script.get(s).getLocationInSubroutine();
        while (l == LocationInSubroutine.START || l == LocationInSubroutine.MIDDLE) {
            l = script.get(++s).getLocationInSubroutine();
        }

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
     *
     * @return didWeDriveSafe, whether or not we crashed during execution
     */
    public boolean getDidWeDriveSafe() {
        return didWeDriveSafe;
    }

    /**
     * Executes command at a specified index
     * @param index of command to execute
     */
    public void executeCommand(int index) {
        didWeDriveSafe = script.get(index).execute();
    }
}

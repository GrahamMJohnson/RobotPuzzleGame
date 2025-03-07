package cos420.robotrally.commands;

import cos420.robotrally.models.CommandList;

public class Repeat implements ICommand {

    /** Attribute that holds a reference to the users script for the level */
    private final CommandList script;
    /** Attribute to store the number of previous commands to be looped */
    private final int numCommands;
    /** Attribute to store the number of times to loop */
    protected final int numLoops;
    /** Attribute to store this commands place in the script */
    private final int spotInScript;

    /**
     * Constructor
     * @param script The script for the level
     * @param numCommands Number of commands to be looped over
     * @param numLoops Number of times to loop
     */
    public Repeat(CommandList script, int numCommands, int numLoops)
    {
        this.script = script;
        this.numCommands = numCommands;
        this.numLoops = numLoops;
        // This is stored so that we can later access this spot in the script for when we are executing the script
        this.spotInScript = script.size();
    }

    /**
     * Method to execute the command
     */
    @Override
    public void execute() {
        // Loop to repeat commands desired number of times
        for (int i = 0; i < numLoops; i++)
        {
            // Loop to go back and execute the desired commands
            for (int offset = numCommands; offset > 0; offset--)
            {
                // We want to access the command in the script that is offset commands before the repeat command was in the script
                int index = spotInScript - offset;
                ICommand command = script.get(index);
                command.execute();
            }
        }
    }
}

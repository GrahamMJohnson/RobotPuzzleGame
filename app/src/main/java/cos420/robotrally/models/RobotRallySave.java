package cos420.robotrally.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This is the robotRallySave class
 */
public class RobotRallySave {

    //Initializes the file writer
    FileWriter save_file;

    //initializes collectibles percentage variable for if/else statement to avoid div by 0
    float collectiblesPercentage;

    //These are the values to change to represent the number of moves for the special moves
    int specialMoveCount1 = 5;
    int specialMoveCount2 = 10;

    //This is initializing the string that is going to return all of the data contained within the
    //save
    String[] fileRead;

    /**
     * This is the default constructor for a robotRallySave object
     * It should create a save file for the game.
     */
    public RobotRallySave() {
        File robotRallySave = new File("robotRallySave.txt");
        //System.out.println(robotRallySave.getAbsolutePath()); //used for debugging purposes
    }

    /**
     * This is the method used to clear the save file for testing purposes
     * @throws IOException if the write to the file fails.
     */
    public void ClearSave() throws IOException {
        //Initializes the writing tools that we will be using
        save_file = new FileWriter("robotRallySave.txt");
        BufferedWriter save_write = new BufferedWriter(this.save_file);

        //Write nothing to the file, which should override the file?
        save_write.write("");
    }

    /**
     * This is the call for writing a save in a file
     * @param moveSequence the current sequence of moves that is the best
     * @param numberOfAttempts the number of attempts the user has taken at the level
     * @param optimalMoveCount is the number of moves for the level that is optimal
     * @param currentMoveCount is the number of moves that the current attempt has
     * @param bestMoveCount is the number of moves that the best attempt had
     * @param tilesCollected is the number of tiles that have been collected by the user
     * @param tilesPossibleToCollect is the number of tiles that have collectibles on them
     * @throws IOException if IO has failed or been interrupted
     */
    public void WriteAllToSave(char[] moveSequence, float numberOfAttempts, int optimalMoveCount,
                               int currentMoveCount, int bestMoveCount, int tilesCollected,
                               int tilesPossibleToCollect) throws IOException {

        //Initializes the writing tools that we will be using
        save_file = new FileWriter("robotRallySave.txt");
        BufferedWriter save_write = new BufferedWriter(this.save_file);

        //Writes all of the move sequence data to the text file
        for(int i: moveSequence) {
            save_write.write(moveSequence[i]);
        }
        save_write.write("\n");

        //Writes the number of attempts to the text file
        save_write.write(numberOfAttempts + "\n");

        //Calculates and writes the efficiency score to the text file
        float efficiencyScore = (numberOfAttempts / optimalMoveCount) * 1000;
        save_write.write(efficiencyScore + "\n");

        //calculates the total squares moved and writes that to the text file
        //In this case, s is special move one (5 moves), x is special move 2 (10 moves)
        int totalSquaresTraveled = 0;
        for(int i : moveSequence) {
            if(moveSequence[i] == 's') {
                totalSquaresTraveled += specialMoveCount1;
            }
            else if(moveSequence[i] == 'x') {
                totalSquaresTraveled += specialMoveCount2;
            }
            else {
                totalSquaresTraveled += 1;
            }
        }
        save_write.write(totalSquaresTraveled + "\n");

        //Calculates the difference in move count between the current and the optimal
        //and writes that to the text file
        int currentDifference = currentMoveCount - optimalMoveCount;
        save_write.write(currentDifference + "\n");


        //Calculates the difference in move count between the current and the optimal
        //and writes that to the text file
        int bestDifference = bestMoveCount - optimalMoveCount;
        save_write.write(bestDifference + "\n");

        //Calculates the percentage of collectibles collected and writes that to the txt file
        //If/Else is to avoid div by 0 if there are no possible collection tiles
        if(tilesPossibleToCollect != 0) {
            collectiblesPercentage = ((float)tilesCollected/ (float)tilesPossibleToCollect) * 100;
        }
        else {
            collectiblesPercentage = 100;
        }
        save_write.write(String.valueOf(collectiblesPercentage));
        save_write.close();
    }

    /**
     * This is the call to write edited data to the save file. It take different params since these values have already
     * been calculated from the game, they have just been edited, and need to be rewritten.
     * @param moveSequence the current sequence of moves that is the best
     * @param numberOfAttempts the number of attempts the user has taken at the level
     * @param efficiencyScore Current Calculation: (number of attempts/optimalMoveCount) * 1000
     * @param tilesTraveled The number of tiles that the user’s best attempt traveled over
     * @param differenceCurrent The difference between the user’s current attempt’s number of tiles traveled over and the optimal number of tiles traveled over
     * @param differenceBest The difference between the user’s best attempt’s number of tiles traveled over and the optimal number of tiles traveled over
     * @param percentCollectibles The percent of possible collectibles the user collected on their best attempt
     * @throws IOException if the write fails
     */
    private void WriteEditedToSave(char[] moveSequence, float numberOfAttempts,
                                   float efficiencyScore, int tilesTraveled, int differenceCurrent,
                                   int differenceBest, float percentCollectibles) throws IOException
    {

        //Initializes the writing tools that we will be using
        save_file = new FileWriter("robotRallySave.txt");
        BufferedWriter save_write = new BufferedWriter(this.save_file);

        //These are the writes that will write each of the params into their own line in the save file
        for(int i : moveSequence) {
            save_write.write(moveSequence[i]);
        }
        save_write.write("\n");
        save_write.write(numberOfAttempts + "\n");
        save_write.write(efficiencyScore + "\n");
        save_write.write(tilesTraveled + "\n");
        save_write.write(differenceCurrent + "\n");
        save_write.write(differenceBest + "\n");
        save_write.write(String.valueOf(percentCollectibles));
        save_write.close();
    }

    /**
     * This is a call to read the move sequence from the save file
     * @param dataPoint the string representing the data point you would like to retrieve.
     * OPTIONS FOR PARAM (moveSequence, numAttempts, efficiencyScore, tilesTraveled, differenceCurrent,
     * differenceBest, percentCollectibles, all) -> empty or invalid returns an empty string
     * @return the index that corresponds with the line that the piece of information should be on in the save file
     * @throws FileNotFoundException if the save file is not found
     */
    public String ReadFromSave(String dataPoint) throws FileNotFoundException {

        //This is the array that is going to contain all of the read-in data
        //There should only be 7 lines in the save file
        String[] readData = {"", "", "", "", "", "", ""};
        String allDataString = "";

        //this is the save file that we are going to be reading from
        File saveFile = new File("robotRallySave.txt");

        //This initializes the scanner to be able to scan through the save file
        Scanner scanner = new Scanner(saveFile);

        //Fills the readMoveSequence array with each line of the save file
        for(int i = 0; i < readData.length - 1; i++) {
            readData[i] = scanner.nextLine();
            allDataString += readData[i] + "\n";
        }
        //The last part of the iteration is out of the loop because the last line doesn't need a new line\
        readData[readData.length - 1] = scanner.nextLine();
        allDataString = readData[readData.length - 1];

        //closes the scanner so that it works properly
        scanner.close();

        //goes through all the possible params to be able to give only the data asked for
        switch(dataPoint){
            case "moveSequence":
                return readData[0];
            case "numAttempts":
                return readData[1];
            case "efficiencyScore":
                return readData[2];
            case "tilesTraveled":
                return readData[3];
            case "differenceCurrent":
                return readData[4];
            case "differenceBest":
                return readData[5];
            case "percentCollectibles":
                return readData[6];
            case "all":
                return allDataString;
            default:
                return "";
        }
    }

    /**
     * This is the method to write a specific data point to the save file
     * @param dataPoint the string representing the data point you would like to write to.
     * OPTIONS FOR PARAM (moveSequence, numAttempts, efficiencyScore, tilesTraveled, differenceCurrent,
     * differenceBest, percentCollectibles) -> empty returns all
     * @param value the value that you would like to store at that specific data point in the save file
     * @throws IOException if the writeAllToSave fails
     */
    public void WriteSpecificToSave(String dataPoint, String value) throws IOException {

        //this is the save file that we are going to be reading from
        File saveFile = new File("robotRallySave.txt");

        //This initializes the scanner to be able to scan through the save file
        Scanner scanner = new Scanner(saveFile);

        //This is the array that is going to contain all of the read-in data.
        //Since we need to overwrite the whole save file, we must save what we already have
        //There should only be 7 lines in the save file
        String[] readData = {"", "", "", "", "", "", ""};

        //Fills the readMoveSequence array with each line of the save file
        for(int i = 0; i < readData.length; i++) {
            readData[i] = scanner.nextLine();
        }

        //goes through all the possible params to be able to give only the data asked for
        switch(dataPoint){
            case "moveSequence":
                readData[0] = value;
                break;
            case "numAttempts":
                readData[1] = value;
                break;
            case "efficiencyScore":
                readData[2] = value;
                break;
            case "tilesTraveled":
                readData[3] = value;
                break;
            case "differenceCurrent":
                readData[4] = value;
                break;
            case "differenceBest":
                readData[5] = value;
                break;
            case "percentCollectibles":
                readData[6] = value;
                break;
            default:
                break;
        }

        //This is to turn the all of the data back into their native types so that they can be used in the writeAllToSave method
        char[] moveSequenceReconstructed = new char[readData[0].length()];
        for(int i = 0; i < readData[0].length(); i++) {
            moveSequenceReconstructed[i] = readData[0].charAt(i);
        }
        float numberOfAttemptsReconstructed = Float.parseFloat(readData[1]);
        float efficiencyScoreReconstructed = Float.parseFloat(readData[2]);
        int tilesTraveledReconstructed = Integer.parseInt(readData[3]);
        int differenceCurrentReconstructed = Integer.parseInt(readData[4]);
        int differenceBestReconstructed = Integer.parseInt(readData[5]);
        float percentCollectiblesReconstructed = Float.parseFloat(readData[6]);

        //This writes all of the reconstructed information back into the save file
        WriteEditedToSave(moveSequenceReconstructed, numberOfAttemptsReconstructed, efficiencyScoreReconstructed,
                tilesTraveledReconstructed, differenceCurrentReconstructed, differenceBestReconstructed,
                percentCollectiblesReconstructed);
    }
}

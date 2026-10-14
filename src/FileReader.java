import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class FileReader {

    //Method meant to turn rows of the stocks files into PriceRecord objects
    public ArrayList<PriceRecord> readFile(String filePath) throws FileNotFoundException {

        //Creates an empty arraylist for the new pricerecord objects to go into
        ArrayList<PriceRecord> prices = new ArrayList<>();

        //This opens a file and then allows reader to read through using its scanner methods
        try (Scanner reader = new Scanner(new File(filePath))) {

            //Takes into account the header line and skips it to start at the numbers
            if (reader.hasNextLine()){
                reader.nextLine();
            }

            //The reader holds all the lines. The temporary vairable of the line holds the current line. 
            //Then that line is split into the respective values by the comma that separates them.
            //The parseDouble method goes through all of the items in values and changes them into their correct typing.
            //This allows those values to be assigned to a PriceRecord object
            while (reader.hasNextLine()){
                String line = reader.nextLine();

                String[] values = line.split(",");

                String date = values[0];
                double open = Double.parseDouble(values[1]);
                double high = Double.parseDouble(values[2]);
                double low = Double.parseDouble(values[3]);
                double close = Double.parseDouble(values[4]);
                long volume = Long.parseLong(values[5]);

                prices.add(new PriceRecord(date, open, high, low, close, volume));
            }
        }
        //Returns an arraylist of PriceRecord of objects for the file/stock that was inserted.
        return prices;
    }
}
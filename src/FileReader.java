import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class FileReader {



    private PriceRecord date;
    private PriceRecord open;
    private PriceRecord high;
    private PriceRecord low;
    private PriceRecord close;
    private PriceRecord volume;



    public static void main(String[] args) {

        try {

            File myObj = new File("/Users/cassius/Desktop/CPSC Object Oriented Lab/Semester Project/Market data_2025/AAPL_2025.csv");
            ArrayList<PriceRecord> prices = new ArrayList<>();
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                String[] arr = data.split(",");

                for(int i = 0; i < arr.length; i+=6) {

                    double open = Double.parseDouble(arr[i++]);
                    double high = Double.parseDouble(arr[i=2]);
                    double low = Double.parseDouble(arr[i+=3]);
                    double close = Double.parseDouble(arr[i+=4]);
                    long volume = Long.parseLong(arr[i+=5]);

                    prices.add(new PriceRecord(arr[0], open, high, low, close, volume));
                    System.out.println(arr[i]);
                }
                //System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

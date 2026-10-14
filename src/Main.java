import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FileReader reader = new FileReader();

        System.out.println("MINI MARKET TERMINAL");
        System.out.println(
                "Tickers: AAPL, AMZN, AVGO, BRK, GOOGL, LLY, META, MSFT, NVDA, TSLA");
        
        //While loop keeps the program running until instructed not to or stopped
        while (true) {
            System.out.print("\nEnter ticker (0 to exit): ");

            //Disregards any spaces and makes the ticker uppercase to match the file names
            String ticker = input.nextLine()
                    .trim().toUpperCase(Locale.ROOT);

            //Exits the program if the user inputs 0
            if (ticker.equals("0")) {
                break;
            }

            //The ticker is inserted in the file path to be grabbed
            try {
                String path = "/Users/cassius/Desktop/CPSC Object Oriented Lab/Semester Project/Market data_2025/"
                        + ticker + "_2025.csv";

                ArrayList<PriceRecord> prices =
                        reader.readFile(path);

                System.out.print("Start date (YYYY-MM-DD): ");

                LocalDate start =
                        LocalDate.parse(input.nextLine().trim());

                System.out.print("End date (YYYY-MM-DD): ");

                LocalDate end =
                        LocalDate.parse(input.nextLine().trim());

                MarketAnalyzer analyzer =
                        new MarketAnalyzer(prices, start, end);

                System.out.println(
                        "\nHISTORICAL RECORDS - " + ticker);
                System.out.println(
                        "Date          Open      High      Low       Close     Volume");

                for (PriceRecord record : analyzer.getSelectedRecords()) {
                    System.out.printf(
                            Locale.US,
                            "%s  %8.2f  %8.2f  %8.2f  %8.2f  %,d%n",
                            record.getDate(),
                            record.getOpen(),
                            record.getHigh(),
                            record.getLow(),
                            record.getClose(),
                            record.getVolume());
                }

                System.out.println("\nTICKER : " + ticker);
                System.out.println(
                        "Period: " + start + " - " + end);

                System.out.printf(
                        Locale.US,
                        "Highest Closing Price: $%.2f%n",
                        analyzer.getHighestClosingPrice());

                System.out.printf(
                        Locale.US,
                        "Lowest Closing Price: $%.2f%n",
                        analyzer.getLowestClose());

                System.out.printf(
                        Locale.US,
                        "Average Closing Price: $%.2f%n",
                        analyzer.getAverageClosingPrice());

                System.out.printf(
                        Locale.US,
                        "Price Change: %+.2f%%%n",
                        analyzer.getPercentageChange());

                System.out.printf(
                        Locale.US,
                        "Average Trading Volume: %,.2f%n",
                        analyzer.getAverageVolume());

                displayDay(
                        "Best Trading Day",
                        analyzer.getBestTradeDay(),
                        analyzer);

                displayDay(
                        "Worst Trading Day",
                        analyzer.getWorstTradeDay(),
                        analyzer);

            } catch (FileNotFoundException exception) {
                System.out.println(
                        "No data file found for that ticker. "
                        + "Check the tickers spelling.");

            } catch (DateTimeParseException exception) {
                System.out.println(
                        "Invalid date. Use a valid date "
                        + "in YYYY-MM-DD format.");

            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            }
        }

        input.close();
        System.out.println("Goodbye.");
    }

    private static void displayDay(
            String label,
            PriceRecord record,
            MarketAnalyzer analyzer) {

        if (record == null) {
            System.out.println(
                    label + ": No qualifying day with a previous close.");
        } else {
            System.out.printf(
                    Locale.US,
                    "%s: %s (%+.2f%%)%n",
                    label,
                    record.getDate(),
                    analyzer.getDailyChange(record));
        }
    }
}
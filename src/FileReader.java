import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

/** Reads and validates the instructor's historical stock CSV files. */
public class FileReader {

    public ArrayList<PriceRecord> readFile(String filePath) throws FileNotFoundException {
        ArrayList<PriceRecord> prices = new ArrayList<>();
        HashSet<String> dates = new HashSet<>();

        // The scanner closes automatically, including when a row is invalid.
        try (Scanner reader = new Scanner(new File(filePath))) {
            if (!reader.hasNextLine()) {
                throw new IllegalArgumentException("The stock file is empty.");
            }
            String header = reader.nextLine().trim();
            if (!header.equals("Date,Open,High,Low,Close,Volume")) {
                throw new IllegalArgumentException("Invalid CSV header in " + filePath);
            }

            int lineNumber = 1;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                // -1 keeps empty trailing columns so missing values are detected.
                String[] values = line.split(",", -1);
                if (values.length != 6) {
                    throw new IllegalArgumentException(
                            "Expected six CSV values on line " + lineNumber + ".");
                }

                try {
                    String date = LocalDate.parse(values[0].trim()).toString();
                    double open = Double.parseDouble(values[1].trim());
                    double high = Double.parseDouble(values[2].trim());
                    double low = Double.parseDouble(values[3].trim());
                    double close = Double.parseDouble(values[4].trim());
                    long volume = Long.parseLong(values[5].trim());

                    if (!Double.isFinite(open) || !Double.isFinite(high)
                            || !Double.isFinite(low) || !Double.isFinite(close)
                            || open <= 0 || high <= 0 || low <= 0 || close <= 0
                            || volume < 0) {
                        throw new IllegalArgumentException(
                                "Invalid price or volume on line " + lineNumber + ".");
                    }
                    if (low > high || open < low || open > high || close < low || close > high) {
                        throw new IllegalArgumentException(
                                "Prices do not fit the low/high range on line " + lineNumber + ".");
                    }
                    if (!dates.add(date)) {
                        throw new IllegalArgumentException(
                                "Duplicate trading date on line " + lineNumber + ": " + date);
                    }
                    prices.add(new PriceRecord(date, open, high, low, close, volume));
                } catch (NumberFormatException | DateTimeParseException exception) {
                    throw new IllegalArgumentException(
                            "Invalid date or number on CSV line " + lineNumber + ".", exception);
                }
            }
        }

        if (prices.isEmpty()) {
            throw new IllegalArgumentException("The stock file contains no trading records.");
        }
        // Calculations and the watchlist always use chronological records.
        prices.sort(Comparator.comparing(PriceRecord::getDate));
        return prices;
    }
}

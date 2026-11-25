import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Console interface for the features required through Milestone Two. */
public class Main {
    private Scanner input;
    private FileReader reader = new FileReader();
    private Watchlist watchlist = new Watchlist();
    private static final String[] TICKERS = {
        "AAPL", "AMZN", "AVGO", "BRK", "GOOGL", "LLY", "META", "MSFT", "NVDA", "TSLA"
    };

    public Main(Scanner input) {
        this.input = input;
    }

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            new Main(input).run();
        }
    }

    // The watchlist is created once and stays available across menu choices.
    public void run() {
        while (true) {
            printMenu();
            try {
                String choice = readLine("Select an option: ");
                switch (choice) {
                    case "1": searchStock(); break;
                    case "2": viewHistory(); break;
                    case "3": analyzeStock(); break;
                    case "4": compareStocks(); break;
                    case "5": viewWatchlist(); break;
                    case "6": addToWatchlist(); break;
                    case "7": removeFromWatchlist(); break;
                    case "0": System.out.println("Goodbye."); return;
                    default: System.out.println("Invalid option. Choose 0 through 7.");
                }
            } catch (FileNotFoundException exception) {
                System.out.println("Cannot open the stock file. Run from the project folder containing Market data_2025.");
            } catch (DateTimeParseException exception) {
                System.out.println("Invalid date. Use a real date in YYYY-MM-DD format.");
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
            } catch (NoSuchElementException exception) {
                System.out.println("\nGoodbye.");
                return;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n============================================================");
        System.out.println("MINI MARKET TERMINAL - MILESTONE TWO");
        System.out.println("Local historical data: 2025");
        System.out.println("============================================================");
        System.out.println("1. Search Stock");
        System.out.println("2. View / Sort / Filter Historical Data");
        System.out.println("3. Analyze Stock");
        System.out.println("4. Compare Stocks");
        System.out.println("5. View Watchlist");
        System.out.println("6. Add Stock to Watchlist");
        System.out.println("7. Remove Stock from Watchlist");
        System.out.println("0. Exit");
    }

    // Relative paths work on another computer when launched from the project root.
    private ArrayList<PriceRecord> loadHistory(String ticker) throws FileNotFoundException {
        for (String available : TICKERS) {
            if (available.equals(ticker)) {
                return reader.readFile("Market data_2025/" + ticker + "_2025.csv");
            }
        }
        throw new IllegalArgumentException("Invalid ticker. Choose: " + String.join(", ", TICKERS));
    }

    private void searchStock() throws FileNotFoundException {
        System.out.println("Available tickers: " + String.join(", ", TICKERS));
        String ticker = readTicker();
        ArrayList<PriceRecord> history = loadHistory(ticker);
        PriceRecord first = history.get(0);
        PriceRecord latest = history.get(history.size() - 1);
        System.out.println("\nSTOCK FOUND: " + ticker);
        System.out.println("Trading records: " + history.size());
        System.out.println("Available period: " + first.getDate() + " to " + latest.getDate());
        System.out.printf(Locale.US, "Latest available close: $%.2f (%s)%n",
                latest.getClose(), latest.getDate());
    }

    private MarketAnalyzer readAnalysis(String ticker) throws FileNotFoundException {
        ArrayList<PriceRecord> history = loadHistory(ticker);
        System.out.println("Available period: " + history.get(0).getDate()
                + " to " + history.get(history.size() - 1).getDate());
        LocalDate start = LocalDate.parse(readLine("Start date (YYYY-MM-DD): "));
        LocalDate end = LocalDate.parse(readLine("End date (YYYY-MM-DD): "));
        return new MarketAnalyzer(history, start, end);
    }

    private void viewHistory() throws FileNotFoundException {
        String ticker = readTicker();
        MarketAnalyzer analyzer = readAnalysis(ticker);
        System.out.println("Optional filters: press Enter to use the default.");
        double minimum = readPrice("Minimum closing price [0]: ", 0);
        double maximum = readPrice("Maximum closing price [no limit]: ", Double.MAX_VALUE);
        long volume = readVolume();
        int criterion = readChoice("Sort by 1=date, 2=closing price, 3=volume: ", 1, 3);
        int order = readChoice("Order 1=ascending, 2=descending: ", 1, 2);

        ArrayList<PriceRecord> records = analyzer.filterRecords(minimum, maximum, volume);
        records = analyzer.sortRecords(records, criterion, order == 1);
        String[] names = {"Date", "Closing price", "Volume"};
        System.out.println("\nHISTORICAL RECORDS - " + ticker);
        System.out.println("Sort: " + names[criterion - 1]
                + (order == 1 ? " ascending" : " descending"));
        System.out.println("Matching records: " + records.size());
        if (records.isEmpty()) {
            System.out.println("No records match these filters.");
            return;
        }
        System.out.printf("%-12s %10s %10s %10s %10s %16s%n",
                "Date", "Open", "High", "Low", "Close", "Volume");
        for (PriceRecord record : records) {
            System.out.printf(Locale.US, "%-12s %10.2f %10.2f %10.2f %10.2f %,16d%n",
                    record.getDate(), record.getOpen(), record.getHigh(),
                    record.getLow(), record.getClose(), record.getVolume());
        }
    }

    private void analyzeStock() throws FileNotFoundException {
        String ticker = readTicker();
        MarketAnalyzer analyzer = readAnalysis(ticker);
        ArrayList<PriceRecord> records = analyzer.getSelectedRecords();
        System.out.println("\nANALYSIS - " + ticker);
        System.out.println("Trading dates used: " + records.get(0).getDate()
                + " to " + records.get(records.size() - 1).getDate());
        System.out.printf(Locale.US, "Highest Closing Price: $%.2f%n", analyzer.getHighestClosingPrice());
        System.out.printf(Locale.US, "Lowest Closing Price: $%.2f%n", analyzer.getLowestClose());
        System.out.printf(Locale.US, "Average Closing Price: $%.2f%n", analyzer.getAverageClose());
        System.out.printf(Locale.US, "Price Change: %+.2f%%%n", analyzer.getPercentageChange());
        System.out.printf(Locale.US, "Average Trading Volume: %,.2f%n", analyzer.getAverageVolume());
        displayDay("Best Trading Day", analyzer.getBestTradeDay(), analyzer);
        displayDay("Worst Trading Day", analyzer.getWorstTradeDay(), analyzer);
    }

    private void compareStocks() throws FileNotFoundException {
        String entered = readLine("Enter at least two tickers, separated by spaces or commas: ")
                .toUpperCase(Locale.ROOT);
        ArrayList<String> tickers = new ArrayList<>();
        for (String ticker : entered.split("[,\\s]+")) {
            if (!ticker.isEmpty() && !tickers.contains(ticker)) {
                tickers.add(ticker);
            }
        }
        if (tickers.size() < 2) {
            throw new IllegalArgumentException("Comparison requires at least two different tickers.");
        }
        ArrayList<ArrayList<PriceRecord>> histories = new ArrayList<>();
        for (String ticker : tickers) {
            histories.add(loadHistory(ticker));
        }
        LocalDate start = LocalDate.parse(readLine("Start date (YYYY-MM-DD): "));
        LocalDate end = LocalDate.parse(readLine("End date (YYYY-MM-DD): "));
        ArrayList<MarketAnalyzer> analyses = new ArrayList<>();
        for (ArrayList<PriceRecord> history : histories) {
            analyses.add(new MarketAnalyzer(history, start, end));
        }
        // Validate every stock and period before printing any comparison rows.
        System.out.println("\nSTOCK COMPARISON - " + start + " to " + end);
        System.out.printf("%-8s %10s %12s %12s %12s %18s%n",
                "Ticker", "Return", "Avg Close", "High Close", "Low Close", "Avg Volume");
        for (int i = 0; i < tickers.size(); i++) {
            MarketAnalyzer analyzer = analyses.get(i);
            System.out.printf(Locale.US, "%-8s %+9.2f%% %12.2f %12.2f %12.2f %,18.2f%n",
                    tickers.get(i), analyzer.getPercentageChange(), analyzer.getAverageClose(),
                    analyzer.getHighestClosingPrice(), analyzer.getLowestClose(), analyzer.getAverageVolume());
        }
    }

    private void viewWatchlist() {
        System.out.println("\nMY WATCHLIST");
        if (watchlist.getTickers().isEmpty()) {
            System.out.println("Your watchlist is empty. Use option 6 to add a stock.");
            return;
        }
        System.out.printf("%-8s %-12s %18s %12s%n", "Ticker", "Latest Date", "Latest Close", "Day Change");
        for (String ticker : watchlist.getTickers()) {
            ArrayList<PriceRecord> history = watchlist.getHistory(ticker);
            PriceRecord latest = history.get(history.size() - 1);
            System.out.printf(Locale.US, "%-8s %-12s $%17.2f ", ticker, latest.getDate(), latest.getClose());
            if (history.size() < 2) {
                System.out.printf("%12s%n", "N/A");
            } else {
                double previous = history.get(history.size() - 2).getClose();
                double change = (latest.getClose() - previous) / previous * 100;
                System.out.printf(Locale.US, "%+11.2f%%%n", change);
            }
        }
        System.out.println("Latest local closes; day change compares with the prior trading close.");
    }

    private void addToWatchlist() throws FileNotFoundException {
        String ticker = readTicker();
        if (watchlist.addStock(ticker, loadHistory(ticker))) {
            System.out.println(ticker + " added to watchlist.");
        } else {
            System.out.println(ticker + " is already in your watchlist.");
        }
    }

    private void removeFromWatchlist() {
        String ticker = readTicker();
        if (watchlist.removeStock(ticker)) {
            System.out.println(ticker + " removed from watchlist.");
        } else {
            System.out.println(ticker + " is not in your watchlist.");
        }
    }

    private String readTicker() {
        return readLine("Ticker: ").toUpperCase(Locale.ROOT);
    }

    // Handle input ending in one place rather than repeating scanner checks.
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) {
            throw new NoSuchElementException("Input ended.");
        }
        return input.nextLine().trim();
    }

    private int readChoice(String prompt, int minimum, int maximum) {
        while (true) {
            try {
                int choice = Integer.parseInt(readLine(prompt));
                if (choice >= minimum && choice <= maximum) {
                    return choice;
                }
            } catch (NumberFormatException exception) {
                // Ask again for non-numeric input.
            }
            System.out.println("Enter a number from " + minimum + " to " + maximum + ".");
        }
    }

    private double readPrice(String prompt, double defaultValue) {
        while (true) {
            String value = readLine(prompt);
            if (value.isEmpty()) {
                return defaultValue;
            }
            try {
                double price = Double.parseDouble(value);
                if (Double.isFinite(price) && price >= 0) {
                    return price;
                }
            } catch (NumberFormatException exception) {
                // Ask again below.
            }
            System.out.println("Enter a non-negative price or press Enter for the default.");
        }
    }

    private long readVolume() {
        while (true) {
            String value = readLine("Minimum trading volume [0]: ");
            if (value.isEmpty()) {
                return 0;
            }
            try {
                long volume = Long.parseLong(value);
                if (volume >= 0) {
                    return volume;
                }
            } catch (NumberFormatException exception) {
                // Ask again below.
            }
            System.out.println("Enter a non-negative whole-number volume.");
        }
    }

    private void displayDay(String label, PriceRecord record, MarketAnalyzer analyzer) {
        if (record == null) {
            System.out.println(label + ": N/A - no qualifying change with a previous close.");
        } else {
            System.out.printf(Locale.US, "%s: %s (%+.2f%%)%n",
                    label, record.getDate(), analyzer.getDailyChange(record));
        }
    }
}

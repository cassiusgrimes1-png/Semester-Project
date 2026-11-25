# Mini Market Terminal

Cassius Grimes - Wrote the MarketAnalyzer and Watchlist classes and worked on stock comparison, sorting, filtering, and the updated menu. Pipelore Rewaju - Wrote the FileReader class and worked on CSV validation, relative file loading, and user-input handling. Both worked on the PriceRecord and Main classes and integrated the Milestone Two features.

A Java console app that analyzes 2025 stocks from CSV files. Users can view historical prices, calculate stock statistics, compare stocks, maintain a watchlist, and sort or filter historical records.

## Run

Requires a Java JDK (8 or newer). Update the CSV folder path in `src/Main.java` to match your local `Market data_2025` folder, then run from the project directory:

'''
String path = "(your filepath here)/Market data_2025/"
                        + ticker + "_2025.csv";
'''

Keep the supplied `Market data_2025` folder in the project root. The program uses relative file paths, so no personal folder path needs to be changed.

Choose a feature from the menu. Enter dates in `YYYY-MM-DD` format and use 2025 dates for the supplied data. For stock comparison, enter at least two different tickers separated by spaces or commas. Enter `0` at the main menu to exit.

**Supported tickers:** AAPL, AMZN, AVGO, BRK, GOOGL, LLY, META, MSFT, NVDA, TSLA.

##UML Class Diagram

## UML Class Diagram

```mermaid
classDiagram
    Main --> FileReader : loads records
    Main --> MarketAnalyzer : analyzes and organizes
    Main --> Watchlist : manages
    FileReader --> PriceRecord : creates
    MarketAnalyzer --> PriceRecord : analyzes
    Watchlist --> PriceRecord : stores

    class Main {
        -Scanner input
        -FileReader reader
        -Watchlist watchlist
        +Main(Scanner input)
        +main(String[] args)$
        +run() void
        -searchStock() void
        -viewHistory() void
        -analyzeStock() void
        -compareStocks() void
        -viewWatchlist() void
        -addToWatchlist() void
        -removeFromWatchlist() void
    }

    class FileReader {
        +readFile(String filePath) ArrayList
    }

    class PriceRecord {
        -String date
        -double open
        -double high
        -double low
        -double close
        -long volume
        +PriceRecord(String date, double open, double high, double low, double close, long volume)
        +getDate() String
        +getOpen() double
        +getHigh() double
        +getLow() double
        +getClose() double
        +getVolume() long
    }

    class MarketAnalyzer {
        -ArrayList history
        -ArrayList selectedRecords
        +MarketAnalyzer(ArrayList history, LocalDate startDate, LocalDate endDate)
        +getSelectedRecords() ArrayList
        +getHighestClosingPrice() double
        +getLowestClose() double
        +getAverageClose() double
        +getAverageClosingPrice() double
        +getPercentageChange() double
        +getAverageVolume() double
        +getDailyChange(PriceRecord record) double
        +getBestTradeDay() PriceRecord
        +getWorstTradeDay() PriceRecord
        +filterRecords(double minimumClose, double maximumClose, long minimumVolume) ArrayList
        +sortRecords(ArrayList records, int criterion, boolean ascending) ArrayList
    }

    class Watchlist {
        -HashMap watchlist
        +addStock(String ticker, ArrayList history) boolean
        +removeStock(String ticker) boolean
        +getTickers() ArrayList
        +getHistory(String ticker) ArrayList
    }
```

## Architecture

The Main class provides the console interface. It displays the menu, reads user input, calls the other classes, and displays historical records, analysis, comparisons, and watchlist information. It handles missing files, invalid tickers, invalid dates, and invalid date ranges.

The PriceRecord class stores the date, opening price, highest price, lowest price, closing price, and trading volume of a single day. Its constructor initializes these values, and getter methods provide access to them.

The FileReader class loads a ticker's CSV file, separates rows at the commas, converts numerical values, and returns an ArrayList of PriceRecord objects. It validates headers, column counts, dates, prices, volume, and duplicate trading dates.

The MarketAnalyzer class selects records within an inclusive date range and calculates closing-price statistics, percentage change, average volume, and the largest daily increase and decrease. It also filters records by closing price and volume and sorts them by date, closing price, or volume. Sorting uses a separate list so calculations remain chronological.

The Watchlist class uses a HashMap to store ticker symbols and their historical records. It supports adding, removing, and retrieving watched stocks while preventing duplicates. The watchlist lasts for the current program session.

Daily percentage changes compare a day's close with the previous available trading day's close. Days without a previous close are excluded. When no qualifying increase or decrease exists, the result displays N/A. Equal changes retain the earliest qualifying day.

## Testing Results

Milestone One

<img width="524" height="835" alt="Screenshot 2026-10-21 at 12 56 32 AM" src="https://github.com/user-attachments/assets/65a3648c-c3a7-4274-8c65-8bb4d95d5410" />

<img width="510" height="215" alt="Screenshot 2026-10-21 at 1 00 23 AM" src="https://github.com/user-attachments/assets/a5ecadf5-d588-4030-8313-62261a802ce7" />

Milestone Two

<img width="545" height="761" alt="Screenshot 2026-11-25 at 1 30 01 AM" src="https://github.com/user-attachments/assets/0f480413-af30-45dc-9d51-16f5b610db76" />

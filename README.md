# Mini Market Terminal

Cassius Grimes - Wrote the MarketAnalyzer class, Pipelore Rewaju - Wrote the FileReader class, both worked on the PriceRecord and Main classes

A Java console app that analyzes 2025 stocks from CSV files. Choose a ticker and date range to view historical prices, closing-price statistics, percentage change, average volume, and the best/worst trading days.

## Run

Requires a Java JDK (8 or newer). Update the CSV folder path in `src/Main.java` to match your local `Market data_2025` folder, then run from the project directory:

'''
String path = "(your filepath here)/Market data_2025/"
                        + ticker + "_2025.csv";
'''

Enter a ticker and start/end dates in `YYYY-MM-DD` format. Enter `0` at the ticker prompt to exit.

**Supported tickers:** AAPL, AMZN, AVGO, BRK, GOOGL, LLY, META, MSFT, NVDA, TSLA.

##UML Class Diagram

<img width="1346" height="757" alt="Screenshot 2026-10-14 at 8 09 16 AM" src="https://github.com/user-attachments/assets/8707c182-1779-45fd-8276-096aed49221d" />

## Architecture
The Main class provides the users console interface. It asks for a ticker and a date range, then calling the other classes, and displays the historical data and calculated results. It handles missing files, invalid dates, reversed date ranges, and ranges containing to records.

The PriceRecord class stores the date, opening, highest, lowest, and closing price, as well as the trading volume of a single day. It initializes those values and gives accessor methods for them as well

The FileReader class loads a tickers CSV file and separates each rows at the commas. It converts the the numerical values and then creates PriceRecord objects which it then returns to an ArrayList.

The MarketAnalyzer class selects all the records in the users date range and calculates the highest, lowest, averages of closing, percentage of change, average volume, and largest increase or decrease of all of the days. 

## Testing Results

<img width="524" height="835" alt="Screenshot 2026-10-21 at 12 56 32 AM" src="https://github.com/user-attachments/assets/65a3648c-c3a7-4274-8c65-8bb4d95d5410" />

<img width="510" height="215" alt="Screenshot 2026-10-21 at 1 00 23 AM" src="https://github.com/user-attachments/assets/a5ecadf5-d588-4030-8313-62261a802ce7" />

# Mini Market Terminal

A Java console app that analyzes 2025 stocks from CSV files. Choose a ticker and date range to view historical prices, closing-price statistics, percentage change, average volume, and the best/worst trading days.

## Run

Requires a Java JDK (8 or newer). Update the CSV folder path in `src/Main.java` to match your local `Market data_2025` folder, then run from the project directory:

'''
String path = "(your filepath here)/Market data_2025/"
                        + ticker + "_2025.csv";
'''

Enter a ticker and start/end dates in `YYYY-MM-DD` format. Enter `0` at the ticker prompt to exit.

**Supported tickers:** AAPL, AMZN, AVGO, BRK, GOOGL, LLY, META, MSFT, NVDA, TSLA.

<img width="1346" height="757" alt="Screenshot 2026-10-14 at 8 09 16 AM" src="https://github.com/user-attachments/assets/8707c182-1779-45fd-8276-096aed49221d" />

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/** Stores the user's chosen stocks for the current program session. */
public class Watchlist {
    private HashMap<String, ArrayList<PriceRecord>> watchlist = new HashMap<>();

    public boolean addStock(String ticker, ArrayList<PriceRecord> history) {
        ticker = ticker.trim().toUpperCase(Locale.ROOT);
        if (history.isEmpty()) {
            throw new IllegalArgumentException("Cannot add a stock without trading records.");
        }
        if (watchlist.containsKey(ticker)) {
            return false;
        }
        watchlist.put(ticker, new ArrayList<>(history));
        return true;
    }

    public boolean removeStock(String ticker) {
        ticker = ticker.trim().toUpperCase(Locale.ROOT);
        return watchlist.remove(ticker) != null;
    }

    public ArrayList<String> getTickers() {
        ArrayList<String> tickers = new ArrayList<>(watchlist.keySet());
        Collections.sort(tickers);
        return tickers;
    }

    // A copy prevents other classes from changing the stored list accidentally.
    public ArrayList<PriceRecord> getHistory(String ticker) {
        ticker = ticker.trim().toUpperCase(Locale.ROOT);
        if (!watchlist.containsKey(ticker)) {
            throw new IllegalArgumentException("That ticker is not in the watchlist.");
        }
        return new ArrayList<>(watchlist.get(ticker));
    }
}

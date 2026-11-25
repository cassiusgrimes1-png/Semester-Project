import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;

public class MarketAnalyzer {

    private ArrayList<PriceRecord> history;
    private ArrayList<PriceRecord> selectedRecords;
    
    //Takes the history of the selected stock from the selected start and end dates to return
    //certain statistics about the stock in that time frame.
    public MarketAnalyzer(ArrayList<PriceRecord> history, LocalDate startDate, LocalDate endDate){
        
        // A single-day period is valid; only reversed dates are rejected.
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be on or before end date.");
        }

        //The field of this MarketAnalyzer is an ArrayList that comes from the history parameter
        //selectedRecords is an ArrayList that will be used to hold pricerecord objects between the start and end dates.
        this.history = new ArrayList<>(history);
        this.history.sort(Comparator.comparing(PriceRecord::getDate));
        selectedRecords = new ArrayList<>();

        //This loop goes through all of the PriceRecord objects and adds them to the selecedRecords list if they are 
        //between the start and end dates.
        for(PriceRecord record : this.history){

            LocalDate date = LocalDate.parse(record.getDate());

            if(!date.isBefore(startDate) && !date.isAfter(endDate)){
                selectedRecords.add(record);
            }
        }

        if(selectedRecords.isEmpty()){
            throw new IllegalArgumentException("There are no trading records between these dates.");
        }
    }
    //Returns the ArrayList of PriceRecord objects that are between the start and end dates.
    public ArrayList<PriceRecord> getSelectedRecords() {
        return new ArrayList<>(selectedRecords);
    }

    //This method shows the highest closing price of the stock by looping
    //through and seeing if a price tops the current highest
    public double getHighestClosingPrice(){
        double highest = selectedRecords.get(0).getClose();

        for (PriceRecord record : selectedRecords){
            if(record.getClose() > highest){
                highest = record.getClose();
            }
        }

        return highest;
    }

    // Find the lowest closing price, rather than the intraday low.
    public double getLowestClose(){
        double lowest = selectedRecords.get(0).getClose();

        for (PriceRecord record : selectedRecords){
            if(record.getClose() < lowest){
                lowest = record.getClose();
            }
        }

        return lowest;


    }

    //The method gets the average of the closing prices of the stock by adding
    //them all together and dividing by the number of records in selectedRecords
    public double getAverageClose(){
        double total = 0;

        for (PriceRecord record : selectedRecords){
            total += record.getClose();
        }

        return total / selectedRecords.size();
    }

    //This method gets the percentage change from the first close to the last by
    //getting the first close in range and the last and using a formula to get the change
    public double getPercentageChange(){
        double first = selectedRecords.get(0).getClose();
        double last = selectedRecords.get(selectedRecords.size() - 1).getClose();

        return ((last - first) / first) * 100;
    }

    //This method adds up the volume of every record in the date range and 
    //takes the average by dividing by the amount there was
    public double getAverageVolume(){
        double total = 0;

        for (PriceRecord record : selectedRecords){
            total += record.getVolume();
        }

        return total / selectedRecords.size();
    }

    //This method gets the change in percentage of price from the close
    //of the day before to the close of the current day
    public double getDailyChange(PriceRecord record){
        int index = history.indexOf(record);

        if (index <= 0) {
            throw new IllegalArgumentException("No prior days to compare it to.");
        }

        double previousClose = history.get(index - 1).getClose();
        if (previousClose <= 0) {
            throw new IllegalArgumentException("A daily change requires a positive previous close.");
        }

        return (record.getClose() - previousClose) / previousClose * 100;
    }

    // Only selected days qualify, but their previous close may be outside the range.
    // No increase returns null. Equal changes keep the earliest trading day.
    public PriceRecord getBestTradeDay(){
        PriceRecord best = null;
        double bestChange = 0;

        for (int i = 1; i < history.size(); i++) {
            PriceRecord record = history.get(i);


            if (selectedRecords.contains(record)) {

                double change = getDailyChange(record);

                if (change > bestChange) {
                    bestChange = change;
                    best = record;
                }
            }
        }
        return best;
    }

    // No decrease returns null, including a flat period or a day with no prior close.
    public PriceRecord getWorstTradeDay(){
        PriceRecord worst = null;
        double worstChange = 0;

        for (int i = 1; i < history.size(); i++) {
            PriceRecord record = history.get(i);

            if (selectedRecords.contains(record)) {

                double change = getDailyChange(record);

                if (change < worstChange) {
                    worstChange = change;
                    worst = record;
                }
            }
        }
        return worst;
    }
    public double getAverageClosingPrice() {
        return getAverageClose();
    }

    // Date filtering is done by the constructor; these are additional inclusive filters.
    public ArrayList<PriceRecord> filterRecords(double minimumClose, double maximumClose,
                                               long minimumVolume) {
        if (!Double.isFinite(minimumClose) || !Double.isFinite(maximumClose)
                || minimumClose < 0 || maximumClose < minimumClose || minimumVolume < 0) {
            throw new IllegalArgumentException("Invalid closing-price or volume filter.");
        }
        ArrayList<PriceRecord> filtered = new ArrayList<>();
        for (PriceRecord record : selectedRecords) {
            if (record.getClose() >= minimumClose && record.getClose() <= maximumClose
                    && record.getVolume() >= minimumVolume) {
                filtered.add(record);
            }
        }
        return filtered;
    }

    // Sort a separate list so changing display order cannot change period returns.
    public ArrayList<PriceRecord> sortRecords(ArrayList<PriceRecord> records,
                                             int criterion, boolean ascending) {
        ArrayList<PriceRecord> sorted = new ArrayList<>(records);
        Comparator<PriceRecord> comparison;
        if (criterion == 1) {
            comparison = Comparator.comparing(PriceRecord::getDate);
        } else if (criterion == 2) {
            comparison = Comparator.comparingDouble(PriceRecord::getClose);
        } else if (criterion == 3) {
            comparison = Comparator.comparingLong(PriceRecord::getVolume);
        } else {
            throw new IllegalArgumentException("Choose date, closing price, or volume.");
        }
        // Dates provide a consistent order when two records have the same price or volume.
        comparison = comparison.thenComparing(PriceRecord::getDate);
        if (!ascending) {
            comparison = comparison.reversed();
        }
        sorted.sort(comparison);
        return sorted;
    }
}

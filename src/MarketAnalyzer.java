import java.util.ArrayList;
import java.time.LocalDate;

public class MarketAnalyzer {

    private ArrayList<PriceRecord> history;
    private ArrayList<PriceRecord> selectedRecords;
    
    //Takes the history of the selected stock from the selected start and end dates to return
    //certain statistics about the stock in that time frame.
    public MarketAnalyzer(ArrayList<PriceRecord> history, LocalDate startDate, LocalDate endDate){
        
        //Makes sure the start date is before the end date and gives an error message if it is not.
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be before end date");
        }

        //The field of this MarketAnalyzer is an ArrayList that comes from the history parameter
        //selectedRecords is an ArrayList that will be used to hold pricerecord objects between the start and end dates.
        this.history = history;
        selectedRecords = new ArrayList<>();

        //This loop goes through all of the PriceRecord objects and adds them to the selecedRecords list if they are 
        //between the start and end dates.
        for(PriceRecord record : history){

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
        return selectedRecords;
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

    //This method shows the highest closing price of the stock by looping
    //through and seeing if a price tops the current lowest
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

        if (index == 0) {
            throw new IllegalArgumentException("No prior days to compare it to.");
        }

        double previousClose = history.get(index - 1).getClose();

        return (record.getClose() - previousClose) / previousClose * 100;
    }

    //This method goes through the range of dates and finds the day
    //with the best daily change in percentage
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

    //This method goes through the range of dates and finds the day
    //with the worst daily change in percentage
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
}

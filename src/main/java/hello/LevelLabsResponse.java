package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevelLabsResponse {

    private String error;
    private Transaction[] transactions;

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public Transaction[] getTransactions() {
        return transactions;
    }
    public void setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

}

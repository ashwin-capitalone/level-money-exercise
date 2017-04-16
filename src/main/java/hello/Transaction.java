package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

	@JsonProperty("transaction-id")
	private String transactionId;
	
	@JsonProperty("account-id")
	private String accountId;
	
	@JsonProperty("raw-merchant")
	private String rawMerchant;
	
	private String merchant;
	
	@JsonProperty("is-pending")
	boolean isPending;
	
	@JsonProperty("transaction-time")
	private String transactionTime;
	
	private long amount;
	
	@JsonProperty("previous-transaction-id")
	private String previousTransactionId;
	
	private String categorization;
	
	@JsonProperty("memo-only-for-testing")
	private String memoOnlyForTesting;
	
	@JsonProperty("payee-name-only-for-testing")		
	private String payeeNameOnlyForTesting;
	
	@JsonProperty("clear-date")		
	private long clearDate;
	
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRawMerchant() {
		return rawMerchant;
	}

	public void setRawMerchant(String rawMerchant) {
		this.rawMerchant = rawMerchant;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getPreviousTransactionId() {
		return previousTransactionId;
	}

	public void setPreviousTransactionId(String previousTransactionId) {
		this.previousTransactionId = previousTransactionId;
	}

	public String getCategorization() {
		return categorization;
	}

	public void setCategorization(String categorization) {
		this.categorization = categorization;
	}

	public String getMemoOnlyForTesting() {
		return memoOnlyForTesting;
	}

	public void setMemoOnlyForTesting(String memoOnlyForTesting) {
		this.memoOnlyForTesting = memoOnlyForTesting;
	}

	public String getPayeeNameOnlyForTesting() {
		return payeeNameOnlyForTesting;
	}

	public void setPayeeNameOnlyForTesting(String payeeNameOnlyForTesting) {
		this.payeeNameOnlyForTesting = payeeNameOnlyForTesting;
	}

	public long getClearDate() {
		return clearDate;
	}

	public void setClearDate(long clearDate) {
		this.clearDate = clearDate;
	}

}

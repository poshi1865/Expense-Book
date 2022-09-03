package com.example.expensebook;

public class BankMessage {
  public static final int AMOUNT_CREDITED = 1;
  
  public static final int AMOUNT_DEBITED = 0;
  
  private double amount;
  
  private int amountType;
  
  private String dateSent;
  
  private String messageBody;
  
  private String messageSenderName;
  
  private String monthSent;
  
  private String personWithWhomTransactionWasDone;
  
  private String yearSent;
  
  public double getAmount() {
    return this.amount;
  }
  
  public int getAmountType() {
    return this.amountType;
  }
  
  public String getDateSent() {
    return this.dateSent;
  }
  
  public String getMessageBody() {
    return this.messageBody;
  }
  
  public String getMonthSent() {
    return this.monthSent;
  }
  
  public String getPersonWithWhomTransactionWasDone() {
    return this.personWithWhomTransactionWasDone;
  }
  
  public String getSenderName() {
    return this.messageSenderName;
  }
  
  public String getYearSent() {
    return this.yearSent;
  }
  
  public void setAmount(double paramDouble) {
    this.amount = paramDouble;
  }
  
  public void setAmountType(int paramInt) {
    this.amountType = paramInt;
  }
  
  public void setDateSent(String paramString) {
    this.dateSent = paramString;
  }
  
  public void setMessageBody(String paramString) {
    this.messageBody = paramString;
  }
  
  public void setMonthSent(String paramString) {
    this.monthSent = paramString;
  }
  
  public void setPersonWithWhomTransactionWasDone(String paramString) {
    this.personWithWhomTransactionWasDone = paramString;
  }
  
  public void setSenderName(String paramString) {
    this.messageSenderName = paramString;
  }
  
  public void setYearSent(String paramString) {
    this.yearSent = paramString;
  }
}

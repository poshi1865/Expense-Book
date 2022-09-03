package com.example.expensebook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankMessageLoader {
  private String BANK_REGULAR_EXPRESSION;
  
  private final String REGULAR_EXPRESSION_FOR_AMOUNT = "Rs\\d+(\\.\\d+)?";
  
  private Cursor cursor;
  
  public BankMessageLoader(Context paramContext, String paramString) {
    this.cursor = paramContext.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC");
    this.BANK_REGULAR_EXPRESSION = paramString;
  }
  
  public ArrayList<BankMessage> loadMessages() {
    ArrayList<BankMessage> arrayList = new ArrayList();
    String str = "";
    if (this.cursor.moveToFirst())
      do {
        BankMessage bankMessage = new BankMessage();
        bankMessage.setMessageBody(this.cursor.getString(12));
        if (str.equals(bankMessage.getMessageBody()))
          continue; 
        String str1 = bankMessage.getMessageBody();
        bankMessage.setSenderName(this.cursor.getString(2));
        str = str1;
        if (!Pattern.compile(this.BANK_REGULAR_EXPRESSION).matcher(bankMessage.getSenderName()).find())
          continue; 
        Date date = new Date(this.cursor.getLong(4));
        bankMessage.setDateSent((new SimpleDateFormat("dd/MM/yyyy")).format(date));
        bankMessage.setMonthSent((new SimpleDateFormat("MMM")).format(date));
        bankMessage.setYearSent((new SimpleDateFormat("yyyy")).format(date));
        if (!Pattern.compile("debited").matcher(bankMessage.getMessageBody()).find()) {
          bankMessage.setAmountType(1);
          String str2 = str1;
        } else {
          bankMessage.setAmountType(0);
          Matcher matcher = Pattern.compile("Rs\\.\\d+(\\.\\d+)?").matcher(bankMessage.getMessageBody());
          String str2 = str1;
          if (matcher.find()) {
            bankMessage.setAmount(Double.parseDouble(matcher.group().substring(3)));
            Matcher matcher1 = Pattern.compile("to .+?(?= on)").matcher(bankMessage.getMessageBody());
            if (matcher1.find()) {
              bankMessage.setPersonWithWhomTransactionWasDone(matcher1.group().substring(2));
            } else {
              bankMessage.setPersonWithWhomTransactionWasDone("NA");
            } 
            arrayList.add(bankMessage);
            String str3 = str1;
          } 
        } 
      } while (this.cursor.moveToNext()); 
    return arrayList;
  }
}

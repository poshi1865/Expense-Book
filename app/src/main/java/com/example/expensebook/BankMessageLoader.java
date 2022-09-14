package com.example.expensebook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankMessageLoader {
    private String BANK_REGULAR_EXPRESSION;
    private final String REGULAR_EXPRESSION_FOR_AMOUNT = "Rs\\d+(\\.\\d+)?";

    private final int MESSAGE_BODY_POSITION = 12;
    private final int SENDER_NAME_POSITION = 2;
    private final int DATE_POSITION = 4;

    private Cursor cursor;

    public BankMessageLoader(Context paramContext, String bankRegularExpression) {
        this.cursor = paramContext.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC");
        this.BANK_REGULAR_EXPRESSION = bankRegularExpression;
    }

    //this method return an ArrayList of BankMessages which have been loaded with message contents
    public ArrayList<BankMessage> loadMessages(String month) {
        ArrayList<BankMessage> arrayList = new ArrayList();
        String previousMessageBody = "";

        //check if cursor has a first value
        if (this.cursor.moveToFirst())
            do {
                /* Compare the bank message body to previousMessageBody. This is done to make sure the same message is not displayed again.
                *  The same message can be displayed again because JD-SBI and AX-SBI (or any other such name) can sometimes send one
                *  message each for the same transaction. */
                if (previousMessageBody.equals(this.cursor.getString(MESSAGE_BODY_POSITION))) {
                    continue;
                }
                //If regular expression for the bank does not match the sender name, continue
                if (!Pattern.compile(this.BANK_REGULAR_EXPRESSION).matcher(this.cursor.getString(SENDER_NAME_POSITION)).find()) {
                    continue;
                }

                //Check if month and year is equal to the selected month and the current year, if not then continue
                Date d = new Date(this.cursor.getLong(DATE_POSITION));
                String currYear = new SimpleDateFormat("yyyy").format(new Date());
                String messageYear = new SimpleDateFormat("yyyy").format(d);
                String messageMonth = new SimpleDateFormat("MMM").format(d);

                if (!(month.equals(messageMonth) && currYear.equals(messageYear))) {
                    continue;
                }

                //Create a new bank message object to add to the ArrayList
                BankMessage bankMessage = new BankMessage();

                //Set the message body and the sender name
                bankMessage.setMessageBody(this.cursor.getString(MESSAGE_BODY_POSITION));
                bankMessage.setSenderName(this.cursor.getString(SENDER_NAME_POSITION));

                //Set previousMessageBody to compare in the next iteration
                previousMessageBody = bankMessage.getMessageBody();


                //Get the date from the cursor and store it into the BankMessage object
                Date date = new Date(this.cursor.getLong(DATE_POSITION));
                bankMessage.setDateSent((new SimpleDateFormat("dd/MM/yyyy")).format(date));
                bankMessage.setMonthSent((new SimpleDateFormat("MMM")).format(date));
                bankMessage.setYearSent((new SimpleDateFormat("yyyy")).format(date));

                //Check if message body DOES NOT contains "debited"
                if (!Pattern.compile("debited").matcher(bankMessage.getMessageBody()).find()) {
                    bankMessage.setAmountType(BankMessage.AMOUNT_CREDITED);
                    continue;
                }

                //Else if message body contains "debited"
                else if (Pattern.compile("debited").matcher(bankMessage.getMessageBody()).find()) {
                    bankMessage.setAmountType(BankMessage.AMOUNT_DEBITED);

                    //Create a matcher object for the amount regular expression
                    Matcher matcher = Pattern.compile("Rs\\d+(\\.\\d+)?").matcher(bankMessage.getMessageBody());

                    //if we find the amount then set it to the BankMessage object
                    if (matcher.find()) {
                        bankMessage.setAmount(Double.parseDouble(matcher.group().substring(3)));

                        //Matcher object to find out with whom the transaction was done
                        Matcher matcher1 = Pattern.compile("to .+?(?= Ref)").matcher(bankMessage.getMessageBody());

                        if (matcher1.find()) {
                            bankMessage.setPersonWithWhomTransactionWasDone(matcher1.group().substring(2));
                        } else {
                            bankMessage.setPersonWithWhomTransactionWasDone("NA");
                        }
                        arrayList.add(bankMessage);
                    }
                }
            } while (this.cursor.moveToNext());
        return arrayList;
    }
}

package com.example.expensebook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FirstFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private final String SBI_REGULAR_EXPRESSION = "SBI";

    private BankMessageLoader bankMessageLoader;

    private ArrayList<BankMessage> bankMessagesArray;

    private Cursor cursor;

    private String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private ScrollView expensesScrollView;

    private TextView expensesTextView;

    private TextView monthHeading;

    private Spinner monthSpinnerView;

    private String monthToDisplay;

    private Double totalAmountForCurrentMonth = Double.valueOf(0.0D);

    TextView totalAmountForCurrentMonthView;

    Typeface typeface;

    public void onViewCreated(View paramView, Bundle paramBundle) {
        this.monthHeading = (TextView)paramView.findViewById(R.id.expenseLabelView);
        this.totalAmountForCurrentMonthView = (TextView)paramView.findViewById(R.id.total_ft_amount);
        this.expensesScrollView = (ScrollView)paramView.findViewById(R.id.expensesScrollView);
        this.expensesTextView = (TextView)paramView.findViewById(R.id.expensesTextView);
        this.typeface = ResourcesCompat.getFont(getContext(), R.font.going_to_school_font);

        //Create the bankMessagesArray list and load messages to it
        this.bankMessagesArray = new ArrayList<>();
        this.bankMessageLoader = new BankMessageLoader(getContext(), SBI_REGULAR_EXPRESSION);

        this.monthSpinnerView = (Spinner)paramView.findViewById(R.id.monthSpinnerView);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource((Context)getActivity(), R.array.months_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.monthSpinnerView.setAdapter((SpinnerAdapter)arrayAdapter);
        this.monthSpinnerView.setOnItemSelectedListener(this);
        this.monthToDisplay = (new SimpleDateFormat("MMM")).format(new Date());
        this.monthSpinnerView.setSelection(Integer.parseInt((new SimpleDateFormat("MM")).format(new Date())) - 1);

        goThroughAllMessagesAndDisplay(this.monthToDisplay);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.fragment_first, paramViewGroup, false);
    }

    //This function goes through all the objects in the bankMessagesArray and displays
    private void goThroughAllMessagesAndDisplay(String month) {
        this.bankMessagesArray = this.bankMessageLoader.loadMessages(month);
        String str = "";
        this.totalAmountForCurrentMonth = Double.valueOf(0.0D);
        this.expensesTextView.setText("");

        for (int b = 0; b < this.bankMessagesArray.size(); b++) {
            String messageMonth = this.bankMessagesArray.get(b).getMonthSent();
            String messageYear = this.bankMessagesArray.get(b).getYearSent();

            messageMonth = this.bankMessagesArray.get(b).getDateSent();
            messageYear = this.bankMessagesArray.get(b).getPersonWithWhomTransactionWasDone();
            double d = this.bankMessagesArray.get(b).getAmount();

            this.totalAmountForCurrentMonth = Double.valueOf(this.totalAmountForCurrentMonth.doubleValue() + d);
            this.expensesTextView.setTypeface(this.typeface);

            if (!messageMonth.equals(str))
                this.expensesTextView.append(messageMonth + "\n");

            NumberFormat numberFormat1 = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
            this.expensesTextView.append("    " + numberFormat1.format(Double.valueOf(d)) + "   --   ");
            this.expensesTextView.append(messageYear);
            this.expensesTextView.append(" \n");
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        this.totalAmountForCurrentMonthView.setText(numberFormat.format(this.totalAmountForCurrentMonth));
    }


    public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        this.monthToDisplay = this.monthSpinnerView.getSelectedItem().toString();
        this.monthHeading.setText("Expenditure in " + this.monthToDisplay + ":");
        goThroughAllMessagesAndDisplay(this.monthToDisplay);
    }

    public void onNothingSelected(AdapterView<?> paramAdapterView) {}

}

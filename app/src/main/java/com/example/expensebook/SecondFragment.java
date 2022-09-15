package com.example.expensebook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

public class SecondFragment extends Fragment {
    private BankMessageLoader bankMessageLoader;

    private ArrayList<BankMessage> bankMessages;

    private String[] colors = new String[] {
            "#800000", "#ffe119", "#911eb4", "#a9a9a9", "#42d4f4", "#3cb44b", "#000075", "#f032e6", "#ffe119", "#9A6324",
            "#dcbeff", "#fffac8" };

    private int[] monthlyAmount;

    private PieChart pieChart;

    private ValueLineChart valueLineChart;

    private static int mapMMMtoMM(String paramString) {
        return Objects.equals(paramString, "Jan") ? 0 : (Objects.equals(paramString, "Feb") ? 1 : (Objects.equals(paramString, "Mar") ? 2 : (Objects.equals(paramString, "Apr") ? 3 : (Objects.equals(paramString, "May") ? 4 : (Objects.equals(paramString, "Jun") ? 5 : (Objects.equals(paramString, "Jul") ? 6 : (Objects.equals(paramString, "Aug") ? 7 : (Objects.equals(paramString, "Sep") ? 8 : (Objects.equals(paramString, "Oct") ? 9 : (Objects.equals(paramString, "Nov") ? 10 : (Objects.equals(paramString, "Dec") ? 11 : -1)))))))))));
    }

    private static String numberToMonth(int paramInt) {
        return (paramInt == 0) ? "Jan" : ((paramInt == 1) ? "Feb" : ((paramInt == 2) ? "Mar" : ((paramInt == 3) ? "Apr" : ((paramInt == 4) ? "May" : ((paramInt == 5) ? "Jun" : ((paramInt == 6) ? "Jul" : ((paramInt == 7) ? "Aug" : ((paramInt == 8) ? "Sep" : ((paramInt == 9) ? "Oct" : ((paramInt == 10) ? "Nov" : ((paramInt == 11) ? "Dec" : "NA")))))))))));
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.fragment_second, paramViewGroup, false);
    }

    public void onViewCreated(View paramView, Bundle paramBundle) {
        this.pieChart = (PieChart)paramView.findViewById(R.id.piechart);
        this.valueLineChart = (ValueLineChart)paramView.findViewById(R.id.valueLineChart);
        ValueLineSeries valueLineSeries = new ValueLineSeries();
        valueLineSeries.setColor(Color.parseColor(colors[1]));
        BankMessageLoader bankMessageLoader = new BankMessageLoader(getContext(), "SBI");
        this.bankMessageLoader = bankMessageLoader;
        this.bankMessages = bankMessageLoader.loadMessages("MMM");
        this.monthlyAmount = new int[12];
        int b;
        for (b = 0; b < 12; b++)
            this.monthlyAmount[b] = 0;
        for (b = 0; b < this.bankMessages.size(); b++) {
            if (((BankMessage)this.bankMessages.get(b)).getYearSent().equals((new SimpleDateFormat("yyyy")).format(new Date())) && ((BankMessage)this.bankMessages.get(b)).getAmountType() == 0) {
                int i = mapMMMtoMM(((BankMessage)this.bankMessages.get(b)).getMonthSent());
                int[] arrayOfInt = this.monthlyAmount;
                arrayOfInt[i] = (int)(arrayOfInt[i] + ((BankMessage)this.bankMessages.get(b)).getAmount());
            }
        }
        for (b = 0; b < 12; b++) {
            this.pieChart.addPieSlice(new PieModel(numberToMonth(b), this.monthlyAmount[b], Color.parseColor(this.colors[b])));
            valueLineSeries.addPoint(new ValueLinePoint(numberToMonth(b), this.monthlyAmount[b]));
        }
        this.valueLineChart.addSeries(valueLineSeries);
        this.valueLineChart.startAnimation();
        this.pieChart.startAnimation();
    }
}

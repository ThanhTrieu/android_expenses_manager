package com.example.asm2_applicationdevelopment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;
    private TextView tvTotalExpenses;
    private ExpenseDatabase expenseDatabase;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        tvTotalExpenses = view.findViewById(R.id.tvTotalExpenses);

        expenseDatabase = new ExpenseDatabase(getActivity());
        List<Expense> expenses = expenseDatabase.getAllExpenses();

        setupBarChart(expenses);
        setupPieChart(expenses);
        calculateTotalExpenses(expenses);

        return view;
    }

    private void setupBarChart(List<Expense> expenses) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for (int i = 0; i < expenses.size(); i++) {
            barEntries.add(new BarEntry(i, (float) expenses.get(i).getAmount()));
            descriptions.add(expenses.get(i).getDescription());
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Expenses");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(descriptions));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
    }

    private void setupPieChart(List<Expense> expenses) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (Expense expense : expenses) {
            pieEntries.add(new PieEntry((float) expense.getAmount(), expense.getDescription()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Expense Distribution");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
    }

    private void calculateTotalExpenses(List<Expense> expenses) {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        tvTotalExpenses.setText("Total Expenses: " + total);
    }
}

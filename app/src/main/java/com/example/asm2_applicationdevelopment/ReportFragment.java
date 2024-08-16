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
import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;
import com.example.asm2_applicationdevelopment.Model.Income;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private BarChart barChartExpenses, barChartIncome;
    private PieChart pieChartExpenses, pieChartIncome;
    private LineChart lineChart1;
    private ScatterChart scatterChart1;
    private TextView tvTotalExpenses, tvTotalIncome;
    private ExpenseDatabase expenseDatabase;
    private IncomeDatabase incomeDatabase;

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

        // Initialize UI components
        barChartExpenses = view.findViewById(R.id.barChartExpense);
        pieChartExpenses = view.findViewById(R.id.pieChartExpense);
        barChartIncome = view.findViewById(R.id.barChartIncome);
        pieChartIncome = view.findViewById(R.id.pieChartIncome);
        lineChart1 = view.findViewById(R.id.lineChart1);
        scatterChart1 = view.findViewById(R.id.scatterChart1);


        // Initialize database instances
        expenseDatabase = new ExpenseDatabase(getActivity());
        incomeDatabase = new IncomeDatabase(getActivity());

        // Get data from databases
        List<Expense> expenses = expenseDatabase.getAllExpenses();
        List<Income> incomes = incomeDatabase.getAllIncomes();

        // Set up charts and text views
        setupBarChart(expenses, barChartExpenses, "Expenses");
        setupPieChart(expenses, pieChartExpenses, "Expense Distribution");
        setupBarChart(incomes, barChartIncome, "Income");
        setupPieChart(incomes, pieChartIncome, "Income Distribution");
        setupLineChart(incomes, lineChart1, "Income Trend");
        setupScatterChart(incomes, scatterChart1, "Income Distribution");

        // Calculate and display total expenses and income


        return view;
    }

    // Generic method to set up bar charts
    private <T> void setupBarChart(List<T> data, BarChart barChart, String label) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof Expense) {
                Expense expense = (Expense) data.get(i);
                barEntries.add(new BarEntry(i, (float) expense.getAmount()));
                descriptions.add(expense.getDescription());
            } else if (data.get(i) instanceof Income) {
                Income income = (Income) data.get(i);
                barEntries.add(new BarEntry(i, (float) income.getAmount()));
                descriptions.add(income.getDescription());
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, label);
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

    // Generic method to set up pie charts
    private <T> void setupPieChart(List<T> data, PieChart pieChart, String label) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (T item : data) {
            if (item instanceof Expense) {
                Expense expense = (Expense) item;
                pieEntries.add(new PieEntry((float) expense.getAmount(), expense.getDescription()));
            } else if (item instanceof Income) {
                Income income = (Income) item;
                pieEntries.add(new PieEntry((float) income.getAmount(), income.getDescription()));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
    }

    // Method to set up line chart
    private void setupLineChart(List<Income> data, LineChart lineChart, String label) {
        ArrayList<Entry> lineEntries = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Income income = data.get(i);
            lineEntries.add(new Entry(i, (float) income.getAmount()));
            descriptions.add(income.getDescription());
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, label);
        lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS[0]);
        lineDataSet.setValueTextColor(ColorTemplate.COLORFUL_COLORS[0]);
        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(descriptions));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        lineChart.getDescription().setEnabled(false);
        lineChart.animateY(1000);
    }

    // Method to set up scatter chart
    private void setupScatterChart(List<Income> data, ScatterChart scatterChart, String label) {
        ArrayList<Entry> scatterEntries = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Income income = data.get(i);
            scatterEntries.add(new Entry(i, (float) income.getAmount()));
            descriptions.add(income.getDescription());
        }

        ScatterDataSet scatterDataSet = new ScatterDataSet(scatterEntries, label);
        scatterDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        ScatterData scatterData = new ScatterData(scatterDataSet);

        scatterChart.setData(scatterData);

        XAxis xAxis = scatterChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(descriptions));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        scatterChart.getDescription().setEnabled(false);
        scatterChart.animateY(1000);
    }


    // Generic method to calculate and display total amount

}

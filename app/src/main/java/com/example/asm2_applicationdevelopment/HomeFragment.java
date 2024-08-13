package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Adapter.ExpenseAdapter;
import com.example.asm2_applicationdevelopment.Adapter.IncomeAdapter;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;
import com.example.asm2_applicationdevelopment.Model.Income;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewIncome;
    private RecyclerView recyclerViewExpense;
    private IncomeAdapter incomeAdapter;
    private ExpenseAdapter expenseAdapter;
    private IncomeDatabase incomeDatabase;
    private ExpenseDatabase expenseDatabase;
    private TextView tvTotalIncome;
    private TextView tvTotalExpense;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomeDatabase = new IncomeDatabase(getActivity());
        expenseDatabase = new ExpenseDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        recyclerViewIncome = view.findViewById(R.id.rvIncomes);
        recyclerViewExpense = view.findViewById(R.id.rvExpenses);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);

        // Set up RecyclerViews with horizontal LinearLayoutManager
        LinearLayoutManager incomeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewIncome.setLayoutManager(incomeLayoutManager);
        incomeAdapter = new IncomeAdapter(incomeDatabase.getAllIncomes(), this::onIncomeItemClick);
        recyclerViewIncome.setAdapter(incomeAdapter);

        LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewExpense.setLayoutManager(expenseLayoutManager);
        expenseAdapter = new ExpenseAdapter(expenseDatabase.getAllExpenses(), this::onExpenseItemClick);
        recyclerViewExpense.setAdapter(expenseAdapter);

        // Add item decoration
        int itemOffset = getResources().getDimensionPixelSize(R.dimen.item_offset); // Ensure this dimension is defined in dimens.xml
        recyclerViewIncome.addItemDecoration(new ItemOffsetDecoration(itemOffset));
        recyclerViewExpense.addItemDecoration(new ItemOffsetDecoration(itemOffset));

        // Calculate and display the total amounts
        updateTotalAmounts();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the RecyclerViews to load the latest data
        incomeAdapter.updateIncomes(incomeDatabase.getAllIncomes());
        expenseAdapter.updateExpenses(expenseDatabase.getAllExpenses());

        // Update total amounts
        updateTotalAmounts();
    }

    private void updateTotalAmounts() {
        List<Income> incomes = incomeDatabase.getAllIncomes();
        List<Expense> expenses = expenseDatabase.getAllExpenses();

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : incomes) {
            totalIncome += income.getAmount();
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        // Làm tròn tổng sau dấu phẩy 3 ký tự
        tvTotalIncome.setText(String.format("%.3f", totalIncome));
        tvTotalExpense.setText(String.format("%.3f", totalExpense));
    }

    private void onIncomeItemClick(Income income) {
        Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
        intent.putExtra("INCOME_ID", income.getId());
        startActivity(intent);
    }

    private void onExpenseItemClick(Expense expense) {
        Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
        intent.putExtra("EXPENSE_ID", expense.getId());
        startActivity(intent);
    }
}

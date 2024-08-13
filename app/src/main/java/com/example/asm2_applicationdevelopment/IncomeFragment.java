package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Adapter.ExpenseAdapter;
import com.example.asm2_applicationdevelopment.Adapter.IncomeAdapter;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Income;

public class IncomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private IncomeAdapter incomeAdapter;
    private IncomeDatabase incomeDatabase;

    public IncomeFragment() {
        // Required empty public constructor
    }

    public static IncomeFragment newInstance() {
        return new IncomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomeDatabase = new IncomeDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.rvIncomes); // Ensure this ID matches your layout

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        incomeAdapter = new IncomeAdapter(incomeDatabase.getAllIncomes(), this::onIncomeItemClick);
        recyclerView.setAdapter(incomeAdapter);
        LinearLayoutManager incomeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(incomeLayoutManager);
        incomeAdapter = new IncomeAdapter(incomeDatabase.getAllIncomes(), this::onIncomeItemClick);
        recyclerView.setAdapter(incomeAdapter);

        // Add item decoration
        int itemOffset = getResources().getDimensionPixelSize(R.dimen.item_offset); // Ensure this dimension is defined in dimens.xml
        recyclerView.addItemDecoration(new ItemOffsetDecoration(itemOffset));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the RecyclerView to load the latest incomes
        incomeAdapter.updateIncomes(incomeDatabase.getAllIncomes());
    }

    private void navigateToAddIncome() {
        Intent intent = new Intent(getActivity(), AddIncomeActivity.class);
        startActivity(intent);
    }

    private void onIncomeItemClick(Income income) {
        Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
        intent.putExtra("INCOME_ID", income.getId());
        startActivity(intent);
    }
}

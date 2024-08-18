package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Adapter.BudgetAdapter;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.BudgetDatabase;
import com.example.asm2_applicationdevelopment.Model.Budget;

public class BudgetFragment extends Fragment {

    private Button btnAddBudget;
    private RecyclerView recyclerView;
    private BudgetAdapter budgetAdapter;
    private BudgetDatabase budgetDatabase;

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance() {
        return new BudgetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        budgetDatabase = new BudgetDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.rvBudgets); // Ensure this ID matches your layout

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetAdapter = new BudgetAdapter(budgetDatabase.getAllBudgets(), this::onBudgetItemClick);
        recyclerView.setAdapter(budgetAdapter);

        LinearLayoutManager budgetLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(budgetLayoutManager);
        int itemOffset = getResources().getDimensionPixelSize(R.dimen.item_offset); // Ensure this dimension is defined in dimens.xml
        recyclerView.addItemDecoration(new ItemOffsetDecoration(itemOffset));

        // Set click listener for add button
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the RecyclerView to load the latest budgets
        budgetAdapter.updateBudgets(budgetDatabase.getAllBudgets());
    }


    private void onBudgetItemClick(Budget budget) {
        Intent intent = new Intent(getActivity(), EditBudgetActivity.class);
        intent.putExtra("BUDGET_ID", budget.getId());
        startActivity(intent);
    }
}

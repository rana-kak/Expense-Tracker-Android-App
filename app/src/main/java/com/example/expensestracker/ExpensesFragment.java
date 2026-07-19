package com.example.expensestracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class ExpensesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private AppDatabase db;
    private Button btnAddExpense;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_expenses,
                container,
                false
        );

        // Init views
        recyclerView = view.findViewById(R.id.recyclerExpenses);
        btnAddExpense = view.findViewById(R.id.btnAddExpense);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        adapter = new ExpenseAdapter(expense -> {
            new Thread(() -> db.expenseDao().delete(expense)).start();
        });
        recyclerView.setAdapter(adapter);

        // Database
        db = AppDatabase.getInstance(requireContext());
        db.expenseDao().getAllExpenses()
                .observe(getViewLifecycleOwner(), expenses -> {
                    adapter.setExpenses(expenses);
                });

        TextView txtMonthlyTotal = view.findViewById(R.id.txtMonthlyTotal);
        //getting the current month
        Calendar calendar = Calendar.getInstance();

        // Start of month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfMonth = calendar.getTimeInMillis();

        // End of month
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        long endOfMonth = calendar.getTimeInMillis();


        db.expenseDao()
                .getMonthlyTotal(startOfMonth, endOfMonth)
                .observe(getViewLifecycleOwner(), total -> {
                    if (total == null) total = 0.0;
                    txtMonthlyTotal.setText("This month: " + total);
                });



        NavController navController =
                NavHostFragment.findNavController(this);

        btnAddExpense.setOnClickListener(v ->
                navController.navigate(
                        R.id.action_expensesFragment_to_addExpenseFragment
                )
        );


        return view;
    }
}

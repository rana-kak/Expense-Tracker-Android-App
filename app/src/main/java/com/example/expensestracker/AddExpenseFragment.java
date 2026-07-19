package com.example.expensestracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddExpenseFragment extends Fragment {

    private EditText edtAmount, edtNote;
    private Spinner spinnerCategory;
    private Button btnSave;
    private AppDatabase db;

    //create the ui
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_add_expense,
                container,
                false
        );

        edtAmount = view.findViewById(R.id.edtAmount);
        edtNote = view.findViewById(R.id.edtNote);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        btnSave = view.findViewById(R.id.btnSave);

        db = AppDatabase.getInstance(requireContext());

        btnSave.setOnClickListener(v -> saveExpense());

        return view;
    }


    //configure ui; adapters, observers, nav
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.categories,
                        android.R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void saveExpense() {

        double amount = Double.parseDouble(
                edtAmount.getText().toString()
        );

        String category =
                spinnerCategory.getSelectedItem().toString();

        String note = edtNote.getText().toString();

        long date = System.currentTimeMillis();

        Expense expense = new Expense(
                amount,
                category,
                date,
                note
        );

//insert  the new expense using a different thread
        AppDatabase.getExecutor().execute(() -> {
            db.expenseDao().insert(expense);

            requireActivity().runOnUiThread(() -> {
                NavHostFragment
                        .findNavController(AddExpenseFragment.this)
                        .popBackStack();
            });
        });

    }
}
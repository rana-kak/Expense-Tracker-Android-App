package com.example.expensestracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses = new ArrayList<>();
    //adapter stores a reference to Fragment logic without knowing what it is
    //bc we dont put database logic here, fragment does that
    private OnDeleteClickListener deleteListener;
    // Interface for delete button
    public interface OnDeleteClickListener {
        void onDelete(Expense expense);
    }
    public ExpenseAdapter(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    // ViewHolder holds reference to the items/views in each row, so we only search for view by id once
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView txtNote, txtAmount, txtDate, txtCategory;
        Button btnDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);//??creates view holder with the textview variables at the top, then continues with the rest of the constructor?
            txtNote = itemView.findViewById(R.id.txtNote);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // Creates a new row; inflates expense_item and wraps it in a viewholder
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    // fill the new row with data
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);  //gets the expense at position in list expenses

        holder.txtAmount.setText("Amount: " + expense.getAmount());
        holder.txtCategory.setText("Category: " + expense.getCategory());
        holder.txtNote.setText("Note: " + expense.getNote());

        //transforming long date into readable string so we can use settext
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.txtDate.setText("Date: " + sdf.format(new Date(expense.getDate())));
        //Fragment handles delete
        holder.btnDelete.setOnClickListener(v -> {
            deleteListener.onDelete(expense);
        });
    }

    // how many rows to display
    @Override
    public int getItemCount() {
        return expenses.size();
    }

    // Helper method for LiveData updates
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();  // forces recycler view to redraw rows wth the new list of expenses
    }
}

package com.ricky.bluejackpharmacy.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ricky.bluejackpharmacy.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Transaction> transactions;
    private ArrayList<Medicine> medicines;

    public OnTransactionClickListener onTransactionClickListener;

    public interface OnTransactionClickListener {
        void setOnDeleteClick(int position, Transaction model);
        void setOnUpdateClick(int position, Transaction model);
    }

    // custom transaction click listener
    public void setOnTransactionClickListener(OnTransactionClickListener onTransactionClickListener) {
        this.onTransactionClickListener = onTransactionClickListener;
    }

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions, ArrayList<Medicine> medicines) {
        this.context = context;
        this.transactions = transactions;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transaction, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction currentTransaction = transactions.get(position);

        holder.tvDate.setText(currentTransaction.getDate());
        holder.tvName.setText(medicines.get(currentTransaction.getMedicineID()-1).getName());
        holder.tvPrice.setText(
                String.valueOf(medicines.get(currentTransaction.getMedicineID()-1).getPrice()));
        holder.tvQuantity.setText(String.valueOf(currentTransaction.getQuantity()));

        holder.btnDelete.setOnClickListener(view -> {
            if (onTransactionClickListener != null) {
                onTransactionClickListener.setOnDeleteClick(holder.getAdapterPosition(), currentTransaction);
            }
        });

        holder.btnUpdate.setOnClickListener(view -> {
            if (onTransactionClickListener != null) {
                onTransactionClickListener.setOnUpdateClick(holder.getAdapterPosition(), currentTransaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvQuantity, tvPrice;
        ImageView btnUpdate, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvTrDate);
            tvName = itemView.findViewById(R.id.tvTrName);
            tvQuantity = itemView.findViewById(R.id.tvTrQuantity);
            tvPrice = itemView.findViewById(R.id.tvTrPrice);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);

            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Medicine> medicines;

    public OnMedicineClickListener onMedicineClickListener;

    public interface OnMedicineClickListener {
        void onMedicineClick(int position, Medicine model);
    }

    // Custom medicine clicked listener
    public void setOnMedicineClickListener(OnMedicineClickListener onMedicineClickListener) {
        this.onMedicineClickListener = onMedicineClickListener;
    }

    public MedicineAdapter(Context context, ArrayList<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_medicine, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine currentMedicine = medicines.get(position);

        holder.tvMedName.setText(currentMedicine.getName());
        holder.tvManufacturer.setText(currentMedicine.getManufacturer());
        holder.tvPrice.setText(String.valueOf(currentMedicine.getPrice()));

        Picasso.get()
                .load(currentMedicine.getImage())
                .into(holder.imgMed);

        holder.itemView.setOnClickListener(view -> {
            if (onMedicineClickListener != null) {
                onMedicineClickListener.onMedicineClick(holder.getAdapterPosition(), currentMedicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedName, tvManufacturer, tvPrice;
        ImageView imgMed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedName = itemView.findViewById(R.id.tvMedName);
            tvManufacturer = itemView.findViewById(R.id.tvManufacturer);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgMed = itemView.findViewById(R.id.imgMedicine);
        }
    }
}

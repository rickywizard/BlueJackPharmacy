package com.ricky.bluejackpharmacy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ricky.bluejackpharmacy.R;
import com.ricky.bluejackpharmacy.model.DatabaseHelper;
import com.ricky.bluejackpharmacy.model.Medicine;
import com.ricky.bluejackpharmacy.model.Transaction;
import com.ricky.bluejackpharmacy.model.TransactionAdapter;

import java.util.ArrayList;

public class TransactionFragment extends Fragment {

    private String loginUserEmail;
    private Integer userID;

    private TextView tvNoData;
    private LinearLayout popUp;
    private EditText etNewQty;
    private Button btnUpdateSubmit, btnUpdateCancel;

    private ArrayList<Transaction> transactions;
    private ArrayList<Medicine> medicines;
    private RecyclerView rvTransaction;
    private DatabaseHelper databaseHelper;

    public TransactionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        loginUserEmail = bundle.getString("user");

        Context thisActivity = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        initializeVariable(view, thisActivity);
        if (transactions.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
        }
        else {
            tvNoData.setVisibility(View.GONE);
            setRecyclerView(thisActivity);
        }

        return view;
    }

    private void initializeVariable(View view, Context thisActivity) {
        tvNoData = view.findViewById(R.id.tvNoData);
        transactions = new ArrayList<>();
        rvTransaction = view.findViewById(R.id.rvTransaction);
        rvTransaction.setHasFixedSize(true);
        databaseHelper = new DatabaseHelper(thisActivity);

        popUp = view.findViewById(R.id.popUp);
        etNewQty = view.findViewById(R.id.etNewQty);
        btnUpdateSubmit = view.findViewById(R.id.btnUpdateSubmit);
        btnUpdateCancel = view.findViewById(R.id.btnUpdateCancel);

        userID = databaseHelper.getUserID(loginUserEmail);
        medicines = databaseHelper.getAllMedicine();
        transactions = databaseHelper.getAllTransaction(userID);
    }

    private void setRecyclerView(Context thisActivity) {
        TransactionAdapter transactionAdapter = new TransactionAdapter(thisActivity, transactions, medicines);
        rvTransaction.setAdapter(transactionAdapter);
        rvTransaction.setLayoutManager(new LinearLayoutManager(thisActivity));

        transactionAdapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void setOnDeleteClick(int position, Transaction model) {
                transactions.remove(position);
                transactionAdapter.notifyItemRemoved(position);
                Boolean delete = databaseHelper.deleteTransaction(model.getTrID());

                if (delete) {
                    Toast.makeText(thisActivity, "Successfully delete the item", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(thisActivity, "Failed to delete. Please try again!", Toast.LENGTH_SHORT).show();
                }

                if (transactions.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setOnUpdateClick(int position, Transaction model) {
                animateStart();
                popUp.setVisibility(View.VISIBLE);

                btnUpdateSubmit.setOnClickListener(view -> {
                    if (etNewQty.getText().toString().isEmpty()) {
                        Toast.makeText(thisActivity, "Quantity can not be empty", Toast.LENGTH_SHORT).show();
                    }
                    else if (etNewQty.getText().toString().equals("0")) {
                        Toast.makeText(thisActivity, "Quantity must more than 0", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Integer newQty = Integer.parseInt(etNewQty.getText().toString());

                        transactions.get(position).setQuantity(newQty);
                        transactionAdapter.notifyItemChanged(position);

                        Boolean update = databaseHelper.updateTransaction(model.getTrID(), newQty);

                        if (update) {
                            Toast.makeText(thisActivity, "Successfully update the item", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(thisActivity, "Failed to update. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                        animateEnd();
                    }
                });

                btnUpdateCancel.setOnClickListener(view -> {
                    animateEnd();
                });
            }
        });
    }

    private void animateStart() {
        int cx = popUp.getWidth();
        int cy = 0;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(popUp, cx, cy,0f, finalRadius);

        anim.start();
    }

    private void animateEnd() {
        int cx = popUp.getWidth();
        int cy = 0;

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(popUp, cx, cy, initialRadius, 0f);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                popUp.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }
}
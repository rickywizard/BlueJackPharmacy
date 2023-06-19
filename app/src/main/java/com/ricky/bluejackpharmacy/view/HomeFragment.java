package com.ricky.bluejackpharmacy.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ricky.bluejackpharmacy.R;
import com.ricky.bluejackpharmacy.model.DatabaseHelper;
import com.ricky.bluejackpharmacy.model.Medicine;
import com.ricky.bluejackpharmacy.model.MedicineAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private String loginUserEmail;

    private ArrayList<Medicine> medicines;

    private RecyclerView rvMedicine;
    private ProgressBar progressBar;

    private DatabaseHelper databaseHelper;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        loginUserEmail = bundle.getString("user");

        Context thisActivity = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializeVariable(view, thisActivity);
        fetchData(thisActivity);

        return view;
    }

    private void initializeVariable(View view, Context thisActivity) {
        medicines = new ArrayList<>();
        rvMedicine = view.findViewById(R.id.rvMedicine);
        rvMedicine.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.loadingBar);
        databaseHelper = new DatabaseHelper(thisActivity);
    }

    private void setRecyclerView(Context thisActivity) {
        MedicineAdapter medicineAdapter = new MedicineAdapter(thisActivity, medicines);
        rvMedicine.setAdapter(medicineAdapter);
        rvMedicine.setLayoutManager(new GridLayoutManager(thisActivity, 2));

        medicineAdapter.setOnMedicineClickListener(new MedicineAdapter.OnMedicineClickListener() {
            @Override
            public void onMedicineClick(int position, Medicine model) {
                Intent intent = new Intent(thisActivity, MedicineDetailActivity.class);
                intent.putExtra(MedicineDetailActivity.MED_IMG, model.getImage());
                intent.putExtra(MedicineDetailActivity.MED_NAME, model.getName());
                intent.putExtra(MedicineDetailActivity.MED_FAC, model.getManufacturer());
                intent.putExtra(MedicineDetailActivity.MED_PRICE, String.valueOf(model.getPrice()));
                intent.putExtra(MedicineDetailActivity.MED_DESC, model.getDescription());
                intent.putExtra("user", loginUserEmail);

                startActivity(intent);
            }
        });
    }

    private void fetchData(Context thisActivity) {
        String url = "https://mocki.io/v1/ae13b04b-13df-4023-88a5-7346d5d3c7eb";

        RequestQueue requestQueue = Volley.newRequestQueue(thisActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray medicineArray = response.getJSONArray("medicines");

                            for (int i = 0; i < medicineArray.length(); i++) {
                                JSONObject jsonObject = medicineArray.getJSONObject(i);

                                String name = jsonObject.getString("name");
                                String manufacturer = jsonObject.getString("manufacturer");
                                Integer price = jsonObject.getInt("price");
                                String imageUrl = jsonObject.getString("image");
                                String description = jsonObject.getString("description");

                                Medicine medicine = new Medicine(name, manufacturer, price, imageUrl, description);

                                medicines.add(medicine);
                            }
                            progressBar.setVisibility(View.GONE);
                            setRecyclerView(thisActivity);

                            if (databaseHelper.checkEmpty()) {
                                for (Medicine medicine : medicines) {
                                    databaseHelper.insertMedicine(
                                            medicine.getName(),
                                            medicine.getManufacturer(),
                                            medicine.getPrice(),
                                            medicine.getImage(),
                                            medicine.getDescription()
                                    );
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", ": Can not fetch data");
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

}
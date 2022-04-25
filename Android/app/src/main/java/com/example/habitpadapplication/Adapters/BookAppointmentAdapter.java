package com.example.habitpadapplication.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.habitpadapplication.DoctorSchedule;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.Urls;
import com.example.habitpadapplication.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookAppointmentAdapter extends SectionedRecyclerViewAdapter<BookAppointmentAdapter.ViewHolder> {

    Activity activity;
    ArrayList<String> sectionList;
    HashMap<String, ArrayList<String>> itemList;
    ArrayList<String> docTimings = new ArrayList<>();
    ArrayList<String> docReserved = new ArrayList<>();

    int selectedSection = -1;
    int selectedItem = -1;

    String dr_ID;

    public BookAppointmentAdapter(Activity activity, ArrayList<String> sectionList, HashMap<String, ArrayList<String>> itemList,
                                  ArrayList<String> docTimings, ArrayList<String> docReserved, String dr_ID) {
        this.activity = activity;
        this.sectionList = sectionList;
        this.itemList = itemList;
        this.docTimings = docTimings;
        this.docReserved = docReserved;
        this.dr_ID = dr_ID;

    }

    @Override
    public int getSectionCount() {
        return sectionList.size();
    }

    @Override
    public int getItemCount(int section) {
        return itemList.get(sectionList.get(section)).size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        holder.textView.setText(sectionList.get(section));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        String sItem = itemList.get(sectionList.get(section)).get(relativePosition);

        holder.textView.setText(sItem);
        holder.textView.setOnClickListener(v -> {
            if (docTimings.contains(sItem)) {
                Toast.makeText(activity, sItem, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activity, "Please select an Available slot", Toast.LENGTH_SHORT).show();
            }
            selectedSection = section;
            selectedItem = relativePosition;
            notifyDataSetChanged();
        });


        if (selectedSection == section && selectedItem == relativePosition && docTimings.contains(sItem)) {
            holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_fill));
            holder.textView.setTextColor(Color.WHITE);
        }
        else{
            if (!docTimings.contains(sItem) && !docReserved.contains(sItem))
                holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_fill_black));
            else if (docReserved.contains(sItem))
                holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_fill_yellow));
            else
                holder.textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_outline));

            holder.textView.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if(section == 1)
            return 0;
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        if (viewType == VIEW_TYPE_HEADER) {
            layout = R.layout.slot_header;
        }else
            layout = R.layout.slot_design;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);

        }
    }

    public String getSelected() {
        if (selectedItem != -1 && selectedSection != 1) {
            String sItem = itemList.get(sectionList.get(selectedSection)).get(selectedItem);
            if (docTimings.contains(sItem)) {
                selectedItem = -1;
                UpdateSlot(dr_ID,sItem,"Reserved");
//                ref.child(sItem).setValue("Reserved");
                notifyDataSetChanged();
                return sItem;
            }
        }
        return null;
    }

    private void UpdateSlot(final String dr_ID, final String sItem,final String slotAvailable) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_DOCTOR_SLOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if (success.equals("1")) {
                                Toast.makeText(activity.getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(activity.getApplicationContext(),"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity.getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("doctorID", dr_ID);
                params.put("slotTime", sItem);
                params.put("slotAvailable", slotAvailable);



                return params;
            }
        };

        VolleySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(stringRequest);

    }




}

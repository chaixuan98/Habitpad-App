package com.example.habitpadapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddFoodActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private Context context;
    private String userID;
    private String intentFrom, foodID, intentUserGender,intentUserAge,intentUserLifestyle,intentUserWeight,intentUserHeight;

    private ProgressDialog loadingbar;

    private TextView foodName, servingSize, numberOfServings, calories, carbs, fat, protein, createdBy;
    private Button addbtn;
    private LinearLayout numberOfServingsContainer;

    private String numberOfSizeValue="1", foodNameValue, servingSizeValue, servingSizeUnitValue, caloriesValue, carbsValue, fatValue, proteinValue,
            foodCreator, createdByValue;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Food");
        setContentView(R.layout.activity_add_food);

        context=getApplicationContext();

        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();
        HashMap<String,String> usersDetails = sessionManager.getUsersDetailFromSession();

        userID = usersDetails.get(SessionManager.KEY_USERID);

        Intent intent = getIntent();
        intentFrom = intent.getExtras().getString("intentFrom");
        if(intentFrom.equals("Breakfast") || intentFrom.equals("Lunch") ||
                intentFrom.equals("Dinner") ||  intentFrom.equals("Snack"))
        {
            foodID = intent.getExtras().getString("foodID");

        }

        intentUserGender = intent.getExtras().getString("intentUserGender");
        intentUserAge = intent.getExtras().getString("intentUserAge");
        intentUserLifestyle = intent.getExtras().getString("intentUserLifestyle");
        intentUserWeight = intent.getExtras().getString("intentUserWeight");
        intentUserHeight = intent.getExtras().getString("intentUserHeight");

        foodName = (TextView) findViewById(R.id.addfood_foodname);
        servingSize = (TextView) findViewById(R.id.add_food_serving_size);
        numberOfServings = (TextView) findViewById(R.id.add_food_number_of_serving);
        calories = (TextView) findViewById(R.id.addfood_calories);
        carbs = (TextView) findViewById(R.id.addfood_carbs);
        fat = (TextView) findViewById(R.id.addfood_fat);
        protein = (TextView) findViewById(R.id.addfood_protein);
        //createdBy = (TextView) findViewById(R.id.addfood_createdby);
        numberOfServingsContainer = findViewById(R.id.add_food_number_of_servings_container);

        addbtn = (Button) findViewById(R.id.addfood_addbutton);

        loadingbar = new ProgressDialog(this);


        if(!TextUtils.isEmpty(foodID))
        {
            GetAndSetFoodDetails();
        }


        addbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddFoodToUserDiary(DateHandler.getCurrentFormedDate(),DateHandler.getCurrentTime());
            }
        });

        numberOfServingsContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupServingSizeEditDialog();
            }
        });


    }

    /* toolbar back button click action */
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    private void PopupServingSizeEditDialog()
    {
        final Dialog numberOfServingEditDialog = new Dialog(this);
        numberOfServingEditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        numberOfServingEditDialog.setContentView(R.layout.number_of_servings_edit_layout);
        numberOfServingEditDialog.setTitle("Number Of Serving Edit Window");
        numberOfServingEditDialog.show();
        Window servingSizeEditWindow = numberOfServingEditDialog.getWindow();
        servingSizeEditWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        final EditText numberOfServingsInput = (EditText) numberOfServingEditDialog.findViewById(R.id.number_of_servings_dialog_input);
        final TextView errorMsg = (TextView) numberOfServingEditDialog.findViewById(R.id.number_of_servings_dialog_error);
        errorMsg.setVisibility(View.GONE);


        /* cancel button click action */
        Button cancelBtn = (Button) numberOfServingEditDialog.findViewById(R.id.number_of_servings_dialog_cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                numberOfServingEditDialog.cancel();
            }
        });


        /* submit button click action */
        Button submitBtn = (Button)numberOfServingEditDialog.findViewById(R.id.number_of_servings_dialog_submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(numberOfServingsInput.getText().toString()) || Double.parseDouble(numberOfServingsInput.getText().toString()) <= 0)
                {
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else
                {
                    numberOfSizeValue = numberOfServingsInput.getText().toString().trim();
                    numberOfServingEditDialog.cancel();
                    NewValueCalculation();
                }
            }
        });
    }



    private void NewValueCalculation()
    {
        numberOfServings.setText(numberOfSizeValue);

        String newCaloriesValue = String.format("%.0f",(Double.parseDouble(numberOfSizeValue) * Double.parseDouble(caloriesValue)) );
        calories.setText(newCaloriesValue +" Calories (kcal)");

        String newCrabsValue = String.format("%.1f",(Double.parseDouble(numberOfSizeValue) * Double.parseDouble(carbsValue)) );
        carbs.setText(newCrabsValue+"g");

        String newFatValue = String.format("%.1f",(Double.parseDouble(numberOfSizeValue) * Double.parseDouble(fatValue)) );
        fat.setText(newFatValue+"g");

        String newProteinValue = String.format("%.1f",(Double.parseDouble(numberOfSizeValue) * Double.parseDouble(proteinValue)));
        protein.setText(newProteinValue+"g");

    }

    private void GetAndSetFoodDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_FOOD_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            //JSONArray array = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("fooddata");
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);


                                    foodNameValue = object.getString("foodName").trim();
                                    servingSizeUnitValue = object.getString("foodServingSizeUnit").trim();
                                    caloriesValue = object.getString("foodCalories").trim();
                                    fatValue = object.getString("foodFat").trim();
                                    proteinValue = object.getString("foodProtein").trim();
                                    carbsValue = object.getString("foodCarbs").trim();

                                    foodName.setText(foodNameValue);
                                    servingSize.setText(servingSizeUnitValue+" g");
                                    calories.setText(caloriesValue+" Calories (kcal)");
                                    fat.setText(fatValue+"g");
                                    protein.setText(proteinValue+"g");
                                    carbs.setText(carbsValue+"g");

                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddFoodActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("foodID",foodID);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }


    private void AddFoodToUserDiary(String date, String time) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.ADD_USER_FOOD_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("OK")) {
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                UserSendToDiaryPage();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context,"Save Error!" + e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("foodID",foodID);
                params.put("foodType", intentFrom.toLowerCase());
                params.put("numberOfServing", numberOfSizeValue);
                params.put("addFoodDate", date);
                params.put("addFoodTime", time);


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void UserSendToDiaryPage()
    {
        Intent diaryIntent = new Intent(AddFoodActivity.this, DiaryActivity.class);
        diaryIntent.putExtra("intentFrom", "AddFoodActivity");
        diaryIntent.putExtra("intentUserID", userID);
        diaryIntent.putExtra("intentUserGender", intentUserGender);
        diaryIntent.putExtra("intentUserAge", intentUserAge);
        diaryIntent.putExtra("intentUserLifestyle", intentUserLifestyle);
        diaryIntent.putExtra("intentUserWeight", intentUserWeight);
        diaryIntent.putExtra("intentUserHeight", intentUserHeight);
        startActivity(diaryIntent);
        finish();
    }
}
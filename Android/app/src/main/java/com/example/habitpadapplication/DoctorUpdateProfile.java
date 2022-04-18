package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Settings.UserSettingsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorUpdateProfile extends AppCompatActivity {

    private TextInputLayout dName, dEmail, dPhone, dEdu, dExp, dHos;
    private CircleImageView dPhoto;
    private TextView changePhotoTV;
    private MaterialButton editDoctorBtn;
    private Bitmap bitmap;
    String encodeImageString;

    DoctorSessionManager doctorSessionManager;
    String doctorID, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Update Profile");
        setContentView(R.layout.activity_doctor_update_profile);

        //checking the permission
        //if the permission is not given we will open setting to add permission
        //else app will not open
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        doctorSessionManager = new DoctorSessionManager(this);
        doctorSessionManager.checkLogin();
        HashMap<String, String> doctorDetails = doctorSessionManager.getDoctorDetailFromSession();

        doctorID = doctorDetails.get(DoctorSessionManager.KEY_DOCTORID);
        password = doctorDetails.get(DoctorSessionManager.KEY_DOCTORPASSWORD);


        dPhoto = findViewById(R.id.doctor_image);
        dName = findViewById(R.id.d_full_name_layout);
        dEmail = findViewById(R.id.d_email_layout);
        dPhone = findViewById(R.id.d_mobileNo_layout);
        dEdu = findViewById(R.id.d_education_layout);
        dExp = findViewById(R.id.d_exp_layout);
        dHos = findViewById(R.id.d_hospital_layout);
        changePhotoTV = findViewById(R.id.change_photo_tv);
        editDoctorBtn = findViewById(R.id.update);

        getDoctorDetails();

        editDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editDoctorDetails(doctorID,
                        dName.getEditText().getText().toString(),
                        password,
                        dEmail.getEditText().getText().toString(),
                        dPhone.getEditText().getText().toString(),
                        dEdu.getEditText().getText().toString(),
                        dExp.getEditText().getText().toString(),
                        dHos.getEditText().getText().toString());

            }
        });


        changePhotoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed(){

        Intent intent = new Intent(DoctorUpdateProfile.this, DoctorMainActivity.class);
        startActivity(intent);

        finish();

    }

    private void getDoctorDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.GET_DOCTOR_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    Log.i("tagconvertstr", "[" + response + "]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("doctor");

                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String doctorPhoto = object.getString("doctorPhoto").trim();
                            String doctorName = object.getString("doctorName").trim();
                            String doctorEmail = object.getString("doctorEmail").trim();
                            String doctorPhone = object.getString("doctorPhone").trim();
                            String doctorEducation = object.getString("doctorEducation").trim();
                            String doctorExp = object.getString("doctorExp").trim();
                            String doctorHospital = object.getString("doctorHospital").trim();


                            Glide.with(getApplicationContext()).asBitmap().load(doctorPhoto)
                                    .fitCenter()
                                    .dontAnimate().into(dPhoto);

                            dName.getEditText().setText(doctorName);
                            dEmail.getEditText().setText(doctorEmail);
                            dPhone.getEditText().setText(doctorPhone);
                            dEdu.getEditText().setText(doctorEducation);
                            dExp.getEditText().setText(doctorExp);
                            dHos.getEditText().setText(doctorHospital);



                        }
                    }
                    if (success.equals("0")) {

                        Toast.makeText(DoctorUpdateProfile.this, "Read error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(DoctorUpdateProfile.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DoctorUpdateProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("doctorID", doctorID);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void editDoctorDetails(final String doctorID,
                                 final String doctorName,
                                 final String doctorPassword,
                                 final String doctorEmail,
                                 final String doctorPhone,
                                 final String doctorEducation,
                                 final String doctorExp,
                                 final String doctorHospital){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_DOCTOR_DETAILS_URL, new Response.Listener<String>()  {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        doctorSessionManager.createDoctorLoginSession(doctorID,doctorName,doctorPassword,doctorEmail,doctorPhone,doctorEducation,doctorExp,doctorHospital);

                    }

                    if (success.equals("0")) {
                        Toast.makeText(DoctorUpdateProfile.this, "Edit error", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Edit Error!",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DoctorUpdateProfile.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("doctorID",doctorID);
                params.put("doctorName",doctorName);
                params.put("doctorPassword",password);
                params.put("doctorEmail",doctorEmail);
                params.put("doctorPhone",doctorPhone);
                params.put("doctorEducation",doctorEducation);
                params.put("doctorExp",doctorExp);
                params.put("doctorHospital",doctorHospital);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                dPhoto.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadDoctorPic(doctorID);

        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private void uploadDoctorPic(final String strDocID) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPLOAD_DOCTOR_PROFILE_PIC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    Log.i("tagconvertstr", "["+response+"]");
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");

                    if (success.equals("1")) {

                        Toast.makeText(DoctorUpdateProfile.this,message,Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(DoctorUpdateProfile.this, "Read error" + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(DoctorUpdateProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("doctorID", strDocID);
                params.put("userPhoto",encodeImageString);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
package com.example.hp.pfa_app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

public class EnquateurActivity extends AppCompatActivity {

    EditText Etnom,Etprenom,Etcin,Etemail,Ettel,Etaddress,Etpassword;
    Button btnchooser,btnsave;
    ImageView imageView;
    Bitmap bitmap;

  //  String url="http://127.0.0.1:82/pfaapp/enquaitaire/registrationenq1.php";
   String url="http://192.168.1.6:82/pfaapp/enquaitaire/registrationenq1.php";
  //   String url="http://10.0.2.2/pfaapp/enquaitaire/registrationenq1.php";

    final int CODE_GALARY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquateur);

        Etnom = (EditText)findViewById(R.id.editTextNomEq);
        Etprenom = (EditText)findViewById(R.id.editTextPrenomEq);
        Etcin = (EditText)findViewById(R.id.editTextCinEq);
        Etemail = (EditText)findViewById(R.id.editTextEmailEq);
        Ettel = (EditText)findViewById(R.id.editTextTelEq);
        Etaddress = (EditText)findViewById(R.id.editTextAddressEq);
        Etpassword = (EditText)findViewById(R.id.editTextPasswordEq);
        btnchooser = (Button) findViewById(R.id.buttonChooserImageEq);
        btnsave = (Button) findViewById(R.id.buttonSaveEq);
        imageView = (ImageView)findViewById(R.id.imageView2);



        btnchooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ActivityCompat.requestPermissions(
                        EnquateurActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALARY
                );*/
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Chooser"),CODE_GALARY);

                       }
        });




        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {


                        try {
                        JSONObject    jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("response");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(EnquateurActivity.this, ListEnqActivity.class));
                    }
                },new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("nom",Etnom.getText().toString());
                        params.put("prenom",Etprenom.getText().toString());
                        params.put("cin",Etcin.getText().toString());
                        params.put("email",Etemail.getText().toString());
                        params.put("tel",Ettel.getText().toString());
                        params.put("address",Etaddress.getText().toString());
                        params.put("password",Etpassword.getText().toString());
                        return params;
                    }
                };
               MySingleTon.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_GALARY && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri filepath = data.getData();
            try {
//                InputStream inputStream = getContentResolver().openInputStream(filepath);
  //              bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);


                Log.d("lo","data");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

      if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){


      }else{
          Toast.makeText(getApplicationContext(),"sorry",Toast.LENGTH_LONG).show();
      }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }




}

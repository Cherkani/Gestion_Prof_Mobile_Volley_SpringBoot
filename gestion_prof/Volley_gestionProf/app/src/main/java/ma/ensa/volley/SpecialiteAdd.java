package ma.ensa.volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class SpecialiteAdd extends AppCompatActivity {
    Button bnRetour ,bnAdd,bnVoirSpecialites;
    EditText libelle,code;
    RequestQueue requestQueue;
    Button backButtonfiliereAdd;
    String postUrl = "http://10.0.2.2:8083/api/v1/specialites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialite_add);



        bnRetour=findViewById(R.id.bnRetour);
        libelle=findViewById(R.id.libelle);
        code=findViewById(R.id.code);
        bnAdd = findViewById(R.id.bnAdd);
        bnRetour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialiteAdd.this, MainActivity.class);
                startActivity(intent);
            }
        });
        bnVoirSpecialites=findViewById(R.id.bnVoirSpecialites);
        bnVoirSpecialites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecialiteAdd.this, SpecialiteVoir.class);
                startActivity(intent);
            }
        });


        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonbody =new JSONObject();
                try{
                    jsonbody.put("code",code.getText().toString());
                    jsonbody.put("libelle",libelle.getText().toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                requestQueue = Volley.newRequestQueue(SpecialiteAdd.this);
                JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST, postUrl,
                        jsonbody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("resultat", response+"");
                        Toast.makeText(SpecialiteAdd.this,"Response:"+response.toString(),Toast.LENGTH_LONG).show();   }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erreur", error.toString());
                        Toast.makeText(SpecialiteAdd.this,"Response:"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                );
                requestQueue.add(request);
            }
        });
    }
}
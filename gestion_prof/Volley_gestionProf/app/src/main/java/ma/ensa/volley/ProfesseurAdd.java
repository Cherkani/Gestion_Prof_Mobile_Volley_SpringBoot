package ma.ensa.volley;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ma.ensa.volley.beans.Professeur;
import ma.ensa.volley.beans.Specialite;

public class ProfesseurAdd extends AppCompatActivity {

////////date picker
EditText dateEmbaucheEditText;
    Calendar calendar = Calendar.getInstance();
    int year, month, day;



    ///////////partie spinner
    private Spinner spinner;
    private List<Specialite> specialitesList = new ArrayList<>();
    private RequestQueue requestQueue;
    private String specialitesUrl = "http://10.0.2.2:8083/api/v1/specialites";

    /////////
    Button bnRetourStudent, bnAddStudent, bnVoirProf;
    EditText nom,prenom,email,telephone,dateEmbauche;



    String postUrl = "http://10.0.2.2:8083/api/v1/professeurs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professeur_add);

        bnVoirProf = findViewById(R.id.bnVoirProf);
        bnVoirProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfesseurAdd.this,ProfesseurVoir.class);
                startActivity(intent);
            }
        });


        ///////////////date picker
        dateEmbaucheEditText = findViewById(R.id.dateEmbaucheeditTextStudent);
        dateEmbaucheEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);




        /////////////////


        ////partie spinner
        spinner = findViewById(R.id.spinnerprofesseurTextStudent);

        requestQueue = Volley.newRequestQueue(this);
        loadProfesseurs();
        ////////////


        bnRetourStudent = findViewById(R.id.bnRetourStudent);
        nom = findViewById(R.id.nomeditTextStudent);
        prenom = findViewById(R.id.prenomeditTextStudent);
        email = findViewById(R.id.emaileditTextStudent);
        telephone = findViewById(R.id.telephoneeditTextStudent);
        spinner = findViewById(R.id.spinnerprofesseurTextStudent);
        dateEmbauche = findViewById(R.id.dateEmbaucheeditTextStudent);

        bnAddStudent = findViewById(R.id.bnAddStudent);
        bnRetourStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfesseurAdd.this, MainActivity.class);
                startActivity(intent);
            }
        });




        bnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonbody = new JSONObject();
                    jsonbody.put("nom", nom.getText().toString());
                    jsonbody.put("prenom", prenom.getText().toString());
                    jsonbody.put("email", email.getText().toString());
                    jsonbody.put("telephone", telephone.getText().toString());
                    jsonbody.put("dateEmbauche", dateEmbauche.getText().toString());

                    // Obtenez l'objet Professeur sélectionné depuis le Spinner
                    Specialite selectedSpecialite = (Specialite) spinner.getSelectedItem();

                    // Créez un objet JSON pour le champ "filiere"
                    JSONObject specialiteJson = new JSONObject();
                    specialiteJson.put("id", selectedSpecialite.getId());
                    specialiteJson.put("code", selectedSpecialite.getCode());
                    specialiteJson.put("libelle", selectedSpecialite.getLibelle());

                    jsonbody.put("specialite", specialiteJson);

                    requestQueue = Volley.newRequestQueue(ProfesseurAdd.this);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl,
                            jsonbody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("resultat", response + "");
                            Toast.makeText(ProfesseurAdd.this, "Response:" + response.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Erreur", error.toString());
                            Toast.makeText(ProfesseurAdd.this, "Response:" + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }






    //////////////spinner
    private void loadProfesseurs() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, specialitesUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject filiereJson = response.getJSONObject(i);
                                Long id = filiereJson.getLong("id");
                                String code = filiereJson.getString("code");
                                String name = filiereJson.getString("libelle");
                                Specialite specialite = new Specialite(id, code, name);
                                specialitesList.add(specialite);
                            }
                            populateSpinner();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(request);
    }

    private void populateSpinner() {
        ArrayAdapter<Specialite> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,specialitesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }




    ///////////////:date picker


    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Mettez à jour le champ de texte avec la date sélectionnée
            String selectedDate =year + "-" + (month + 1) + "-" + day ; // Format de date personnalisé
            dateEmbaucheEditText.setText(selectedDate);
        }
    };







    /////////////////////:










}

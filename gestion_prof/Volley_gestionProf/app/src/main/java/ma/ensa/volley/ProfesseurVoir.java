package ma.ensa.volley;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ma.ensa.volley.adapter.AdapterProfesseur;
import ma.ensa.volley.beans.Professeur;
import ma.ensa.volley.beans.Specialite;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
public class ProfesseurVoir extends AppCompatActivity {
//////////date picker
int year;
    int month;
    int day;
 EditText dateEmbaucheInput;

        ////////////////



    ///////:spinner

    private Spinner spinner;
    private List<Specialite> specialiteList  = new ArrayList<>();
    private RequestQueue requestQueue;

    private String filieresUrl = "http://10.0.2.2:8083/api/v1/specialites";
    ////////////////


    private ListView idProfesseurListView;
    private AdapterProfesseur professeursAdapter;
private Button backButtonProfesseurAdd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professeur_lists);


//////////spinner
                spinner = findViewById(R.id.spinnerprofesseurTextStudent);
        requestQueue = Volley.newRequestQueue(this);
        loadFilieres();
 ///////////////:
        backButtonProfesseurAdd=findViewById(R.id.backButtonProfesseurAdd);

        backButtonProfesseurAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ProfesseurAdd.class);
                startActivity(intent);
            }
        });




        idProfesseurListView = findViewById(R.id.idProfesseurListView);
        fetchProfesseursFromServer();






    }

    private void fetchProfesseursFromServer() {
        String fetchUrl = "http://10.0.2.2:8083/api/v1/professeurs";
        requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, fetchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                Log.d("Response", "--------------------------------------------");
                List<Professeur> professeurs = parseProfesseursData(response);
                professeursAdapter = new AdapterProfesseur(ProfesseurVoir.this, professeurs);
                idProfesseurListView.setAdapter(professeursAdapter);

                idProfesseurListView.setOnItemClickListener((parent, view, position, id) -> {
                    showActionDialog(professeurs.get(position));
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérez les erreurs ici
                Log.e("Erreur", "Échec de la récupération des données depuis le serveur: " + error.getMessage());
                Toast.makeText(ProfesseurVoir.this, "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private List<Professeur> parseProfesseursData(String response) {
        List<Professeur> professeurs = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.getLong("id");
                String nom = jsonObject.getString("nom");
                String prenom = jsonObject.getString("prenom");
                String email = jsonObject.getString("email");
                String telephone = jsonObject.getString("telephone");
///////////formater la date
                String dateEmbauche = jsonObject.getString("dateEmbauche");
                Date FormdateEmbauche = SimpleFormaterNewDate(dateEmbauche);

//                String dateString = "Tue Nov 30 00:00:00 GMT+00:00 2023";
//                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//
//                Date FormdateEmbauche = sdf.parse(dateString);
/////////////////////////////
                // Parsez d'autres données nécessaires ici
                JSONObject specialiteJson = jsonObject.getJSONObject("specialite");
                Long specialiteId = specialiteJson.getLong("id");
                String specialiteCode = specialiteJson.getString("code");
                String specialiteLibelle = specialiteJson.getString("libelle");

                Specialite specialite = new Specialite(specialiteId, specialiteCode, specialiteLibelle);

                Professeur professeur = new Professeur(id, nom, prenom, telephone, email, FormdateEmbauche, specialite);
                    professeurs.add(professeur);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return professeurs;
    }
        private void showActionDialog(final Professeur professeur) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actions")
                .setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showUpdateDialog(professeur);
                        } else {
                            showDeleteConfirmationDialog(professeur.getId());
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
        private void showDeleteConfirmationDialog(final Long professeurId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm the delete")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteStudent(professeurId);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
        private void deleteStudent(Long professeurId) {
        String deleteUrl = "http://10.0.2.2:8083/api/v1/professeurs/" + professeurId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ProfesseurVoir.this, "prof deleted successfully", Toast.LENGTH_LONG).show();
                fetchProfesseursFromServer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfesseurVoir.this, "Error deleting prof", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    private void showUpdateDialog(final Professeur professeur) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Student Information");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);




        final EditText prenomInput = new EditText(this);
        prenomInput.setHint("prenom");
        prenomInput.setText(professeur.getPrenom());
        layout.addView(prenomInput);

        final EditText nomeInput = new EditText(this);
        nomeInput.setHint("nom");
        nomeInput.setText(professeur.getNom());
        layout.addView(nomeInput);

        final EditText telephoneInput = new EditText(this);
        telephoneInput.setHint("telephone");
        telephoneInput.setText(professeur.getTelephone());
        layout.addView(telephoneInput);






        final EditText emailInput = new EditText(this);
        emailInput.setHint("email");
        emailInput.setText(professeur.getEmail());
        layout.addView(emailInput);


        /////////////recuperation date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(professeur.getDateEmbauche());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // Créez un champ EditText et ajoutez-le à votre layout
         dateEmbaucheInput = new EditText(this);
        dateEmbaucheInput.setHint("dateEmbauche");
        dateEmbaucheInput.setText(professeur.getDateEmbauche().toString());
        layout.addView(dateEmbaucheInput);
        dateEmbaucheInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });




///////////ajout spinner dans le modal
        // Ajouter un Spinner pour la filière
        Spinner specialiteSpinner = new Spinner(this);
        ArrayAdapter<Specialite> professeurAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specialiteList );
        professeurAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialiteSpinner.setAdapter(professeurAdapter);

        // Sélectionnez la filière actuelle de l'étudiant dans le Spinner
        int currentFilierePosition = professeurAdapter.getPosition(professeur.getSpecialite());
        specialiteSpinner.setSelection(currentFilierePosition);

        layout.addView(specialiteSpinner);
///////////////////////////
        builder.setView(layout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override







            public void onClick(DialogInterface dialog, int which) {
                String newprenom = prenomInput.getText().toString();
                String newnome = nomeInput.getText().toString();
                String newtelephone= telephoneInput.getText().toString();
                String newemail= emailInput.getText().toString();
                String newdateEmbauche = dateEmbaucheInput.getText().toString();
//////////////---------------->>>>>>>>>>>
                // Obtenez la nouvelle filière sélectionnée depuis le Spinner
                Specialite newSpecialite = (Specialite) specialiteSpinner.getSelectedItem();

                professeur.setNom(newnome);
                professeur.setPrenom(newprenom);
                professeur.setTelephone(newtelephone);
                professeur.setEmail(newemail);
////////////////////////////cas de recuperation de date dans un date picker
                // Define the format that matches the format of newdateEmbaucheStr
                Date newdateEmbaucheFormate =FormaterNewDate(newdateEmbauche);
                    professeur.setDateEmbauche(newdateEmbaucheFormate);


////////////////////////////////////////////////
                professeur.setSpecialite(newSpecialite);



              //  student.setPassword(newPassword);
                professeur.setSpecialite(newSpecialite);
                Log.d("response", String.valueOf(professeur));
                sendUpdateRequest(professeur);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void sendUpdateRequest(final Professeur professeur) {
        String updateUrl = "http://10.0.2.2:8083/api/v1/professeurs/" + professeur.getId();
        JSONObject jsonBody = new JSONObject();
        try {





            jsonBody.put("nom", professeur.getNom());
            jsonBody.put("prenom", professeur.getPrenom());
            jsonBody.put("email", professeur.getEmail());
            jsonBody.put("telephone", professeur.getTelephone());

      //   jsonBody.put("dateEmbauche", professeur.getDateEmbauche());
            // Formatage de la date au format "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
            String formattedDate = dateFormat.format(professeur.getDateEmbauche());

            jsonBody.put("dateEmbauche", formattedDate);


///////////---------------------->>>>>>>>>>>>>  private Date dateEmbauche;
            // Créez un objet JSON pour la filière
            JSONObject SpecialiteJson = new JSONObject();
            SpecialiteJson.put("id", professeur.getSpecialite().getId());
            SpecialiteJson.put("code",professeur.getSpecialite().getCode());
            SpecialiteJson.put("libelle", professeur.getSpecialite().getLibelle());

            jsonBody.put("specialite", SpecialiteJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, updateUrl, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ProfesseurVoir.this, "Prof updated successfully", Toast.LENGTH_LONG).show();
                        fetchProfesseursFromServer();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfesseurVoir.this, "Error updating prof", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

///////////////spinner
    private void loadFilieres() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, filieresUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject filiereJson = response.getJSONObject(i);
                                int id = filiereJson.getInt("id");
                                String code = filiereJson.getString("code");
                                String name = filiereJson.getString("libelle");
                                Specialite specialite = new Specialite((long) id, code, name);
                                specialiteList .add(specialite);
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
        ArrayAdapter<Specialite> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,specialiteList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
/////////////////////////////


    ///////////////:methode pour formater date
    private Date FormaterNewDate(String dateEmbauche) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(dateEmbauche);
            Log.d("FormattedDate", parsedDate.toString());
        } catch (ParseException e) {
            Log.e("Date", "Error parsing date: " + e.getMessage());
        }
        return parsedDate;
    }



    private Date SimpleFormaterNewDate(String dateEmbauche) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateEmbauche, formatter);

            if (offsetDateTime != null) {
                return Date.from(offsetDateTime.toInstant());
            } else {
                 return getDefaultDate();
            }
        } else {
              return getDefaultDate();
        }
    }

    private Date getDefaultDate() {
        // Retourner une valeur par défaut, par exemple, la date actuelle
        return new Date();
    }







    // Méthode pour afficher le DatePickerDialog
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ProfesseurVoir.this.year = year;
                ProfesseurVoir.this.month = monthOfYear;
                ProfesseurVoir.this.day = dayOfMonth;

                String dateEmbaucheStr = year + "-" + String.format(Locale.getDefault(), "%02d", (monthOfYear + 1)) + "-" + String.format(Locale.getDefault(), "%02d", dayOfMonth);
                dateEmbaucheInput.setText(SimpleFormaterNewDate(dateEmbaucheStr).toString());
            }
        }, year, month, day);

        datePickerDialog.show();
    }



}

package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Professeur;
import ma.ensa.volley.beans.Specialite;

public class AdapterProfesseur extends ArrayAdapter<Professeur> {
    private Context context;
    private List<Professeur> professeursList;

    public AdapterProfesseur(Context context, List<Professeur> professeursList) {
        super(context, 0, professeursList);
        this.context = context;
        this.professeursList = professeursList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.professeur_item, parent, false);
        }

        Professeur professeur = professeursList.get(position);

        TextView nomeditTextStudent = convertView.findViewById(R.id.nomeditTextStudent);
        TextView prenomeditTextStudent = convertView.findViewById(R.id.prenomeditTextStudent);
        TextView emaileditTextStudent = convertView.findViewById(R.id.emaileditTextStudent);

        TextView telephoneeditTextStudent = convertView.findViewById(R.id.telephoneeditTextStudent);
        TextView dateEmbaucheeditTextStudent = convertView.findViewById(R.id.dateEmbaucheeditTextStudent);
        TextView spinnerprofesseurTextStudent = convertView.findViewById(R.id.spinnerprofesseurTextStudent);

        nomeditTextStudent.setText("Nom : " + professeur.getNom());
        prenomeditTextStudent.setText("Prénom : " + professeur.getPrenom());
        emaileditTextStudent.setText("Email : " + professeur.getEmail());
        telephoneeditTextStudent.setText("Téléphone : " + professeur.getTelephone());

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
       // String dateEmbauche = dateFormat.format(professeur.getDateEmbauche());
        dateEmbaucheeditTextStudent.setText("Date d'embauche : " + professeur.getDateEmbauche());

        spinnerprofesseurTextStudent.setText("Spécialité : " + professeur.getSpecialite().getLibelle());

        return convertView;
    }
}

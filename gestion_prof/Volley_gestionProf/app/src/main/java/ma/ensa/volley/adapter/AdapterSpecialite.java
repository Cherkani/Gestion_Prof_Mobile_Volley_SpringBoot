package ma.ensa.volley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Specialite;

public class AdapterSpecialite  extends ArrayAdapter<Specialite> {
    private Context context;
    private List<Specialite> specialites;

    public AdapterSpecialite(Context context, List<Specialite> specialites) {
        super(context, 0, specialites);
        this.context = context;
        this.specialites = specialites;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.specialite_item, parent, false);
        }


        Specialite filiere1 = specialites.get(position);

        TextView nameTextView = convertView.findViewById(R.id.nameText);
        TextView codeTextView = convertView.findViewById(R.id.codeText);

        nameTextView.setText(filiere1.getLibelle());
        codeTextView.setText(filiere1.getCode());

        return convertView;
    }

}

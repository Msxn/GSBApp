package com.gsbapp.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddHorsForfaitActivity extends AppCompatActivity implements TaskComplete {

    private EditText libelle, montant, jour, mois, annee;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hors_forfait);

        final SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);

        send = findViewById(R.id.send);

        libelle = findViewById(R.id.libelle);
        montant = findViewById(R.id.montant);
        jour = findViewById(R.id.jour);
        mois = findViewById(R.id.mois);
        annee = findViewById(R.id.annee);

        final String login = user.getString("login", "NULL");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lib = libelle.getText().toString();
                String mont = montant.getText().toString();
                String date = annee.getText().toString() + "-" + mois.getText().toString() + "-" + jour.getText().toString();
                RequestByURL req = new RequestByURL(AddHorsForfaitActivity.this);
                req.execute("http://mcol.myqnapcloud.com/PPE4/api.php?mode=envoihorsforfait&libelle="+lib+"&montant="+mont+"&date="+date+"&user="+login);
            }
        });

    }

    @Override
    public void complete(String value) {
        Intent i = new Intent(AddHorsForfaitActivity.this, VisiteurActivity.class);
        startActivity(i);
        finish();
    }
}

package com.gsbapp.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class SaisirFicheActivity  extends AppCompatActivity implements TaskComplete{

    private EditText etpin,kmin,nuiin,repin;
    private TextView month;
    private Button addhf, send;
    private int exist;
    final LoadingDialog loading = new LoadingDialog(SaisirFicheActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisir_fiche);

        final SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);

        etpin = findViewById(R.id.ETP);
        kmin = findViewById(R.id.KM);
        nuiin = findViewById(R.id.NUI);
        repin = findViewById(R.id.REP);
        month = findViewById(R.id.month);
        send = findViewById(R.id.send);

        final String login = user.getString("login", "NULL");

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String dateString = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);

        month.setText(dateString);

        final Bundle intent = getIntent().getExtras();

        etpin.setText(intent.getString("etp"));
        kmin.setText(intent.getString("km"));
        nuiin.setText(intent.getString("nui"));
        repin.setText(intent.getString("rep"));
        final String exist = intent.getString("exist");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestByURL req = new RequestByURL(SaisirFicheActivity.this);
                loading.startLoadingDialog();
                req.execute("http://mcol.myqnapcloud.com/PPE4/api.php?mode=envoi&etp="+etpin.getText().toString()+"&km="+kmin.getText().toString()+"&nui="+nuiin.getText().toString()+"&rep="+repin.getText().toString()+"&exist="+exist+"&user="+login);
            }
        });


    }

    @Override
    public void complete(String value) {
        Toast.makeText(SaisirFicheActivity.this, value, Toast.LENGTH_SHORT).show();
        loading.dismissDialog();
        Intent i = new Intent(SaisirFicheActivity.this, VisiteurActivity.class);
        startActivity(i);
        finish();
    }
}

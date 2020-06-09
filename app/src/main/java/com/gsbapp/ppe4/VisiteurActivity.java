package com.gsbapp.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class VisiteurActivity extends AppCompatActivity implements TaskComplete {

    private TextView hello;
    private Button sendBtn, consultBtn, disconnectBtn, sendhorsforfaitBtn;
    final LoadingDialog loading = new LoadingDialog(VisiteurActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiteurpage);

        final SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);

        sendBtn = findViewById(R.id.send);
        sendhorsforfaitBtn = findViewById(R.id.sendhorsforfait);
        consultBtn = findViewById(R.id.consult);
        disconnectBtn = findViewById(R.id.disconnect);
        hello = findViewById(R.id.hello);


        final String login = user.getString("login", "NULL");
        hello.setText("Bonjour "+login+"!");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestByURL req = new RequestByURL(VisiteurActivity.this);
                req.execute("http://mcol.myqnapcloud.com/PPE4/api.php?mode=saisie&user="+login);
            }
        });

        sendhorsforfaitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VisiteurActivity.this, AddHorsForfaitActivity.class);
                startActivity(i);
            }
        });


        consultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VisiteurActivity.this, ConsultVisActivity.class);
                startActivity(i);
            }
        });


        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VisiteurActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void complete(String value) {
        //Toast.makeText(VisiteurActivity.this, value, Toast.LENGTH_SHORT).show();
        try {
            loading.startLoadingDialog();
            if(!value.equals("none")) {
                JSONObject json = new JSONObject(value);
                JSONObject forfait = json.getJSONObject("forfait");
                //Toast.makeText(VisiteurActivity.this, json.getString("forfait"), Toast.LENGTH_SHORT).show();

                JSONObject OBJetp = forfait.getJSONObject("ETP");
                String etp = OBJetp.getString("quantite");

                JSONObject OBJkm = forfait.getJSONObject("KM");
                String km = OBJkm.getString("quantite");

                JSONObject OBJnui = forfait.getJSONObject("NUI");
                String nui = OBJnui.getString("quantite");

                JSONObject OBJrep = forfait.getJSONObject("REP");
                String rep = OBJrep.getString("quantite");


                Intent i = new Intent(VisiteurActivity.this, SaisirFicheActivity.class);

                if (!etp.equals("") && !km.equals("") && !nui.equals("") && !rep.equals("")) {
                    i.putExtra("etp", etp);
                    i.putExtra("km", km);
                    i.putExtra("nui", nui);
                    i.putExtra("rep", rep);
                    i.putExtra("exist", "1");
                }
                startActivity(i);
            }else{
                Intent i = new Intent(VisiteurActivity.this, SaisirFicheActivity.class);
                i.putExtra("etp", "");
                i.putExtra("km", "");
                i.putExtra("nui", "");
                i.putExtra("rep", "");
                i.putExtra("exist", "0");
                startActivity(i);
            }
            loading.dismissDialog();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

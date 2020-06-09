package com.gsbapp.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements TaskComplete {

    private EditText login, mdp;
    private RadioButton visiteur, comptable;
    private Button submit;
    final LoadingDialog loading = new LoadingDialog(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //hide the title bar


        login = findViewById(R.id.login);
        mdp = findViewById(R.id.mdp);
        visiteur = findViewById(R.id.visiteur);
        comptable = findViewById(R.id.comptable);
        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logincontent = login.getText().toString();
                String mdpcontent = mdp.getText().toString();
                String type = "";

                if(visiteur.isChecked()){
                    type = "visiteur";
                }else if(comptable.isChecked()){
                    type = "comptable";
                }

                RequestByURL req = new RequestByURL(LoginActivity.this);
                loading.startLoadingDialog();
                req.execute("http://mcol.myqnapcloud.com/PPE4/api.php?mode=login&login="+logincontent+"&mdp="+mdpcontent+"&type="+type);
            }
        });

    }

    @Override
    public void complete(String value) {
        if(value.equals("match")){
            final SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = user.edit();
            editor.putString("login", login.getText().toString());
            editor.putString("mdp", mdp.getText().toString());
            editor.apply();
            Toast.makeText(LoginActivity.this, "Vous êtes bien identifié "+login.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, VisiteurActivity.class);
            startActivity(i);
            loading.dismissDialog();
            finish();
        }else{
            Toast.makeText(LoginActivity.this, "Identifiant incorrect", Toast.LENGTH_SHORT).show();
            loading.dismissDialog();
        }
    }
}

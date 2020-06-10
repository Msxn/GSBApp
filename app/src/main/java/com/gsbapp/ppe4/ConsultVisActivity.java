package com.gsbapp.ppe4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

public class ConsultVisActivity extends AppCompatActivity implements TaskComplete{

    private TextView month, title, ETP, KM, NUI, REP, total, etat, horsforfait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_vis);
        month = findViewById(R.id.month);
        title = findViewById(R.id.title);
        ETP = findViewById(R.id.ETP);
        KM = findViewById(R.id.KM);
        NUI = findViewById(R.id.NUI);
        REP = findViewById(R.id.REP);
        horsforfait = findViewById(R.id.horsforfait);
        total = findViewById(R.id.total);
        etat = findViewById(R.id.etat);

        final SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String login = user.getString("login", "NULL");

        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        final String dateString = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);

        month.setText(dateString);
        title.setText("Fiche frais du "+dateString);

        RequestByURL req = new RequestByURL(ConsultVisActivity.this);
        req.execute("http://mcol.myqnapcloud.com/PPE4/api.php?mode=consult&user="+login);
    }

    @Override
    public void complete(String value) {
        //Toast.makeText(ConsultVisActivity.this, value, Toast.LENGTH_SHORT).show();
        try {
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

            String etatStr = json.getString("etat");

            String hftotalStr = json.getString("horsforfaittotal");

            Integer totalint = (Integer.parseInt(etp)+Integer.parseInt(km)+Integer.parseInt(nui)+Integer.parseInt(rep));
            String totalStr = totalint.toString();

            ETP.setText("Forfait Etape : "+etp+"€");
            KM.setText("Forfait kilométrique : "+km+"€");
            NUI.setText("Nuitée Hotêl : "+nui+"€");
            REP.setText("Forfait repas : "+rep+"€");
            total.setText("TOTAL : "+totalStr+"€");
            etat.setText("Etat de la fiche de frais : "+etatStr);
            horsforfait.setText("Montant hors-forfait total : "+hftotalStr+"€");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
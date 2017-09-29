package comtelekpsi.github.oviedofireandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VehicleSubActivity extends AppCompatActivity {

    private String vehicleId;
    private String vehicleName;
    private String uid;
    private String username;
    public static final String UID_SAVE = "UIDSaveFile";
    private Uri uri;
    private URL url;
    Context context;
    ArrayList<Button> buttons = new ArrayList();
    LinearLayout mLinearLayout;
    TextView mTextView;
    TableLayout mTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_sub);
        mTableLayout=(TableLayout) findViewById(R.id.tableLayout);
        //mTableLayout.isStretchAllColumns();
        //mTableLayout.isShrinkAllColumns();
        context = this;
        mTextView = (TextView) findViewById(R.id.textView);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        vehicleName= getIntent().getStringExtra("VEHICLE_NAME");
        vehicleId = getIntent().getStringExtra("VEHICLE_ID");
        TextView tv1=(TextView)findViewById(R.id.textView);
        tv1.setText(vehicleName+" sections:");
        TextView mUsernameTextView=(TextView) findViewById(R.id.usernameTextView);
        SharedPreferences uidSave = getSharedPreferences(UID_SAVE, Context.MODE_PRIVATE);
        uid = uidSave.getString("pUID", null);
        username = uidSave.getString("pUsername", null);
        mUsernameTextView.setText(username);
        new VehicleSubActivity.RetrieveJSON().execute();
    }

class RetrieveJSON extends AsyncTask<Void, Void, String> {
    private Exception exception;
    protected void onPreExecute() {
    }
    protected String doInBackground(Void... urls) {
        // Do some validation here
        try {
            url = new URL("https://us-central1-oviedofiresd-55a71.cloudfunctions.net/vehicleCompartments/?uid="+uid+"&vehicleId="+vehicleId);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                System.out.println(stringBuilder.toString());
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);
        buttons.clear();
        buttons=SubJSONParser.subParse(response, mTableLayout, context);
        for (int i=0; i<buttons.size(); i++){
            final int j=i;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FormActivity.class);
                    intent.putExtra("VEHICLE_NAME", vehicleName);
                    intent.putExtra("FORM_ID", buttons.get(j).getHint().toString());
                    intent.putExtra("VEHICLE_SECTION", buttons.get(j).getText());
                    startActivity(intent);
                }
            });

        }
        //mTextView.setText(response);
    }
}
}

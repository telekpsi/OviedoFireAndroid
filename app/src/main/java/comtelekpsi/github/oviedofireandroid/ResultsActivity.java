package comtelekpsi.github.oviedofireandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ResultsActivity extends AppCompatActivity {

    private TableLayout mTableLayout;
    private LinearLayout mLinearLayout;
    private TextView mUsernameTextView;
    private TextView mCBTextView;
    private TextView mTitleTextView;
    private TextView mDateCBTextView;
    private String formId;
    private String offFormName;
    private String vehicleName;
    private String sectionName;
    private String uid;
    private String username;
    public static final String UID_SAVE = "UIDSaveFile";
    private URL url;
    Context context;
    ArrayList<Equipment> eList;
    String jsonString;
    AsyncTask aTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        SharedPreferences uidSave = getSharedPreferences(UID_SAVE, Context.MODE_PRIVATE);
        uid = uidSave.getString("pUID", null);
        username = uidSave.getString("pUsername", null);
        Log.e("HEREHERE","Results successfully retried username: "+username);
        vehicleName= getIntent().getStringExtra("VEHICLE_NAME");
        sectionName= getIntent().getStringExtra("VEHICLE_SECTION");
        formId = getIntent().getStringExtra("FORM_ID");
        offFormName = getIntent().getStringExtra("OFF_FORM_NAME");
        mTableLayout=(TableLayout) findViewById(R.id.tableResultsLayout);
        //TODO: fix column widths
        //mTableLayout.isStretchAllColumns();
        //mTableLayout.isShrinkAllColumns();
        context = this;
        mCBTextView = (TextView) findViewById(R.id.completedByResultsTextView);
        mDateCBTextView = (TextView) findViewById(R.id.dateCBResultsTextView);
        mTitleTextView = (TextView) findViewById(R.id.formTitleResultsTextView);
        mTitleTextView.setTextColor(Color.BLACK);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearResultsLayout);
        mUsernameTextView=(TextView) findViewById(R.id.usernameResultsTextView);
        mUsernameTextView.setText(username);
        if (!vehicleName.equals("davidcheck")) {
            mTitleTextView.setText(vehicleName + " " + sectionName);
            new ResultsActivity.RetrieveJSON().execute();
        }
        else if (offFormName!=null){
            mTitleTextView.setText(offFormName);
            new ResultsActivity.RetrieveJSON().execute();
        }
        else{
            mTitleTextView.setText(sectionName);
            new ResultsActivity.RetrieveJSON().execute();
        }
    }

    class RetrieveJSON extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(ResultsActivity.this);
        protected void onPreExecute() {
            this.dialog.setMessage("LOADING");
            this.dialog.show();
        }
        protected String doInBackground(Void... urls) {
            try {
                url = new URL("https://us-central1-oviedofiresd-55a71.cloudfunctions.net/results/?uid="+uid+"&formId="+formId);
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
            if(response == null)
                response = "THERE WAS AN ERROR";
            Log.i("INFO", response);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            ResultsJSONParser.resultsParse(response, mTitleTextView, mCBTextView, mDateCBTextView, mTableLayout, context);
        }
    }

}

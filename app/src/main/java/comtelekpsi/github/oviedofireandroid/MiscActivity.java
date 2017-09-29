package comtelekpsi.github.oviedofireandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MiscActivity extends AppCompatActivity {

    private String uid;
    private String username;
    public static final String UID_SAVE = "UIDSaveFile";
    private URL url;
    Context context;
    ArrayList<Button> buttons = new ArrayList();
    LinearLayout mLinearLayout;
    TextView mTextView;
    TableLayout mTableLayout;
    ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misc);
        mTableLayout=(TableLayout) findViewById(R.id.tableLayout);
        mTableLayout.isStretchAllColumns();
        //mTableLayout.isShrinkAllColumns();
        context = this;
        mTextView = (TextView) findViewById(R.id.textView);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView mUsernameTextView=(TextView) findViewById(R.id.usernameTextView);
        SharedPreferences uidSave = getSharedPreferences(UID_SAVE, Context.MODE_PRIVATE);
        uid = uidSave.getString("pUID", null);
        username = uidSave.getString("pUsername", null);
        mUsernameTextView.setText(username);
        /*logoutButton = (ImageButton) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(VehicleSubActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });*/
        new MiscActivity.RetrieveJSON().execute();
    }

    class RetrieveJSON extends AsyncTask<Void, Void, String> {
        private Exception exception;
        protected void onPreExecute() {
        }
        protected String doInBackground(Void... urls) {
            // Do some validation here
            try {
                url = new URL("https://us-central1-oviedofiresd-55a71.cloudfunctions.net/misc/?uid="+uid);
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
            buttons=OffJSONParser.offParse(response, mTableLayout, context);
            for (int i=0; i<buttons.size(); i++){
                final int j=i;
                buttons.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FormActivity.class);
                        intent.putExtra("FORM_ID", buttons.get(j).getHint().toString());
                        intent.putExtra("OFF_FORM_NAME", buttons.get(j).getText());
                        startActivity(intent);
                    }
                });

            }
            //mTextView.setText(response);
        }
    }

}

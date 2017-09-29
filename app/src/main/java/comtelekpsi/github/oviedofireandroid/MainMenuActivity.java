package comtelekpsi.github.oviedofireandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.R.attr.strokeColor;


public class MainMenuActivity extends AppCompatActivity {

    private String uid;
    private String username;
    ImageButton logoutButton;
    Button aButton;
    Button bButton;
    Button cButton;
    Button dButton;
    Context context;
    public static final String UID_SAVE = "UIDSaveFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        context=this;
        TextView mTextView=(TextView) findViewById(R.id.usernameTextView);
        username = getIntent().getStringExtra("USER_NAME");
        uid = getIntent().getStringExtra("USER_ID");
        SharedPreferences uidSave = getSharedPreferences(UID_SAVE, Context.MODE_PRIVATE);
        if(uid == null || uid.isEmpty()) {
            uid = uidSave.getString("pUID", null);
            username = uidSave.getString("pUsername", null);
        }
        mTextView.setText(username);
        logoutButton = (ImageButton) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                //logout of firebase
                                //return to login screen
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
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
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        TextView mTextView=(TextView) findViewById(R.id.usernameTextView);
        SharedPreferences uidSave = getSharedPreferences(UID_SAVE, Context.MODE_PRIVATE);
        if(uid == null || uid.isEmpty()) {
            uid = uidSave.getString("pUID", null);
            username = uidSave.getString("pUsername", null);
        }
        mTextView.setText(username);
    }

    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences uidSave = getSharedPreferences(UID_SAVE, 0);
        SharedPreferences.Editor editor = uidSave.edit();
        editor.putString("pUID", uid);
        editor.putString("pUsername", username);
        editor.commit();
    }
    public void activeVehicle(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, ActiveVehicleActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("USER_ID", uid);
        startActivity(intent);
    }

    public void offTruck(View view)
    {
        Intent intent = new Intent(MainMenuActivity.this, OffTruckActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("USER_ID", uid);
        startActivity(intent);
    }

    public void toDoList(View view){
        Intent intent = new Intent(MainMenuActivity.this, ToDoListActivity.class);
        intent.putExtra("USER_NAME", username);
        intent.putExtra("USER_ID", uid);
        startActivity(intent);
    }


    public void qrScanner(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("Scan the QR code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Unsuccessful scan", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                /*String contents=result.getContents();
                long formID = Long.parseLong(contents);
                Intent intent = new Intent(this, FormActivity.class);
                intent.putExtra("FORM_ID", formID);
                intent.putExtra("FORM_ID_STRING", contents);
                intent.putExtra("USER_NAME", username);
                System.out.println("Intent Integrator Username: "+username);
                intent.putExtra("USER_ID", uid);
                startActivity(intent);*/

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

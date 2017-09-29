package comtelekpsi.github.oviedofireandroid;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.view.View.generateViewId;

/**
 * Created by David on 9/9/2017.
 */

public class PMR {
    String status;
    String caption;
    String repairString;
    EditText repairEditText;
    RadioButton pRadioButton;
    RadioButton mRadioButton;
    RadioButton rRadioButton;

    public PMR(){
    }
    public PMR(final TableLayout tableLayout, final TableRow tableRow, final Context context, String caption){
        this.caption=caption;
        status="incomplete";
        repairString=null;
        final RadioGroup radioGroup=new RadioGroup(context);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        tableRow.addView(radioGroup);

        pRadioButton = new RadioButton(context);
        pRadioButton.setText("Present");
        pRadioButton.setId(generateViewId());
        final int pId=pRadioButton.getId();

        mRadioButton = new RadioButton(context);
        mRadioButton.setText("Missing");
        mRadioButton.setId(generateViewId());
        final int mId=mRadioButton.getId();

        rRadioButton = new RadioButton(context);
        rRadioButton.setText("Repairs Needed");
        rRadioButton.setId(generateViewId());
        final int rId=rRadioButton.getId();

        radioGroup.addView(pRadioButton);
        radioGroup.addView(mRadioButton);
        radioGroup.addView(rRadioButton);

        final TableRow textRow=new TableRow(context);
        tableLayout.addView(textRow);
        textRow.setTag("Text Row");

        final TextView labelText = new TextView(context);
        labelText.setText("");
        textRow.addView(labelText);

        repairEditText = new EditText(context);
        repairEditText.setEnabled(false);
        repairEditText.setText("");
        //repairEditText.setVisibility(View.INVISIBLE);
        textRow.addView(repairEditText);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                //Log.d("chk", "id" + checkedId);

                if (checkedId!=rId && repairEditText.getText().toString()!=null){
                   repairString=repairEditText.getText().toString();
                }
                if (checkedId == pId) {
                    status="Present";
                    repairEditText.setText("");
                    repairEditText.setEnabled(false);
                    labelText.setText("");
                    radioGroup.setTag("pTag");
                } else if (checkedId == mId) {
                    status="Missing";
                    repairEditText.setText("");
                    repairEditText.setEnabled(false);
                    labelText.setText("");
                    radioGroup.setTag("mTag");
                } else if (checkedId == rId) {
                    status="Repairs Needed";
                    if (repairString!=null){
                        repairEditText.setText(repairString);
                    }
                    repairEditText.setEnabled(true);
                    repairEditText.requestFocus();
                    labelText.setText("Notes:");
                    radioGroup.setTag("rTag");
                }
            }
        });
    }
}

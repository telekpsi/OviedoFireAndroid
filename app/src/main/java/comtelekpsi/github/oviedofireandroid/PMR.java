package comtelekpsi.github.oviedofireandroid;

import android.content.Context;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    String result;
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
        result="incomplete";
        repairString=null;
        final RadioGroup radioGroup=new RadioGroup(context);
        radioGroup.setTag("incomplete");
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

        final LinearLayout textLayout=new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.HORIZONTAL);
        tableLayout.addView(textLayout);
        textLayout.setTag("Text Row");

        final TextView labelText = new TextView(context);
        labelText.setText("");
        textLayout.addView(labelText);

        repairEditText = new EditText(context);
        repairEditText.setEnabled(false);
        repairEditText.setText("");
        //repairEditText.setVisibility(View.INVISIBLE);
        textLayout.addView(repairEditText);

        final String prefix="Notes: ";
        //TODO: update submit function to not just look for rows

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                //Log.d("chk", "id" + checkedId);

                if (checkedId!=rId && repairEditText.getText().toString()!=null){
                   repairString=repairEditText.getText().toString();
                }
                if (checkedId == pId) {
                    result="Present";
                    repairEditText.setText("");
                    repairEditText.setEnabled(false);
                    labelText.setText("");
                    radioGroup.setTag("pTag");
                } else if (checkedId == mId) {
                    result="Missing";
                    repairEditText.setText("");
                    repairEditText.setEnabled(false);
                    labelText.setText("");
                    radioGroup.setTag("mTag");
                } else if (checkedId == rId) {
                    result="Repairs Needed";
                    if (repairString!=null){
                        repairEditText.setText(repairString);
                    }
                    repairEditText.setEnabled(true);
                    repairEditText.requestFocus();
                    labelText.setText(prefix);
                    radioGroup.setTag("rTag");
                }
                else {
                    result = "incomplete";
                    radioGroup.setTag("incomplete");
                }
            }
        });
    }
}

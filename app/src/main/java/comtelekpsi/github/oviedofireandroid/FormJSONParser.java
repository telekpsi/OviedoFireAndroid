package comtelekpsi.github.oviedofireandroid;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by David on 9/9/2017.
 */

public class FormJSONParser {
    public static void formParse(String str, TableLayout mTableLayout, Context context){
        try {
            JSONObject object = (JSONObject) new JSONObject(str);
            String title=object.getString("title");
            JSONArray items = object.getJSONArray("inputElements");
            System.out.println("items length is "+items.length());
            for (int i=0; i<items.length(); i++){
                TableRow tableRow = new TableRow(context);
                mTableLayout.addView(tableRow);
                TextView textView = new TextView(context);
                JSONObject object2=items.getJSONObject(i);
                String caption=object2.getString("caption");
                textView.setText(caption);
                textView.setTextColor(Color.BLACK);
                tableRow.addView(textView);
                String type=object2.getString("type");
                tableRow.setTag(type);
                textView.setTag("caption");
                if (type.equals("pmr")){
                    PMR pmr = new PMR(mTableLayout, tableRow, context, caption);
                }
                if (type.equals("pf")){
                    PF pf = new PF(mTableLayout, tableRow, context, caption);
                }
                if (type.equals("num")){
                    EditText editText = new EditText(context);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    tableRow.addView(editText);
                }
                if (type.equals("per")){
                    SeekBar seekBar = new SeekBar(context);
                    seekBar.setMax(100);
                    seekBar.setBackgroundColor(Color.GREEN);
                    tableRow.addView(seekBar);

                    TableRow textRow=new TableRow(context);
                    textRow.setTag("Text Row");
                    mTableLayout.addView(textRow);
                    TextView labelText = new TextView(context);
                    labelText.setText("");
                    textRow.addView(labelText);

                    final TextView numberText = new TextView(context);
                    numberText.setText("");
                    textRow.addView(numberText);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            numberText.setText(Integer.toString(progress) +"%");
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

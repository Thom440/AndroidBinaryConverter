package com.geekproduction.binaryconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class ConvertString extends AppCompatActivity {
    private TextView octalText;
    private TextView binaryText;
    private TextView hexText;
    private TextView decimalText;

    String octalResult;
    String binaryResult;
    String hexResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_string);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("String");

        decimalText = (TextView)findViewById(R.id.decimalTextView5);
        octalText = (TextView)findViewById(R.id.octalTextView5);
        binaryText = (TextView)findViewById(R.id.binaryTextView5);
        hexText = findViewById(R.id.hexTextView5);


        EditText edit = (EditText)findViewById(R.id.convertString);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String decimalResult = "";;
                octalResult = "";
                binaryResult = "";
                hexResult = "";

                int number = 0;
                for (int i = 0; i < edit.getText().length(); i++) {
                    char letter = edit.getText().charAt(i);
                    number = letter;
                    decimalResult += number + " ";

                    String value = String.valueOf(number);
                    new DoConversions().execute(value);
                }
                decimalText.setText(decimalResult);
                binaryText.setText(binaryResult);
                octalText.setText(octalResult);
                hexText.setText((hexResult));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void clearFields(View v) {
        EditText edit = findViewById(R.id.convertString);
        edit.setText("");
    }

    public void copyToClipBoard(View v) {
        String copyText = "";
        String viewName = "";
        if (v == findViewById(R.id.decimalClipBoard5)) {
            copyText = (String)decimalText.getText();
            viewName = "Decimal";
        }
        else if (v == findViewById(R.id.binaryClipBoard5)) {
            copyText = (String)binaryText.getText();
            viewName = "Binary";
        }
        else if (v == findViewById(R.id.octalClipBoard5)) {
            copyText = (String)octalText.getText();
            viewName = "Octal";
        }
        else {
            copyText = (String)hexText.getText();
            viewName = "Hex";
        }
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label",copyText);
        clipboard.setPrimaryClip(clip);

        Context context = getApplicationContext();
        CharSequence text = viewName + " copied to clipboard";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.menuBinary) {
            intent = new Intent(this, Binary.class);
        }
        else if (id == R.id.menuOctal) {
            intent = new Intent(this, Octal.class);
        }
        else if (id == R.id.menuHex) {
            intent = new Intent(this, Hex.class);
        }
        else if (id == R.id.menuDecimal){
            intent = new Intent(this, Decimal.class);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private class DoConversions extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... values) {
            BigInteger bigInt = new BigInteger(String.valueOf(values[0]));
            String binary = Convert.decimalToBinary(bigInt) + " ";
            String octal = Convert.decimalToOctal(bigInt) + " ";
            String hex = Convert.decimalToHex(bigInt) + " ";

            String[] result = {binary, octal, hex};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            octalResult += result[1];
            binaryResult += result[0];
            hexResult += result[2];
            octalText.setText(octalResult);
            binaryText.setText(binaryResult);
            hexText.setText(hexResult);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }
    }
}
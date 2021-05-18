package com.geekproduction.binaryconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class ConvertString extends AppCompatActivity {
    private TextView octalText;
    private TextView binaryText;

    String octalResult;
    String binaryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_string);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("String");

        TextView decimalText = (TextView)findViewById(R.id.decimalTextView5);
        octalText = (TextView)findViewById(R.id.octalTextView5);
        binaryText = (TextView)findViewById(R.id.binaryTextView5);



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

                //binaryText.setText("");
                //octalText.setText("");
                BigInteger bigInt;
                int number = 0;
                for (int i = 0; i < edit.getText().length(); i++) {
                    char letter = edit.getText().charAt(i);
                    number = letter;
                    decimalResult += number + " ";

                    bigInt = new BigInteger(String.valueOf(number));
                    //binaryResult += Convert.decimalToBinary(bigInt) + " ";
                    //octalResult += Convert.decimalToOctal(bigInt) + " ";
                    String value = String.valueOf(number);
                    new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, value);
                    //new DoConversions().execute(value);
                }
                decimalText.setText(decimalResult);
                binaryText.setText(binaryResult);
                octalText.setText(octalResult);
                //binaryResult = "";
                //octalResult = "";

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

            //octal += values[2];
            //binary += values[1];

            String[] result = {binary, octal};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            octalResult += result[1];
            binaryResult += result[0];
            octalText.setText(octalResult);
            binaryText.setText(binaryResult);

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
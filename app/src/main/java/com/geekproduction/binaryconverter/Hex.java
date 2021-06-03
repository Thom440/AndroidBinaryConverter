package com.geekproduction.binaryconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;

public class Hex extends AppCompatActivity {

    private TextView hexText;
    private TextView decimalText;
    private TextView binaryText;
    private TextView octalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("Hex");

        hexText = findViewById(R.id.hexTextView4);
        decimalText = findViewById(R.id.decimalTextView4);
        binaryText = findViewById(R.id.binaryTextView4);
        octalText = findViewById(R.id.octalTextView4);
    }

    public void onClick(View v) {
        String hexTextValue = hexText.getText().toString();
        if (((Button)v).getText().equals("")) {
            if (!hexTextValue.equals("")) {
                hexTextValue = hexTextValue.substring(0, hexTextValue.length() - 1);
            }
        }
        else {
            hexTextValue += ((Button)v).getText().toString();
        }
        hexText.setText(hexTextValue);
        if (!hexTextValue.equals("")) {
            new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hexTextValue);
        }
        else {
            decimalText.setText("");
            octalText.setText("");
            hexText.setText("");
        }
    }

    public void clearFields(View v) {
        hexText.setText("");
        decimalText.setText("");
        binaryText.setText("");
        octalText.setText("");
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
        if (id == R.id.menuDecimal) {
            intent = new Intent(this, Decimal.class);
        }
        else if (id == R.id.menuBinary) {
            intent = new Intent(this, Binary.class);
        }
        else if (id == R.id.menuOctal) {
            intent = new Intent(this, Octal.class);
        }
        else if (id == R.id.menuString) {
            intent = new Intent(this, ConvertString.class);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private class DoConversions extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... hex) {
            String decimal = Convert.hexToDecimal(hex[0]);
            BigInteger decimalBigInt = new BigInteger(decimal);
            String binary = Convert.decimalToBinary(decimalBigInt);
            String octal = Convert.decimalToOctal(decimalBigInt);

            String[] result = {decimal, binary, octal};
            return result;
        }

        @Override
        protected void onPostExecute(String... result) {
            octalText.setText(result[2]);
            binaryText.setText(result[1]);
            decimalText.setText(result[0]);
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
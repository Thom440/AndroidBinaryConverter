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

public class Octal extends AppCompatActivity {

    private TextView octalText;
    private TextView decimalText;
    private TextView binaryText;
    private TextView hexText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octal);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("Octal Conversion");

        octalText = findViewById(R.id.octalTextView3);
        decimalText = findViewById(R.id.decimalTextView3);
        binaryText = findViewById(R.id.binaryTextView3);
        hexText = findViewById(R.id.hexTextView3);
    }

    public void onClick(View v) {
        String octalTextValue = octalText.getText().toString();
        if (((Button)v).getText().equals("")) {
            if (!octalText.getText().equals("")) {
                octalTextValue = octalTextValue.substring(0, octalTextValue.length() - 1);
            }
        }
        else {
            octalTextValue += ((Button)v).getText();
        }
        octalText.setText(octalTextValue);
        if (Validator.validBigInteger(octalTextValue)) {
            BigInteger bigInt = new BigInteger(octalTextValue);
            new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigInt);
        }
        else {
            decimalText.setText("");
            binaryText.setText("");
            hexText.setText("");
        }
    }

    public void clearFields(View v) {
        octalText.setText("");
        decimalText.setText("");
        binaryText.setText("");
        hexText.setText("");
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
        else if (id == R.id.menuHex) {
            intent = new Intent(this, Hex.class);
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

    private class DoConversions extends AsyncTask<BigInteger, Void, String[]> {
        @Override
        protected String[] doInBackground(BigInteger... bigInt) {
            String decimal = Convert.octalToDecimal(bigInt[0]);
            BigInteger decimalBigInt = new BigInteger(decimal);
            String hex = Convert.decimalToHex(decimalBigInt);
            String binary = Convert.decimalToBinary(decimalBigInt);

            String[] result = {binary, hex, decimal};
            return result;
        }

        @Override
        protected void onPostExecute(String... result) {
            binaryText.setText(result[0]);
            hexText.setText(result[1]);
            decimalText.setText(result[2]);
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
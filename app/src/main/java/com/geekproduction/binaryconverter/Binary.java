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

public class Binary extends AppCompatActivity {
    private TextView decimalText;
    private TextView hexText;
    private TextView octalText;
    private TextView binaryText;
    private TextView complementText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("Binary Conversion");

        decimalText = (TextView)findViewById(R.id.decimalTextView2);
        decimalText.setText("");

        binaryText = (TextView)findViewById(R.id.binaryTextView2);
        binaryText.setText("");

        octalText = (TextView)findViewById(R.id.octalTextView2);
        octalText.setText("");

        hexText = (TextView)findViewById(R.id.hexTextView2);
        hexText.setText("");

        complementText = (TextView)findViewById(R.id.complimentView);
        complementText.setText("");
    }

    public void onClick(View v) {
        String binaryTextValue = (String) binaryText.getText();
        if (((Button)v).getText().equals("")) {
            if (!(binaryText.getText().equals(""))) {
                binaryTextValue = binaryTextValue.substring(0, binaryTextValue.length() - 1);
            }
        }
        else {
            binaryTextValue += (String)((Button)v).getText();
        }
        binaryText.setText(binaryTextValue);
        if (Validator.validBigInteger(binaryTextValue)) {
            BigInteger bigInt = new BigInteger(String.valueOf(binaryTextValue));
            new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigInt);
        }
        else {
            decimalText.setText("");
            octalText.setText("");
            hexText.setText("");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("MyBoolean", true);
        outState.putString("Binary", (String)binaryText.getText());
        outState.putString("Decimal", (String)decimalText.getText());
        outState.putString("Octal", (String)octalText.getText());
        outState.putString("Hex", (String)hexText.getText());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        binaryText.setText(savedInstanceState.getString("Binary"));
        decimalText.setText(savedInstanceState.getString("Decimal"));
        octalText.setText(savedInstanceState.getString("Octal"));
        hexText.setText(savedInstanceState.getString("Hex"));
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
        else if (id == R.id.menuOctal) {
            intent = new Intent(this, Octal.class);
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
            String decimal = Convert.binaryToDecimal(bigInt[0]);
            String twosComplement = Convert.findTwosComplement(decimal);
            BigInteger decimalBigInt = new BigInteger(decimal);
            String octal = Convert.decimalToOctal(decimalBigInt);
            String hex = Convert.decimalToHex(decimalBigInt);

            String[] result = {octal, hex, decimal, twosComplement};
            return result;
        }

        @Override
        protected void onPostExecute(String... result) {
            octalText.setText(result[0]);
            hexText.setText(result[1]);
            decimalText.setText(result[2]);
            complementText.setText(result[3]);
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
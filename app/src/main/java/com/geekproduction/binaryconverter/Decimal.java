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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class Decimal extends AppCompatActivity {
    private TextView decimalText;
    private TextView binaryText;
    private TextView octalText;
    private TextView hexText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decimal);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("Decimal");

        decimalText = (TextView)findViewById(R.id.decimalTextView);
        decimalText.setText("");

        binaryText = (TextView)findViewById(R.id.binaryTextView);
        binaryText.setText("");

        octalText = (TextView)findViewById(R.id.octalTextView);
        octalText.setText("");

        hexText = (TextView)findViewById(R.id.hexTextView);
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
        if (id == R.id.menuBinary) {
            intent = new Intent(this, Binary.class);
        }
        else if (id == R.id.menuOctal) {
            intent = new Intent(this, Octal.class);
        }
        else if (id == R.id.menuHex) {
            intent = new Intent(this, Hex.class);
        }
        else if (id == R.id.menuString){
            intent = new Intent(this, ConvertString.class);
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        String decimalTextValue = (String) decimalText.getText();
        if (((Button)v).getText().equals("-")) {
            if (decimalTextValue.equals("")) {
                decimalTextValue = (String)((Button)v).getText();
                decimalText.setText(decimalTextValue);
                return;
            }
        }
        else if (((Button)v).getText().equals("")) {
            if (!(decimalText.getText().equals(""))) {
                decimalTextValue = decimalTextValue.substring(0, decimalTextValue.length() - 1);
            }
        }
        else {
            decimalTextValue += (String)((Button)v).getText();
        }
        decimalText.setText(decimalTextValue);
        if (Validator.validBigInteger(decimalTextValue)) {
            BigInteger bigInt = new BigInteger(decimalTextValue);
            if (bigInt.compareTo(BigInteger.ZERO) < 0 && Validator.validShort(decimalTextValue)) {
                int value = (Math.abs((int)Short.MIN_VALUE) + Short.MAX_VALUE) + Short.parseShort(decimalTextValue) + 1;
                bigInt = new BigInteger(String.valueOf(value));
            }
            else if (bigInt.compareTo(BigInteger.ZERO) < 0 && Validator.validInt(decimalTextValue)) {
                long value = (Math.abs((long)Integer.MIN_VALUE) + Integer.MAX_VALUE + Integer.parseInt(decimalTextValue) + 1);
                bigInt = new BigInteger(String.valueOf(value));
            }
            else if (bigInt.compareTo(BigInteger.ZERO) < 0 && Validator.validLong(decimalTextValue)) {
                BigInteger negativeValue = new BigInteger(String.valueOf(Long.MIN_VALUE));
                negativeValue = negativeValue.abs();
                bigInt = bigInt.add(negativeValue);
                bigInt = bigInt.add(BigInteger.valueOf(Long.MAX_VALUE));
                bigInt = bigInt.add(BigInteger.valueOf(Long.parseLong(decimalTextValue)));
                bigInt = bigInt.add(BigInteger.ONE);
            }
            else {
                bigInt = bigInt.abs();
            }
            new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigInt);
        }
        else {
            octalText.setText("");
            binaryText.setText("");
            hexText.setText("");
        }
    }

    public void clearFields(View v) {
        decimalText.setText("");
        binaryText.setText("");
        octalText.setText("");
        hexText.setText("");
    }

    public void copyToClipBoard(View v) {
        String copyText = "";
        String viewName = "";
        if ((Button)v == findViewById(R.id.decimalClipBoard)) {
            copyText = (String)decimalText.getText();
            viewName = "Decimal";
        }
        else if (v == findViewById(R.id.binaryClipBoard)) {
            copyText = (String)binaryText.getText();
            viewName = "Binary";
        }
        else if (v == findViewById(R.id.octalClipBoard)) {
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

    private class DoConversions extends AsyncTask<BigInteger, Void, String[]> {
        @Override
        protected String[] doInBackground(BigInteger... bigInt) {
            String binary = Convert.decimalToBinary(bigInt[0]);
            String octal = Convert.decimalToOctal(bigInt[0]);
            String hex = Convert.decimalToHex(bigInt[0]);

            String[] result = {binary, octal, hex};
            return result;
        }

        @Override
        protected void onPostExecute(String... result) {
            octalText.setText(result[1]);
            binaryText.setText(result[0]);
            hexText.setText(result[2]);
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
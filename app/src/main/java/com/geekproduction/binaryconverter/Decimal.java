package com.geekproduction.binaryconverter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void onClick(View v) {
        String decimalTextValue = (String) decimalText.getText();
        if (((Button)v).getText().equals("-") && decimalTextValue.equals("")) {
            decimalTextValue = (String)((Button)v).getText();
            decimalText.setText(decimalTextValue);
            return;
        }
        else if (((Button)v).getText().equals("")) {
            if (!(decimalText.getText().equals(""))) {
                decimalTextValue = decimalTextValue.substring(0, decimalTextValue.length() - 1);
                decimalText.setText(decimalTextValue);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                doConversions(bigInt);
            }
            else {
                String binary = Convert.decimalToBinary(bigInt);
                String octal = Convert.decimalToOctal(bigInt);
                octalText.setText(octal);
                binaryText.setText(binary);
            }
        }
        else {
            octalText.setText("");
            binaryText.setText("");
            hexText.setText("");
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void doConversions(final BigInteger bigInt) {
        getMainExecutor().execute(() -> {
            String binary = Convert.decimalToBinary(bigInt);

            binaryText.setText(binary);
            getMainExecutor().execute(() -> {
                String octal = Convert.decimalToOctal(bigInt);
                octalText.setText(octal);
            });

        });
    }

    public void copyToClipBoard(View v) {
        String copyText = (String)decimalText.getText();
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label",copyText);
        clipboard.setPrimaryClip(clip);
    }
}
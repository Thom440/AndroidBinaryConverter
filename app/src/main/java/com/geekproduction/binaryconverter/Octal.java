package com.geekproduction.binaryconverter;

import androidx.annotation.NonNull;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Octal extends AppCompatActivity {

    private TextView octalText;
    private TextView decimalText;
    private TextView binaryText;
    private TextView hexText;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octal);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("Octal Conversion");

        octalText = findViewById(R.id.octalTextView3);
        decimalText = findViewById(R.id.decimalTextView3);
        binaryText = findViewById(R.id.binaryTextView3);
        hexText = findViewById(R.id.hexTextView3);

        restoreState();
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

    private void saveState() {
        try {
            File path = getFilesDir();
            File file = new File(path, "Octal.txt");
            try (FileOutputStream output = new FileOutputStream(file)) {
                if (octalText.getText().equals("")) {
                    output.write("".getBytes());
                } else {
                    output.write(octalText.getText().toString().getBytes());
                }
            }
        }
        catch (IOException ex) {
            Context context = getApplicationContext();
            CharSequence text = "Failed to save state";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        }
    }

    @SuppressWarnings("all")
    private void restoreState() {
        File path = getFilesDir();
        File file = new File(path, "Octal.txt");
        if (file.exists()) {
            try {
                int length = (int)file.length();
                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                String octal = new String(bytes);
                if (octal.equals("")) {
                    fillInFields();
                }
                else {
                    octalText.setText(octal);
                    BigInteger bigInt = new BigInteger(octal);
                    new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigInt);
                }
            }
            catch (FileNotFoundException ex) {
                fillInFields();
            }
            catch (IOException ex) {
                fillInFields();
            }
        }
        else {
            fillInFields();
        }
    }

    public void copyToClipBoard(View v) {
        String copyText;
        String viewName;
        if (v == findViewById(R.id.decimalClipBoard3)) {
            copyText = decimalText.getText().toString();
            viewName = "Decimal";
        }
        else if (v == findViewById(R.id.binaryClipBoard3)) {
            copyText = binaryText.getText().toString();
            viewName = "Binary";
        }
        else if (v == findViewById(R.id.octalClipBoard3)) {
            copyText = octalText.getText().toString();
            viewName = "Octal";
        }
        else {
            copyText = hexText.getText().toString();
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

    private void fillInFields() {
        octalText.setText("");
        decimalText.setText("");
        binaryText.setText("");
        hexText.setText("");
    }

    public void clearFields(View v) {
        octalText.setText("");
        decimalText.setText("");
        binaryText.setText("");
        hexText.setText("");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
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

    @SuppressWarnings("all")
    private class DoConversions extends AsyncTask<BigInteger, Void, String[]> {
        @Override
        protected String[] doInBackground(BigInteger... bigInt) {
            String decimal = Convert.octalToDecimal(bigInt[0]);
            BigInteger decimalBigInt = new BigInteger(decimal);
            String hex = Convert.decimalToHex(decimalBigInt);
            String binary = Convert.decimalToBinary(decimalBigInt);

            return new String[]{binary, hex, decimal};
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
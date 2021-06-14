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
import java.math.BigDecimal;
import java.math.BigInteger;

public class Hex extends AppCompatActivity {

    private TextView hexText;
    private TextView decimalText;
    private TextView binaryText;
    private TextView octalText;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        Toolbar myToolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("Hex Conversion");

        hexText = findViewById(R.id.hexTextView4);
        decimalText = findViewById(R.id.decimalTextView4);
        binaryText = findViewById(R.id.binaryTextView4);
        octalText = findViewById(R.id.octalTextView4);

        restoreState();
    }

    private void saveState() {
        try {
            File path = getFilesDir();
            File file = new File(path, "Hex.txt");
            try (FileOutputStream output = new FileOutputStream(file)) {
                if (hexText.getText().equals("")) {
                    output.write("".getBytes());
                } else {
                    output.write(hexText.getText().toString().getBytes());
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
        File file = new File(path, "Hex.txt");
        if (file.exists()) {
            try {
                int length = (int)file.length();
                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                String hex = new String(bytes);
                if (hex.equals("")) {
                    fillInFields();
                }
                else if (hex.contains(".") && hex.indexOf(".") != hex.length() - 1) {
                    hexText.setText(hex);
                    new DoDecimalConversion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hex);
                }
                else if (!hex.contains(".")) {
                    hexText.setText(hex);
                    new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hex);
                }
                else {
                    fillInFields();
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

    private void fillInFields() {
        octalText.setText("");
        decimalText.setText("");
        binaryText.setText("");
        hexText.setText("");
    }

    public void copyToClipBoard(View v) {
        String copyText;
        String viewName;
        if (v == findViewById(R.id.decimalClipBoard4)) {
            copyText = decimalText.getText().toString();
            viewName = "Decimal";
        }
        else if (v == findViewById(R.id.binaryClipBoard4)) {
            copyText = binaryText.getText().toString();
            viewName = "Binary";
        }
        else if (v == findViewById(R.id.octalClipBoard4)) {
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

    public void onClick(View v) {
        String hexTextValue = hexText.getText().toString();
        if (((Button)v).getText().equals("")) {
            if (!hexTextValue.equals("")) {
                hexTextValue = hexTextValue.substring(0, hexTextValue.length() - 1);
            }
        }
        else if (((Button)v).getText().equals(".")) {
            if (hexTextValue.equals("")) {
                hexText.setText("0.");
                return;
            }
            else if (hexTextValue.contains(".")) {
                return;
            }
            else {
                hexTextValue += ((Button)v).getText().toString();
                hexText.setText(hexTextValue);
                return;
            }
        }
        else {
            hexTextValue += ((Button)v).getText().toString();
        }
        hexText.setText(hexTextValue);
        if (!hexTextValue.equals("")) {
            if (hexTextValue.contains(".") && hexTextValue.indexOf(".") != hexTextValue.length() - 1) {
                new DoDecimalConversion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hexTextValue);
            }
            else {
                new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, hexTextValue);
            }
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

    @SuppressWarnings("all")
    private class DoConversions extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... hex) {
            String decimal = Convert.hexToDecimal(hex[0]);
            BigInteger decimalBigInt = new BigInteger(decimal);
            String binary = Convert.decimalToBinary(decimalBigInt);
            String octal = Convert.decimalToOctal(decimalBigInt);

            return new String[]{decimal, binary, octal};
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

    private class DoDecimalConversion extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... strings) {
            String decimal = Convert.hexDecimalPointToDecimal(strings[0]);
            String binary = Convert.decimalPointToBinary(new BigDecimal(decimal));
            String octal = Convert.decimalPointToOctal(new BigDecimal(decimal));
            return new String[]{decimal, binary, octal};
        }

        @Override
        protected void onPostExecute(String[] strings) {
            decimalText.setText(strings[0]);
            binaryText.setText(strings[1]);
            octalText.setText(strings[2]);
        }
    }
}
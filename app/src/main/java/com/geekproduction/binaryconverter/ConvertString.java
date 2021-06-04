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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class ConvertString extends AppCompatActivity {
    private TextView octalText;
    private TextView binaryText;
    private TextView hexText;
    private TextView decimalText;
    private EditText edit;

    String octalResult;
    String binaryResult;
    String hexResult;

    @Override
    @SuppressWarnings("all")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_string);

        Toolbar myToolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(myToolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.menu_icon));
        getSupportActionBar().setTitle("String");

        decimalText = findViewById(R.id.decimalTextView5);
        octalText = findViewById(R.id.octalTextView5);
        binaryText = findViewById(R.id.binaryTextView5);
        hexText = findViewById(R.id.hexTextView5);
        decimalText.setText("");
        octalText.setText("");
        binaryText.setText("");
        hexText.setText("");

        edit = findViewById(R.id.convertString);
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
        restoreState();
    }

    private void saveState() {
        try {
            File path = getFilesDir();
            File file = new File(path, "String.txt");
            try (FileOutputStream output = new FileOutputStream(file)) {
                if (edit.getText().toString().equals("")) {
                    output.write("".getBytes());
                } else {
                    output.write(edit.getText().toString().getBytes());
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
        File file = new File(path, "String.txt");
        if (file.exists()) {
            try {
                int length = (int)file.length();
                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                String string = new String(bytes);
                if (string.equals("")) {
                    return;
                }
                else {
                    edit.setText(string);
                }
            }
            catch (FileNotFoundException ex) {
                return;
            }
            catch (IOException ex) {
                return;
            }
        }
    }

    public void clearFields(View v) {
        EditText edit = findViewById(R.id.convertString);
        edit.setText("");
    }

    public void copyToClipBoard(View v) {
        String copyText;
        String viewName;
        if (v == findViewById(R.id.decimalClipBoard5)) {
            copyText = decimalText.getText().toString();
            viewName = "Decimal";
        }
        else if (v == findViewById(R.id.binaryClipBoard5)) {
            copyText = binaryText.getText().toString();
            viewName = "Binary";
        }
        else if (v == findViewById(R.id.octalClipBoard5)) {
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

    @SuppressWarnings("all")
    private class DoConversions extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... values) {
            BigInteger bigInt = new BigInteger(String.valueOf(values[0]));
            String binary = Convert.decimalToBinary(bigInt) + " ";
            String octal = Convert.decimalToOctal(bigInt) + " ";
            String hex = Convert.decimalToHex(bigInt) + " ";

            return new String[]{binary, octal, hex};
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
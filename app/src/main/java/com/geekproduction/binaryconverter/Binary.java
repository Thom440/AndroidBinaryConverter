package com.geekproduction.binaryconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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

        binaryText = (TextView)findViewById(R.id.binaryTextView2);

        octalText = (TextView)findViewById(R.id.octalTextView2);

        hexText = (TextView)findViewById(R.id.hexTextView2);

        complementText = (TextView)findViewById(R.id.complimentView);

        restoreState();
    }

    private void saveState() {
        try {
            File path = getFilesDir();
            File file = new File(path, "Binary.txt");
            try (FileOutputStream output = new FileOutputStream(file)) {
                if (binaryText.getText().equals("")) {
                    output.write("".getBytes());
                } else {
                    output.write(binaryText.getText().toString().getBytes());
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

    private void restoreState() {
        File path = getFilesDir();
        File file = new File(path, "Binary.txt");
        if (file.exists()) {
            try {
                int length = (int)file.length();
                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                String binary = new String(bytes);
                if (binary.equals("")) {
                    fillInFields();
                }
                else {
                    binaryText.setText(binary);
                    BigInteger bigInt = new BigInteger(binary);
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

    private void fillInFields() {
        decimalText.setText("");
        binaryText.setText("");
        octalText.setText("");
        hexText.setText("");
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
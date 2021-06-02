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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
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

        binaryText = (TextView)findViewById(R.id.binaryTextView);

        octalText = (TextView)findViewById(R.id.octalTextView);

        hexText = (TextView)findViewById(R.id.hexTextView);

        restoreState();
    }

    private void restoreState() {
        File path = getFilesDir();
        File file = new File(path, "Decimal.txt");
        if (file.exists()) {
            try {
                int length = (int)file.length();
                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(file);
                in.read(bytes);
                String decimal = new String(bytes);
                if (decimal.equals("") || decimal.equals("-") || decimal.substring(decimal.length()).equals(".")) {
                    fillInFields();
                }
                else if (decimal.contains(".")) {
                    decimalText.setText(decimal);
                    BigDecimal bigDecimal = new BigDecimal(decimal);
                    new DoDecimalPointConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigDecimal);
                }
                else {
                    decimalText.setText(decimal);
                    BigInteger bigInt = getBigInteger(decimal);
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
                decimalTextValue = ((Button)v).getText().toString();
                decimalText.setText(decimalTextValue);
                return;
            }
        }
        else if (((Button)v).getText().equals(".")) {
            if (decimalTextValue.equals("")) {
                decimalText.setText("0.");
                return;
            }
            else if (decimalTextValue.contains("-") || decimalTextValue.contains(".")) {
                return;
            }
            else {
                decimalTextValue += ((Button)v).getText().toString();
                decimalText.setText(decimalTextValue);
                return;
            }
        }
        else if (((Button)v).getText().equals("")) {
            if (!(decimalText.getText().equals(""))) {
                decimalTextValue = decimalTextValue.substring(0, decimalTextValue.length() - 1);
                if (decimalTextValue.indexOf(".") == decimalTextValue.length() - 1) {
                    decimalText.setText(decimalTextValue);
                    return;
                }
            }
        }
        else {
            decimalTextValue += ((Button)v).getText().toString();
        }
        decimalText.setText(decimalTextValue);
        // Checking to see if the input is a valid Big Decimal
        if (Validator.validBigDecimal(decimalTextValue)) {
            BigDecimal bigDecimal = new BigDecimal(decimalTextValue);
            new DoDecimalPointConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigDecimal);
        }
        // Checking to see if the input is a valid big integer
        else if (Validator.validBigInteger(decimalTextValue)) {
            BigInteger bigInt = getBigInteger(decimalTextValue);
            new DoConversions().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bigInt);
        }
        // If the number is not valid then set all of the fields to empty strings
        else {
            octalText.setText("");
            binaryText.setText("");
            hexText.setText("");
        }
    }

    private BigInteger getBigInteger(String decimalTextValue) {
        BigInteger bigInt = new BigInteger(decimalTextValue);
        // Checking to see if the number is negative and is a valid short
        if (bigInt.compareTo(BigInteger.ZERO) < 0 && Validator.validShort(decimalTextValue)) {
            int value = (Math.abs((int)Short.MIN_VALUE) + Short.MAX_VALUE) + Short.parseShort(decimalTextValue) + 1;
            bigInt = new BigInteger(String.valueOf(value));
        }
        // Checking to see if the number is negative and a valid integer
        else if (bigInt.compareTo(BigInteger.ZERO) < 0 && Validator.validInt(decimalTextValue)) {
            long value = (Math.abs((long)Integer.MIN_VALUE) + Integer.MAX_VALUE + Integer.parseInt(decimalTextValue) + 1);
            bigInt = new BigInteger(String.valueOf(value));
        }
        // Checking to see if the number is negative and a valid long
        else if (bigInt.compareTo(BigInteger.ZERO) < 0 && Validator.validLong(decimalTextValue)) {
            BigInteger negativeValue = new BigInteger(String.valueOf(Long.MIN_VALUE));
            negativeValue = negativeValue.abs();
            bigInt = negativeValue
                    .add(BigInteger.valueOf(Long.MAX_VALUE))
                    .add(BigInteger.valueOf(Long.parseLong(decimalTextValue)))
                    .add(BigInteger.ONE);
        }
        else {
            // If the number is not any of the valid types just convert it to the positive equivalent
            bigInt = bigInt.abs();
        }
        return bigInt;
    }

    private void saveState() {
        try {
            File path = getFilesDir();
            File file = new File(path, "Decimal.txt");
            try (FileOutputStream output = new FileOutputStream(file)) {
                if (decimalText.getText().equals("")) {
                    output.write("".getBytes());
                } else {
                    output.write(decimalText.getText().toString().getBytes());
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

            return new String[]{binary, octal, hex};
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

    private class DoDecimalPointConversions extends AsyncTask<BigDecimal, Void, String[]> {
        @Override
        protected String[] doInBackground(BigDecimal... bigDecimal) {
            String binary = Convert.decimalPointToBinary(bigDecimal[0]);
            String octal = Convert.decimalPointToOctal(bigDecimal[0]);
            String hex = Convert.decimalPointToHex(bigDecimal[0]);
            return new String[]{binary, octal, hex};
        }

        @Override
        protected void onPostExecute(String... result) {
            binaryText.setText(result[0]);
            octalText.setText(result[1]);
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
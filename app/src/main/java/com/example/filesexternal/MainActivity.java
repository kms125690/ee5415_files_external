package com.example.filesexternal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText mEditText;
    TextView mTextView;
    static final int READ_BLOCK_SIZE = 100;

    private static final int REQUEST_CODE = 1;
    public static String[] storge_permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, storge_permissions, REQUEST_CODE);
        mEditText = (EditText) findViewById(R.id.editText1);
        mTextView = (TextView) findViewById(R.id.tv_dir);
    }

    public void onClickSave(View v) {
        String str = mEditText.getText().toString();
        try {
            //--- SD Card Storage ---
            String sdCard = this.getExternalFilesDir(null).getAbsolutePath();
            File directory = new File (sdCard + "/FilesExternal");
            mTextView.setText(sdCard + "/FilesExternal");
            directory.mkdirs();
            File file = new File(directory, "textfile.txt");
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // --- Write the string to the file ---
            osw.write(str);
            osw.flush();
            osw.close();
            // --- Display file saved message ---
            Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_LONG).show();
            //--- Clears the EditText ---
            mEditText.setText("");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void onClickLoad(View v) {
        try {
            //--- SD Storage ---
            String sdCard = this.getExternalFilesDir(null).getAbsolutePath();
            File directory = new File (sdCard + "/FilesExternal");
            File file = new File(directory, "textfile.txt");
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = "";
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                //--- Convert the chars to a String ---
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
                inputBuffer = new char[READ_BLOCK_SIZE];
            }
            //--- Set the EditText to the text that has been read ---
            mEditText.setText(s);
            Toast.makeText(getBaseContext(), "File loaded successfully!", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
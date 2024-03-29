package com.pina.mescoe_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdf_reader extends AppCompatActivity {

    private TextView textView;
    private PDFView pdfView;
    private  FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("url");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        textView = findViewById(R.id.textView);
        pdfView = findViewById(R.id.pdfView);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                textView.setText(value);
                Toast.makeText(getApplicationContext(), "Loaded Successfully", Toast.LENGTH_SHORT).show();
                String url = textView.getText().toString();
                new RetrivePdfStream().execute(url);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(pdf_reader.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });}

        @SuppressLint("StaticFieldLeak")
        class RetrivePdfStream extends AsyncTask<String,Void, InputStream>{

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream = null;
                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                    if (urlConnection.getResponseCode()==200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    }
                }catch (IOException e){
                    return null ;
                }
                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).load();
            }
        }
    }


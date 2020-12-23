package com.pina.mescoe_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pina.mescoe_admin.noticefeedview.FeedActivity;

import java.io.File;

public class Notice extends AppCompatActivity {

    private EditText pdfTitle;
    private final int REQ = 1;
    private Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private TextView pdftextview;
    private String pdfName,title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        pdfTitle = findViewById(R.id.pdfTitle);
        pdftextview = findViewById(R.id.pdftextview);
        Button noticefeed = findViewById(R.id.noticefeed);

        noticefeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FeedActivity.class));
            }
        });


        findViewById(R.id.choosefile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 selectPDF();
            }
        });
        findViewById(R.id.uploadnotice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                } else if (pdfData == null) {
                    Toast.makeText(Notice.this, "Please upload pdf", Toast.LENGTH_SHORT).show();
                }else {
                    uploadPDFfile();
                }
            }

        });
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("pdf");
        }

    private void uploadPDFfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();


        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis());
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri uri1 = uri.getResult();

                        uploadPDF uploadPDF = new uploadPDF(pdfTitle.getText().toString(), uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
                        Toast.makeText(Notice.this, "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        pdfTitle.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Notice.this, "Failed to upload selected file", Toast.LENGTH_SHORT).show();
            }
        });

//        pd = new ProgressDialog(this);
//        pdfTitle = findViewById(R.id.pdfTitle);
//
//        findViewById(R.id.choosefile).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openGallery();
//            }
//        });
//
//        findViewById(R.id.uploadnotice).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                title = pdfTitle.getText().toString();
//                if(title.isEmpty()){
//                    pdfTitle.setError("Empty");
//                    pdfTitle.requestFocus();
//                } else if (pdfData == null) {
//                    Toast.makeText(Notice.this, "Please upload pdf", Toast.LENGTH_SHORT).show();
//                }else {
//                    uploadPdf();
//                }
//            }
//        });
//        findViewById(R.id.noticefeed).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), FeedActivity.class));
//            }
//        });
//
//
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//    }
//
//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("pdf/docs/ppt");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"), REQ);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQ && resultCode == RESULT_OK && data != null && data.getData() != null){
//            pdfData = data.getData();
//
//            if(pdfData.toString().startsWith("content://")){
//                Cursor cursor = null;
//                try {
//                    cursor = Notice.this.getContentResolver().query(pdfData,null,null,null,null);
//                    if(cursor != null && cursor.moveToFirst()){
//                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else if (pdfData.toString().startsWith("file://")) {
//                pdfName = new File(pdfData.toString()).getName();
//            }
//            pdftextview.setText(pdfName);
//        }
//    }
//
//    private void uploadPdf() {
//        pd.setTitle("Please wait...");
//        pd.setMessage("Uploading pdf");
//        pd.show();
//        StorageReference reference = storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
//        reference.putFile(pdfData)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isComplete());
//                        Uri uri = uriTask.getResult();
//                        uploadData(String.valueOf(uri));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                Toast.makeText(Notice.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void uploadData(String downloadUrl) {
//        String uniqueKey = databaseReference.child("pdf").push().getKey();
//
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("pdfTitle",title);
//        data.put("pdfUrl",downloadUrl);
//
//        databaseReference.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                pd.dismiss();
//                Toast.makeText(Notice.this, "Pdf uploaded successfully", Toast.LENGTH_SHORT).show();
//                pdfTitle.setText("");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                Toast.makeText(Notice.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("pdf/docs/ppt");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file"),REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK  && data != null && data.getData() != null){
            pdfData = data.getData();
            if(pdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = Notice.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (pdfData.toString().startsWith("file://")) {
                pdfName = new File(pdfData.toString()).getName();
            }
            pdftextview.setText(pdfName);
        }
    }

}
package com.pina.mescoe_admin.TimetableImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.pina.mescoe_admin.pdf_reader;
import com.pina.mescoe_admin.R;
import com.pina.mescoe_admin.uploadPDF;

import java.io.File;
import java.util.Objects;

public class timetable_uploader extends AppCompatActivity {


//    private ImageView noticeImageView;

    private final int REQ = 1;
//    private Bitmap bitmap;
//    private EditText imageTitle;
    private Button comp1,comp2,compss;
    private EditText pdfTitle;
    private Uri pdfData;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private TextView pdftextview,checkimg;
    private String pdfName,title;



//    private DatabaseReference reference, dbRef;
//    private StorageReference storageReference;
//    private ImageView imageView;
//    String downloadUrl = "";
//    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_uploader_activity);
        comp1 = findViewById(R.id.comp1);
        comp2 = findViewById(R.id.comp2);
        compss = findViewById(R.id.compss);
        pdfTitle = findViewById(R.id.pdfTitle);
        pdftextview = findViewById(R.id.pdftextview);
        checkimg=findViewById(R.id.checkimg);
        CardView addpdf = findViewById(R.id.addpdf);

        checkimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), pdf_reader.class));
            }
        });

       addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });
        comp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("required");
                    pdfTitle.requestFocus();
                } else if (pdfData == null) {
                    Toast.makeText(getApplicationContext(), "Please select pdf file", Toast.LENGTH_SHORT).show();
                }else {
                    uploadPDFfile();
                }
            }

        });
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("SE COMP 1");
    }

    private void uploadPDFfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();


        StorageReference reference = storageReference.child("SE COMP 1/"+pdfName+"-"+System.currentTimeMillis());
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri uri1 = uri.getResult();

                        uploadPDF uploadPDF = new uploadPDF(pdfTitle.getText().toString(), uri.toString());
                        databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(uploadPDF);
                        Toast.makeText(getApplicationContext(), "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        pdftextview.setText("✔");
                        pdfTitle.setText(null);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(timetable_uploader.this, "Failed to upload selected file", Toast.LENGTH_SHORT).show();
            }
        });


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
                    cursor = timetable_uploader.this.getContentResolver().query(pdfData,null,null,null,null);
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


//        addImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openGallery();
//            }
//        });
//        comp1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(imageTitle.getText().toString().isEmpty()){
//                    imageTitle.setError("Empty");
//                    imageTitle.requestFocus();
//                }else if(bitmap == null){
//                    uploadData();
//                }else {
//                    uploadImage();
//                }
//            }
//        });
//    }
//
//    private void uploadImage() {
//        pd.setMessage("Uploading...");
//        pd.show();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50 ,baos);
//        byte[] finalimg = baos.toByteArray();
//        final StorageReference filePath;
//        filePath = storageReference.child("Image").child(Arrays.toString(finalimg) +"jpg");
//        final UploadTask uploadTask = filePath.putBytes(finalimg);
//        uploadTask.addOnCompleteListener(timetable_uploader.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    downloadUrl = String.valueOf(uri);
//                                    uploadData();
//                                }
//                            });
//                        }
//                    });
//                }else {
//                    pd.dismiss();
//                    Toast.makeText(timetable_uploader.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void uploadData() {
//        dbRef = reference.child("Image");
//        final String uniqueKey = dbRef.push().getKey();
//
//        String title = imageTitle.getText().toString();
//
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-mm-yy");
//        String date = currentDate.format(calForDate.getTime());
//
//        Calendar calForTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
//        String time = currentTime.format(calForTime.getTime());
//
//        ImgData imgData = new ImgData(title,downloadUrl,date,time,uniqueKey);
//
//        assert uniqueKey != null;
//        dbRef.child(uniqueKey).setValue(imgData).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                pd.dismiss();
//                Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//    private void openGallery() {
//        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickImage,REQ);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQ && resultCode == RESULT_OK){
//            assert data != null;
//            Uri uri = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            imageView.setImageBitmap(bitmap);
//        }
//    }
}

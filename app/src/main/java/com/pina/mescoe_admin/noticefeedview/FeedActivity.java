package com.pina.mescoe_admin.noticefeedview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pina.mescoe_admin.R;
import com.pina.mescoe_admin.uploadPDF;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView feedRecycler;
    private DatabaseReference reference;
    private List<uploadPDF> list;
    private PdfAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecycler = findViewById(R.id.feedRecyclerview);
        reference = FirebaseDatabase.getInstance().getReference().child("pdf");

        getData();

    }
    

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                 for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    uploadPDF data = snapshot.getValue(uploadPDF.class);
                    list.add(data);
                }
                adapter = new PdfAdapter(FeedActivity.this,list);
                feedRecycler.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
                feedRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeText(FeedActivity.this, databaseError.getMessage(), LENGTH_SHORT).show();
            }
        });
    }

}
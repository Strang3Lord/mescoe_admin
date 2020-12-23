package com.pina.mescoe_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pina.mescoe_admin.TimetableImage.timetable_uploader;

public class Dashboard extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private FirebaseAuth firebaseAuth;
    private ImageView popup_opener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        popup_opener = findViewById(R.id.popup_opener);

//        findViewById(R.id.gotouploadtimetable).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), timetable_uploader.class));
//            }
//        });

        findViewById(R.id.notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Notice.class));
            }
        });
        findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }




    public void signOut(){
        firebaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.se:
                startActivity(new Intent(getApplicationContext(),timetable_uploader.class));
                return true;
            case R.id.te:
                startActivity(new Intent(getApplicationContext(),timetable_uploader.class));
                return true;
            case R.id.be:
                startActivity(new Intent(getApplicationContext(),timetable_uploader.class));
                return true;

        }
        return false;
    }

}


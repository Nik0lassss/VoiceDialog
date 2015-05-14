package com.example.user.voicedialog;




import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.example.user.voicedialog.fragments.VoiceDialogFragment;


public class VoiceDialogActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerMain, new VoiceDialogFragment()).commit();
        }
    }

}
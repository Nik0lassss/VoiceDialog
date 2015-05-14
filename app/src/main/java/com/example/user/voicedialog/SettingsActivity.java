package com.example.user.voicedialog;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Bunis on 08.05.2015.
 */
public class SettingsActivity extends PreferenceActivity{
    public static final String KEY_PREF_SYNC_ADDRESS="edit_text_preference_pref_ip";
    public static final String CHECK_BOX_PREF_VOICE_START_REQUEST= "check_box_preference_voice_start_request";
    public static final String CHECK_BOX_PREF_START_ANSWER_PLAY= "check_box_preferemce_start_answer_play";
    public static final String SAVE_HISTORY_PREFERENCE = "save_history_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting_legacy);
        //Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
        //actionbar.setTitle("Settings");
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        //actionbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //actionbar.setBackgroundColor(getResources().getColor(R.color.voice_dialog_layout_toolbar_color));
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
        root.addView(bar,0);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              SettingsActivity.this.finish();
            }
        });
        addPreferencesFromResource(R.xml.setting);


    }
}

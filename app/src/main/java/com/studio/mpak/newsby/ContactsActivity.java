package com.studio.mpak.newsby;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author Andrei Kuzniatsou
 */
public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(ContactsActivity.this);
    }


    public void vkAction(View view) {
        Uri uri = Uri.parse("https://vk.com/arshanka").buildUpon().build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        startActivity(i);
    }

    public void okAction(View view) {
        Uri uri = Uri.parse("https://ok.ru/arshanskay?st._aid=ExternalGroupWidget_OpenGroup").buildUpon().build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        startActivity(i);
    }

    public void mailAction(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ag41@mail.ru", null));
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}

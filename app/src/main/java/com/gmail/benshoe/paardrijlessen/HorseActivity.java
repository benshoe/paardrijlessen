package com.gmail.benshoe.paardrijlessen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.benshoe.paardrijlessen.db.Horse;

public class HorseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Horse horse = (Horse) intent.getSerializableExtra("horse");
        setContentView(R.layout.activity_horse);

        TextView horseName = (TextView) findViewById(R.id.horse_name);
        horseName.setText(horse.getName());
        TextView horseType = (TextView) findViewById(R.id.horse_type);
        horseType.setText(horse.getHorseType().getName());

        if(horse.getImage() != null) {
            Uri uri = Uri.parse(horse.getImage());
            ImageView horseImage = (ImageView) findViewById(R.id.horse_image);
            horseImage.setImageURI(uri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.horse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

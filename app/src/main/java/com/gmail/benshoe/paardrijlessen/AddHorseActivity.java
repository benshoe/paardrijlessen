package com.gmail.benshoe.paardrijlessen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmail.benshoe.paardrijlessen.util.CameraUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddHorseActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri m_fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horse);

        Spinner spinner = (Spinner) findViewById(R.id.horse_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.horse_types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_horse, menu);
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

    public void save(View view) throws Exception {
        EditText horseNameText = (EditText) findViewById(R.id.horse);
        String horseName = horseNameText.getText().toString();
        if(checkHorseName(horseName)) {
            Spinner horseTypeSpinner = (Spinner) findViewById(R.id.horse_type_spinner);
            String horseType = horseTypeSpinner.getSelectedItem().toString();
            Intent intent = new Intent();
            intent.putExtra("horseName", horseName);
            intent.putExtra("horseType", horseType);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean checkHorseName(String horseName) {
        if(horseName == null || "".equals(horseName.trim())) {
            Toast.makeText(getApplicationContext(), getString(R.string.unique_name_mandatory), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) findViewById(R.id.horse_type_spinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                if(data == null) {
                    Toast.makeText(this, "Er ging iets mis bij het opslaan van de foto.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Image saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();
                }
            } else {
                //User cancelled the image capture
                Toast.makeText(this, "Je wilde toch geen foto maken?\nProbeer het gerust opnieuw als je zin hebt.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Er ging iets fout bij het maken van de foto. \nProbeer het opnieuw.", Toast.LENGTH_LONG).show();
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        m_fileUri = getOutputMediaFileUri(CameraUtil.MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, m_fileUri); // set the image file name
        intent.putExtra("return-data", true);

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /*
    Camera code
     */
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "paardrijlessen");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Toast.makeText(this, "mediaStorageDir: " + mediaStorageDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}

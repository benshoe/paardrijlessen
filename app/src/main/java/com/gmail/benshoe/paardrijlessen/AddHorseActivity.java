package com.gmail.benshoe.paardrijlessen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gmail.benshoe.paardrijlessen.util.CameraUtil;

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

        String horseName = validateHorseName();
        if(horseName == null)
            return;
        Spinner horseTypeSpinner = (Spinner) findViewById(R.id.horse_type_spinner);
        String horseType = horseTypeSpinner.getSelectedItem().toString();
        Intent intent = new Intent();
        intent.putExtra("horseName", horseName);
        intent.putExtra("horseType", horseType);
        intent.putExtra("horseImage", m_fileUri != null ? m_fileUri.toString(): null);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String validateHorseName() {
        EditText horseNameText = (EditText) findViewById(R.id.horse);
        String horseName = horseNameText.getText().toString();
        if(horseName == null || "".equals(horseName.trim())) {
            Toast.makeText(getApplicationContext(), getString(R.string.unique_name_mandatory), Toast.LENGTH_LONG).show();
            return null;
        }
        return horseName;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) findViewById(R.id.horse_type_spinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                Toast.makeText(this, "Image saved to:\n" + m_fileUri.toString(), Toast.LENGTH_LONG).show();
                showImage();
            } else {
                //User cancelled the image capture
                Toast.makeText(this, "Je wilde toch geen foto maken?\nProbeer het gerust opnieuw als je zin hebt.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Er ging iets fout bij het maken van de foto. \nProbeer het opnieuw.", Toast.LENGTH_LONG).show();
        }
    }

    private void showImage() {
        ImageView imageView = (ImageView) findViewById(R.id.horse_image);
        imageView.setImageURI(m_fileUri);
    }

    public void takePicture(View view) {
        String horseName = validateHorseName();
        m_fileUri = CameraUtil.getOutputMediaFileUri(CameraUtil.MEDIA_TYPE_IMAGE, horseName); // create a file to save the image
        if(m_fileUri == null)
            return;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, m_fileUri); // set the image file name
        intent.putExtra("return-data", true);

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
}

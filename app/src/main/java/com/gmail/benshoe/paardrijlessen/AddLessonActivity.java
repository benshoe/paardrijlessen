package com.gmail.benshoe.paardrijlessen;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AddLessonActivity extends Activity {

    private EditText m_lessonDate;
    private int m_year;
    private int m_month;
    private int m_day;

    private Spinner m_horseNameSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        m_lessonDate = (EditText) findViewById(R.id.lesson_date);

        m_horseNameSpinner = (Spinner) findViewById(R.id.horse_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> horseNames = getIntent().getStringArrayListExtra("horses");
        Collections.sort(horseNames);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, horseNames);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        m_horseNameSpinner.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_lesson, menu);
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

    public void save(View view) {
        EditText description = (EditText) findViewById(R.id.lesson_summary);
        EditText lessonGrade = (EditText) findViewById(R.id.lesson_grade);
        EditText lessonGroup = (EditText) findViewById(R.id.lesson_group);
        Intent intent = new Intent();
        intent.putExtra("lessonDescription", description.getText().toString());
        intent.putExtra("lessonDate", m_lessonDate.getText().toString());
        intent.putExtra("horseName", m_horseNameSpinner.getSelectedItem().toString());
        intent.putExtra("lessonGrade", lessonGrade.getText().toString());
        intent.putExtra("lessonGroup", lessonGroup.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDatePickerDialog(View view) {
        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in lessonDate field
                        m_lessonDate.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, m_year, m_month, m_day);
        dpd.show();
    }
}

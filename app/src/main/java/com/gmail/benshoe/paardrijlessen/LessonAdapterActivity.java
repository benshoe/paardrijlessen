package com.gmail.benshoe.paardrijlessen;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.benshoe.paardrijlessen.db.Horse;
import com.gmail.benshoe.paardrijlessen.db.HorseDataSource;
import com.gmail.benshoe.paardrijlessen.db.Lesson;
import com.gmail.benshoe.paardrijlessen.db.LessonDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LessonAdapterActivity extends ListActivity {

    public static final int REQUEST_CODE = 23456;
    private LessonDataSource m_datasource;
    private List<Lesson> m_lessons = new ArrayList<Lesson>();
    private List<String> m_horseNames = new ArrayList<String>();
    private LessonAdapter m_adapter;
    private HorseDataSource m_horseDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_horseDataSource = new HorseDataSource(this);
        m_horseDataSource.open();
        List<Horse> horses = m_horseDataSource.getAllHorses();
        for(Horse horse: horses){
            m_horseNames.add(horse.getName());
        }

        m_datasource = new LessonDataSource(this);
        m_datasource.open();
        m_lessons = m_datasource.getAllLessons();

        m_adapter = new LessonAdapter(this, m_lessons);

        View header = getLayoutInflater().inflate(R.layout.header, null);
        ListView listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String horseName = m_horseDataSource.getHorseById(m_adapter.getItem(position - 1).getHorse()).getName();
                Intent intent = new Intent(parent.getContext(), LessonActivity.class);
                intent.putExtra("lesson", m_adapter.getItem(position - 1));
                intent.putExtra("horseName", horseName);
                startActivity(intent);
            }
        });

        listView.addHeaderView(header);
        listView.setAdapter(m_adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                onResume();
                String lessonDescription = data.getStringExtra("lessonDescription");
                String lessonDateString = data.getStringExtra("lessonDate");
                String horseName = data.getStringExtra("horseName");
                String lessonGrade = data.getStringExtra("lessonGrade");
                lessonGrade = lessonGrade.equals("") ? "0" : lessonGrade;
                Horse horse = m_horseDataSource.getHorseByName(horseName);

                Date lessonDate = null;
                try {
                    lessonDate = m_datasource.stringToDate(lessonDateString);
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), R.string.exception_date, Toast.LENGTH_LONG).show();
                }

                Lesson lesson = m_datasource.createLesson(lessonDate, lessonDescription, (int) horse.getId(), Long.valueOf(lessonGrade).longValue());
                m_lessons.add(lesson);

                m_adapter.addLesson(lesson);
                m_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lesson_adapter, menu);
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

    public void onClick(View view) {
        Intent intent = new Intent(this, AddLessonActivity.class);
        intent.putStringArrayListExtra("horses", (ArrayList<String>) m_horseNames);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
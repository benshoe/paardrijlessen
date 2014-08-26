package com.gmail.benshoe.paardrijlessen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.benshoe.paardrijlessen.db.Lesson;
import com.gmail.benshoe.paardrijlessen.db.LessonDataSource;
import com.gmail.benshoe.paardrijlessen.util.DateUtil;


public class LessonActivity extends Activity {

    private Lesson m_lesson;
    private String m_horseName;
    private long m_lessonDate;
    private String m_lessonDescription;
    private long m_lessonGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        Intent intent = getIntent();
        m_lesson = (Lesson) intent.getSerializableExtra("lesson");
        m_horseName = intent.getStringExtra("horseName");
        m_lessonDate = m_lesson.getDate();
        m_lessonDescription = m_lesson.getDescription();
        m_lessonGrade = m_lesson.getGrade();

        TextView lessonDate = (TextView) findViewById(R.id.lesson_date);
        lessonDate.setText(DateUtil.dateFrom(m_lessonDate));

        TextView horseNameText = (TextView) findViewById(R.id.horse_name);
        horseNameText.setText(m_horseName);

        TextView lessonDescription = (TextView) findViewById(R.id.lesson_description);
        lessonDescription.setText(m_lessonDescription);

        TextView lessonGrade = (TextView) findViewById(R.id.lesson_grade);
        lessonGrade.setText(Long.valueOf(m_lessonGrade).toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lesson, menu);
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

    public void adjustLesson(View view) {
        findViewById(R.id.lesson_grade).setVisibility(View.INVISIBLE);
        findViewById(R.id.lesson_description).setVisibility(View.INVISIBLE);

        EditText lessonDescription = (EditText) findViewById(R.id.lesson_description_editable);
        lessonDescription.setVisibility(View.VISIBLE);
        lessonDescription.setText(m_lessonDescription);

        EditText lessonGrade = (EditText) findViewById(R.id.lesson_grade_editable);
        lessonGrade.setVisibility(View.VISIBLE);
        lessonGrade.setText(Long.valueOf(m_lessonGrade).toString());
    }

    public void save(View view) {
        EditText lessonDescription = (EditText) findViewById(R.id.lesson_description_editable);
        m_lesson.setDescription(lessonDescription.getText().toString());

        EditText lessonGrade = (EditText) findViewById(R.id.lesson_grade_editable);
        String grade = lessonGrade.getText().toString();
        m_lesson.setGrade(Long.valueOf(grade).longValue());
        LessonDataSource dataSource = new LessonDataSource(getApplicationContext());
        dataSource.open();
        dataSource.updateLesson(m_lesson);
        dataSource.close();
        Intent intent = new Intent(getApplicationContext(), LessonAdapterActivity.class);
        startActivity(intent);
    }
}

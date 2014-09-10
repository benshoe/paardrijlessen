package com.gmail.benshoe.paardrijlessen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.benshoe.paardrijlessen.db.Lesson;
import com.gmail.benshoe.paardrijlessen.db.LessonDataSource;
import com.gmail.benshoe.paardrijlessen.util.CameraUtil;
import com.gmail.benshoe.paardrijlessen.util.DateUtil;

import java.util.Collections;
import java.util.List;

import static android.view.GestureDetector.OnGestureListener;

public class LessonActivity extends Activity implements OnGestureListener {

    private Lesson m_lesson;
    private String m_lessonDescription;
    private long m_lessonGrade;
    private List<Lesson> m_lessons;

    private GestureDetector m_gestureScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_gestureScanner = new GestureDetector(this, this);

        LessonDataSource lessonDataSource = new LessonDataSource(this);
        if(m_lessons == null) {
            lessonDataSource.open();
            m_lessons = lessonDataSource.getAllLessons();
            lessonDataSource.close();
            Collections.sort(m_lessons);
        }

        setContentView(R.layout.activity_lesson);

        Intent intent = getIntent();
        m_lesson = (Lesson) intent.getSerializableExtra("lesson");
        String horseName = intent.getStringExtra("horseName");
        long lessonDateValue = m_lesson.getDate();
        m_lessonDescription = m_lesson.getDescription();
        m_lessonGrade = m_lesson.getGrade();

        TextView lessonDate = (TextView) findViewById(R.id.lesson_date);
        lessonDate.setText(DateUtil.dateFrom(lessonDateValue));

        TextView horseNameText = (TextView) findViewById(R.id.horse_name);
        horseNameText.setText(horseName);

        TextView lessonDescription = (TextView) findViewById(R.id.lesson_description);
        lessonDescription.setText(m_lessonDescription);

        TextView lessonGrade = (TextView) findViewById(R.id.lesson_grade);
        lessonGrade.setText(Long.valueOf(m_lessonGrade).toString());

        ImageView horseImage = (ImageView) findViewById(R.id.horse_image);
        Uri uri = CameraUtil.getOutputMediaFileUri(CameraUtil.MEDIA_TYPE_IMAGE, horseName);
        horseImage.setImageURI(uri);
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

        ImageView horseImage = (ImageView) findViewById(R.id.horse_image);
        horseImage.setVisibility(View.INVISIBLE);
    }

    public void save(View view) {
        EditText lessonDescription = (EditText) findViewById(R.id.lesson_description_editable);
        m_lesson.setDescription(lessonDescription.getText().toString());

        EditText lessonGrade = (EditText) findViewById(R.id.lesson_grade_editable);
        String grade = lessonGrade.getText().toString();
        m_lesson.setGrade(Long.valueOf(grade).longValue());
        LessonDataSource dataSource = new LessonDataSource(this);
        dataSource.open();
        dataSource.updateLesson(m_lesson);
        dataSource.close();
        Intent intent = new Intent(getApplicationContext(), LessonAdapterActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return m_gestureScanner.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Toast.makeText(this, "onDown.", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Toast.makeText(this, "onShowPress.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * This is continuously called as long as the user keeps the finger on the page
     * @param motionEvent
     * @param motionEvent2
     * @param v
     * @param v2
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
//        if(Math.abs(v) < Math.abs(v2)) {
//            Toast.makeText(this, "v < v2 dus doen we niets.", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if(v > 0) {
////            moveItemRight();
//            Toast.makeText(this, "v > 0 dus gaan we naar rechts.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "v < 0 dus gaan we naar links.", Toast.LENGTH_SHORT).show();
////            moveItemLeft();
//        }
        return false;
    }

    private void moveItemRight() {
        int pos = m_lessons.indexOf(m_lesson);
        if(pos == m_lessons.size()) {
            return;
        }
        m_lesson = m_lessons.get(pos + 1);
        //TODO Hier moet de Activity opnieuw gestart worden?
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    /**
     * This is triggered when the onDown is done and the user moves the finger and lets go of the screen
     * @param motionEvent
     * @param motionEvent2
     * @param v
     * @param v2
     * @return
     */
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }
}

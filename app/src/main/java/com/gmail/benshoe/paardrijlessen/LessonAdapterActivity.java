package com.gmail.benshoe.paardrijlessen;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.benshoe.paardrijlessen.db.Horse;
import com.gmail.benshoe.paardrijlessen.db.HorseDataSource;
import com.gmail.benshoe.paardrijlessen.db.Lesson;
import com.gmail.benshoe.paardrijlessen.db.LessonDataSource;
import com.gmail.benshoe.paardrijlessen.util.DateUtil;

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
                Horse horse = m_horseDataSource.getHorseById(m_adapter.getItem(position - 1).getHorse());
                String horseName = horse.getName();
                Intent intent = new Intent(parent.getContext(), LessonActivity.class);
                intent.putExtra("lesson", m_adapter.getItem(position - 1));
                intent.putExtra("horseName", horseName);
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);

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
                String lessonGroup = data.getStringExtra("lessonGroup");
                lessonGrade = lessonGrade.equals("") ? "0" : lessonGrade;
                lessonGroup = lessonGroup.equals("") ? "0" : lessonGroup;
                Horse horse = m_horseDataSource.getHorseByName(horseName);

                Date lessonDate = null;
                try {
                    lessonDate = m_datasource.stringToDate(lessonDateString);
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), R.string.exception_date, Toast.LENGTH_LONG).show();
                }

                Lesson lesson = m_datasource.createLesson(lessonDate, lessonDescription, (int) horse.getId(), Long.valueOf(lessonGrade).longValue(), Long.valueOf(lessonGroup).longValue());
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Lesson lesson = m_adapter.getItem(info.position - 1);
        switch (item.getItemId()) {
            case R.id.delete:
                confirmDelete(lesson);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void confirmDelete(final Lesson lesson) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Verwijder les");

        // set dialog message
        alertDialogBuilder
                .setMessage("Weet je zeker dat je de les van " + DateUtil.dateFrom(lesson.getDate()) + " wilt verwijderen?")
                .setCancelable(false)
                .setPositiveButton("Ja",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, delete the lesson
                        m_datasource.deleteLesson(lesson);
                        m_adapter.deleteLesson(lesson);
                        m_adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Nee",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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

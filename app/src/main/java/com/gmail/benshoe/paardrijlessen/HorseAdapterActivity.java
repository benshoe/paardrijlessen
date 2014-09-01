package com.gmail.benshoe.paardrijlessen;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
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

import java.util.ArrayList;
import java.util.List;

public class HorseAdapterActivity extends ListActivity {
    public static final int REQUEST_CODE = 12345;

    private HorseAdapter m_horseAdapter;

    private HorseDataSource m_datasource;
    private List<Horse> m_horses = new ArrayList<Horse>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_datasource = new HorseDataSource(this);
        m_datasource.open();

        m_horses = m_datasource.getAllHorses();

        m_horseAdapter = new HorseAdapter(this, m_horses);

        View header = getLayoutInflater().inflate(R.layout.header, null);
        ListView listView = getListView();
        listView.addHeaderView(header);

        registerForContextMenu(listView);

        setListAdapter(m_horseAdapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in horse_list_layout.xml
    public void onClick(View view) {
        Intent intent = new Intent(this, AddHorseActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                onResume();
                // save the new horse to the database
                String horseName = data.getStringExtra("horseName").trim();
                String horseTypeName = data.getStringExtra("horseType");
                HorseType horseType = HorseType.fromString(horseTypeName);
                String horseImage = data.getStringExtra("horseImage");

                for(Horse horse: m_horses) {
                    if (horse.getName().equals(horseName)){
                        Toast.makeText(getApplicationContext(), getString(R.string.unique_name), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                Horse horse = m_datasource.createHorse(horseName, horseType, horseImage);
                m_horses.add(horse);

                m_horseAdapter.addHorse(horse);
                m_horseAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Horse horse = m_horseAdapter.getItem(info.position - 1);
        switch (item.getItemId()) {
            case R.id.view:
                Intent intent = new Intent(this, HorseActivity.class);
                intent.putExtra("horse", horse);
                startActivity(intent);
                return true;
            case R.id.delete:
                if(canDelete(horse)) {
                    m_datasource.deleteHorse(horse);
                    m_horseAdapter.deleteHorse(horse);
                    m_horseAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_deleting_horse, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private boolean canDelete(Horse horse) {
        LessonDataSource dataSource = new LessonDataSource(this);
        List<Lesson> lessons = dataSource.getLessonsByHorse(horse);
        if(lessons.size() == 0)
            return true;
        return false;
    }

    @Override
    protected void onResume() {
        m_datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        m_datasource.close();
        super.onPause();
    }
}

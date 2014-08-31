package com.gmail.benshoe.paardrijlessen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gmail.benshoe.paardrijlessen.db.HorseDataSource;
import com.gmail.benshoe.paardrijlessen.db.Lesson;
import com.gmail.benshoe.paardrijlessen.util.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ben on 8/23/14.
 */
public class LessonAdapter extends BaseAdapter {

    private final Context m_context;

    private final List<Lesson> m_data = new ArrayList<Lesson>();

    public LessonAdapter(Context context, List<Lesson> lessons) {
        m_context = context;
        m_data.addAll(lessons);
        Collections.sort(m_data);
    }

    public void addLesson(Lesson lesson) {
        m_data.add(lesson);
        Collections.sort(m_data);
    }

    @Override
    public int getCount() {
        return m_data.size();
    }

    @Override
    public Lesson getItem(int position) {
        return m_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.lesson_list_layout, null, false);
        }

        Lesson lesson = m_data.get(position);
        if(lesson == null)
            return convertView;


        HorseDataSource dataSource = new HorseDataSource(m_context);
        dataSource.open();

        String horseName = dataSource.getHorseById(lesson.getHorse()).getName();
        dataSource.close();

        TextView lessonSummary = (TextView) convertView.findViewById(R.id.lesson_summary);
        lessonSummary.setText(DateUtil.dateFrom(lesson.getDate()) + " (" + horseName + ")");

        return convertView;
    }
}

package com.gmail.benshoe.paardrijlessen.db;

import com.gmail.benshoe.paardrijlessen.util.DateUtil;

import java.io.Serializable;

/**
 * Created by ben on 8/18/14.
 */
public class Lesson implements Comparable<Lesson>, Serializable {
    private long m_id;
    private long m_date;
    private long m_horse;
    private String m_description;
    private long m_grade;

    public Lesson(long date, long horse, String description, long grade) {
        m_date = date;
        m_horse = horse;
        m_description = description;
        m_grade = grade;
    }

    public Lesson () {}

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public long getDate() {
        return m_date;
    }

    public void setDate(long date) {
        m_date = date;
    }

    public long getHorse() {
        return m_horse;
    }

    public void setHorse(long horse) {
        m_horse = horse;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public long getGrade() {
        return m_grade;
    }

    public void setGrade(long grade) {
        m_grade = grade;
    }

    public String toString() {
        return " Op " + DateUtil.dateFrom(m_date) + " gereden op " + m_horse;
    }

    @Override
    public int compareTo(Lesson another) {
        if(getDate() > another.getDate())
            return -1;
        if(getDate() < another.getDate())
            return 1;
        return 0;
    }
}

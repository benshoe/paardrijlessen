package com.gmail.benshoe.paardrijlessen.db;

import com.gmail.benshoe.paardrijlessen.HorseType;

import java.io.Serializable;

/**
 * Created by ben on 8/10/14.
 */
public class Horse implements Comparable<Horse>, Serializable {

    private long m_id;
    String m_name;
    HorseType m_horseType;
    String m_image;

    public Horse(){}

    public Horse(String name, HorseType horseType, int test) {
        this(name, horseType, null);
    }

    public Horse(String name, HorseType horseType, String image) {
        m_name = name;
        m_horseType = horseType;
        m_image = image;
    }

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public void setName(String name){
        m_name = name;
    }
    public String getName() {
        return m_name;
    }

    public void setHorseType(HorseType horseType) {
        m_horseType = horseType;
    }

    public HorseType getHorseType() {
        if(m_horseType == null)
            return HorseType.ONBEKEND;
        return m_horseType;
    }

    public String getImage() {
        return m_image;
    }

    public void setImage(String fileName) {
        m_image = fileName;
    }

    @Override
    public int compareTo(Horse another) {
        return this.getName().compareTo(another.getName());
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return m_name;
    }
}

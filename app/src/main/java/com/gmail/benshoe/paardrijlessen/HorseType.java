package com.gmail.benshoe.paardrijlessen;

/**
 * Created by ben on 8/10/14.
 */
public enum HorseType {
    APPALOOSA("Appaloosa"),
    ARABIER("Arabier"),
    FJORD("Fjord"),
    FRIES("Fries"),
    HAFLINGER("Haflinger"),
    KWPN("KWPN"),
    NEWFOREST("New Forest"),
    NRPS("NRPS"),
    ONBEKEND("Onbekend"),
    SHETLANDER("Shetlander"),
    TINKER("Tinker"),
    WELSH("Welsh"),
    WELSH_COB("Welsh Cob");

    String m_name;

    HorseType(String name) {
        m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public static HorseType fromString(String text) {
        if (text != null) {
            for (HorseType horseType : HorseType.values()) {
                if (text.equalsIgnoreCase(horseType.name())) {
                    return horseType;
                }
            }
        }
        return ONBEKEND;
    }
}

package com.studio.mpak.newsby.data.category;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andrei Kuzniatsou
 */
public enum CategoryEnum {
    MAIN(1034, "Главное", 1),
    ACTUAL(74, "Актуально", 0),
    SOCIETY(3639, "Общество", 1),
    CULTURE(154, "Культура", 1),
    EVENTS(2278, "События", 1),
    ECONOMY(1906, "Экономика", 1),
    HEALS(48, "Здоровье", 1),
    SPORT(24, "Спорт", 1),
    PHOTOS(3623, "Фоторепортажи", 1),
    ADS(3668, "Партнерский материал", 1),
    OFFICIAL(1067, "Официально", 1),
    CITIZEN(3651, "Оршанцы", 0),
    GROUPS(3664, "Профсоюзы", 0),
    ANNIVERSARY(3667, "Юбилей города", 0),
    ELECTION(3669, "Выборы-2018", 0);

    private final int id;
    private final String name;
    private final int isVisibleDefault;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIsVisibleDefault() {
        return isVisibleDefault;
    }

    CategoryEnum(int id, String name, int isVisibleDefault) {
        this.id = id;
        this.name = name;
        this.isVisibleDefault = isVisibleDefault;
    }

    public static CategoryEnum lookupByName(String name) {
        for (CategoryEnum type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new RuntimeException(String.format("Category '%s' does not exist", name));
    }

    public static CategoryEnum lookupByPosition(int position) {
        for (CategoryEnum type : values()) {
            if (type.ordinal() == position) {
                return type;
            }
        }
        throw new RuntimeException(String.format("Category with position '%d' does not exist", position));
    }

    public static Set<String> categories() {
        Set<String> categories = new HashSet<>(values().length);
        for (CategoryEnum type : values()) {
            categories.add(type.name);
        }
        return categories;
    }
}

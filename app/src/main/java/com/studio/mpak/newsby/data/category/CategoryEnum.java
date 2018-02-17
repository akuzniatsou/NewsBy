package com.studio.mpak.newsby.data.category;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andrei Kuzniatsou
 */
public enum CategoryEnum {
    ACTUAL(74, "Актуально"),
    MAIN(1034, "Главное"),
    SOCIETY(3639, "Общество"),
    CULTURE(154, "Культура"),
    OFFICIAL(1067, "Официально"),
    EVENTS(2278, "События"),
    ECONOMY(1906, "Экономика"),
    HEALS(48, "Здоровье"),
    SPORT(24, "Спорт"),
    CITIZEN(3651, "Оршанцы"),
    PHOTOS(3623, "Фоторепортажи"),
    ADS(3668, "Партнерский материал"),
    GROUPS(3664, "Профсоюзы"),
    ANNIVERSARY(3667, "Юбилей города"),
    ELECTION(3669, "Выборы-2018");

    private final int id;
    private final String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    CategoryEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryEnum lookupByName(String name) {
        for (CategoryEnum type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new RuntimeException(String.format("Category '%s' does not exist", name));
    }

    public static Set<String> categories() {
        Set<String> categories = new HashSet<>(values().length);
        for (CategoryEnum type : values()) {
            categories.add(type.name);
        }
        return categories;
    }
}

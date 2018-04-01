package com.studio.mpak.newsby.util;

import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public final class CollectionUtils {

    public static ArrayList<Announcement<Vacancy>> deepCopy(ArrayList<Announcement<Vacancy>> source) {
        ArrayList<Announcement<Vacancy>> clonedList = new ArrayList<>(source.size());
        for (Announcement<Vacancy> announcement : source) {
            Announcement<Vacancy> cloned = new Announcement<>(announcement);
            cloned.setEvents(deepCopy(announcement.getEvents()));
            clonedList.add(cloned);
        }
        return clonedList;
    }

    private static List<Vacancy> deepCopy(List<Vacancy> source) {
        List<Vacancy> clonedList = new ArrayList<>(source.size());
        for (Vacancy vacancy : source) {
            clonedList.add(new Vacancy(vacancy));
        }
        return clonedList;
    }

    public static ArrayList<Announcement<String>> deepCopySimple(ArrayList<Announcement<String>> source) {
        ArrayList<Announcement<String>> clonedList = new ArrayList<>(source.size());
        for (Announcement<String> announcement : source) {
            Announcement<String> cloned = new Announcement<>(announcement);
            cloned.setEvents(new ArrayList<>(announcement.getEvents()));
            clonedList.add(cloned);
        }
        return clonedList;
    }

}

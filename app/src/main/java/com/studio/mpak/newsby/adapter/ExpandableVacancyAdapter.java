package com.studio.mpak.newsby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.studio.mpak.newsby.R;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;
import com.studio.mpak.newsby.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class ExpandableVacancyAdapter extends BaseExpandableListAdapter {

    private ArrayList<Announcement<Vacancy>> announcements;
    private ArrayList<Announcement<Vacancy>> origin = new ArrayList<>();
    private Context context;

    public ExpandableVacancyAdapter(Context context, ArrayList<Announcement<Vacancy>> announcements) {
        this.context = context;
        this.announcements = new ArrayList<>(announcements);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return announcements.get(groupPosition).getEvents().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vacancy_item, null);
        }

        Vacancy vacancy = announcements.get(groupPosition).getEvents().get(childPosition);
        TextView tvPosition = convertView.findViewById(R.id.position);
        tvPosition.setText(String.format("● %s", vacancy.getPosition()));
        TextView tvSalary = convertView.findViewById(R.id.salary);
        tvSalary.setText(vacancy.getSalary());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return announcements.get(groupPosition).getEvents().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return announcements.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return announcements.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.announcement_header, null);
        }

        TextView textGroup = convertView.findViewById(R.id.header);
        Announcement header = announcements.get(groupPosition);
        textGroup.setText(header.getPlace());

        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public void addAll(ArrayList<Announcement<Vacancy>> data) {
        announcements.addAll(data);
        origin.addAll(data);
    }

    public void clear() {
        announcements = new ArrayList<>();
        origin = new ArrayList<>();
    }

    public void filter(String filter) {
        String query = filter.toLowerCase();
        announcements.clear();
        announcements = CollectionUtils.deepCopy(origin);
        if (query.isEmpty()) {
            return;
        }
        Iterator<Announcement<Vacancy>> announcementIterator = announcements.iterator();
        while (announcementIterator.hasNext()) {
            Announcement<Vacancy> announcement = announcementIterator.next();
            Iterator<Vacancy> iterator = announcement.getEvents().iterator();

            String placeText = announcement.getPlace().toLowerCase();
            if (placeText.contains(query)) {
                continue;
            }
            boolean hasQuery = false;
            while (iterator.hasNext()) {
                Vacancy vacancy = iterator.next();
                if (vacancy.contains(query)) {
                    hasQuery = true;
                    continue;
                }
                iterator.remove();
            }
            if (!hasQuery) {
                announcementIterator.remove();
            }
        }
        notifyDataSetChanged();
    }
}

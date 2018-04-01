package com.studio.mpak.newsby.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.studio.mpak.newsby.R;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Andrei Kuzniatsou
 */
public class ExpandableAnnouncementAdapter extends BaseExpandableListAdapter {

    private ArrayList<Announcement<String>> announcements;
    private ArrayList<Announcement<String>> origin = new ArrayList<>();
    private Context context;

    public ExpandableAnnouncementAdapter(Context context, ArrayList<Announcement<String>> announcements) {
        this.context = context;
        this.announcements = announcements;
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
            convertView = inflater.inflate(R.layout.announcement_item, null);
        }

        TextView textChild = convertView.findViewById(R.id.child);
        String tvChild = announcements.get(groupPosition).getEvents().get(childPosition);
        textChild.setText(String.format("● %s", tvChild));

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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

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


    public void addAll(ArrayList<Announcement<String>> data) {
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
        announcements = CollectionUtils.deepCopySimple(origin);
        if (query.isEmpty()) {
            return;
        }
        Iterator<Announcement<String>> announcementIterator = announcements.iterator();
        while (announcementIterator.hasNext()) {
            Announcement<String> announcement = announcementIterator.next();
            Iterator<String> iterator = announcement.getEvents().iterator();

            String placeText = announcement.getPlace().toLowerCase();
            if (placeText.contains(query)) {
                continue;
            }
            boolean hasQuery = false;
            while (iterator.hasNext()) {
                String event = iterator.next();
                if (event.contains(query)) {
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

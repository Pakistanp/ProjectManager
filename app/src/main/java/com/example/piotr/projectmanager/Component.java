package com.example.piotr.projectmanager;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.piotr.projectmanager.Model.Task;

import java.util.List;

/**
 * Created by Piotr on 04.03.2018.
 */

public class Component {
    public static void setListViewHeight(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static int getAllFinishedTasksCount(List<Task> tasks){
        int count = 0;
        for (Task task:tasks) {
            if(task.isStatus())
                count++;
        }
        return count;
    }
}

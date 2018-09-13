package com.example.piotr.projectmanager;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.piotr.projectmanager.Model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Piotr on 04.03.2018.
 */

public class Component {
    public static void setListViewHeight(ListView listView) {
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

    public static int getAllFinishedTasksCount(List<Task> tasks) {
        int count = 0;
        for (Task task : tasks) {
            if (task.isStatus())
                count++;
        }
        return count;
    }

    public static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = new Date();
                try

        {
            convertedDate = dateFormat.parse(dateString);
        } catch(
        ParseException e)

        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static boolean compareDateWithCurrentDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar curCal = Calendar.getInstance();
        curCal.setTime(Calendar.getInstance().getTime());
        if(cal.get(Calendar.YEAR) == curCal.get(Calendar.YEAR) && (cal.get(Calendar.DAY_OF_YEAR) - curCal.get(Calendar.DAY_OF_YEAR)) < 4)
            return true;
        else
            return false;
    }
}

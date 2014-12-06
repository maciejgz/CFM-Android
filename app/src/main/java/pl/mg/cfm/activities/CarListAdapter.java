package pl.mg.cfm.activities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by m on 2014-12-02.
 */
public class CarListAdapter extends SimpleAdapter {


    public CarListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); - See more at: http://www.survivingwithandroid.com/2013/02/android-listview-adapter-imageview.html#sthash.2ALwoqAB.dpuf

        return v;
    }
}

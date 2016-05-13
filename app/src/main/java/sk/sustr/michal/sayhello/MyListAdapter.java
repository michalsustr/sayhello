package sk.sustr.michal.sayhello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Michal Sustr [michal.sustr@gmail.com] on 3/2/16.
 */
public class MyListAdapter extends ArrayAdapter<Friend> {

    private final Context context;
    private final List<Friend> values;

    public MyListAdapter(Context context, List<Friend> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(values.get(position) == null)  {
            return null;
        }
        Friend friend = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView days = (TextView) rowView.findViewById(R.id.days);
        int ndays = (friend.getLastContacted()-MainActivity.getToday()+friend.getDays());
        String txt = "";
        if(ndays > 0 ) {
            if (ndays == 1) {
                txt = "1 day to go";
            } else {
                txt = ndays + " days to go";
            }
        } else if(ndays == 0) {
            txt = "today";
        } else {
            if(ndays == -1) {
                txt = ndays + " day late";
            } else {
                txt = ndays + " days late";
            }
        }
        name.setText(friend.getName());
        days.setText(txt);

        return rowView;
    }
}

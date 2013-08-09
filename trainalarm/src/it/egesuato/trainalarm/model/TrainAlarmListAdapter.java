package it.egesuato.trainalarm.model;

import java.util.List;

import it.egesuato.trainalarm.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrainAlarmListAdapter extends ArrayAdapter<TrainAlarm> {

	private int layoutResourceId;

	private List<TrainAlarm> alarms;

	private static final String LOG_TAG = "TrainAlarmListAdapter";

	public TrainAlarmListAdapter(Context context, int textViewResourceId, List<TrainAlarm> alarms) {
		super(context, textViewResourceId, alarms);
		layoutResourceId = textViewResourceId;
		this.alarms = alarms;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_item_row, null);
        }
        TrainAlarm o = alarms.get(position);
        if (o != null) {
                TextView tt = (TextView) v.findViewById(R.id.txtTitle);
                TextView bt = (TextView) v.findViewById(R.id.txtDescription);
                if (tt != null) {
                      tt.setText("Alarm at " + o.getStartAlarmAt() +" for train " + String.valueOf(o.getTrainNumber()));                            }
                if(bt != null){
                      bt.setText(o.getDescription());
                }
        }
        return v;
	}
}
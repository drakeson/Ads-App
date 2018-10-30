package smith.drake.kato.ucuinterview.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import smith.drake.kato.ucuinterview.Model.Address;
import smith.drake.kato.ucuinterview.R;


public class LocationAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Address> feedItems;

    public LocationAdapter(Activity activity, List<Address> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.

        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        Address item = feedItems.get(position);


        holder.reqName.setText(item.getTitle());
        String firstLetter = String.valueOf(holder.reqName.getText().charAt(0));



        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter.toUpperCase(), color); // radius in px

        holder.imageView.setImageDrawable(drawable);

        holder.reqName.setText("Address: " + item.getTitle());
        holder.reqAmt.setText("Distance: " + item.getDesc());



        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView reqName, reqAmt;

        public ViewHolder(View v) {
            imageView = v.findViewById(R.id.image_view);
            reqName = v.findViewById(R.id.reqname);
            reqAmt = v.findViewById(R.id.amount);
        }
    }
}

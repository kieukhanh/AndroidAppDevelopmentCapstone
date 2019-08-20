package khanhkt.fe.edu.vn.readmagazinerssapp.Adapter;

import android.content.ContentValues;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import khanhkt.fe.edu.vn.readmagazinerssapp.Model.RssFeedModel;
import khanhkt.fe.edu.vn.readmagazinerssapp.R;

public class RssFeedsAdapter extends BaseAdapter {
    private List<RssFeedModel> listFeeds;

    public RssFeedsAdapter() {
        //do nothing
        this.listFeeds = new ArrayList<>();
    }

    public RssFeedsAdapter(List<RssFeedModel> listFeeds) {
        this.listFeeds = listFeeds;
    }

    public void setListFeeds(List<RssFeedModel> listFeeds) {
        this.listFeeds = listFeeds;
    }

    @Override
    public int getCount() {
        return this.listFeeds.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listFeeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            //set view to list item on specific activity
            view = inflater.inflate(R.layout.feed_item_layout, viewGroup, false);
        }//end if view is null
        //get value of current cursor in list Feeds
        RssFeedModel feed = this.listFeeds.get(position);
        // get control on view
        TextView lblTitle = (TextView) view.findViewById(R.id.lblTitle);
        TextView lblDes = (TextView) view.findViewById(R.id.lblDescription);
        TextView lblDate = (TextView) view.findViewById(R.id.lblDate);
        //set value to control
        lblTitle.setText(feed.getTitle());
        lblDes.setText(feed.getDescription());
        lblDate.setText(feed.getPublicDate());

        return view;
    }
}

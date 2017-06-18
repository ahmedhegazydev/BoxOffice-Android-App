package info.androidhive.slidingmenu.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

import info.androidhive.slidingmenu.ImageRecord;
import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.model.BitmapLruCache;

/**
 * Created by ahmed on 15/06/17.
 */

public class ImageRecordsAdapter extends ArrayAdapter<ImageRecord> {


    ImageLoader imageLoader = null;
    List<ImageRecord> imageRecords = null;

    public ImageRecordsAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        this.imageLoader = new ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache());

    }


    @Nullable
    @Override
    public ImageRecord getItem(int position) {
        return super.getItem(position);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);
        TextView textView = (TextView) convertView.findViewById(R.id.tvMovieTitle);
        ImageRecord imageRecord = getItem(position);

        networkImageView.setImageUrl(imageRecord.getUrl(), imageLoader);
        textView.setText(imageRecord.getTitle());
        convertView.setTag(imageRecord.getId());


        return convertView;

    }

    public void swapImageRecords(List<ImageRecord> imageRecords) {
        clear();

        for (ImageRecord imageRecord : imageRecords) {
            add(imageRecord);

        }

        notifyDataSetChanged();


    }



}

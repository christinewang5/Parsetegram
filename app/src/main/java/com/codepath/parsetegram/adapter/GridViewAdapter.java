package com.codepath.parsetegram.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter{
    private Context context;
    private int layoutResourceId;
    private List<Post> posts = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.posts = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        posts.get(position).getImage().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            // decode the byte[] into Bitmap
                            Bitmap bmp = BitmapFactory.decodeByteArray(
                                    data, 0,
                                    data.length);
                            // set the Bitmap into the ivImage
                            holder.image.setImageBitmap(bmp);
                        }
                        else {
                            Log.d("PostAdapter","Problem load image the data.");
                        }
                    }
                });
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}

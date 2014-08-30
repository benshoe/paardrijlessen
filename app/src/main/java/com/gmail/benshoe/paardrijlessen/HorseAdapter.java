package com.gmail.benshoe.paardrijlessen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.benshoe.paardrijlessen.db.Horse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ben on 8/10/14.
 */
public class HorseAdapter extends BaseAdapter {

    private final Context m_context;

    private final List<Horse> m_data = new ArrayList<Horse>();

    public HorseAdapter(Context context, List<Horse> horses) {
        m_context = context;
        m_data.addAll(horses);
        Collections.sort(m_data);
    }

    public void addHorse(Horse horse){
        m_data.add(horse);
        Collections.sort(m_data);
    }

    @Override
    public int getCount() {
        return m_data.size();
    }

    @Override
    public Horse getItem(int position) {
        return m_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Horse horse = m_data.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // If you have created your own custom layout you can replace it here
            convertView = layoutInflater.inflate(R.layout.horse_list_layout, null, false);
        }

        ImageView horseImage = (ImageView) convertView.findViewById(R.id.horse_image);
        Bitmap bitmap = null;
        String image = horse.getImage();
        if(image != null) {
            bitmap = decodeSampledBitmapFromPath(image.substring(6), 50, 50);
        }
        horseImage.setImageBitmap(bitmap);

        TextView horseName = (TextView) convertView.findViewById(R.id.horse_name);
        horseName.setText(horse.getName());

        TextView horseType = (TextView) convertView.findViewById(R.id.horse_type);
        horseType.setText(horse.getHorseType().getName());

        return convertView;
    }

    /**
     * Load the images
     */
    public Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                              int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

// Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
}

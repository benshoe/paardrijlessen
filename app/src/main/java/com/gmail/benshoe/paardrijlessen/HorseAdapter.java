package com.gmail.benshoe.paardrijlessen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.benshoe.paardrijlessen.db.Horse;
import com.gmail.benshoe.paardrijlessen.util.ImageUtil;

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

    public void deleteHorse(Horse horse) {
        m_data.remove(horse);
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Horse horse = m_data.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // If you have created your own custom layout you can replace it here
            convertView = layoutInflater.inflate(R.layout.horse_list_layout, null, false);
        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HorseActivity.class);
                intent.putExtra("horse", getItem(position));
                view.getContext().startActivity(intent);
            }
        });

        ImageView horseImage = (ImageView) convertView.findViewById(R.id.horse_image);
        Bitmap bitmap = null;
        String image = horse.getImage();
        if(image != null) {
            image = image.startsWith("file") ? image.substring(6):image;
            bitmap = ImageUtil.decodeSampledBitmapFromPath(image, 50, 50);
        }
        horseImage.setImageBitmap(bitmap);

        TextView horseName = (TextView) convertView.findViewById(R.id.horse_name);
        horseName.setText(horse.getName());

        TextView horseType = (TextView) convertView.findViewById(R.id.horse_type);
        horseType.setText(horse.getHorseType().getName());

        return convertView;
    }


}

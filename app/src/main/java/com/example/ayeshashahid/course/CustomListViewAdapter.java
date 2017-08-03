package com.example.ayeshashahid.course;

/**
 * Created by ayesha.shahid on 7/27/2017.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Book> {

    private Activity activity;

    public CustomListViewAdapter(Activity activity, int resource, List<Book> books) {
        super(activity, resource, books);
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);

        holder.name.setText(book.getName());
        holder.authorName.setText("By "+book.getAuthorName());
        holder.course_category.setText(book.getCourse_category());
        holder.tution_fee.setText(book.getTutionfee());
//        Picasso.with(activity).load(book.getImageUrl()).into(holder.image);


        String imageString = book.getImageUrl();

        //decode base64 string to image
        byte[]  imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.image.setImageBitmap(decodedImage);


        return convertView;
    }

    private static class ViewHolder {
        private TextView name,course_category,tution_fee;
        private TextView authorName;
        private ImageView image;

        public ViewHolder(View v) {
            course_category = (TextView) v.findViewById(R.id.course_category);
            tution_fee = (TextView) v.findViewById(R.id.tutionfee);
            name = (TextView) v.findViewById(R.id.course_name);
            image = (ImageView) v.findViewById(R.id.course_image);
            authorName = (TextView) v.findViewById(R.id.author_name);
        }
    }

}
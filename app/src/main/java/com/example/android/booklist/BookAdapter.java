package com.example.android.booklist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {
    private Context mContext;

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();


    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
       mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_book, parent, false);

            viewHolder.bookCover = (ImageView) convertView.findViewById(R.id.book_cover);

            viewHolder.bookTitleView = (TextView) convertView.findViewById(R.id.book_title);

            viewHolder.bookAuthorView = (TextView) convertView.findViewById(R.id.book_author);

            viewHolder.bookDateView = (TextView) convertView.findViewById(R.id.published_date);
            
            viewHolder.bookRatingView = (TextView) convertView.findViewById(R.id.rating);
            
            viewHolder.bookPage = (TextView) convertView.findViewById(R.id.page_count); 

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book currentBook = getItem(position);

        viewHolder.textContainer = (View) convertView.findViewById(R.id.list_bucket);

        viewHolder.bookTitleView.setText(currentBook.getBookTitle());

        viewHolder.bookAuthorView.setText(currentBook.getAuthor());

        viewHolder.bookDateView.setText(currentBook.getPublishedDate());

        double averageRating = currentBook.getAverageRating();
        if (averageRating == 0.0) {
            viewHolder.bookRatingView.setText("N/A");
        }else {
            viewHolder.bookRatingView.setText(averageRating + "");
        }

        viewHolder.bookPage.setText(Integer.toString(currentBook.getPageCount()));

        RequestOptions requestOptions = new RequestOptions()
                .error(R.mipmap.ic_launcher_round);

        Glide.with(mContext)
                .load(currentBook.getSmallThumbnail())
                        .error(R.drawable.ic_launcher_background)
                        .dontAnimate()
                        .into(viewHolder.bookCover);




            return convertView;
        }

    private static class ViewHolder {
        ImageView bookCover;
        TextView bookTitleView;
        TextView bookAuthorView;
        TextView bookDateView;
        TextView bookRatingView;
        TextView bookPage;
        View textContainer;
    }
}
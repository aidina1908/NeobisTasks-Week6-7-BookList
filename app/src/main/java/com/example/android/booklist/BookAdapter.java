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

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {
    Context context;
    int resourse;
  // RequestOptions option;

    private static final String LOG_TAG = BookAdapter.class.getSimpleName();


    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.resourse = resourse;

    }

    //@Override
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

        RequestOptions requestOptions = new RequestOptions().centerCrop()
                .error(R.mipmap.ic_launcher_round);

        //try {

            Glide.with(context).load(currentBook.getThumbnail()).apply(requestOptions).into(viewHolder.bookCover);
      /*  } catch (Exception e){
            Log.e("BookAdapter", "Error");

        }*/

        /* Glide.with(context)
                .load(currentBook.getSmallThumbnail()).placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .dontAnimate()
                        .into(viewHolder.bookCover);*/
      /*  Glide.with(context).load(currentBook.getSmallThumbnail())
                .thumbnail(Glide.with(context).load("https://www.googleapis.com/books/v1/volumes?q====/"))
                .apply(requestOptions).into(viewHolder.bookCover);*/
      /*String url = "https://icon-icons.com/ru/%D0%B7%D0%BD%D0%B0%D1%87%D0%BE%D0%BA/%D0%9E%D1%81%D0%B5%D0%BB-3/80633.png";
      Glide.with(context).load(url).apply(requestOptions).into(viewHolder.bookCover);*/

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
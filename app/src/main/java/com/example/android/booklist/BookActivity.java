package com.example.android.booklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import java.util.ArrayList;
import java.util.List;
import androidx.core.app.CoreComponentFactory;

    public class BookActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private static String googleBooks =
            "https://www.googleapis.com/books/v1/volumes?q====/";
    private BookAdapter mAdapter;

    private TextView mEmptyStateView;

    private ProgressBar loadingData;

    private ImageView termSearch;

    private String searchTerm;

    private ListView bookListView;

    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        termSearch = (ImageView)findViewById(R.id.subject_search);
        bookListView = (ListView) findViewById(R.id.list);

        loadingData = (ProgressBar) findViewById(R.id.loading_progress);
        loadingData.setVisibility(View.INVISIBLE);

        LoaderManager  loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID,  null,BookActivity.this).forceLoad();

        final EditText subjectEntered = (EditText) findViewById(R.id.subject_text);

        mAdapter = new BookAdapter(BookActivity.this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        mEmptyStateView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateView);

        termSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                searchTerm = subjectEntered.getText().toString();

                if (searchTerm == null || searchTerm.equals("")) {
                    Toast.makeText(BookActivity.this, getString(R.string.no_search_term),
                            Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(BookActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(subjectEntered.getWindowToken(), 0);

                    loadingData.setVisibility(View.VISIBLE);

                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        LoaderManager loaderManager = getLoaderManager();

                        loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this).
                                forceLoad();
                    } else {
                        loadingData.setVisibility(View.GONE);
                        mEmptyStateView.setText(R.string.no_connection);
                    }
                }
            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = mAdapter.getItem(position);


                Uri bookUri = Uri.parse(currentBook.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        if (searchTerm == null) {
            return new Loader<List<Book>>(this);
        } else {
            searchTerm = searchTerm.replace(" ", "%20");
        }
        return new BookLoader(this, googleBooks + searchTerm);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mEmptyStateView.setText(R.string.no_books);

        loadingData.setVisibility(View.INVISIBLE);

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}



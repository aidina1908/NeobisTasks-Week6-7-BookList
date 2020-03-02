package com.example.android.booklist;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class SearchUtils {

    public static final String LOG_TAG = SearchUtils.class.getSimpleName();


    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        List<Book> books = extractBookInfoFromJson(jsonResponse);

        return books;
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractBookInfoFromJson(String booksJSON) {

        List<Book> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {

                JSONObject currentBook = booksArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String bookTitle = volumeInfo.optString("title");

                String url = volumeInfo.optString("canonicalVolumeLink");

                String publishedDate = volumeInfo.optString("publishedDate");

                Integer pageCount = volumeInfo.optInt("pageCount");

                double maturityRating = volumeInfo.optDouble("averageRating");

                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                String smallThumbnail = imageLinks.getString("smallThumbnail");


                String authors;
                if (volumeInfo.has("authors")) {
                    JSONArray authorsJsonArray = volumeInfo.getJSONArray("authors");

                    authors = authorsJsonArray.optString(0);

                    for (int j = 0; j < authorsJsonArray.length(); j++) {
                        if (authorsJsonArray.length() == 1) {
                            authors = authorsJsonArray.optString(j) + " ";
                        } else if (authorsJsonArray.length() > 1) {
                            authors = authors + ", " + authorsJsonArray.optString(j);
                        }
                    }
                } else {
                    authors = "No authors listed.";
                }



               /* JSONObject imageLinks = null;
                try {
                    imageLinks = volumeInfo.getJSONObject("imageLinks");
                } catch (JSONException ignored) {
                }
                String smallThumbnail = "";
                if (imageLinks == null) {
                    smallThumbnail = "null";
                } else {
                    smallThumbnail = imageLinks.getString("smallThumbnail");*/

                books.add(new Book(bookTitle, authors, publishedDate, maturityRating, pageCount, url, smallThumbnail));
            }
        } catch (JSONException e) {
            Log.e("SearchUtils", "Problem parsing the book JSON results", e);
        }
        return books;
    }
}
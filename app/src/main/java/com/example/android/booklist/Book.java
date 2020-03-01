package com.example.android.booklist;

public class Book {
    private String mBookTitle;

    private String mAuthors;

    private String mPublishedDate;

    private Double mAverageRating;

    private Integer mPageCount;

    private String mUrl;

    private String mThumbnail;


    public Book(String bookTitle, String authors, String publishedDate, Double averageRating, Integer pageCount, String url, String thumbnail) {
        mBookTitle = bookTitle;
        mAuthors = authors;
        mPublishedDate = publishedDate;
        mAverageRating = averageRating;
        mPageCount = pageCount;
        mUrl = url;
        mThumbnail = thumbnail;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getAuthor() {
        return mAuthors;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public Double getAverageRating() {
        return mAverageRating;
    }

    public Integer getPageCount() {
        return mPageCount;
    }

    public String getUrl() {
        return mUrl;
    }


    public String getThumbnail() {
        return mThumbnail;

    }

}
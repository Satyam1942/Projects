package com.example.majorproject;

public class bookModel {
    String nameOfBook;
    String publisherOfBook;
    String cost;
    String publishingYear;
    String noOfPages;
    String imgUri;



    public String getBookVolumeId() {
        return bookVolumeId;
    }

    public void setBookVolumeId(String bookVolumeId) {
        this.bookVolumeId = bookVolumeId;
    }

    String bookVolumeId;
    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }



    public String getNameOfBook() {
        return nameOfBook;
    }

    public void setNameOfBook(String nameOfBook) {
        this.nameOfBook = nameOfBook;
    }

    public String getPublisherOfBook() {
        return publisherOfBook;
    }

    public void setPublisherOfBook(String publisherOfBook) {
        this.publisherOfBook = publisherOfBook;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(String noOfPages) {
        this.noOfPages = noOfPages;
    }
}

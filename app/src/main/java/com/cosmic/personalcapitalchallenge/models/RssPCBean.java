package com.cosmic.personalcapitalchallenge.models;

import com.cosmic.personalcapitalchallenge.utils.ParseRelativeDate;

/**
 * Created by anushree on 8/31/2017.
 * This is Java Bean class which holds the RSS Feed Item
 * Has following Fieilds
 * title, link, description, imageURL, published date of every RSS feed item
 */

public class RssPCBean {

    private String title;
    private String link;
    private String description;
    private String imageURL;
    private String pubDate;

    public RssPCBean(String title, String link, String description, String image, String date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.imageURL = image;
        this.pubDate = date;
    }

    public String getPubDate() {
        return ParseRelativeDate.getExpandedDate(pubDate);
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link+"?displayMobileNavigation=0";
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssPCBean that = (RssPCBean) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "RssFeedBean{" +
                "title='" + title + '\'' +
                '}';
    }
}


package com.ezio.org.tanngo.data;

import cn.bmob.v3.BmobObject;

/**
 * Created by Ezio on 2015/4/11.
 */
public class WordsBooksTable extends BmobObject {

    private String bookName;
    private int wordsCount;
    private String describe;
    private String level;
    private int price;
    private String author;
    private int hot;
    private String url;
    private String tag;
    private String imageUrl;

    public String getBookName() {
        return bookName;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public String getDescribe() {
        return describe;
    }

    public String getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }

    public int getHot() {
        return hot;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

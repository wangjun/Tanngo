package com.ezio.org.tanngo.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Ezio on 2015/4/11.
 */
public class WordsBooksTable extends BmobObject {

    private String bookName;
    private Integer wordsCount;
    private String describe;
    private String level;
    private Integer price;
    private String author;
    private Integer hot;
    private String url;
    private String tag;
    private String imageUrl;
    private BmobFile file;

    public String getBookName() {
        return bookName;
    }

    public Integer getWordsCount() {
        return wordsCount;
    }

    public String getDescribe() {
        return describe;
    }

    public String getLevel() {
        return level;
    }

    public Integer getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getHot() {
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

    public BmobFile getFile() {
        return file;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setWordsCount(Integer wordsCount) {
        this.wordsCount = wordsCount;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setHot(Integer hot) {
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

    public void setFile(BmobFile file) {
        this.file = file;
    }
}

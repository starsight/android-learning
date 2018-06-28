package com.wenjiehe.android_learning.Entry;

import java.util.List;

public class GankItem {
    public String id;
    public String createAt;
    public String desc;
    public String publishedAt;
    public String type;
    public String url;
    public String who;
    public String day;
    public List<String> images;
    public String image;
    public boolean like;

    public String getImage() {
        if (image != null) {
            return image;
        } else if (!type.equals("福利")){
            return images == null || images.size() == 0 ? null : images.get(0);
        } else {
            return url;
        }
    }

    @Override
    public String toString() {
        return "GankItem{" +
                "id='" + id + '\'' +
                ", createAt='" + createAt + '\'' +
                ", desc='" + desc + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", who='" + who + '\'' +
                ", day='" + day + '\'' +
                ", images=" + images +
                ", image='" + image + '\'' +
                ", like=" + like +
                '}';
    }
}

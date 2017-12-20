package com.ictnews.ongtien.rssnews;

class RssFeedModel {
    String Title;
    String Link;
    String Description;
    String ThumbnailUrl;
    RssFeedModel(String title, String link, String description, String thumbnailUrl){
        Title = title;
        Link = link;
        Description = description;
        ThumbnailUrl = thumbnailUrl;
    }
}

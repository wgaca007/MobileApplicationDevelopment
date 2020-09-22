package com.example.inclass11;

import com.google.firebase.storage.StorageReference;

public class ImageInfo {
    public String url;
    public StorageReference imageref;

    public ImageInfo(String url, StorageReference imageref) {
        this.url = url;
        this.imageref = imageref;
    }
}

package ua.shuriak.animage.object;

import com.google.gson.annotations.SerializedName;

public class DanbooruObject {

    public @SerializedName("preview_file_url")  String preview_url;
    public @SerializedName("file_url")          String file_url;
    public @SerializedName("file_size")         int file_size;

    public @SerializedName("image_height")      int height;
    public @SerializedName("image_width")       int width;
}

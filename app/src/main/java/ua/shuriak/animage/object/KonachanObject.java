package ua.shuriak.animage.object;

import com.google.gson.annotations.SerializedName;

public class KonachanObject {

    public @SerializedName("preview_url")   String preview_url;
    public @SerializedName("file_url")      String file_url;
    public @SerializedName("file_size")     int file_size;

    public @SerializedName("sample_height") int height;
    public @SerializedName("sample_width")  int width;
}

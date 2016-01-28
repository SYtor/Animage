package ua.shuriak.animage.object;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "posts")
public class SafebooruObject {

    @Attribute public String count;
    @Attribute public String offset;

    @ElementList(inline = true, required = false) public List<Post> list;

    @Root
    public static class Post{

        @Attribute public int height;
        @Attribute public int score;
        @Attribute public String file_url;
        @Attribute public String parent_id;
        @Attribute public String sample_url;
        @Attribute public int sample_width;
        @Attribute public int sample_height;
        @Attribute public String preview_url;
        @Attribute public String rating;
        @Attribute public String tags;

        @Attribute public int id;
        @Attribute public int width;
        @Attribute public int change;
        @Attribute public String md5;
        @Attribute public int creator_id;
        @Attribute public String has_children;
        @Attribute public String created_at;
        @Attribute public String status;
        @Attribute public String source;
        @Attribute public String has_notes;
        @Attribute public String has_comments;
        @Attribute public int preview_width;
        @Attribute public int preview_height;


    }
}

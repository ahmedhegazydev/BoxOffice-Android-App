package info.androidhive.slidingmenu;

import java.util.HashMap;

/**
 * Created by ahmed on 15/06/17.
 */

public class ImageRecord {



    public String url = "";//the movie poster image path
    public String title = "";//the original title for movie
   String id = "";



    public ImageRecord(String url, String title){
        this.url = url;
        this.title = title;

    }

    public String getId() {
        return id;
    }

    public ImageRecord(String id, String posterPath, String original_title) {
        this.url = posterPath;
        this.title = original_title;
        this.id = id;

    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

}

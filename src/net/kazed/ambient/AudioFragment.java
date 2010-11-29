package net.kazed.ambient;

import android.net.Uri;

/**
 * Audio fragment.
 */
public class AudioFragment {

    public static final Uri CONTENT_URI = Uri.parse("content://" + AmbientPlayer.PACKAGE + "/audiofragment");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ambientplayer.fragment";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ambientplayer.fragment";

    private long id;
    private String path;
    private String name;
    private boolean repeat;
    
    /**
     * Constructor.
     * @param id ID.
     * @param path File path.
     * @param name Fragment name.
     */
    public AudioFragment(long id, String path, String name) {
        super();
        this.id = id;
        this.path = path;
        this.name = name;
    }

    /**
     * Constructor.
     * @param path File path.
     * @param name Fragment name.
     */
    public AudioFragment(String path, String name) {
        super();
        this.path = path;
        this.name = name;
    }

    /**
     * @return ID.
     */
    public long getId() {
        return id;
    }

    /**
     * @return File path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return Fragment name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return If true, repeat this during playback.
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * @param repeat If true, repeat this during playback.
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
    
}

package gt.com.whoscounting;

import com.google.api.server.spi.Constant;

/**
 * Contains the client IDs and scopes for allowed clients consuming the conference API.
 */
public class Constants {
    public static final String WEB_CLIENT_ID = "679347850781-oq66qotc6m7qlj4nmqjn58dn175424af.apps.googleusercontent.com";
    public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
    public static final String IOS_CLIENT_ID = "115814606132-24i7f5psvb3547h3g4o67vri014p6nd0.apps.googleusercontent.com";
    public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
    public static final String EMAIL_SCOPE = Constant.API_EMAIL_SCOPE;
    public static final String API_EXPLORER_CLIENT_ID = Constant.API_EXPLORER_CLIENT_ID;

    public static final String MEMCACHE_ANNOUNCEMENTS_KEY = "RECENT_ANNOUNCEMENTS";
}
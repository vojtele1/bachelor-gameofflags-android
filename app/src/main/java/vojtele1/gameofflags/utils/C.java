package vojtele1.gameofflags.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trida obsahujici Configuracni a Constantni hodnoty
 * <p/>
 * Leon Vojtěch 2016
 */
public class C {
    public static final String LOG_BLESCAN = "BLE Scan";
    public static final String LOG_WIFISCAN = "Wifi Scan";
    public static final String LOG_CELLSCAN = "GSM Scan";
    public static final String LOG_NOTIFICATION = "Notification";
    public static final String LOG_ALARMRECEIVER = "AlarmReceiver";
    public static final String LOG_RETRYINGSENDER = "RetryingSender";
    public static final String LOG_WEBVIEWONCLICK = "WebviewOnClick";
    public static final String LOG_ACT1LOGIN = "Act1Login";
    public static final String LOG_ACT2WEBVIEW = "Act2WebView";
    public static final String LOG_ACT3AR = "Act3AR";
    public static final String LOG_ACT4SETTINGS = "Act4Settings";
    /**
     * Jak dlouho ma probihat skenovani pri sberu dat
     */
    public static int SCAN_COLLECTOR_TIME = 20000;

    /**
     * Jak casto ma byt skenovano pri hledani (cle)
     */
    public static int SCAN_FINDER_TIME = 5000;

    /**
     * Jak dlouho ma byt vlajka immuni proti dalsimu zabrani
     */
    public static int FLAG_IMMUNE_TIME = 600000;

    public static final String PACKAGE_NAME = "vojtele1.gameofflags";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    public static final String NOTIFICATION_ADDED_KEY = PACKAGE_NAME + ".NOTIFICATION_ADDED_KEY";

    public static final String PLAYER_FRACTION = PACKAGE_NAME + ".PLAYER_FRACTION";

    public static final String SHOWN_FLOOR = PACKAGE_NAME + ".SHOWN_FLOOR";

    public static final String TOKEN = PACKAGE_NAME + ".TOKEN";

    public static final float GEOFENCE_RADIUS_IN_METERS = 1600; // 1 mile, 1.6 km

    /**
     * souradnice Fakulty pro geofencing
     */
    public static final HashMap<String, LatLng> GEOFENCING_LANDMARKS = new HashMap<>();
    static {
        // poloha FIM
        GEOFENCING_LANDMARKS.put("FIM", new LatLng(50.204474, 15.829622));
    }

    /**
     * veskere MAC adresy eduroamu na FIM, ktere byly ziskany
     */
    public static final ArrayList<String> MAC_EDUROAM = new ArrayList<>();
    static {
        MAC_EDUROAM.add("00:1a:e3:d2:d3:50");
        MAC_EDUROAM.add("00:25:45:24:7e:bf");
        MAC_EDUROAM.add("00:25:45:24:7e:b0");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:5f");
        MAC_EDUROAM.add("00:24:14:3a:bb:b0");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:a0");
        MAC_EDUROAM.add("00:24:14:3a:ae:10");
        MAC_EDUROAM.add("00:1a:e3:d2:e7:60");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:af");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:80");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:60");
        MAC_EDUROAM.add("00:21:a0:f9:54:c0");
        MAC_EDUROAM.add("00:1a:e3:d2:e7:20");
        MAC_EDUROAM.add("00:1a:e3:d2:e7:2f");
        MAC_EDUROAM.add("00:1a:e3:d2:d9:60");
        MAC_EDUROAM.add("00:1a:e3:d2:e7:6f");
        MAC_EDUROAM.add("00:24:14:3a:ac:70");
        MAC_EDUROAM.add("1c:6a:7a:9d:a1:e0");
        MAC_EDUROAM.add("00:24:14:3a:bb:bf");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:8f");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:7f");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:70");
        MAC_EDUROAM.add("00:1a:e3:d2:a6:30");
        MAC_EDUROAM.add("00:1a:e3:d2:d9:6f");
        MAC_EDUROAM.add("00:1a:e3:d2:d2:d0");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:6f");
        MAC_EDUROAM.add("00:1a:e3:d2:a6:3f");
        MAC_EDUROAM.add("00:1a:e3:d2:e8:40");
        MAC_EDUROAM.add("00:24:14:3a:b2:90");
        MAC_EDUROAM.add("00:24:c4:ac:cf:50");
        MAC_EDUROAM.add("00:1a:e3:d2:e8:4f");
        MAC_EDUROAM.add("00:24:c4:ac:cf:5f");
        MAC_EDUROAM.add("00:24:14:3a:b2:9f");
        MAC_EDUROAM.add("00:1a:e3:d2:d2:df");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:00");
        MAC_EDUROAM.add("1c:6a:7a:9d:a1:ef");
        MAC_EDUROAM.add("00:1a:e3:d2:d3:0f");
        MAC_EDUROAM.add("00:24:14:3a:ae:1f");
        MAC_EDUROAM.add("00:24:14:3a:ac:7f");
        MAC_EDUROAM.add("00:21:a0:f9:54:cf");
    }

    public static final String[] EXCEPTION_EMAIL = { "vojtele1@uhk.cz" };

    public static final String SERVER_URL = "http://gameofflags-vojtele1.rhcloud.com/android/";

    public static final String WEBVIEW_SCORE_FRACTION = SERVER_URL + "webviewscorefraction";

    public static final String GET_FLAG_INFO_USER = SERVER_URL + "getflaginfouser";

    public static final String LOGIN_PLAYER = SERVER_URL + "loginplayer";

    public static final String CHANGE_PLAYER_NAME = SERVER_URL + "changeplayername";

    public static final String WEBVIEW_PLAYER = SERVER_URL + "webviewplayer";

    public static final String GET_FLAG_INFO = SERVER_URL + "getflaginfo";

    public static final String SEND_SCAN = SERVER_URL + "sendscan";

    public static final String CHANGE_PLAYER_SCORE = SERVER_URL + "changeplayerscore";

    public static final String CHANGE_FLAG_OWNER = SERVER_URL + "changeflagowner";

    public static final String GET_QR_CODES = SERVER_URL + "getqrcodes";

    public static final String CHANGE_FRACTION = SERVER_URL + "changefraction";

    public static final String GET_PLAYER_FRACTION = SERVER_URL + "getplayerfraction";
}

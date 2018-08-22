package configs;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferenceUtils {
    private Preferences prefs;

    public PreferenceUtils(){
        prefs=Preferences.userRoot().node("/unipampa/gama/gloveappspc");
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }


    public void saveUsedGlove(String gloveName){
        prefs.put("lastGloveUsed", gloveName);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public String getUsedGlove(){
        return prefs.get("lastGloveUsed","LUVAMOUSE");
    }
}

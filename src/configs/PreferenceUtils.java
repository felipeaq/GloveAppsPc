package configs;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferenceUtils {
    private Preferences prefs;
    private final String DEFAULT_PATH_IMAGE="images/noTitle2.png";


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

    public void setLastUsedPathImage(String path){

        prefs.put("lastUsedPathImage", path);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }


    }
    public String getLastUsedPathImage(){
        return prefs.get("lastUsedPathImage", DEFAULT_PATH_IMAGE);
    }



    public void setDefaultPathImage(){

        prefs.put("lastUsedPathImage", DEFAULT_PATH_IMAGE);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }


    }

    public boolean isDefaultPathImage(){
        String s = getLastUsedPathImage();
        return getLastUsedPathImage().equalsIgnoreCase(DEFAULT_PATH_IMAGE);
    }


}

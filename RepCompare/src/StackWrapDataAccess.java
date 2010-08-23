import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.sf.stackwrap4j.StackWrapper;
import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.stackauth.StackAuth;
import net.sf.stackwrap4j.stackauth.entities.Site;

public class StackWrapDataAccess {

    private static Map<String, Image> icons;

    static {
        new Thread() {
            public void run() {
                icons = new HashMap<String, Image>();
                Map<String, Site> sites = null;
                try {
                    sites = StackAuth.getNameSiteMap();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                for (Entry<String, Site> entry : sites.entrySet()) {
                    try {
                        icons.put(entry.getKey(), ImageIO.read(new URL(entry.getValue()
                                .getIconUrl())));
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    private Map<String, StackWrapper> allSites;

    public StackWrapDataAccess() throws IOException, JSONException {
        this(null);
    }

    public StackWrapDataAccess(String key) throws IOException, JSONException {
        allSites = new HashMap<String, StackWrapper>();
        Map<String, Site> sites = StackAuth.getNameSiteMap();
        for (Entry<String, Site> entry : sites.entrySet()) {
            allSites.put(entry.getKey(), entry.getValue().getStackWrapper(key));
        }
    }

    public User getUser(String site, int id) throws JSONException, IOException {
        return allSites.get(site).getUserById(id);
    }

    public Image getIconBySite(String site) {
        return StackWrapDataAccess.icons.get(site);
    }

    public List<String> getAllSites() throws IOException, JSONException {
        List<String> ret = new ArrayList<String>();
        for (Site s : StackAuth.getAllSites()) {
            ret.add(s.getName());
        }
        return ret;
    }

    public Site getSiteFromName(String name) throws IOException, JSONException {
        return StackAuth.getNameSiteMap().get(name);
    }

    public static String Key = "RhtZB9-r0EKYJi-OjKSRUg";
}

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.stackwrap4j.StackWrapper;
import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.stackauth.StackAuth;
import net.sf.stackwrap4j.stackauth.entities.Site;

public class StackWrapDataAccess {

    private Map<String, StackWrapper> allSites;

    public StackWrapDataAccess() throws IOException, JSONException{
        this(null);
    }
    
    public StackWrapDataAccess(String key) throws IOException, JSONException {
        allSites = new HashMap<String, StackWrapper>();
        Map<String, Site> sites = StackAuth.getNameSiteMap();
        for (Entry<String, Site> entry : sites.entrySet()) {
            allSites.put(entry.getKey(), entry.getValue().getStackWrapper(key));
        }
    }
    
    public User getUser(String site, int id) throws JSONException, IOException{
        return allSites.get(site).getUserById(id);
    }
    
    public List<String> getAllSites() throws IOException, JSONException{
        List<String> ret = new ArrayList<String>();
        for (Site s: StackAuth.getAllSites()){
            ret.add(s.getName());
        }
        return ret;
    }
    
    public static String Key = "RhtZB9-r0EKYJi-OjKSRUg";
}

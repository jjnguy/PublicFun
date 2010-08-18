import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.stackwrap4j.StackWrapper;
import net.sf.stackwrap4j.entities.User;
import net.sf.stackwrap4j.json.JSONException;
import net.sf.stackwrap4j.stackauth.StackAuth;
import net.sf.stackwrap4j.stackauth.entities.Site;

public class StackWrapDataAccess {

    private Map<String, StackWrapper> allSites;

    public StackWrapDataAccess() throws IOException, JSONException {
        allSites = new HashMap<String, StackWrapper>();
        Map<String, Site> sites = StackAuth.getNameSiteMap();
        for (Entry<String, Site> entry : sites.entrySet()) {
            allSites.put(entry.getKey(), entry.getValue().getStackWrapper());
        }
    }
    
    public User getUser(String site, int id) throws JSONException, IOException{
        return allSites.get(site).getUserById(id);
    }
}

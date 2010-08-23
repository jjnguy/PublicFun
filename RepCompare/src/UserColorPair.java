import java.awt.Color;

import net.sf.stackwrap4j.entities.User;

public class UserColorPair {
    public final Color color;
    public final User user;
    public final String site;

    public UserColorPair(User u, String s, Color c) {
        color = c;
        user = u;
        site = s;
    }
}
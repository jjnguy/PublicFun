import java.awt.Color;

import net.sf.stackwrap4j.entities.User;

public class UserColorPair {
    public final Color color;
    public final User user;

    public UserColorPair(User u, Color c) {
        color = c;
        user = u;
    }
}
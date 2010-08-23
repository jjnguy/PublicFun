import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class AddUserInfo extends Info {

    private String userName;
    private JProgressBar progress;

    public AddUserInfo(String userName) {
        this.userName = userName;
        progress = new JProgressBar(0, 100);
        progress.setIndeterminate(true);
        add(new JLabel(this.userName));
        add(progress);
    }

    public void setProgress(int progress) {
        this.progress.setValue(progress);
    }
}

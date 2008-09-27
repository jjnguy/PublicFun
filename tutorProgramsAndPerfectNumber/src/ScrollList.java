import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class ScrollList extends JFrame {
    final int defaultValue = 20;

    ScrollList() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	Container cp = getContentPane();
        JList list = createList();

        cp.add(createScrollPane(list));
        pack();
        list.ensureIndexIsVisible(list.getSelectedIndex());
        
        setVisible(true);
    }

    JList createList() {
        Integer[] model = new Integer[73];
        JList list = new JList(model);
        
        for (int i = 1; i < model.length; i++)
            model[i] = i;
        list.setSelectedIndex(defaultValue);
        return list;
    }

    JScrollPane createScrollPane(JList list) {
        JScrollPane s = new JScrollPane(list);
        s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        s.setPreferredSize(new Dimension(100, 200));
        return s;
    }

    public static void main(String[] args) {
        new ScrollList();
    }
}
package tudbut.tools;

import javax.swing.*;
import java.awt.*;

public class JButtonList {
    public final JPanel pane;
    public final JScrollPane scrollPane;

    public JButtonList(Container component) {
        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane = new JScrollPane(pane);
        component.add(scrollPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
    }

    public void addButton(JButton button, JButtonListRunnable onClick) {
        button.addActionListener(actionEvent -> onClick.run(button, pane, this));
        pane.add(button);
        pane.add(Box.createRigidArea(new Dimension(0,5)));
        pane.doLayout();
        scrollPane.updateUI();
        scrollPane.repaint();
    }

    public interface JButtonListRunnable {
        void run(JButton button, JPanel pane, JButtonList buttonList);
    }
}

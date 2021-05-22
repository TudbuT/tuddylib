package tudbut.tools;

import tudbut.rendering.Maths2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayApp {
    
    private static SystemTray tray;
    static {
        if(SystemTray.isSupported())
            tray = SystemTray.getSystemTray();
    }
    private final TrayIcon trayIcon;
    
    public TrayApp(BufferedImage icon) throws AWTException {
        if(tray == null)
            throw new AWTException("Tray not supported");
        trayIcon = new TrayIcon(Maths2D.distortImage(icon, tray.getTrayIconSize().width, tray.getTrayIconSize().height, 1));
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
    }
    
    public void setImage(BufferedImage image) {
        trayIcon.setImage(Maths2D.distortImage(image, 32, 32, 1));
    }
    
    public void onClick(Runnable runnable) {
        trayIcon.addActionListener((e) -> runnable.run());
    }
}

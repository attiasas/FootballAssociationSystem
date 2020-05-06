package PL.tray;

import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import lombok.extern.log4j.Log4j;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Log4j
public class TrayLoader extends Thread {

    public static void main(String[] args) {
        new TrayLoader().start();
    }

    /**
     * Implementation of {@link SystemTray} using
     * <a href="https://github.com/dorkbox/SystemTray">SystemTray from Dorkbox</a>.
     */
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SystemTray.SWING_UI = new CustomSwingUI();
        SystemTray systemTray = SystemTray.get();
        Desktop desktop = Desktop.getDesktop();
        if (systemTray == null)
            log.warn("System tray is currently not supported.");
        systemTray.setImage(getClass().getResource("/img/icon.png"));
        systemTray.setStatus("Sportify");
        final MenuItem openAppMenuItem = new MenuItem("Open App", e -> JOptionPane
                .showMessageDialog(null, "BOOO", "Sportify on build", JOptionPane.WARNING_MESSAGE));
        systemTray.getMenu().add(openAppMenuItem).setShortcut(1);
        systemTray.getMenu().add(new MenuItem("log History", e -> {
            try {
                desktop.open(new File("log/event_log.log"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }));
        systemTray.getMenu().add(new Separator());
        systemTray.getMenu().add(new MenuItem("Preference...")).setShortcut(3);
        systemTray.getMenu().add(new MenuItem("About")).setShortcut(4);
        systemTray.getMenu().add(new MenuItem("Donate")).setShortcut(5);
        systemTray.getMenu().add(new MenuItem("Quit", e -> systemTray.shutdown())).setShortcut('q');
    }
}

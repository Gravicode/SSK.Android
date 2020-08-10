package p000;

import java.applet.Applet;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* renamed from: AppletFrame */
/* compiled from: svm_toy */
class AppletFrame extends Frame {
    AppletFrame(String title, Applet applet, int width, int height) {
        super(title);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        applet.init();
        applet.setSize(width, height);
        applet.start();
        add(applet);
        pack();
        setVisible(true);
    }
}

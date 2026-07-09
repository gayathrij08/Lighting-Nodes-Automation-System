import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public class StreetLightGUI {
    public static void main(String[] args) {
        applyUiTheme();
        com.streetlight.ui.StreetLightGUI.main(args);
    }

    private static void applyUiTheme() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", new Color(247, 248, 250));
            UIManager.put("info", new Color(247, 248, 250));
            UIManager.put("nimbusBase", new Color(32, 33, 36));
            UIManager.put("nimbusAlertYellow", new Color(248, 195, 59));
            UIManager.put("nimbusDisabledText", new Color(117, 125, 141));
            UIManager.put("nimbusFocus", new Color(73, 129, 232));
            UIManager.put("nimbusGreen", new Color(77, 143, 234));
            UIManager.put("nimbusInfoBlue", new Color(70, 131, 255));
            UIManager.put("nimbusLightBackground", new Color(247, 248, 250));
            UIManager.put("nimbusOrange", new Color(243, 146, 0));
            UIManager.put("nimbusRed", new Color(239, 68, 68));
            UIManager.put("nimbusSelectedText", Color.WHITE);
            UIManager.put("nimbusSelectionBackground", new Color(37, 99, 235));
            UIManager.put("text", new Color(30, 41, 59));
            UIManager.put("Button.background", new Color(37, 99, 235));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Panel.background", new Color(247, 248, 250));
            UIManager.put("Label.font", UIManager.getFont("Label.font").deriveFont(13f));
            UIManager.put("Button.font", UIManager.getFont("Button.font").deriveFont(13f));
        } catch (UnsupportedLookAndFeelException ignored) {
            // Fall back to default LAF when Nimbus is unavailable.
        }
    }
}

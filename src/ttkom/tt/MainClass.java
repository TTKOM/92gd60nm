package ttkom.tt;

import de.muntjak.tinylookandfeel.TinyLookAndFeel;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 21.11.12
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class MainClass {

    public static String version = "1.0.2"; // Версия программы

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        try {
            UIManager.setLookAndFeel(new TinyLookAndFeel());

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new MainWindow();
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Samarth
 */
public class SwingHelper {

    public static boolean showConfirm(String msg, JInternalFrame frame) {
        int ok = JOptionPane.showConfirmDialog(frame, msg, "Alert", JOptionPane.OK_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            return true;
        }
        return false;
    }

    public static void formatJTable(JTable jtable1, int[] columns) {


        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        for (int i = 0; i < jtable1.getRowCount(); i++) {

            for (int j = 0; j < columns.length; j++) {
//                jtable1.setValueAt(StringHelper.rupeeFormat(jtable1.getValueAt(i, columns[j])), i, columns[j]);
                jtable1.getColumnModel().getColumn(columns[j]).setCellRenderer(rightRenderer);


            }
        }
    }
    private static final KeyStroke escapeStroke =
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    public static final String dispatchWindowClosingActionMapKey =
            "com.spodding.tackline.dispatch:WINDOW_CLOSING";

    public static void installEscapeCloseOperation(final JInternalFrame dialog) {
        Action dispatchClosing = new AbstractAction() {

            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(false);
                dialog.dispose();;
//                MainScreen.menuScreen.requestFocus();
            }
        };
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    public static void installEscapeCloseOperation(final JFrame dialog) {
        Action dispatchClosing = new AbstractAction() {

            public void actionPerformed(ActionEvent event) {


                dialog.dispatchEvent(new WindowEvent(
                        dialog, WindowEvent.WINDOW_CLOSING));
//                MainScreen.menuScreen.requestFocus();
            }
        };
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                escapeStroke, dispatchWindowClosingActionMapKey);
        root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
    }

    public static void setTheme() {
        try {
            UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setScreenCenter(Frame f) {
        int w = f.getToolkit().getScreenSize().width;
        int h = f.getToolkit().getScreenSize().height;

        int wid = f.getSize().width;
        int ht = f.getSize().height;

        int x = (w - wid) / 2;
        int y = (h - ht) / 2;

// Move the window
        f.setLocation(x, y);


    }

    public static void setScreenCenter(JInternalFrame f) {
        int w = f.getToolkit().getScreenSize().width;
        int h = f.getToolkit().getScreenSize().height;

        int wid = f.getSize().width;
        int ht = f.getSize().height;

        int x = (w - wid) / 2;
        int y = (h - ht) / 2;

// Move the window
        f.setLocation(x, y);


    }

    public static void setScreenRight(JInternalFrame f) {
        int w = f.getToolkit().getScreenSize().width;
        int h = f.getToolkit().getScreenSize().height;

        int wid = f.getSize().width;
        int ht = f.getSize().height;

        int x = (w - wid);
        int y = 0;

// Move the window
        f.setLocation(x, y);


    }
}

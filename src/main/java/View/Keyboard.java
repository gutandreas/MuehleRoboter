package View;

import javax.swing.*;   // JFrame, JPanel, JLabel, JButton
import java.awt.*;      // GridBagLayout, GridBagConstraints, Insets, Font

public class Keyboard {

    private final JPanel keyboard = new JPanel();
    Color aliceblue = new Color(200, 235, 255);

    private static final String[][] key = {
            {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Backspace"},
            {"Tab", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"},
            {"Caps", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "Enter"},
            {"Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "\u2191"},
            {" ", "<", "\u2193", ">"}
    };

    public Keyboard() {
        keyboard.setLayout(new GridBagLayout());

        Insets zeroInset = new Insets(0, 0, 0, 0);
        Font monospace = new Font(Font.MONOSPACED, Font.PLAIN, 12);

        JPanel pRow;
        JButton b;

        GridBagConstraints cRow = new GridBagConstraints(),
                cButton = new GridBagConstraints();
        cRow.anchor = GridBagConstraints.WEST;
        cButton.ipady = 21;

        // first dimension of the key array
        // representing a row on the keyboard
        for (int row = 0, i = 0; row < key.length; ++row) {
            pRow = new JPanel(new GridBagLayout());
            pRow.setOpaque(false);

            cRow.gridy = row;

            // second dimension representing each key
            for (int col = 0; col < key[row].length; ++col, ++i) {

                // specify padding and insets for the buttons
                switch (key[row][col]) {
                    case "Backspace":   cButton.ipadx = 0; break;
                    case "Tab":         cButton.ipadx = 17; break;
                    case "Caps":        cButton.ipadx = 10; break;
                    case "Enter":       cButton.ipadx = 27; break;
                    case "Shift":       cButton.ipadx = 27; break;
                    case "/":
                        cButton.insets = new Insets(0, 0, 0, 24);
                        break;
                    case " ":
                        cButton.ipadx = 247;
                        cButton.insets = new Insets(0, 192, 0, 72);
                        break;
                    default:
                        cButton.ipadx = 7;
                        cButton.insets = zeroInset;
                }

                b = new JButton(key[row][col]);
                b.setForeground(Color.BLACK);
                b.setFont(monospace);
                b.setFocusable(false);
                pRow.add(b, cButton);
            }

            keyboard.add(pRow, cRow);
            keyboard.setOpaque(false);
        }

    }

    public JPanel getKeyboard() {
        return keyboard;
    }
}
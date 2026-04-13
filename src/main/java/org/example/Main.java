package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import org.example.authenticate.ui.Login;
import org.example.product.ui.ProductUI;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(500, 500);
        frame.setSize(1600, 1400);

        frame.add(new Login(frame),  BorderLayout.CENTER);
        frame.setVisible(true);

    }
}

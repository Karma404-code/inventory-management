package org.example;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;

class Layout {
    static void main() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("Scratch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel name = new JLabel("Name: ");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(name, c);

        JTextField nameField = new JTextField(15);
        // nameField.setSize(100, 30);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(nameField, c);

        JLabel price = new JLabel("Price: ");
        c.gridx = 0;
        c.gridy = 2;
        panel.add(price, c);

        JTextField priceField = new JTextField(15);
        c.gridx = 0;
        c.gridy = 3;
        panel.add(priceField, c);

        DefaultComboBoxModel<String> optionModel = new DefaultComboBoxModel<>();
        optionModel.addElement("1");
        optionModel.addElement("2");
        optionModel.addElement("3");
        JComboBox<String> option = new JComboBox<>(optionModel);

        c.gridx = 0;
        c.gridy = 4;
        panel.add(option, c);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}


package org.example.authenticate.ui;

import net.miginfocom.swing.MigLayout;
import org.example.authenticate.AccountService;
import org.example.product.ui.ProductUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class Login extends JPanel {

    AccountService accountService;

    JFrame parentFrame;
    JPanel loginForm;
    JTextField name;
    JPasswordField pwd;
    JLabel nameLabel, pwdLabel, createAccountLabel;
    JButton loginBtn;

    public Login(JFrame parentFrame) {
        accountService = AccountService.getInstance();
        this.parentFrame = parentFrame;

        loginForm = new JPanel();
        loginForm.setLayout(new MigLayout("insets 10"));

        nameLabel = new JLabel("Name:");
        name = new JTextField(15);

        pwdLabel = new JLabel("Password:");
        pwd = new JPasswordField(15);

        loginBtn = new JButton("login");
        loginBtn.setBackground(new  Color(25, 25, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));

        createAccountLabel = new JLabel("Create Account:");

        loginBtn.addActionListener((e) ->
        {
            String inputName = name.getText();
            String inputPwd = new String(pwd.getPassword());

            if(accountService.authenticate(inputName, inputPwd)) {
                switchToProduct();
            } else {
                // Show error message
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            }

        });

        loginForm.add(nameLabel);
        loginForm.add(name, "wrap");

        loginForm.add(pwdLabel);
        loginForm.add(pwd,  "wrap");

        loginForm.add(loginBtn, "wrap, cell 1 4, growx");
        // loginForm.add(createAccountLabel, "growx");
        add(loginForm, BorderLayout.CENTER);
    }

    private void switchToProduct() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new ProductUI(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

}


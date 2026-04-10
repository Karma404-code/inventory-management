package org.example.authenticate.ui;

import org.example.authenticate.AccountService;
import org.example.product.ui.ProductUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;

public class Login extends JPanel {

    AccountService accountService;

    JFrame parentFrame;
    JTextField name;
    JPasswordField pwd;
    JLabel nameLabel, pwdLabel, createAccountLabel;
    JButton loginBtn;

    public Login(JFrame parentFrame) {
        accountService = AccountService.getInstance();
        this.parentFrame = parentFrame;

        nameLabel = new JLabel("Name:");
        name = new JTextField();

        pwdLabel = new JLabel("Password:");
        pwd = new JPasswordField();

        loginBtn = new JButton("login");
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

        add(nameLabel);
        add(name);

        add(pwdLabel);
        add(pwd);

        add(loginBtn);
    }

    private void switchToProduct() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new ProductUI(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

}


package com.bonche;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

import static com.bonche.AddForm.MAXIMUM_NAME_CHARACTERS;
import static java.lang.Short.*;
import static javax.swing.GroupLayout.*;
import static javax.swing.GroupLayout.Alignment.*;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

/** Created by Petar Bonchev ( 1607262 )**/

public class LoginForm extends JFrame {

    private JButton createNewAccountButton, loginButton;

    private JLabel passwordLabel, usernameLabel;

    private JPasswordField passwordTextField;
    private JTextField usernameTextField;

    private Database database;

    private User user;

    public LoginForm(Database database) {
        this.database = database;
        this.database.getDatabaseList().add(new User("Peterr", "Peter123", 1000, 20));
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * GENERATED GUI -> initComponents()
     **/
    private void initComponents() {

        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");

        loginButton = new JButton("Login");
        createNewAccountButton = new JButton("Create new account!");

        usernameTextField = new JTextField("Peterr", MAXIMUM_NAME_CHARACTERS);
        passwordTextField = new JPasswordField("Peter123", MAXIMUM_NAME_CHARACTERS);

        setOtherComponents();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(TRAILING)
                                        .addComponent(usernameLabel)
                                        .addComponent(passwordLabel))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addGroup(layout.createParallelGroup(LEADING, false)
                                                .addComponent(passwordTextField, DEFAULT_SIZE, 200, MAX_VALUE)
                                                .addComponent(usernameTextField))
                                        .addGroup(layout.createParallelGroup(TRAILING, false)
                                                .addComponent(createNewAccountButton, LEADING, DEFAULT_SIZE, 154, MAX_VALUE)
                                                .addComponent(loginButton, LEADING, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)))
                                .addContainerGap(33, MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(usernameTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(usernameLabel))
                                .addPreferredGap(UNRELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(passwordTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(passwordLabel))
                                .addPreferredGap(UNRELATED)
                                .addComponent(loginButton)
                                .addPreferredGap(RELATED)
                                .addComponent(createNewAccountButton)
                                .addContainerGap(22, MAX_VALUE))
        );
        pack();
    }

    private void setOtherComponents() {
        this.createNewAccountButton.addActionListener(new CreateNewAccountListener());
        this.loginButton.addActionListener(new LoginListener());

        this.loginButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        this.loginButton.getActionMap().put("Enter", loginButtonAction());
    }

    private class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (userCheck()) dispose();
            else JOptionPane.showMessageDialog(null, "Your username or password is incorrect!");
        }
    }

    private class CreateNewAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventQueue.invokeLater(() -> new NewAccount(LoginForm.this).setVisible(true));
            dispose();
        }
    }

    public AbstractAction loginButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.requestFocusInWindow();
                loginButton.doClick();
            }
        };
    }

    public boolean userCheck() {

        for (int i = 0; i < database.getDatabaseList().size(); i++) {
            User aUser = database.getDatabaseList().get(i);
            if (Objects.equals(aUser.getUsername(), usernameTextField.getText())
                    && Objects.equals(aUser.getPassword(), String.valueOf(passwordTextField.getPassword()))) {

                user = database.getDatabaseList().get(i);
                database.setCurrentUser(user);
                EventQueue.invokeLater(() -> new MusicForm(user, LoginForm.this).setVisible(true));
                return true;
            }
            if (i == database.getDatabaseList().size() - 1) {
                passwordTextField.setText("");
            }
        }
        return false;
    }

    public void addUser(String username, String password, int balance, int age) {
        this.database.getDatabaseList().add(new User(username, password, balance, age));
    }

    public ArrayList<User> getUsers() {
        return database.getDatabaseList();
    }

    public Database getDatabase() {
        return database;
    }
}

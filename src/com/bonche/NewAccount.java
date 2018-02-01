package com.bonche;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static com.bonche.AddForm.ZERO_INDEX;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;
import static com.bonche.AddForm.MAXIMUM_AGE_DIGITS;
import static com.bonche.AddForm.MAXIMUM_NAME_CHARACTERS;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.*;

/** Created by Petar Bonchev ( 1607262 )**/


public class NewAccount extends JFrame {

    private final int MAXIMUM_BALANCE_DIGITS = 4;
    private final int MINIMUM_NUMBER_OF_CHARACTERS = 6;

    public final static String CONTAINS_A_CAPITAL_OR_LOWERCASE_LETTER = "[a-zA-Z]+$";

    /**
     * Password verification means that it must have at least:
     * one number,
     * one capital letter,
     * one lowercase letter,
     * and the size should be between
     * (see constants: MINIMUM_NUMBER_OF_CHARACTERS && MAXIMUM_NUMBER_OF_CHARACTERS
     **/

    private final String PASSWORD_VALIDATION_EXPRESSION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])." +
            "{" + MINIMUM_NUMBER_OF_CHARACTERS + "," + MAXIMUM_NAME_CHARACTERS + "}$";
    private final int INITIAL_BALANCE_FOR_NEW_USERS = 444;

    private JLabel balanceLabel;
    private JLabel passwordLabel;
    private JLabel ageLabel;
    private JLabel usernameLabel;

    private JButton createNewAccountButton;

    private JTextField ageTextField;
    private JTextField balanceTextField;
    private JPasswordField passwordTextField;
    private JTextField usernameTextField;

    private LoginForm loginForm;
    private Database database;

    public NewAccount(LoginForm loginForm) {
        this.loginForm = loginForm;
        this.database = loginForm.getDatabase();
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * GENERATED GUI -> initComponents()
     **/
    private void initComponents() {

        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        balanceLabel = new JLabel("Balance");
        ageLabel = new JLabel("Age");

        createNewAccountButton = new JButton("Create new account!");
        this.createNewAccountButton.addActionListener(new NewAccountListener());

        usernameTextField = new JTextField(MAXIMUM_NAME_CHARACTERS);
        passwordTextField = new JPasswordField(MAXIMUM_NAME_CHARACTERS);
        balanceTextField = new JTextField(MAXIMUM_BALANCE_DIGITS);
        ageTextField = new JTextField(MAXIMUM_AGE_DIGITS);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(TRAILING)
                                        .addComponent(createNewAccountButton, LEADING, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                        .addGroup(LEADING, layout.createSequentialGroup()
                                                .addGap(0, 0, MAX_VALUE)
                                                .addComponent(ageLabel, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addPreferredGap(UNRELATED)
                                                .addComponent(ageTextField, PREFERRED_SIZE, 25, PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addComponent(balanceLabel, PREFERRED_SIZE, 53, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .addComponent(balanceTextField, PREFERRED_SIZE, 44, PREFERRED_SIZE))
                                        .addGroup(LEADING, layout.createSequentialGroup()
                                                .addComponent(passwordLabel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                                .addPreferredGap(RELATED)
                                                .addComponent(passwordTextField, PREFERRED_SIZE, 144, PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(usernameLabel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                                .addPreferredGap(RELATED)
                                                .addComponent(usernameTextField, PREFERRED_SIZE, 144, PREFERRED_SIZE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(usernameLabel)
                                        .addComponent(usernameTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addPreferredGap(RELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(ageTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(ageLabel)
                                        .addComponent(balanceLabel)
                                        .addComponent(balanceTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(createNewAccountButton)
                                .addContainerGap(18, MAX_VALUE))
        );
        pack();
    }

    private class NewAccountListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean usernameFlag = true;
            boolean passwordFlag = true;
            String output = "";
            int balance = 0, age = 0;
            String username, password;
            try {
                username = usernameTextField.getText().trim();
                password = String.valueOf(passwordTextField.getPassword());

                if (username.length() < MINIMUM_NUMBER_OF_CHARACTERS || username.length() >= MAXIMUM_NAME_CHARACTERS) {
                    usernameFlag = false;
                    output += "Your username is incorrect, it should be between 6 and " + MAXIMUM_NAME_CHARACTERS + " characters long!";
                }

                if (!Pattern.matches(PASSWORD_VALIDATION_EXPRESSION, password)) {
                    passwordFlag = false;
                    passwordTextField.setText("");
                    output += addNewLineIfTextBefore(output) + "Your password is incorrect, it should contain at least 1 capital letter,\n" +
                            " 1 lowercase letter, 1 digit, and be between 6 and " + MAXIMUM_NAME_CHARACTERS + " characters long!";

                }

                balance = Integer.parseInt(balanceTextField.getText().trim());
                age = Integer.parseInt(ageTextField.getText().trim());

                if (checkIfExists(username)) {
                    usernameFlag = false;
                    output += addNewLineIfTextBefore(output) + "Your username already exists, please choose a different one!";
                }

            } catch (NumberFormatException nfe) {
                usernameFlag = false;
                passwordFlag = false;
                if (Pattern.matches(CONTAINS_A_CAPITAL_OR_LOWERCASE_LETTER, balanceTextField.getText()) || balanceTextField.getText().trim().length() == 0) {
                    output += addNewLineIfTextBefore(output) + "You are supposed to write numbers in the balance field!";
                    balanceTextField.setText("");
                }
                if (Pattern.matches(CONTAINS_A_CAPITAL_OR_LOWERCASE_LETTER, ageTextField.getText()) || ageTextField.getText().trim().length() == 0) {
                    output += addNewLineIfTextBefore(output) + "You are supposed to write numbers in the age field!";
                    ageTextField.setText("");
                }
            }

            if (usernameFlag && passwordFlag) {
                loginForm.addUser(usernameTextField.getText().trim(), String.valueOf(passwordTextField.getPassword()), balance + INITIAL_BALANCE_FOR_NEW_USERS, age);
                dispose();
                JOptionPane.showMessageDialog(new Frame(), "Your account has been successfully created!");
                database.execute(database.createUserTable(usernameTextField.getText().trim()));
                database.execute(database.insertInto(usernameTextField.getText().trim(), String.valueOf(passwordTextField.getPassword())));
                EventQueue.invokeLater(() -> new LoginForm(loginForm.getDatabase()).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(new Frame("Error"), output);
            }
        }
    }

    private boolean checkIfExists(String username) {
        for (int i = 0; i < loginForm.getUsers().size(); i++) {
            if (loginForm.getUsers().get(i).getUsername().equals(username)) return true;
        }
        return false;
    }

    private String addNewLineIfTextBefore(String output) {
        return output.length() > ZERO_INDEX ? "\n" : "";
    }
}

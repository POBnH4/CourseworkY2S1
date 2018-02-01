package com.bonche;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import static com.bonche.MusicForm.POUND_SYMBOL;
import static com.bonche.NewAccount.CONTAINS_A_CAPITAL_OR_LOWERCASE_LETTER;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

/** Created by Petar Bonchev ( 1607262 )**/

public class TopUpAccount extends JFrame {

    private AmountChooser amountChooser;

    private final int CREDIT_CARD_NUMBERS_LENGTH = 16;
    private final int CVV_NUMBERS_LENGTH = 3;

    private JLabel CVVLabel, cardNumberLabel, expirationDateLabel, imageIcon;
    private JTextField CVVTextField, cardNumberTextField;
    private JComboBox<String> monthComboBox, yearComboBox;
    private JButton submit;

    public TopUpAccount(AmountChooser amountChooser) {
        this.amountChooser = amountChooser;
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * GENERATED GUI -> initComponents()
     **/
    private void initComponents() {

        this.cardNumberLabel = new JLabel("Card number:");
        this.expirationDateLabel = new JLabel("Expiration date:");
        this.CVVLabel = new JLabel("CVV:");
        this.imageIcon = new JLabel(new ImageIcon("ibmsecurity_1151678.jpg"));

        this.cardNumberTextField = new JTextField(CREDIT_CARD_NUMBERS_LENGTH);
        this.CVVTextField = new JTextField(CVV_NUMBERS_LENGTH);

        this.monthComboBox = new JComboBox<>();
        this.yearComboBox = new JComboBox<>();

        this.submit = new JButton("submit");
        this.submit.addActionListener(new SubmitListener());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.monthComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

        this.yearComboBox.setModel(new DefaultComboBoxModel<>(
                new String[]{"2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"}));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(TRAILING)
                                                        .addComponent(this.CVVLabel)
                                                        .addComponent(this.expirationDateLabel)
                                                        .addComponent(this.cardNumberLabel))
                                                .addPreferredGap(UNRELATED)
                                                .addGroup(layout.createParallelGroup(LEADING)
                                                        .addComponent(this.submit, PREFERRED_SIZE, 99, PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(this.monthComboBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                                                .addPreferredGap(RELATED)
                                                                .addComponent(this.yearComboBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                                        .addComponent(this.CVVTextField, PREFERRED_SIZE, 39, PREFERRED_SIZE)
                                                        .addComponent(this.cardNumberTextField, PREFERRED_SIZE, 174, PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(119, 119, 119)
                                                .addComponent(this.imageIcon, PREFERRED_SIZE, 63, PREFERRED_SIZE)))
                                .addContainerGap(21, MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(this.imageIcon, PREFERRED_SIZE, 45, PREFERRED_SIZE)
                                .addPreferredGap(UNRELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(this.cardNumberTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(this.cardNumberLabel))
                                .addPreferredGap(RELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(this.CVVLabel)
                                        .addComponent(this.CVVTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addPreferredGap(UNRELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(this.monthComboBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(this.yearComboBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(this.expirationDateLabel))
                                .addGap(18, 18, 18)
                                .addComponent(this.submit)
                                .addContainerGap(DEFAULT_SIZE, MAX_VALUE))
        );
        pack();
    }

    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (checkDetails(CVVTextField, cardNumberTextField)) {
                amountChooser.getMusicForm().setNewTotalBalance(amountChooser.getAmount());
                dispose();
                JOptionPane.showMessageDialog(null, "You have successfully topped up with " + amountChooser.getAmount() + POUND_SYMBOL);
                EventQueue.invokeLater(() -> amountChooser.getMusicForm().setVisible(true));
            }
        }
    }

    public boolean checkDetails(JTextField CVVTextField, JTextField cardNumberTextField) {
        if (cardNumberTextField.getText().length() != CREDIT_CARD_NUMBERS_LENGTH) {
            JOptionPane.showMessageDialog(null, "You are supposed to write " + CREDIT_CARD_NUMBERS_LENGTH + " numbers in the credit card text field!");
            return false;
        }
        if (CVVTextField.getText().length() != CVV_NUMBERS_LENGTH) {
            JOptionPane.showMessageDialog(null, "You are supposed to write " + CVV_NUMBERS_LENGTH + " numbers in the CVV text field!");
            return false;
        }
        if (Pattern.matches(CONTAINS_A_CAPITAL_OR_LOWERCASE_LETTER, cardNumberTextField.getText())) {
            JOptionPane.showMessageDialog(null, "You are not supposed to write letters in credit card text field!");
            return false;
        }
        if (Pattern.matches(CONTAINS_A_CAPITAL_OR_LOWERCASE_LETTER, CVVTextField.getText())) {
            JOptionPane.showMessageDialog(null, "You are not supposed to write letters in the CVV text field!");
            return false;
        }
        return true;
    }

}

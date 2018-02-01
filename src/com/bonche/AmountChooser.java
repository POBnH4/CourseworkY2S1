package com.bonche;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

/** Created by Petar Bonchev ( 1607262 )**/

public class AmountChooser extends JFrame {

    private JButton fiveButton, tenButton, fifteenButton, twentyButton, twentyFiveButton, thirtyButton;
    private JLabel infoLabel;
    private MusicForm musicForm;
    private boolean amountIsSelected = false;
    private int amount = 0;

    public AmountChooser(MusicForm musicForm) {
        this.musicForm = musicForm;
        initComponents();
        this.setLocationRelativeTo(null);

    }

    private void initComponents() {

        fiveButton = new JButton("5");
        tenButton = new JButton("10");
        fifteenButton = new JButton("15");
        twentyButton = new JButton("20");
        twentyFiveButton = new JButton("25");
        thirtyButton = new JButton("30");

        infoLabel = new JLabel("Please choose with what amount would you like to top-up!");

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        fiveButton.setFont(new Font("Tahoma", 0, 48)); // NOI18N
        tenButton.setFont(new Font("Tahoma", 0, 48)); // NOI18N
        fifteenButton.setFont(new Font("Tahoma", 0, 48)); // NOI18N
        twentyButton.setFont(new Font("Tahoma", 0, 48)); // NOI18N
        twentyFiveButton.setFont(new Font("Tahoma", 0, 48)); // NOI18N
        thirtyButton.setFont(new Font("Tahoma", 0, 48)); // NOI18N
        infoLabel.setFont(new Font("Tahoma", 1, 11)); // NOI18N

        setOtherComponents();
        setButtonsBackgroundLightGray();

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(LEADING, false)
                                        .addComponent(fiveButton, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                        .addComponent(twentyButton, DEFAULT_SIZE, 112, MAX_VALUE))
                                .addPreferredGap(RELATED)
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addComponent(twentyFiveButton, PREFERRED_SIZE, 122, PREFERRED_SIZE)
                                        .addComponent(tenButton, PREFERRED_SIZE, 122, PREFERRED_SIZE))
                                .addPreferredGap(RELATED)
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addComponent(thirtyButton, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                        .addComponent(fifteenButton, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE))
                                .addContainerGap())
                        .addGroup(TRAILING, layout.createSequentialGroup()
                                .addContainerGap(55, MAX_VALUE)
                                .addComponent(infoLabel)
                                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(TRAILING, false)
                                        .addComponent(tenButton, LEADING, DEFAULT_SIZE, 100, MAX_VALUE)
                                        .addComponent(fifteenButton, LEADING, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                        .addComponent(fiveButton, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE))
                                .addPreferredGap(RELATED)
                                .addComponent(infoLabel)
                                .addPreferredGap(RELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(twentyButton, PREFERRED_SIZE, 101, PREFERRED_SIZE)
                                        .addComponent(twentyFiveButton, PREFERRED_SIZE, 101, PREFERRED_SIZE)
                                        .addComponent(thirtyButton, PREFERRED_SIZE, 101, PREFERRED_SIZE))
                                .addContainerGap(20, MAX_VALUE))
        );
        pack();
    }

    private void setOtherComponents() {
        this.fiveButton.addActionListener(new FiveButtonListener());
        this.tenButton.addActionListener(new TenButtonListener());
        this.fifteenButton.addActionListener(new FifteenButtonListener());
        this.twentyButton.addActionListener(new TwentyButtonListener());
        this.twentyFiveButton.addActionListener(new TwentyFiveButtonListener());
        this.thirtyButton.addActionListener(new ThirtyButtonListener());

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyboardDispatcher());
    }

    private class KeyboardDispatcher implements KeyEventDispatcher {

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (amountIsSelected) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    EventQueue.invokeLater(() -> new TopUpAccount(AmountChooser.this).setVisible(true));
                    dispose();
                }
            }
            return false;
        }
    }

    private class FiveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsBackgroundLightGray();
            amount = Integer.parseInt(fiveButton.getText());
            fiveButton.setBackground(new Color(255, 87, 51));
            buttonConsequence();
        }
    }

    private class TenButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsBackgroundLightGray();
            amount = Integer.parseInt(tenButton.getText());
            tenButton.setBackground(new Color(255, 87, 51));
            buttonConsequence();
        }
    }

    private class FifteenButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsBackgroundLightGray();
            amount = Integer.parseInt(fifteenButton.getText());
            fifteenButton.setBackground(new Color(255, 87, 51));
            buttonConsequence();
        }
    }

    private class TwentyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsBackgroundLightGray();
            amount = Integer.parseInt(twentyButton.getText());
            twentyButton.setBackground(new Color(255, 87, 51));
            buttonConsequence();
        }
    }

    private class TwentyFiveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsBackgroundLightGray();
            amount = Integer.parseInt(twentyFiveButton.getText());
            twentyFiveButton.setBackground(new Color(255, 87, 51));
            buttonConsequence();
        }
    }

    private class ThirtyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setButtonsBackgroundLightGray();
            amount = Integer.parseInt(thirtyButton.getText());
            thirtyButton.setBackground(new Color(255, 87, 51));
            buttonConsequence();
        }
    }

    public void buttonConsequence() {
        infoLabel.setText("Great! Now please press ENTER to continue to next stage!");
        amountIsSelected = true;
    }

    public void setButtonsBackgroundLightGray() {
        fiveButton.setBackground(Color.LIGHT_GRAY);
        tenButton.setBackground(Color.LIGHT_GRAY);
        fifteenButton.setBackground(Color.LIGHT_GRAY);
        twentyButton.setBackground(Color.LIGHT_GRAY);
        twentyFiveButton.setBackground(Color.LIGHT_GRAY);
        thirtyButton.setBackground(Color.LIGHT_GRAY);
    }

    public int getAmount() {
        return amount;
    }

    public MusicForm getMusicForm() {
        return musicForm;
    }
}

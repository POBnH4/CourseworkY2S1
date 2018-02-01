package com.bonche;

import java.awt.*;

/** Created by Petar Bonchev ( 1607262 )**/

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        EventQueue.invokeLater(() -> new LoginForm(database).setVisible(true));
    }
}

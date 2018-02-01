package com.bonche;

import java.awt.*;

/** Created by Petar Bonchev ( 1607262 )**/

public class Main {
    public static void main(String[] args) {
        /** Before continuing READ the README file,
         *  all the information + comments about the program are in it!**/
        Database database = new Database();
        EventQueue.invokeLater(() -> new LoginForm(database).setVisible(true));
    }
}

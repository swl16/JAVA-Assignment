package cinema;

import javax.swing.*;

public class Cinema {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage();
        }); 
    }
    
}

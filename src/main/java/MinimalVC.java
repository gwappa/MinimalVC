/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import javax.swing.JFrame;

public class MinimalVC {
    static final String TITLE_EMPTY = "(MinimalVC) <no repository selected>";

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(600, 400);
        frame.setLocation(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(TITLE_EMPTY);
        frame.setVisible(true);
    }
}
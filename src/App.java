
import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // mention of the window in pixel
        int boardWidth = 360;
        int boardHeight = 640;
        ImageIcon logo = new ImageIcon("Image_URL");

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setIconImage(logo.getImage());

        //centers the JFrame window on the screen when it is displayed
        frame.setLocationRelativeTo(null);

        //JFrame window cannot be resized by the user
        frame.setResizable(false);

        // argument: user closes window -> the app exists
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird1 flappyBird = new FlappyBird1();
        frame.add(flappyBird);
        
        frame.pack();
        
        flappyBird.requestFocus();
        // ensures that the frame is displayed on the screen
        frame.setVisible(true);
    }
}

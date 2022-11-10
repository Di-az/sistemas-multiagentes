import javax.swing.JFrame;
import javax.swing.JPanel;

class Loader{
      public static void main(String[] args) {
      KeyboardAndFocusDemo applet = new KeyboardAndFocusDemo();
      applet.init();

      // Construct a JFrame.
      final JFrame frame = new JFrame("Keyboard and Focus Demo");

      // Transfer the applet's context pane to the JFrame.
      frame.setContentPane(applet.getContentPane());

      // Transfer the applet's menu bar into the JFrame.
      // This line can be omitted if the applet
      // does not create a menu bar.
      frame.setJMenuBar(applet.getJMenuBar());

      // Make the application shut down when the user clicks
      // on the close button.
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Set the size of the frame.
      // To pack the frame as tightly as possible
      // replace the setSize() message with the following.
      // frame.pack();
      frame.setSize(800, 600);

      // Set the location of the frame.
      frame.setLocation(100, 100);

      // Show the frame.
      frame.setVisible(true);

      // Invoke the applet's start() method.
      // This line can be omitted if the applet
      // does not define a start method.
      applet.start();

    }

}
// Programa que renderiza un wireframe de esfera en base al numero de meridianos y paralelos
// Carlos Daniel Diaz Arrazate - A01734902
// Jose Angel Gonzalez Carrera - A01552274
// Carlos Eduardo Ruiz Lira - A01735706
// 16/11/22

import javax.swing.JFrame;
import javax.swing.JPanel;

class Esfera{
      public static void main(String[] args) {
            WireframeJApplet applet = new WireframeJApplet();
            applet.init(Float.valueOf(args[0]),Integer.valueOf(args[1]),Integer.valueOf(args[2]));
            final JFrame frame = new JFrame("Tarea Esfera WireFrame");
            frame.setContentPane(applet.getContentPane());
            frame.setJMenuBar(applet.getJMenuBar());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocation(100, 100);
            frame.setVisible(true);
            applet.start();
      }
}
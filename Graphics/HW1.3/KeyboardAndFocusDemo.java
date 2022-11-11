// Programa que renderiza las transformaciones hechas a un avion
// Carlos Daniel Diaz Arrazate - A01734902
// Jose Angel Gonzalez Carrera - A01552274
// Carlos Eduardo Ruiz Lira - A01735706
// 11/11/22

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KeyboardAndFocusDemo extends JApplet
      implements KeyListener, FocusListener, MouseListener {
   // (Note: MouseListener is implemented only so that
   // the applet can request the input focus when
   // the user clicks on it.)

   Color planeColor; // The color of the square.

   double rotation;
   float scale;

   float centroid_x, centroid_y, centroid_z;

   float[] plane_x, plane_y, plane_z; // Coordinates of transformed plane

   float[] original_plane_x, original_plane_y, original_plane_z; // Coordinates of original plane

   boolean focussed = false; // True when this applet has input focus.

   DisplayPanel canvas; // The drawing surface on which the applet draws,
                        // belonging to a nested class DisplayPanel, which
                        // is defined below.

   public void init() {
      // Initialize the applet; set it up to receive keyboard
      // and focus events. Place the square in the middle of
      // the applet, and make the initial color of the square red.
      // Then, set up the drawing surface.
      rotation = 0;
      scale = 1;
      centroid_x = 59.2409475603279f;
      centroid_y = 76.0100070402073f;
      centroid_z = 1f;

      plane_x = new float[] { 20, 20, 28, 9, 9, 26, 34, 28, 28, 105, 110, 105, 60, 55,
            55, 60, 60, 55, 63, 156, 165, 165, 156, 63, 55, 60, 60, 55, 55, 60, 105, 110,
            105, 39, 28, 28, 34, 26, 9, 9, 28, 20, 20 };

      plane_y = new float[] { 75, 84, 91, 91, 99, 99, 139, 139, 144, 144, 141, 139, 139,
            136, 100, 100, 91, 88, 86, 80, 77, 74, 70, 65, 62, 60, 51, 49, 13, 11,
            11, 9, 7, 7, 8, 11, 11, 51, 51, 59, 59, 65, 75 };

      plane_z = new float[] { 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1 };

      original_plane_x = new float[43];
      original_plane_y = new float[43];
      original_plane_z = new float[43];

      for (int i = 0; i < 43; i++) {
         original_plane_x[i] = plane_x[i];
         original_plane_y[i] = plane_y[i];
         original_plane_z[i] = plane_z[i];
      }

      setSize(1000, 600);

      planeColor = Color.red;

      canvas = new DisplayPanel(); // Create drawing surface and
      setContentPane(canvas); // install it as the applet's content pane.

      canvas.setBackground(Color.white); // Set the background color of the canvas.

      canvas.addFocusListener(this); // Set up the applet to listen for events
      canvas.addKeyListener(this); // from the canvas.
      canvas.addMouseListener(this);

   } // end init();

   class DisplayPanel extends JPanel {
      // An object belonging to this nested class is used as
      // the content pane of the applet. It displays the
      // moving square on a white background with a border
      // that changes color depending on whether this
      // component has the input focus or not.

      public void paintComponent(Graphics g) {

         super.paintComponent(g); // Fills the panel with its
                                  // background color, which is white.

         /*
          * Draw a 3-pixel border, colored cyan if the applet has the
          * keyboard focus, or in light gray if it does not.
          */

         if (focussed)
            g.setColor(Color.cyan);
         else
            g.setColor(Color.lightGray);

         int width = getSize().width; // Width of the applet.
         int height = getSize().height; // Height of the applet.
         g.drawRect(0, 0, width - 1, height - 1);
         g.drawRect(1, 1, width - 3, height - 3);
         g.drawRect(2, 2, width - 5, height - 5);

         /* Draw the plane. */
         g.setColor(planeColor);

         int[] x = new int[43];
         int[] y = new int[43];

         for (int i = 0; i < 43; i++) {
            x[i] = (int) plane_x[i];
            y[i] = (int) plane_y[i];
         }

         g.drawPolyline(x, y, 43);

         // g.setColor(Color.MAGENTA);
         // g.drawPolyline(mem_x, mem_y, 43);
         // g.drawLine((int) centroid_x, (int) centroid_y, (int) centroid_x, (int)
         // centroid_y);

         /* If the applet does not have input focus, print a message. */
         if (!focussed) {
            g.setColor(Color.magenta);
            g.drawString("Click to activate", 7, 20);
         }

      } // end paintComponent()

   } // end nested class DisplayPanel

   // ------------------- Event handling methods ----------------------
   public void focusGained(FocusEvent evt) {
      // The applet now has the input focus.
      focussed = true;
      canvas.repaint(); // redraw with cyan border
   }

   public void focusLost(FocusEvent evt) {
      // The applet has now lost the input focus.
      focussed = false;
      canvas.repaint(); // redraw without cyan border
   }

   public void keyTyped(KeyEvent evt) {
      // The user has typed a character, while the
      // applet has the input focus. If it is one
      // of the keys that represents a color, change
      // the color of the square and redraw the applet.

      char ch = evt.getKeyChar(); // The character typed.

      if (ch == 'B' || ch == 'b') {
         planeColor = Color.blue;
         canvas.repaint();
      } else if (ch == 'G' || ch == 'g') {
         planeColor = Color.green;
         canvas.repaint();
      } else if (ch == 'R' || ch == 'r') {
         planeColor = Color.red;
         canvas.repaint();
      } else if (ch == 'K' || ch == 'k') {
         planeColor = Color.black;
         canvas.repaint();
      }

   } // end keyTyped()

   public void calculate() {

      // Move centroid from point P to origin
      // Scale figure
      // Rotate figure
      // return centroid to positon P
      ////Time Complexity O(1) (There are only 43 elements, or 0(n) depending on the number of points given)
      
      for (int i = 0; i < 43; i++) {
         // Translation
         // [x']   | 1 0 dx |    | x |  | x + dx|
         // [y'] = | 0 1 dy | x | y | = | y + dy|
         // [1]    | 0 0 1  |   | 1 | = | 1 |
         original_plane_x[i] = original_plane_x[i] - centroid_x;
         original_plane_y[i] = original_plane_y[i] - centroid_y;
         original_plane_z[i] = 1;

         // Scaling
         // [x']   | sx 0 0 |    | x |  | x *sx|
         // [y'] = | 0 xy 0 | x | y | = | y*sy|
         // [1]    | 0 0 1  |   | 1 | = | 1 |

         plane_x[i] = original_plane_x[i] * scale;
         plane_y[i] = original_plane_y[i] * scale;
         plane_z[i] = 1;

         // Rotation
         // [x']   | cos -sin 0 |    | x |  | cosx-siny|
         // [y'] = | sin cos 0 | x | y | = | sinx+cosy|
         // [1]    | 0    0  1 |   | 1 | = | 1 |
         
         float temp_x = plane_x[i];
         plane_x[i] = (float) ((Math.cos(Math.toRadians(rotation)) * plane_x[i])
               - (Math.sin(Math.toRadians(rotation)) * plane_y[i]));
         plane_y[i] = (float) ((Math.sin(Math.toRadians(rotation)) * temp_x)
               + (Math.cos(Math.toRadians(rotation)) * plane_y[i]));
         plane_z[i] = 1;
         
         plane_x[i] = plane_x[i] + centroid_x;
         plane_y[i] = plane_y[i] + centroid_y;
         plane_z[i] = 1;
 
         original_plane_x[i] = original_plane_x[i] + centroid_x;
         original_plane_y[i] = original_plane_y[i] + centroid_y;
         original_plane_z[i] = 1;
      }

   }

   public void keyPressed(KeyEvent evt) {
      // Called when the user has pressed a key, which can be
      // a special key such as an arrow key. If the key pressed
      // was one of the arrow keys, move the square (but make sure
      // that it doesn't move off the edge, allowing for a
      // 3-pixel border all around the applet).

      int key = evt.getKeyCode(); // keyboard code for the key that was pressed

      if (key == KeyEvent.VK_LEFT) {
         calculate();

         double radian = Math.toRadians(180 - (90 + rotation));

         for (int i = 0; i < 43; i++) {
            plane_x[i] -= (float) 8 * (-Math.cos(radian));
            plane_y[i] -= (float) 8 * Math.sin(radian);
            plane_z[i] = 1;
            original_plane_x[i] -= (float) 8 * (-Math.cos(radian));
            original_plane_y[i] -= (float) 8 * Math.sin(radian);
            original_plane_z[i] = 1;
         }

         centroid_x -= (float) 8 * (-Math.cos(radian));
         centroid_y -= (float) 8 * Math.sin(radian);
         centroid_z = 1;


         canvas.repaint();
      } else if (key == KeyEvent.VK_RIGHT) {

         calculate();

         double radian = Math.toRadians(180 - (90 + rotation));

         for (int i = 0; i < 43; i++) {
            plane_x[i] += (float) 8 * (-Math.cos(radian));
            plane_y[i] += (float) 8 * Math.sin(radian);
            plane_z[i] = 1;
            original_plane_x[i] += (float) 8 * (-Math.cos(radian));
            original_plane_y[i] += (float) 8 * Math.sin(radian);
            original_plane_z[i] = 1;
         }

         centroid_x += (float) 8 * (-Math.cos(radian));
         centroid_y += (float) 8 * Math.sin(radian);
         centroid_z = 1;

         canvas.repaint();
      } else if (key == KeyEvent.VK_UP) {

         calculate();

         double radians = Math.toRadians(rotation);

         for (int i = 0; i < 43; i++) {
            plane_x[i] += (float) 8 * Math.cos(radians);
            plane_y[i] += (float) 8 * Math.sin(radians);
            plane_z[i] = 1;
            original_plane_x[i] += (float) 8 * Math.cos(radians);
            original_plane_y[i] += (float) 8 * Math.sin(radians);
            original_plane_z[i] = 1;
         }

         centroid_x += (float) 8 * Math.cos(radians);
         centroid_y += (float) 8 * Math.sin(radians);
         centroid_z = 1;


         canvas.repaint();
      } else if (key == KeyEvent.VK_DOWN) {

         calculate();

         double radians = Math.toRadians(rotation);

         for (int i = 0; i < 43; i++) {
            plane_x[i] -= (float) 8 * Math.cos(radians);
            plane_y[i] -= (float) 8 * Math.sin(radians);
            plane_z[i] = 1;
            original_plane_x[i] -= (float) 8 * Math.cos(radians);
            original_plane_y[i] -= (float) 8 * Math.sin(radians);
            original_plane_z[i] = 1;
         }

         centroid_x -= (float) 8 * Math.cos(radians);
         centroid_y -= (float) 8 * Math.sin(radians);
         centroid_z = 1;


         canvas.repaint();
      } else if (key == KeyEvent.VK_R || key == KeyEvent.VK_F) {

         if (key == KeyEvent.VK_R) {
            if (scale > 2)
               return;
            scale += 0.1;
         } else {
            if (scale < 0.4)
               return;
            scale -= 0.1;
         }

         calculate();

         canvas.repaint();
      } else if (key == KeyEvent.VK_E || key == KeyEvent.VK_D) {
         if (key == KeyEvent.VK_D) {
            if (rotation == 350)
               rotation = 0;
            else
               rotation += 10;
         } else {
            if (rotation == 0)
               rotation = 350;
            else
               rotation -= 10;
         }

         calculate();

         canvas.repaint();
      }
   } // end keyPressed()

   public void keyReleased(KeyEvent evt) {
      // empty method, required by the KeyListener Interface
   }

   public void mousePressed(MouseEvent evt) {
      // Request that the input focus be given to the
      // canvas when the user clicks on the applet.
      canvas.requestFocus();
   }

   public void mouseEntered(MouseEvent evt) {
   } // Required by the

   public void mouseExited(MouseEvent evt) {
   } // MouseListener

   public void mouseReleased(MouseEvent evt) {
   } // interface.

   public void mouseClicked(MouseEvent evt) {
   }

} // end class KeyboardAndFocusDemo

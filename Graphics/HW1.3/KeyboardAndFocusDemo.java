import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KeyboardAndFocusDemo extends JApplet 
                  implements KeyListener, FocusListener, MouseListener {
                      // (Note:  MouseListener is implemented only so that
                      //         the applet can request the input focus when
                      //         the user clicks on it.)

   static final int SQUARE_SIZE = 40;  // Length of a side of the square.
   
   Color squareColor;  // The color of the square.
   
   double rotation;
   
   float centroid_x,centroid_y;

   float[] plane_x, plane_y;  // Coordinates of top-left corner of square.

   float[] mem_plane_x, mem_plane_y;
   
   boolean focussed = false;   // True when this applet has input focus.
   
   DisplayPanel canvas;  // The drawing surface on which the applet draws,
                         // belonging to a nested class DisplayPanel, which
                         // is defined below.

   public void init() {
        // Initialize the applet; set it up to receive keyboard
        // and focus events.  Place the square in the middle of
        // the applet, and make the initial color of the square red.
        // Then, set up the drawing surface.

        rotation = 0;
        centroid_x = 59.2409475603279f;
        centroid_y = 76.0100070402073f;
        
        plane_x = new float[] {
         20,
         20,
         28,
         9,
         9,
         26,
         34,
         28,
         28,
         105,
         110,
         105,
         60,
         55,
         55,
         60,
         60,
         55,
         63,
         156,
         165,
         165,
         156,
         63,
         55,
         60,
         60,
         55,
         55,
         60,
         105,
         110,
         105,
         39,
         28,
         28,
         34,
         26,
         9,
         9,
         28,
         20,
         20};

      plane_y = new float[] {
         75,
         84,
         91,
         91,
         99,
         99,
         139,
         139,
         144,
         144,
         141,
         139,
         139,
         136,
         100,
         100,
         91,
         88,
         86,
         80,
         77,
         74,
         70,
         65,
         62,
         60,
         51,
         49,
         13,
         11,
         11,
         9,
         7,
         7,
         8,
         11,
         11,
         51,
         51,
         59,
         59,
         65,
         75};

      mem_plane_x = new float[43];
      mem_plane_y = new float[43];

      for(int i = 0; i < 43; i++){
         mem_plane_x[i] = plane_x[i];
         mem_plane_y[i] = plane_y[i];
      }

      setSize(800,600); 
      
      squareColor = Color.red;
      
      canvas = new DisplayPanel();  // Create drawing surface and 
      setContentPane(canvas);       //    install it as the applet's content pane.

      canvas.setBackground(Color.white);  // Set the background color of the canvas.

      canvas.addFocusListener(this);   // Set up the applet to listen for events
      canvas.addKeyListener(this);     //                          from the canvas.
      canvas.addMouseListener(this);
      
   } // end init();
   
   
   class DisplayPanel extends JPanel {
          // An object belonging to this nested class is used as
          // the content pane of the applet.  It displays the
          // moving square on a white background with a border
          // that changes color depending on whether this 
          // component has the input focus or not.
   
      public void paintComponent(Graphics g) {
      
         super.paintComponent(g);  // Fills the panel with its
                                   // background color, which is white.

         /* Draw a 3-pixel border, colored cyan if the applet has the
            keyboard focus, or in light gray if it does not. */

         if (focussed) 
            g.setColor(Color.cyan);
         else
            g.setColor(Color.lightGray);
         
         

         int width = getSize().width;  // Width of the applet.
         int height = getSize().height; // Height of the applet.
         g.drawRect(0,0,width-1,height-1);
         g.drawRect(1,1,width-3,height-3);
         g.drawRect(2,2,width-5,height-5);

         /* Draw the square. */
         
         g.setColor(squareColor);


         int[] x = new int[43];
         int[] y = new int[43];
         // int[] mem_x = new int[43];
         // int[] mem_y = new int[43];

         for(int i=0; i < 43; i++){
            x[i] = (int) plane_x[i];
            y[i] = (int) plane_y[i];
            // mem_x[i] = (int) mem_plane_x[i];
            // mem_y[i] = (int) mem_plane_y[i];
         }

         g.drawPolyline(x, y, 43);
         // g.setColor(Color.MAGENTA);
         // g.drawPolyline(mem_x, mem_y, 43);
         g.drawLine((int) centroid_x, (int) centroid_y, (int) centroid_x, (int) centroid_y);

         /* If the applet does not have input focus, print a message. */

         if (!focussed) {
            g.setColor(Color.magenta);
            g.drawString("Click to activate",7,20);
         }

      }  // end paintComponent()
    
    } // end nested class DisplayPanel 
    
    

   // ------------------- Event handling methods ----------------------
   
   public void focusGained(FocusEvent evt) {
         // The applet now has the input focus.
      focussed = true;
      canvas.repaint();  // redraw with cyan border
   }
   

   public void focusLost(FocusEvent evt) {
         // The applet has now lost the input focus.
      focussed = false;
      canvas.repaint();  // redraw without cyan border
   }
   
   
   public void keyTyped(KeyEvent evt) {
          // The user has typed a character, while the
          // applet has the input focus.  If it is one
          // of the keys that represents a color, change
          // the color of the square and redraw the applet.
          
      char ch = evt.getKeyChar();  // The character typed.

      if (ch == 'B' || ch == 'b') {
         squareColor = Color.blue;
         canvas.repaint();
      }
      else if (ch == 'G' || ch == 'g') {
         squareColor = Color.green;
         canvas.repaint();
      }
      else if (ch == 'R' || ch == 'r') {
         squareColor = Color.red;
         canvas.repaint();
      }
      else if (ch == 'K' || ch == 'k') {
         squareColor = Color.black;
         canvas.repaint();
      }

   }  // end keyTyped()
   
   
   public void keyPressed(KeyEvent evt) { 
          // Called when the user has pressed a key, which can be
          // a special key such as an arrow key.  If the key pressed
          // was one of the arrow keys, move the square (but make sure
          // that it doesn't move off the edge, allowing for a 
          // 3-pixel border all around the applet).
          
      int key = evt.getKeyCode();  // keyboard code for the key that was pressed
      
      if (key == KeyEvent.VK_LEFT) {
         
         for(int i = 0; i < 43; i++){
            plane_x[i] = mem_plane_x[i];
            plane_y[i] = mem_plane_y[i];
         }

         double radians = Math.toRadians(rotation);
         
         for(int i = 0; i < 43; i++){
            plane_x[i] -= (float) 8 * Math.cos(radians);
            plane_y[i] -= (float) 8 * Math.sin(radians);
            mem_plane_x[i] = plane_x[i];
            mem_plane_y[i] = plane_y[i];
         }

         centroid_x -= (float) 8 * Math.cos(radians);
         centroid_y -= (float) 8 * Math.sin(radians);
         
         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            float temp_x = plane_x[i];
            plane_x[i] = (float) ((Math.cos(radians)*plane_x[i])-(Math.sin(radians)*plane_y[i]));
            plane_y[i] = (float) ((Math.sin(radians)*temp_x)+(Math.cos(radians)*plane_y[i]));
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
         }

         canvas.repaint();
      }
      else if (key == KeyEvent.VK_RIGHT) {
         for(int i = 0; i < 43; i++){
            plane_x[i] = mem_plane_x[i];
            plane_y[i] = mem_plane_y[i];
         }

         double radians = Math.toRadians(rotation);
         
         for(int i = 0; i < 43; i++){
            plane_x[i] +=  (float) 8 * Math.cos(radians);
            plane_y[i] += (float) 8 * Math.sin(radians);
            mem_plane_x[i] = plane_x[i];
            mem_plane_y[i] = plane_y[i];
         }

         centroid_x += (float) 8 * Math.cos(radians);
         centroid_y += (float) 8 * Math.sin(radians);
         
         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            float temp_x = plane_x[i];
            plane_x[i] = (float) ((Math.cos(radians)*plane_x[i])-(Math.sin(radians)*plane_y[i]));
            plane_y[i] = (float) ((Math.sin(radians)*temp_x)+(Math.cos(radians)*plane_y[i]));
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
         }

         canvas.repaint();
      }
      else if (key == KeyEvent.VK_UP) {
         for(int i = 0; i < 43; i++){
            plane_x[i] = mem_plane_x[i];
            plane_y[i] = mem_plane_y[i];
         }

         double radian = Math.toRadians(180-(90+rotation));

         for(int i = 0; i < 43; i++){
            plane_x[i] +=  (float) 8 * (-Math.cos(radian));
            plane_y[i] += (float) 8 * Math.sin(radian);
            mem_plane_x[i] = plane_x[i];
            mem_plane_y[i] = plane_y[i];
         }

         centroid_x += (float) 8 * (-Math.cos(radian));
         centroid_y += (float) 8 * Math.sin(radian);
         
         double radians = Math.toRadians(rotation);

         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            float temp_x = plane_x[i];
            plane_x[i] = (float) ((Math.cos(radians)*plane_x[i])-(Math.sin(radians)*plane_y[i]));
            plane_y[i] = (float) ((Math.sin(radians)*temp_x)+(Math.cos(radians)*plane_y[i]));
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
         }


         canvas.repaint();
      }
      else if (key == KeyEvent.VK_DOWN) {

         for(int i = 0; i < 43; i++){
            plane_x[i] = mem_plane_x[i];
            plane_y[i] = mem_plane_y[i];
         }
         
         double radian = Math.toRadians(180-(90+rotation));
         
         
         for(int i = 0; i < 43; i++){
            plane_x[i] -=  (float) 8 * (-Math.cos(radian));
            plane_y[i] -= (float) 8 * Math.sin(radian);
            mem_plane_x[i] = plane_x[i];
            mem_plane_y[i] = plane_y[i];
         }

         centroid_x -= (float) 8 * (-Math.cos(radian));
         centroid_y -= (float) 8 * Math.sin(radian);
         
         double radians = Math.toRadians(rotation);
         
         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            float temp_x = plane_x[i];
            plane_x[i] = (float) ((Math.cos(radians)*plane_x[i])-(Math.sin(radians)*plane_y[i]));
            plane_y[i] = (float) ((Math.sin(radians)*temp_x)+(Math.cos(radians)*plane_y[i]));
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
         }

         canvas.repaint();
      }
      else if (key == KeyEvent.VK_R) {
         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            mem_plane_x[i] -= centroid_x;
            mem_plane_y[i] -= centroid_y;
            plane_x[i] *= 2;
            plane_y[i] *= 2;
            mem_plane_x[i] *= 2;
            mem_plane_y[i] *= 2;
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
            mem_plane_x[i] += centroid_x;
            mem_plane_y[i] += centroid_y;
            
         }
         canvas.repaint();
      }
      else if (key == KeyEvent.VK_F) {
         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            mem_plane_x[i] -= centroid_x;
            mem_plane_y[i] -= centroid_y;
            plane_x[i] *= 0.5;
            plane_y[i] *= 0.5;
            mem_plane_x[i] *= 0.5;
            mem_plane_y[i] *= 0.5;
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
            mem_plane_x[i] += centroid_x;
            mem_plane_y[i] += centroid_y;
         }
         canvas.repaint();
      }
      else if (key == KeyEvent.VK_E || key == KeyEvent.VK_D) {
         for(int i = 0; i < 43; i++){
            plane_x[i] = mem_plane_x[i];
            plane_y[i] = mem_plane_y[i];
         }
         
         if(key == KeyEvent.VK_E){
            if(rotation == 350) rotation = 0;
            else rotation += 10;
         } else {
            if(rotation == 0) rotation = 350;
            else rotation -= 10;
         }
         
         double radians = Math.toRadians(rotation);
         
         for(int i = 0; i < 43; i++){
            plane_x[i] -= centroid_x;
            plane_y[i] -= centroid_y;
            float temp_x = plane_x[i];
            plane_x[i] = (float) ((Math.cos(radians)*plane_x[i])-(Math.sin(radians)*plane_y[i]));
            plane_y[i] = (float) ((Math.sin(radians)*temp_x)+(Math.cos(radians)*plane_y[i]));
            plane_x[i] += centroid_x;
            plane_y[i] += centroid_y;
         }
         canvas.repaint();
      }
   }  // end keyPressed()


   public void keyReleased(KeyEvent evt) { 
      // empty method, required by the KeyListener Interface
   }
   

   public void mousePressed(MouseEvent evt) {
        // Request that the input focus be given to the
        // canvas when the user clicks on the applet.
      canvas.requestFocus();
   }   
   
   
   public void mouseEntered(MouseEvent evt) { }  // Required by the
   public void mouseExited(MouseEvent evt) { }   //    MouseListener
   public void mouseReleased(MouseEvent evt) { } //       interface.
   public void mouseClicked(MouseEvent evt) { }

   


} // end class KeyboardAndFocusDemo

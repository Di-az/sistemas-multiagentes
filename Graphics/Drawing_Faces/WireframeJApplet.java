
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Point3D {
   public int x, y, z;
   public Point3D( int X, int Y, int Z ) {
      x = X;  y = Y;  z = Z;
   }
}

class Edge {
   public int a, b;
   public Edge( int A, int B ) {
      a = A;  b = B;
   }
}

class Face {
   public int a,b,c,d;
   public Color color;
   public Face(int A, int B, int C, int D, Color RGB){
      a = A;
      b = B;
      c = C;
      d = D;
      color = RGB;
   }
}

public class WireframeJApplet extends JApplet 
                  implements KeyListener, FocusListener, MouseListener {
                      
   int width, height;
   // int mx, my;  // the most recently recorded mouse coordinates

   int azimuth = 0, elevation = 0;

   Point3D[] vertices;
   Edge[] edges;
   Face[] faces;

   boolean focussed = false;   // True when this applet has input focus.
   
   DisplayPanel canvas;  

   public void init() {
      vertices = new Point3D[ 20 ];
      vertices[0] = new Point3D( -1, -1, -1 ); 
      vertices[1] = new Point3D( -1, -1,  1 );
      vertices[2] = new Point3D( -1,  1, -1 );
      vertices[3] = new Point3D( -1,  1,  1 );
      vertices[4] = new Point3D(  1, -1, -1 );
      vertices[5] = new Point3D(  1, -1,  1 );
      vertices[6] = new Point3D(  1,  1, -1 );
      vertices[7] = new Point3D(  1,  1,  1 );
      vertices[8] = new Point3D(  3,  1,  -1 );
      vertices[9] = new Point3D(  3,  -1,  -1 );
      vertices[10] = new Point3D(  3,  1,  1 );
      vertices[11] = new Point3D(  3,  -1,  1 );
      vertices[12] = new Point3D(  1,  1,  3 );
      vertices[13] = new Point3D(  1,  -1,  3 );
      vertices[14] = new Point3D(  3,  1,  3 );
      vertices[15] = new Point3D(  3,  -1,  3 );
      vertices[16] = new Point3D(  5,  1,  3 );
      vertices[17] = new Point3D(  5,  -1,  3 );
      vertices[18] = new Point3D(  5,  1,  1 );
      vertices[19] = new Point3D(  5,  -1,  1 );

      faces = new Face[18];
      //First cube
      faces[0] = new Face(0,4,5,1, new Color(239,160,11));
      faces[1] = new Face(2,6,7,3, new Color(89,31,10));
      faces[2] = new Face(1,5,7,3, new Color(230,170,104));
      faces[3] = new Face(0,4,6,2, new Color(29,26,5));
      faces[4] = new Face(2,0,1,3, new Color(147,181,198));
      //Second cube
      faces[5] = new Face(4,9,11,5, new Color(240,207,101));
      faces[6] = new Face(6,8,10,7, new Color(235,186,185));
      faces[7] = new Face(4,9,8,6, new Color(56,134,151));
      faces[8] = new Face(8,9,11,10, new Color(63,13,18));
      //Third cube      
      faces[9] = new Face(5,11,15,13, new Color(213,191,134));
      faces[10] = new Face(7,10,14,12, new Color(88,81,35));
      faces[11] = new Face(13,15,14,12, new Color(238,193,112));
      faces[12] = new Face(7,5,13,12, new Color(15,196,132));
      
      //Fourth cube
      faces[13] = new Face(11,19,17,15, new Color(255,0,0));
      faces[14] = new Face(10,18,16,14, new Color(0,255,0));
      faces[15] = new Face(15,17,16,14, new Color(0,255,255));
      faces[16] = new Face(11,19,18,10, new Color(255,255,0));
      faces[17] = new Face(18,19,17,16, new Color(255,114,0));
      

      canvas = new DisplayPanel();  // Create drawing surface and 
      setContentPane(canvas);       //    install it as the applet's content pane.
   
      canvas.addFocusListener(this);   // Set up the applet to listen for events
      canvas.addKeyListener(this);     //                          from the canvas.
      canvas.addMouseListener(this);
      
   } // end init();
   
   class DisplayPanel extends JPanel {
      public void paintComponent(Graphics g) {
         super.paintComponent(g);  

         if (focussed) 
            g.setColor(Color.cyan);
         else
            g.setColor(Color.lightGray);

         int width = getSize().width;  // Width of the applet.
         int height = getSize().height; // Height of the applet.
         g.drawRect(0,0,width-1,height-1);
         g.drawRect(1,1,width-3,height-3);
         g.drawRect(2,2,width-5,height-5);

         if (!focussed) {
            g.setColor(Color.magenta);
            g.drawString("Click to activate",100,120);
            g.drawString("Use arrow keys to change azimuth and elevation",100,150);

         }
         else {

            double theta = Math.PI * azimuth / 180.0;
            double phi = Math.PI * elevation / 180.0;
            float cosT = (float)Math.cos( theta ), sinT = (float)Math.sin( theta );
            float cosP = (float)Math.cos( phi ), sinP = (float)Math.sin( phi );
            float cosTcosP = cosT*cosP, cosTsinP = cosT*sinP,
                  sinTcosP = sinT*cosP, sinTsinP = sinT*sinP;

            // project vertices onto the 2D viewport
            Point[] points;
            points = new Point[ vertices.length ];
            int j;
            int scaleFactor = width/8;
            float near = 3;  // distance from eye to near plane
            float nearToObj = 7f;  // distance from near plane to center of object
            for ( j = 0; j < vertices.length; ++j ) {
               int x0 = vertices[j].x;
               int y0 = vertices[j].y;
               int z0 = vertices[j].z;

               // compute an orthographic projection
               float x1 = cosT*x0 + sinT*z0;
               float y1 = -sinTsinP*x0 + cosP*y0 + cosTsinP*z0;
               float z1 = cosTcosP*z0 - sinTcosP*x0 - sinP*y0;

               // now adjust things to get a perspective projection
               x1 = x1*near/(z1+near+nearToObj);
               y1 = y1*near/(z1+near+nearToObj);

               // the 0.5 is to round off when converting to int
               points[j] = new Point(
                  (int)(width/2 + scaleFactor*x1 + 0.5),
                  (int)(height/2 - scaleFactor*y1 + 0.5)
               );
            }

            // draw the wireframe
            g.setColor( Color.black );
            g.fillRect( 0, 0, width, height );
            g.setColor( Color.white );
            // for ( j = 0; j < edges.length; j++ ) {
            //    g.drawLine(
            //       points[ edges[j].a ].x, points[ edges[j].a ].y,
            //       points[ edges[j].b ].x, points[ edges[j].b ].y
            //    );
            // }

                  //6 11 7 5

                  // 6 7
                  // 2 6
                  // 2 3
                  // 3 7

            for(int k = 0; k<faces.length; k++){
               int x[] = new int[5];
               int y[] = new int[5];
               x[0] = points[faces[k].a].x;
               x[1] = points[faces[k].b].x;
               x[2] = points[faces[k].c].x;
               x[3] = points[faces[k].d].x;
               x[4] = points[faces[k].a].x;


               y[0] = points[faces[k].a].y;
               y[1] = points[faces[k].b].y;
               y[2] = points[faces[k].c].y;
               y[3] = points[faces[k].d].y;
               y[4] = points[faces[k].a].y;

               g.setColor(faces[k].color);
               g.fillPolygon(x, y, 5);
            }


         } 
      }  // end paintComponent()    
    } // end nested class DisplayPanel 

   // ------------------- Event handling methods ----------------------
   
   public void focusGained(FocusEvent evt) {
      focussed = true;
      canvas.repaint();
   }
   
   public void focusLost(FocusEvent evt) {
      focussed = false;
      canvas.repaint(); 
   }
      
   public void keyTyped(KeyEvent evt) {  
   }  // end keyTyped()
      
   public void keyPressed(KeyEvent evt) { 
       
      int key = evt.getKeyCode();  // keyboard code for the key that was pressed
      
      if (key == KeyEvent.VK_LEFT) {
         azimuth += 5;
         canvas.repaint();         
      }
      else if (key == KeyEvent.VK_RIGHT) {
         azimuth -= 5;
         canvas.repaint();         
      }
      else if (key == KeyEvent.VK_UP) {
         elevation -= 5;
         canvas.repaint();         
      }
      else if (key == KeyEvent.VK_DOWN) {
         elevation += 5;         
         canvas.repaint();
      }

   }  // end keyPressed()

   public void keyReleased(KeyEvent evt) { 
      // empty method, required by the KeyListener Interface
   }
   
   public void mousePressed(MouseEvent evt) {      
      canvas.requestFocus();
   }      
   
   public void mouseEntered(MouseEvent evt) { }  // Required by the
   public void mouseExited(MouseEvent evt) { }   //    MouseListener
   public void mouseReleased(MouseEvent evt) { } //       interface.
   public void mouseClicked(MouseEvent evt) { }
   public void mouseDragged( MouseEvent e ) { }
} // end class 

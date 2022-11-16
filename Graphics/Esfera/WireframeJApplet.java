
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

class Point3D {
   public float x, y, z;
   public Point3D( float X, float Y, float Z ) {
      x = X;  y = Y;  z = Z;
   }
}

class Edge {
   public int a, b;
   public Edge( int A, int B ) {
      a = A;  b = B;
   }
}

public class WireframeJApplet extends JApplet 
                  implements KeyListener, FocusListener, MouseListener {
                      
   int width, height;
   // int mx, my;  // the most recently recorded mouse coordinates

   int azimuth = 0, elevation = 0;
   int meridianos, paralelos;
   float radius;

   Point3D[] vertices;
   Edge[] edges;

   boolean focussed = false;   // True when this applet has input focus.
   
   DisplayPanel canvas;  

   static public final float map(int value, 
                              int istart, 
                              int istop, 
                              double ostart, 
                              double ostop) {

                                 float division = (float) (value-istart) / (istop-istart);
                                 return (float) (ostart + (ostop - ostart) * (division));
                              }  

   public void init(float r,int m,int p) {

      radius = r;
      meridianos = 2*m;
      paralelos = p;

      vertices = new Point3D[(meridianos+1)*(paralelos+1)];
      int count = 0;

      try {
         FileWriter myWriter = new FileWriter("vertex.txt");

         for(int i = 0; i < paralelos+1; i++){
            float latitude = map(i,0, paralelos, 0, Math.PI);
   
            for(int j = 0; j < meridianos+1; j++) {
               float longitude = map(j,0,meridianos,0, 2*Math.PI);
               
               float x = (float) (radius * Math.sin(latitude) * Math.cos(longitude));
               float y = (float) (radius * Math.sin(latitude) * Math.sin(longitude));
               float z = (float) (radius * Math.cos(latitude));
               
               myWriter.write("Count: " +count + " -> ("+x+", "+y+","+z+")\n");
               vertices[count] = new Point3D(x, y, z);
               count++;
            }
         }
         myWriter.close();

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }


      edges = new Edge[2*meridianos*paralelos];
      count = 0;
      int vertex = 0;

      try {
         FileWriter myWriter = new FileWriter("edges.txt");

         for(int i = 0; i < paralelos; i++){
            for(int j = 0; j < meridianos; j++) {
   
               //to left
               edges[count] = new Edge(vertex, vertex+meridianos+1);
               myWriter.write("Edge: " +count+ " -> ("+vertex+", "+(vertex+meridianos+1)+")\n");
               count++;
               edges[count] = new Edge(vertex, vertex+1);
               myWriter.write("Edge: " +count+ " -> ("+vertex+", "+(vertex+1)+")\n");
               count++;
               vertex++;
            }
            vertex++;
         }
         myWriter.close();

      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

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
            Point3D[] temp;
            temp = new Point3D[vertices.length];
            points = new Point[ vertices.length ];
            int j;
            int scaleFactor = width/8;
            float near = 20;  // distance from eye to near plane
            float nearToObj = 1f;  // distance from near plane to center of object
            for ( j = 0; j < vertices.length; ++j ) {
               float x0 = vertices[j].x;
               float y0 = vertices[j].y;
               float z0 = vertices[j].z;

               // compute an orthographic projection
               float x1 = cosT*x0 + sinT*z0;
               float y1 = -sinTsinP*x0 + cosP*y0 + cosTsinP*z0;
               float z1 = cosTcosP*z0 - sinTcosP*x0 - sinP*y0;

               // now adjust things to get a perspective projection
               x1 = x1*near/(z1+near+nearToObj);
               y1 = y1*near/(z1+near+nearToObj);

               temp[j] = new Point3D(x1, y1, 0);

               // the 0.5 is to round off when converting to int
               points[j] = new Point(
                  (int)(width/2 + scaleFactor*x1 + 0.5),
                  (int)(height/2 - scaleFactor*y1 + 0.5)
               );
            }

            try {
               FileWriter myWriter = new FileWriter("currentRotation.txt");
      
               for(int i = 0; i < temp.length; i++){
                  myWriter.write("Count: " + i + "-> ("+temp[i].x+", "+temp[i].y+")\n");
               }
               myWriter.close();
      
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }

            // draw the wireframe
            g.setColor( Color.black );
            g.fillRect( 0, 0, width, height );
            g.setColor( Color.white );
            for ( j = 0; j < edges.length; j++ ) {
               g.drawLine(
                  points[ edges[j].a ].x, points[ edges[j].a ].y,
                  points[ edges[j].b ].x, points[ edges[j].b ].y
               );
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

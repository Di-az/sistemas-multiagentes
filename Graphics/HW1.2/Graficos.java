// Programa que renderiza un gradiente de cuatro colores
// Carlos Daniel Diaz Arrazate - A01734902
// Jose Angel Gonzalez Carrera - A01552274
// Carlos Eduardo Ruiz Lira - A01735706
// 3/11/22



import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
public class Graficos extends JPanel{

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//Bi-dimensional matrix of colors
		//From: (255,255,0) to: (0,255,255)
		//To: (255,0,0) to: (0,0,255)
		//Time Complexiy O(n^2)

		for(int row=0;row<512;row++){
			for(int col=0;col<512;col++){
				g.setColor(new Color(255-(col/2),255-(row/2),col/2));
				g.drawLine(col, row, col+1, row);
			}
		}


		//512x512 area
		g.setColor(new Color(0, 0, 255));
		g.drawRect(0, 0, 512, 512);	

	}

	public static void main(String args[]){
		Graficos panel = new Graficos();
		JFrame application = new JFrame();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.add(panel);
		application.setSize(512, 512);
		application.setVisible(true);
	}
}

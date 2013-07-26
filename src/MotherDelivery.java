import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;

import java.awt.Rectangle;

import processing.core.*;
import mother.library.*;
import mpe.config.FileParser;
import oscP5.*;
import netP5.*;

public class MotherDelivery extends PApplet
{
	Mother m_Mother;
	static boolean fullScreen;
	static int pos_X;
	static int pos_Y;
	
	public void init() {
		m_Mother = new Mother(this);
		
		m_Mother.init();
		
		super.init();
	}
	
	/**
	 * I had trouble getting the program to close, while closing the window worked the process remained running somehow.
	 * This did the trick back then, not sure if it is still necessary with Processing 2.0 though.
	 */
	public void setup() {
		m_Mother.setup();
		
		ImageIcon titlebaricon = new ImageIcon(loadBytes("mother_icon.jpg"));
		frame.setIconImage(titlebaricon.getImage());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					this.finalize();
					System.exit(0);
				}
				catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void draw() {
		// From ProcessingHacks, for fullscreen without problems
		// where window minimizes when focus is lost.
		if (fullScreen)
			frame.setLocation(pos_X, pos_Y);

		if (frame.getExtendedState() == 1) // If minimized, expand again
			frame.setExtendedState(0);
		 
		m_Mother.draw();
	}
	
	public void keyPressed() {
		m_Mother.keyPressed();
	}
	
	static public void main(String args[]) {
		
		
		FileParser fp = new FileParser("data//mother" + ".ini");

		// parse ini file if it exists
		if (fp.fileExists()) {
			if (fp.getIntValue("FullScreen") == 1) {
				fullScreen = true;
				int outputScreen = fp.getIntValue("outputScreen");

				GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice devices[] = environment.getScreenDevices();
				String location;

				Rectangle virtualBounds = new Rectangle();

				if (devices.length > outputScreen) { // we have a 2nd display/projector

					GraphicsConfiguration[] gc = devices[outputScreen].getConfigurations();

					if (gc.length > 0)	{
						virtualBounds = gc[0].getBounds();
					}

					location = "--location=" + virtualBounds.x + "," + virtualBounds.y;

					pos_X = virtualBounds.x;
					pos_Y = virtualBounds.y;
				}
				else { // leave on primary display
					location = "--location=0,0";

					pos_X = 0;
					pos_Y = 0;
				}

				PApplet.main(new String[] { location, "--hide-stop", "MotherDelivery" });
			}
			else {
				fullScreen = false;
				PApplet.main(new String[] { "MotherDelivery" });
			}
		}
	}

}


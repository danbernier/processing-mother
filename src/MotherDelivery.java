import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import processing.core.*;
import mother.library.*;
import mpe.config.FileParser;
import oscP5.*;
import netP5.*;

public class MotherDelivery extends PApplet
{
	Mother m_Mother;
	
	public static int 		pos_X;
	public static int 		pos_Y;
	
	public void init() {
		m_Mother = new Mother(this);
		
		m_Mother.init();
		
		super.init();
	}
	
	public void setup() {
		m_Mother.setup();
	}
	
	public void draw() {
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
				PApplet.main(new String[] { "MotherDelivery" });
			}
		}
	}

}


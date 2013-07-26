import processing.core.*;

import mother.library.*;

public class MotherDelivery extends PApplet
{
	Mother m_Mother;

	public void init()
	{
		m_Mother = new Mother(this);
		
		m_Mother.init();
		
		super.init();
	}
	
	public void setup()
	{
//		size(800, 600, OPENGL);
//		frameRate(24);
		
		m_Mother.setup();
	}
	
	public void draw()
	{
		m_Mother.draw();
	}
	
	public void keyPressed()
	{
		m_Mother.keyPressed();
	}
}
package onar3d.mother;



import foetus.*;
import processing.core.PApplet;
import processing.opengl.*;
import oscP5.*;
import netP5.*;
import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

/**
 * LightsGL. 
 * Modified from an example by Simon Greenwold. 
 * 
 * Display a box with three different kinds of lights. 
 */

public class CubeSpine extends PApplet
{
	PGraphicsOpenGL pgl;

	GL opengl;

	GLU glu;

	//	 Declare Fuetus
	public Foetus f;

	FoetusParameter m_Scale;
	FoetusParameter m_Repetitions_1;
	FoetusParameter m_InitSize;
	FoetusParameter m_Step;
	FoetusParameter m_A;
	FoetusParameter m_B;
	FoetusParameter m_Red;
	FoetusParameter m_Green;
	FoetusParameter m_Blue;
	FoetusParameter m_Position_X;
	FoetusParameter m_Position_Y;

	public void setup()
	{
		size(800, 600, OPENGL);

		hint(ENABLE_OPENGL_4X_SMOOTH);

		smooth();

		initializeFoetus();
	}

	void initializeFoetus()
	{
		// Instantiate foetus object here  
		f = new Foetus(this);
		
		pgl = (PGraphicsOpenGL) g;
		opengl = pgl.gl;
		glu = ((PGraphicsOpenGL) g).glu;
		
		m_Scale 		= new FoetusParameter(f, 1.0f, 	"/Scale", 			"f");
		m_Repetitions_1 = new FoetusParameter(f, 25, 	"/Repetitions_1", 	"i");
		m_InitSize 		= new FoetusParameter(f, 100, 	"/InitSize", 		"i");
		m_Step 			= new FoetusParameter(f, 1.19f, "/Step", 			"f");
		m_A 			= new FoetusParameter(f, 0, 	"/A", 				"f");
		m_B 			= new FoetusParameter(f, 0, 	"/B", 				"f");
		m_Red 			= new FoetusParameter(f, 255,	"/Red", 			"i");
		m_Green 		= new FoetusParameter(f, 0, 	"/Green", 			"i");
		m_Blue 			= new FoetusParameter(f, 0, 	"/Blue", 			"i");
		m_Position_X 	= new FoetusParameter(f, 0.5f, 	"/Position_X", 		"f");
		m_Position_Y 	= new FoetusParameter(f, 0.5f, 	"/Position_Y", 		"f");	
	}

	public void draw()
	{
//	  	m_A = mouseX;
//	  	m_B = mouseY;

		noStroke();

//	  	background(0);
		defineLights();

		float size = 100;
		float step = 100;

		pushMatrix();

		translate(m_Position_X.getValue() * width, m_Position_Y.getValue() * height, -100);

		scale(m_Scale.getValue());

		rotateY(map(m_A.getValue() * 4, 0, width, 0, PI));

		for (int i = 0; i < m_Repetitions_1.getValue(); i++)
		{

			rotateY(map(m_B.getValue(), 0, 360, 0, PI));
			rotateZ(map(m_A.getValue(), 0, 360, 0, PI));

			size = m_InitSize.getValue();
			step = 100;

			scale(0.93f);

			pushMatrix();

			while (size >= 3)
			{
				size = size / m_Step.getValue();

				step = size;

				translate(0, 0, 10);

				pushMatrix();

				rotateX(map(-m_B.getValue() * 4, 0, height, 0, PI));
				rotateY(map(m_A.getValue() * 4, 0, width, 0, PI));

				rotateX(map(step * m_B.getValue() / 10.0f, 0, height, 0, PI));

				//box(size);

				sixColouredCube(size);

				popMatrix();

				translate(0, -step);
			}

			popMatrix();
		}

		popMatrix();
	}

	void defineLights()
	{
		// Orange point light on the right
		pointLight(m_Red.getValue(), m_Green.getValue(), m_Blue.getValue(), // Color
				width, height, 100); // Position

		// Blue directional light from the left
		directionalLight(255, 255, 255, // Color
				1, 0, 0); // The x-, y-, z-axis direction

		// Yellow spotlight from the front
		/* spotLight(255, 255, 109,  // Color
		 width/2, height/2, 200,     // Position
		 0, -0.5, -0.5,  // Direction
		 PI, 20);     // Angle, concentration
		 */
	}

	void sixColouredCube(float cubeSize)
	{
		fill(color(m_Red.getValue(), m_Green.getValue(), m_Blue.getValue()));

		beginShape(QUADS);
		vertex(0, 0, 0);
		vertex(cubeSize, 0, 0);
		vertex(cubeSize, cubeSize, 0);
		vertex(0, cubeSize, 0);
		endShape();

		fill(color(255, 255, 255));
		beginShape(QUADS);
		vertex(0, 0, 0);
		vertex(0, cubeSize, 0);
		vertex(0, cubeSize, cubeSize);
		vertex(0, 0, cubeSize);
		endShape();

	}

	/**
	 * This method is called when an OSC message is received by the synth.
	 */
	void oscEvent(OscMessage theOscMessage)
	{
		try
		{
			if (theOscMessage.checkAddrPattern("/Scale") == true)
			{
				if (theOscMessage.checkTypetag("f"))
					m_Scale.setValue(theOscMessage.get(0).floatValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Repetitions_1") == true)
			{
				if (theOscMessage.checkTypetag("i"))
					m_Repetitions_1.setValue(theOscMessage.get(0).intValue());
			} 
			else if (theOscMessage.checkAddrPattern("/InitSize") == true)
			{
				if (theOscMessage.checkTypetag("i"))
					m_InitSize.setValue(theOscMessage.get(0).intValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Step") == true)
			{
				if (theOscMessage.checkTypetag("f"))
					m_Step.setValue(theOscMessage.get(0).floatValue());
			} 
			else if (theOscMessage.checkAddrPattern("/A") == true)
			{
				if (theOscMessage.checkTypetag("f"))
					m_A.setValue(theOscMessage.get(0).floatValue());
			} 
			else if (theOscMessage.checkAddrPattern("/B") == true)
			{
				if (theOscMessage.checkTypetag("f"))
					m_B.setValue(theOscMessage.get(0).floatValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Red") == true)
			{
				if (theOscMessage.checkTypetag("i"))
					m_Red.setValue(theOscMessage.get(0).intValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Green") == true)
			{
				if (theOscMessage.checkTypetag("i"))
					m_Green.setValue(theOscMessage.get(0).intValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Blue") == true)
			{
				if (theOscMessage.checkTypetag("i"))
					m_Blue.setValue(theOscMessage.get(0).intValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Position_X") == true)
			{
				if (theOscMessage.checkTypetag("f"))
					m_Position_X.setValue(theOscMessage.get(0).floatValue());
			} 
			else if (theOscMessage.checkAddrPattern("/Position_Y") == true)
			{
				if (theOscMessage.checkTypetag("f"))
					m_Position_Y.setValue(theOscMessage.get(0).floatValue());
			}
		} 
		catch (Exception e)
		{
			println("Exception while processing CubeSpine input " + " " + theOscMessage.addrPattern());
		}
	}

}

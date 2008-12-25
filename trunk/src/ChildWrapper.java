

import processing.core.*; 
import processing.opengl.*;
import oscP5.*;
import netP5.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import foetus.Foetus;

import java.util.*;
import java.io.File;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.*;

import onar3d.Render_To_Texture.*;

/**
 *
 */
public class ChildWrapper
{
	PApplet m_Child;
	
	RenderSketchToTexture m_RenderToTexture;
		
	boolean m_RenderBillboard = false;
	
	int m_Width;
	int m_Height;
	
	int m_Blending_Source;
	int m_Blending_Destination;
	
	String m_Name;
	
	public PApplet Child()	{ return m_Child;	}
	
	public boolean GetRenderBillboard()							{ return m_RenderBillboard; }
	public void	   SetRenderBillboard(boolean renderBillboard)	{ m_RenderBillboard = renderBillboard; }
	
	public int 	GetBlending_Source()							{ return m_Blending_Source; }
	public void	SetBlending_Source(int source)					{ m_Blending_Source = source; }
	
	public int 	GetBlending_Destination()						{ return m_Blending_Destination; }
	public void	SetBlending_Destination(int dest)				{ m_Blending_Destination = dest; }
		
	public String GetName() { return m_Name; }
		
	/**
	 * CONSTRUCTOR
	 */
	

	public ChildWrapper(int w, int h, String classPath, String className, String name, boolean billboard)
	{	
		m_Width 			= w;
		m_Height 			= h;
		m_Name 				= name;
		m_RenderBillboard 	= billboard;
		
		m_Blending_Source 		= GL.GL_SRC_ALPHA;
		m_Blending_Destination 	= GL.GL_ONE_MINUS_SRC_ALPHA;
		
		m_Child = LoadSketch(classPath, className);
	}
		
	/**
	 * METHODS
	 */
	
	public void draw(int i)
	{		
		// Checking whether applet thread has been properly initialized
		if( (m_Child.g != null) && (((PGraphicsOpenGL)m_Child.g).gl != null) )
		{
			if(m_RenderBillboard)
			{
				if( m_RenderToTexture == null)
				{
					m_RenderToTexture = new RenderSketchToTexture(m_Width, m_Height, m_Child);
					System.out.println(m_Name);
				}
				
				m_RenderToTexture.draw(i);				
			}
			else
			{
			//	m_Child.draw();
			}
		}
		else
		{
//			System.out.println("Applet thread not yet initialized, g == null");
			return;
		}
	}
	
	/**
	 * Loads a sketch from disk
	 * @param classPath
	 * @param className
	 * @return
	 */
	
	private PApplet LoadSketch(String classPath, String className)
    {   
        File oooClassPath; // = new File(classPath + "//" + className + ".jar");
        
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1)
        	oooClassPath = new File(classPath); // Mac
        else 
        	oooClassPath = new File(classPath + "//" + className + ".jar"); // Windows
        
        try
        {
        	URLClassLoader cl = new URLClassLoader( new URL[] {oooClassPath.toURI().toURL()} );

            return (PApplet)Class.forName(className, true, cl).newInstance();
        } 
        catch (Exception ex)
        {
            System.out.println(ex.toString() +  ex.getMessage());
        }
        
        return new PApplet();
    } 

	public void Set_Color(float r, float g, float b, float a)
	{
		m_RenderToTexture.SetR(r);
		m_RenderToTexture.SetG(g);
		m_RenderToTexture.SetB(b);
		m_RenderToTexture.SetA(a);
	}
}

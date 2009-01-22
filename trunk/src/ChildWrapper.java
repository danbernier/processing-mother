
import processing.core.*; 
import processing.opengl.*;
import javax.media.opengl.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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
	

	public ChildWrapper(int w, int h, String classPath, URL[] libraryULS, String className, String name, boolean billboard)
	{	
		m_Width 			= w;
		m_Height 			= h;
		m_Name 				= name;
		m_RenderBillboard 	= billboard;
		
	    File dir1 = new File (".");
	    try 
	    {
	      System.out.println ("Current dir : " + dir1.getCanonicalPath());
	    }
	    catch(Exception e)
	    {
	    	 
	    }
		
		m_Blending_Source 		= GL.GL_SRC_ALPHA;
		m_Blending_Destination 	= GL.GL_ONE_MINUS_SRC_ALPHA;
		
		//if(className!="source_particles")
			m_Child = LoadSketch(classPath, className, libraryULS);
		//else
		//	m_Child = new source_particles();
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
				}
				
				m_RenderToTexture.draw(i);				
			}
			else
			{
				((PGraphicsOpenGL)m_Child.g).gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
				((PGraphicsOpenGL)m_Child.g).pushMatrix();
				m_Child.draw();
				((PGraphicsOpenGL)m_Child.g).popMatrix();
			}
		}
		else
		{
//			System.out.println("Applet thread not yet initialized, g == null");
			return;
		}
	}
	
	/*
	 * This worked but doesn't help much for the plugin handling: "-Djava.ext.dirs=C:\libraries"
	 */
	
	/**
	 * Loads a sketch from disk
	 * @param classPath
	 * @param className
	 * @return
	 */
	
	private PApplet LoadSketch(String classPath, String className, URL[] libraryURLS)
    {   
        File oooClassPath; // = new File(classPath + "//" + className + ".jar");
        
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1)
        	oooClassPath = new File(classPath); // Mac
        else 
        	oooClassPath = new File(classPath + "//" + className + ".jar"); // Windows

        URL[] toUse = new URL[1 + libraryURLS.length];
        
        try
        { 
        	for(int i = 0; i<libraryURLS.length; i++ )
            {
        		toUse[i] = libraryURLS[i];
            }
             
            toUse[libraryURLS.length] = oooClassPath.toURI().toURL();
             
        	URLClassLoader cl = new URLClassLoader( toUse, ClassLoader.getSystemClassLoader() );

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


import processing.core.*; 
import processing.opengl.*;
import javax.media.opengl.*;
/*
import org.apache.log4j.Logger;*/

import foetus.Foetus;
import foetus.FoetusParameter;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import onar3d.Render_To_Texture.*;

/**
 *
 */
public class ChildWrapper
{
//	private Logger logger = null;
	
	PApplet m_Child, r_Mother;
	
	RenderSketchToTexture m_RenderToTexture;
		
	boolean m_RenderBillboard = true;
	
	int m_Width;
	int m_Height;
	
	int m_Blending_Source;
	int m_Blending_Destination;
	
	String m_Name;
	
	public PApplet Child()	{ return m_Child;	}
	
	public PApplet Mother()	{ return r_Mother;	}
	
	public boolean GetRenderBillboard()							{ return m_RenderBillboard; }
	public void	   SetRenderBillboard(boolean renderBillboard)	{ m_RenderBillboard = renderBillboard; }
	
	public int 	GetBlending_Source()							{ return m_Blending_Source; }
	public void	SetBlending_Source(int source)					{ m_Blending_Source = source; }
	
	public int 	GetBlending_Destination()						{ return m_Blending_Destination; }
	public void	SetBlending_Destination(int dest)				{ m_Blending_Destination = dest; }
		
	public String GetName() { return m_Name; }
		
	Foetus foetusField;
		
	public Foetus getFoetusField()
	{
		return foetusField;
	}

	public void setFoetusField(Foetus foetusField)
	{
		this.foetusField = foetusField;
	}

	/**
	 * CONSTRUCTORRenderToTexture
	 */
	

	public ChildWrapper(int w, int h, String classPath, URL[] libraryULS, String className, String name, boolean billboard, PApplet mother)
	{	
		r_Mother			= mother;
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
			
//		logger = Logger.getLogger(this.GetName());
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
					m_RenderToTexture = new RenderSketchToTexture(m_Width, m_Height, m_Child, r_Mother);
				}
				
				m_RenderToTexture.draw();				
			}
			else
			{
//				logger.info("Before Draw: " + m_Name);
				
				m_Child.frameCount	= r_Mother.frameCount;
				
				((PGraphicsOpenGL)m_Child.g).gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
				((PGraphicsOpenGL)m_Child.g).pushMatrix();

				ArrayList<FoetusParameter> params = this.foetusField.getParameters();
				
				for(int pi = 0; pi < params.size(); pi++)
				{
					params.get(pi).tick();
				}
				
				m_Child.draw();
				((PGraphicsOpenGL)m_Child.g).popMatrix();
				
//				logger.info("After Draw: " + m_Name);
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
		File dir1 = new File (".");
	    
		try 
	    {
	      System.out.println ("Current dir : " + dir1.getCanonicalPath());
	    }
	    catch(Exception e)
	    {
	    	 
	    }
		
	    
        File oooClassPath; // = new File(classPath + "//" + className + ".jar");
        
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1)
        	oooClassPath = new File(classPath + "//" + className + ".jar"); // Mac
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

        	PApplet toReturn = (PApplet)Class.forName(className, true, cl).newInstance(); 
        	
        	toReturn.noLoop();
        	
            return toReturn;
        } 
        catch (Exception ex)
        {
            System.out.println("Crash: while loading sketch: " +  ex.getMessage());
            ex.printStackTrace();
        }
        
        return new PApplet();
    } 
}

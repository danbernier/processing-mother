/*
Copyright 2008 Ilias Bergstrom.
  
This file is part of "Mother".

Mother is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Foobar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Mother.  If not, see <http://www.gnu.org/licenses/>.
 
onar3d@hotmail.com, www.onar3d.com
 
*/

package onar3d.mother;

import processing.core.*; 
import processing.opengl.*;
import oscP5.*;
import netP5.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
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
	
	public int 	GetBlending_Source()			{ return m_Blending_Source; }
	public void	SetBlending_Source(int source)	{ m_Blending_Source = source; }
	
	public int 	GetBlending_Destination()			{ return m_Blending_Destination; }
	public void	SetBlending_Destination(int dest)	{ m_Blending_Destination = dest; }
		
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
		
		//m_Child = LoadSketch(classPath, className);
		
		m_Child = new CubeSpine();
	}
		
	/**
	 * METHODS
	 */
	
	public void draw()
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
				
				m_RenderToTexture.draw();
			}
			else
			{
				m_Child.draw();
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
		String fileName = "";
		PApplet toReturn;
		
		File oooClassPath = new File(classPath);
		File[] files = oooClassPath.listFiles();
		
		URL[] urls = new URL[files.length];
		
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				fileName = files[i].getName();
				urls[i] = files[i].toURL();
			} 
			catch (MalformedURLException ex)
			{
				System.out.println("MalformedURLException: " + ex.getMessage());
			}
		}
		
		URLClassLoader cl = new URLClassLoader(urls);

		String[] derivedClassNames = fileName.split(".jar"); 
		
		try
		{		
			toReturn = (PApplet)Class.forName(className, true, cl).newInstance();
						
			return toReturn;
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

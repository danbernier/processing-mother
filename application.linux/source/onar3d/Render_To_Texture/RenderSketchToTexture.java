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
 
www.onar3d.com
 
*/

package onar3d.Render_To_Texture;

import javax.media.opengl.GL;

import codeanticode.glgraphics.*;

import processing.core.*; 
import processing.opengl.PGraphicsOpenGL;

/**
 * @author FugMom
 *
 */
public class RenderSketchToTexture extends RenderToTexture
{
	protected PApplet m_Sketch;
		
	/**
	 * @param w
	 * @param h
	 * @param pgl
	 */
	public RenderSketchToTexture(int w, int h, PApplet sketch, PApplet mother)
	{
		super(w, h, (PGraphicsOpenGL)mother.g, mother);
		
		m_Sketch = sketch;
		
		r_Mother.noStroke();
	}
	
	/* (non-Javadoc)
	 * @see onar3d.Render_To_Texture.RenderToTexture#drawGeometry()
	 */
	protected void drawGeometry()
	{
		/*m_Gl.glPushMatrix();
		m_Gl.glColor4f(0, 1.0f, 0, 1.0f);
    	m_Gl.glTranslatef((m_Width/2.0f),(m_Height/2.0f) , 0f);
    	m_Pgl.box(100);
    	m_Gl.glPopMatrix();*/
   	
		if(!glinited)
			initGL();

	//	m_Sketch.pushMatrix();
	//	m_Gl.glEnable(GL.GL_LIGHTING);
 		m_Sketch.draw();
 	//	m_Sketch.popMatrix();
	}
	
	boolean glinited = false;
	
	/***
	 * 
	 */
	void initGL()
	{
		
		glinited = true; 
	}
}

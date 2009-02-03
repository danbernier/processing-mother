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

import processing.core.*;
import com.sun.opengl.util.BufferUtil;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import processing.opengl.PGraphicsOpenGL;
import java.nio.IntBuffer;

public abstract class RenderToTexture
{
	int m_Width;
	int m_Height;
	
	protected int m_Texture_Width;	// = 640;
	protected int m_Texture_Height;	// = 480;

	// An Unsigned Int To Store The Texture Number
	//protected static int[] m_Texture = {0};
		
	protected static boolean m_TextureCreated 	= false;

	GL m_Gl;
	GLU m_Glu;
	PGraphicsOpenGL m_Pgl;
	
	protected static int[] mfbo = {0};
	protected static int[] colorBuffer = {0};
	protected static int[] depthBuffer = {0};
	protected static int[] fbo = {0};
	protected static int[] texture = {0};
	
	protected int m_OGL_PixelFormat = GL.GL_RGBA16F_ARB;
	protected int m_multisamples = 4;
	
	protected PApplet r_Mother;
	
	/**
	 * CONSTRUCTOR
	 * 
	 * @param w
	 * @param h
	 * @param pgl
	 */
	public RenderToTexture(int w, int h, PGraphicsOpenGL pgl, PApplet mother)
	{
		r_Mother = mother;
		m_Width  = w;
		m_Height = h;
		m_Texture_Width 	= w;
		m_Texture_Height 	= h;
		
		m_Pgl 	= (PGraphicsOpenGL) pgl;
		m_Gl 	= m_Pgl.gl;
		m_Glu 	= ((PGraphicsOpenGL) pgl).glu;

		// Create Our Empty Texture
		if(!m_TextureCreated)
		{
			/*m_Texture 			=*/ createTexture(m_Gl);
			m_TextureCreated 	= true;
		}

		if (!GLDrawableFactory.getFactory().canCreateGLPbuffer())
		{
			System.out.println("Requires pbuffer support");
			System.exit(1);
		}
		
		if (m_Gl.isExtensionAvailable("GL_ARB_texture_rectangle"))
		{
			// Query the maximum texture size available
			IntBuffer val = BufferUtil.newIntBuffer(1);
			m_Gl.glGetIntegerv(GL.GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB, val);			
			System.out.println("Max rectangle texture size: " + val.get(0));
		}
		else
		{
			m_Texture_Height = m_Texture_Width;
		}
	}
	
	// Create An Empty Texture
	private /*int[]*/ void createTexture(GL gl)
	{
		gl.glEnable(GL.GL_TEXTURE_RECTANGLE_ARB);
		
		// Creating multisampled color buffer
		gl.glGenRenderbuffersEXT(1, colorBuffer, 0);
		gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, colorBuffer[0]); // Binding render buffer		
		gl.glRenderbufferStorageMultisampleEXT(GL.GL_RENDERBUFFER_EXT, m_multisamples, GL.GL_RGBA8, m_Texture_Width, m_Texture_Height);
		
		// Creating depth buffer
		gl.glGenRenderbuffersEXT(1, depthBuffer, 0);
		gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, depthBuffer[0]); // Binding depth buffer
		// Allocating space for multisampled depth buffer
		gl.glRenderbufferStorageMultisampleEXT(GL.GL_RENDERBUFFER_EXT, m_multisamples, GL.GL_DEPTH_COMPONENT, m_Texture_Width, m_Texture_Height);
	
		// Creating handle for FBO
		gl.glGenFramebuffersEXT(1, mfbo, 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, mfbo[0]);		// Binding FBO
		gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_RENDERBUFFER_EXT, colorBuffer[0]);
		gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, depthBuffer[0]);
			
		// Creating texture
		gl.glGenTextures(1, texture, 0);
		gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, texture[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_RECTANGLE_ARB, 0, GL.GL_RGBA, m_Texture_Width, m_Texture_Height, 0, GL.GL_RGBA, GL.GL_INT, null);
		
		// Creating actual resolution FBO
		gl.glGenFramebuffersEXT(1, fbo, 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
		// Attaching texture to FBO
		gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_RECTANGLE_ARB, texture[0], 0);
		
		int status = gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);
		
		if(status == GL.GL_FRAMEBUFFER_COMPLETE_EXT)
		{
			System.out.println("GL_FRAMEBUFFER_EXT Status Complete:" + status);
		}
		else
		{
			System.out.println("GL_FRAMEBUFFER_EXT Status Not complete... " + status);
		}
	}

	// Set Up an Ortho View
	protected void viewOrtho()
	{
		// Select Projection
		m_Gl.glMatrixMode(GL.GL_PROJECTION);
		// Push The Matrix
		m_Gl.glPushMatrix();
		// Reset The Matrix
		m_Gl.glLoadIdentity();
		// Select Ortho Mode 
		m_Gl.glOrtho(0, m_Width, m_Height, 0, -1, 1);
		// Select Modelview Matrix
		m_Gl.glMatrixMode(GL.GL_MODELVIEW);
		// Push The Matrix
		m_Gl.glPushMatrix();
		// Reset The Matrix
		m_Gl.glLoadIdentity();
	}

	// Set Up a Perspective View
	protected void viewPerspective()
	{
		// Select Projection
		m_Gl.glMatrixMode(GL.GL_PROJECTION);
		// Pop The Matrix
		m_Gl.glPopMatrix();
		// Select Modelview
		m_Gl.glMatrixMode(GL.GL_MODELVIEW);
		// Pop The Matrix
		m_Gl.glPopMatrix();
	}

	public void draw()
	{
		renderToTexture(); // Render To A Texture
	
		drawBillboard(texture, m_Texture_Width, m_Texture_Height);
	
		drawEffect();

		// Flush The GL Rendering Pipeline
		m_Gl.glFlush();
	}

	protected abstract void drawGeometry();
	
	protected abstract void drawEffect();
	
	/**
	 * Renders To A Texture
	 */
	private void renderToTexture()
	{
		// First draw the multisampled scene
		m_Gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, mfbo[0]);
		
		m_Gl.glPushAttrib(GL.GL_VIEWPORT_BIT);
		
		m_Gl.glViewport(0, 0, m_Texture_Width, m_Texture_Height);

		m_Gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		m_Gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		//-----------------------------
	
		m_Pgl.pushMatrix();
		
    	drawGeometry();
    	
    	m_Pgl.popMatrix();

		m_Gl.glPopAttrib();

		// Then downsample the multisampled to the normal buffer with a blit

		m_Gl.glBindFramebufferEXT(GL.GL_READ_FRAMEBUFFER_EXT, mfbo[0]); // source
		m_Gl.glBindFramebufferEXT(GL.GL_DRAW_FRAMEBUFFER_EXT, fbo[0]); // dest
		m_Gl.glBlitFramebufferEXT(0, 0, m_Texture_Width, m_Texture_Height, 0, 0, m_Texture_Width, m_Texture_Height, GL.GL_COLOR_BUFFER_BIT, GL.GL_LINEAR);
	
		m_Gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		
		m_Gl.glActiveTexture(GL.GL_TEXTURE0);
	}

	/***
	 *  Draw The Billboard
	 * @param texture
	 * @param width
	 * @param height
	 */
	public void drawBillboard(int[] texture, int width, int height)
	{
		// Disable AutoTexture Coordinates
		m_Gl.glDisable(GL.GL_TEXTURE_GEN_S);
		m_Gl.glDisable(GL.GL_TEXTURE_GEN_T);
		
		// Disable Depth Testing
		m_Gl.glDisable(GL.GL_DEPTH_TEST);
		m_Gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		 
		m_Gl.glEnable(GL.GL_BLEND);
			
		m_Gl.glDisable(GL.GL_LIGHTING);
		
		// Enable 2D Texture Mapping
		m_Gl.glEnable(GL.GL_TEXTURE_RECTANGLE_ARB);
		
		// Bind To The Texture
		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, texture[0]);
				
		// Switch To An Ortho View
		viewOrtho();
		
		m_Gl.glPushMatrix();
		
		r_Mother.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		m_Gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		m_Gl.glBegin(GL.GL_QUADS);
		
		// Texture Coordinate ( 0, 1 )
		m_Gl.glTexCoord2f(0, m_Texture_Height);
		// First Vertex ( 0, 0 )
		m_Gl.glVertex2f(0, 0);
		// Texture Coordinate ( 0, 0 )
		m_Gl.glTexCoord2f(0, 0);
		// Second Vertex ( 0, height )
		m_Gl.glVertex2f(0, height);
		// Texture Coordinate ( 1, 0 )
		m_Gl.glTexCoord2f(m_Texture_Width, 0);
		// Third Vertex ( width, height )
		m_Gl.glVertex2f(width, height);
		// Texture Coordinate ( 1, 1 )
		m_Gl.glTexCoord2f(m_Texture_Width, m_Texture_Height);
		// Fourth Vertex ( width, 0 )
		m_Gl.glVertex2f(width, 0);

		m_Gl.glEnd();
		m_Gl.glPopMatrix();
		
		m_Gl.glDisable(GL.GL_BLEND);
		
		// Switch To A Perspective View
		viewPerspective();
    			
		// Enable Depth Testing
		m_Gl.glEnable(GL.GL_DEPTH_TEST);
		
		// Disable 2D Texture Mapping
		m_Gl.glDisable(GL.GL_TEXTURE_RECTANGLE_ARB);

		// Unbind The Blur Texture
		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, 0);
	}
}
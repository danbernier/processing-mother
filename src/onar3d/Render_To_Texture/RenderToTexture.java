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

package onar3d.Render_To_Texture;

import com.sun.opengl.util.BufferUtil;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import processing.opengl.PGraphicsOpenGL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class RenderToTexture
{
	private int m_Texture_Width  = 640;
	private int m_Texture_Height = 480;
	
	private int m_Texture_Coordinate_W;
	private int m_Texture_Coordinate_H;
	private boolean m_Texture_Rectange_Available;

	private float m_R = 1;
	private float m_G = 1;
	private float m_B = 1;
	private float m_A = 1;
	
	public float GetR() { return m_R; }
	public float GetG() { return m_G; }
	public float GetB() { return m_B; }
	public float GetA() { return m_A; }
	
	public void SetR(float r) { m_R = r; }
	public void SetG(float g) { m_G = g; }
	public void SetB(float b) { m_B = b; }
	public void SetA(float a) { m_A = a; }
	
	// An Unsigned Int To Store The Texture Number
	private static int m_Texture;

	private static int m_FrameBufferObject = -1;

	private static boolean m_TextureCreated = false;
	private static boolean m_FrameBufferCreated = false;
	
	PGraphicsOpenGL m_Pgl;

	GL m_Gl;

	GLU m_Glu;

	int m_Width;

	int m_Height;

	/**
	 * CONSTRUCTOR
	 * 
	 * @param w
	 * @param h
	 * @param pgl
	 */
	public RenderToTexture(int w, int h, PGraphicsOpenGL pgl)
	{
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
			m_Texture = createBlurTexture(m_Gl);
			m_TextureCreated = true;
		}
		
		if (m_Gl.isExtensionAvailable("GL_EXT_framebuffer_object") && !m_FrameBufferCreated)
		{
			m_FrameBufferObject = createFrameBufferObject();
			m_FrameBufferCreated = true;
		}

		if (m_FrameBufferObject != -1)
		{
			System.out.println(" using frame buffer object");
		} else
		{
			System.out.println(" using default frame buffer");
		}
		
		if (m_Gl.isExtensionAvailable("GL_ARB_texture_rectangle"))
		{
			// Query the maximum texture size available
//			IntBuffer val = BufferUtil.newIntBuffer(1);
//			m_Gl.glGetIntegerv(GL.GL_MAX_RECTANGLE_TEXTURE_SIZE_ARB, val);			
//			System.out.println("Max rectangle texture size: " + val.get(0));
			
			m_Texture_Coordinate_W 		= m_Texture_Width;
			m_Texture_Coordinate_H 		= m_Texture_Height;
			m_Texture_Rectange_Available = true;
		}
		else
		{
			m_Texture_Coordinate_W 		= 1;
			m_Texture_Coordinate_H 		= 1;
			m_Texture_Rectange_Available = false;
			m_Texture_Height = m_Texture_Width;
		}
	}

	// Create An Empty Texture
	private int createBlurTexture(GL gl)
	{
		ByteBuffer data = BufferUtil.newByteBuffer(m_Texture_Width * m_Texture_Height * 4); // Create Storage Space For
		// Texture
		// Data (128x128x4)
		data.limit(data.capacity());

		int[] txtnumber = new int[1];

		gl.glGenTextures(1, txtnumber, 0); // Create 1 Texture
		gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, txtnumber[0]); // Bind The Texture

		// Build Texture Using Information In data
		gl.glTexImage2D(GL.GL_TEXTURE_RECTANGLE_ARB, 0, GL.GL_RGBA, m_Texture_Width, m_Texture_Height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data);
		gl.glTexParameteri(GL.GL_TEXTURE_RECTANGLE_ARB, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_RECTANGLE_ARB, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);

		return txtnumber[0]; // Return The Texture ID
	}

	// Set Up an Ortho View
	private void viewOrtho()
	{
		// Select Projection
		m_Gl.glMatrixMode(GL.GL_PROJECTION);
		// Push The Matrix
		m_Gl.glPushMatrix();
		// Reset The Matrix
		m_Gl.glLoadIdentity();
		// Select Ortho Mode (640x480)
		m_Gl.glOrtho(0, m_Width, m_Height, 0, -1, 1);
		// Select Modelview Matrix
		m_Gl.glMatrixMode(GL.GL_MODELVIEW);
		// Push The Matrix
		m_Gl.glPushMatrix();
		// Reset The Matrix
		m_Gl.glLoadIdentity();
	}

	// Set Up a Perspective View
	private void viewPerspective()
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
		m_Pgl.pushMatrix();

		// If I uncomment this, the y axis is inverted
		// m_Gl.glLoadIdentity(); // Reset The View
		renderToTexture(); // Render To A Texture

		// Moved from within renderToTexture
		if (m_FrameBufferObject != -1)
		{
			// If we used the fbo, restore the default frame buffer
			m_Gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		}

		m_Pgl.popMatrix();

		// drawGeometry();
		
//		m_Gl.glEnable(GL.GL_DEPTH_TEST);
		
		// Draw The Blur Effect
		// drawBlur(25, 0.005f);
		drawBillboard();

//		m_Gl.glDisable(GL.GL_DEPTH_TEST); // Disable Depth Testing
		
		// Flush The GL Rendering Pipeline
		m_Gl.glFlush();
	}

	protected abstract void drawGeometry();

	/**
	 * Renders To A Texture
	 */
	private void renderToTexture()
	{
		if (m_FrameBufferObject != -1)
		{
			// Bind the fbo
			m_Gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, m_FrameBufferObject);
		}

		// Set Our Viewport (Match Texture Size)
		m_Gl.glViewport(0, 0, m_Texture_Width, m_Texture_Height);

		// Clear the frame buffer (either the default frame buffer or the fbo)
		m_Gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		m_Gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		m_Pgl.hint( m_Pgl.ENABLE_OPENGL_4X_SMOOTH );
		
		drawGeometry();

		// Copy Our ViewPort To The Blur Texture (From 0,0 To 128,128... No Border)
		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, m_Texture);
		m_Gl.glCopyTexImage2D(GL.GL_TEXTURE_RECTANGLE_ARB, 0, GL.GL_RGBA, 0, 0, m_Texture_Width, m_Texture_Height, 0);
		
		// if (m_FrameBufferObject != -1)
		// {
		// // If we used the fbo, restore the default frame buffer
		// m_Gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		// }

		// Restore the viewport (0,0 to 640x480)
		m_Gl.glViewport(0, 0, m_Width, m_Height);
	}

	// Draw The Blurred Image
	public void drawBillboard()
	{
		// Disable AutoTexture Coordinates
		m_Gl.glDisable(GL.GL_TEXTURE_GEN_S);
		m_Gl.glDisable(GL.GL_TEXTURE_GEN_T);

		// Disable Depth Testing
		m_Gl.glDisable(GL.GL_DEPTH_TEST);

		// Enable 2D Texture Mapping
		m_Gl.glEnable(GL.GL_TEXTURE_RECTANGLE_ARB);

		// Bind To The Texture
		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, m_Texture);
		
		// Switch To An Ortho View
		viewOrtho();

		m_Gl.glBegin(GL.GL_QUADS);

		m_Gl.glColor4f(m_R, m_G, m_B, m_A);

		// Texture Coordinate ( 0, 1 )
		m_Gl.glTexCoord2f(0, m_Texture_Height);
		// First Vertex ( 0, 0 )
		m_Gl.glVertex2f(0, 0);
		// Texture Coordinate ( 0, 0 )
		m_Gl.glTexCoord2f(0, 0);
		// Second Vertex ( 0, height )
		m_Gl.glVertex2f(0, m_Height);
		// Texture Coordinate ( 1, 0 )
		m_Gl.glTexCoord2f(m_Texture_Width, 0);
		// Third Vertex ( width, height )
		m_Gl.glVertex2f(m_Width, m_Height);
		// Texture Coordinate ( 1, 1 )
		m_Gl.glTexCoord2f(m_Texture_Width, m_Texture_Height);
		// Fourth Vertex ( width, 0 )
		m_Gl.glVertex2f(m_Width, 0);

		m_Gl.glEnd();

		// Switch To A Perspective View
		viewPerspective();

		// Enable Depth Testing
		m_Gl.glEnable(GL.GL_DEPTH_TEST);
		
		// Disable 2D Texture Mapping
		m_Gl.glDisable(GL.GL_TEXTURE_RECTANGLE_ARB);

		// Unbind The Blur Texture
		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, 0);

	}

//	// Draw The Blurred Image
//	public void drawBlur(int times, float inc)
//	{
//		// Starting Texture Coordinate Offset
//		float spost = 0.0f;
//		// Starting Alpha Value
//		float alpha = 0.2f;
//
//		// Disable AutoTexture Coordinates
//		m_Gl.glDisable(GL.GL_TEXTURE_GEN_S);
//		m_Gl.glDisable(GL.GL_TEXTURE_GEN_T);
//
//		// Enable 2D Texture Mapping
//		m_Gl.glEnable(GL.GL_TEXTURE_2D);
//
//		// Disable Depth Testing
//		m_Gl.glDisable(GL.GL_DEPTH_TEST);
//
//		// Set Blending Mode
//		m_Gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
//
//		// Enable Blending
//		m_Gl.glEnable(GL.GL_BLEND);
//
//		// Bind To The Blur Texture
//		m_Gl.glBindTexture(GL.GL_TEXTURE_2D, m_Texture);
//
//		// Switch To An Ortho View
//		viewOrtho();
//
//		// alphainc=0.2f / Times To Render Blur
//		float alphainc = alpha / times;
//
//		m_Gl.glBegin(GL.GL_QUADS);
//
//		// Number Of Times To Render Blur
//		for (int num = 0; num < times; num++)
//		{
//			// Set The Alpha Value (Starts At 0.2)
//			m_Gl.glColor4f(1.0f, 1.0f, 1.0f, alpha);
//			// Texture Coordinate ( 0, 1 )
//			m_Gl.glTexCoord2f(0 + spost, 1 - spost);
//			// First Vertex ( 0, 0 )
//			m_Gl.glVertex2f(0, 0);
//			// Texture Coordinate ( 0, 0 )
//			m_Gl.glTexCoord2f(0 + spost, 0 + spost);
//			// Second Vertex ( 0, 480 )
//			m_Gl.glVertex2f(0, m_Height);
//			// Texture Coordinate ( 1, 0 )
//			m_Gl.glTexCoord2f(1 - spost, 0 + spost);
//			// Third Vertex ( 640, 480 )
//			m_Gl.glVertex2f(m_Width, m_Height);
//			// Texture Coordinate ( 1, 1 )
//			m_Gl.glTexCoord2f(1 - spost, 1 - spost);
//			// Fourth Vertex ( 640, 0 )
//			m_Gl.glVertex2f(m_Width, 0);
//
//			// Gradually Increase spost (Zooming Closer To Texture Center)
//			spost += inc;
//
//			// Gradually Decrease alpha (Gradually Fading Image Out)
//			alpha = alpha - alphainc;
//		}
//
//		m_Gl.glEnd();
//
//		// Switch To A Perspective View
//		viewPerspective();
//
//		// Enable Depth Testing
//		m_Gl.glEnable(GL.GL_DEPTH_TEST);
//		// Disable 2D Texture Mapping
//		m_Gl.glDisable(GL.GL_TEXTURE_2D);
//		// Disable Blending
//		m_Gl.glDisable(GL.GL_BLEND);
//		// Unbind The Blur Texture
//		m_Gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
//	}

	/**
	 * Creates a frame buffer object.
	 * 
	 * @return the newly created frame buffer object is or -1 if a frame buffer object could not be created
	 */
	private int createFrameBufferObject()
	{
		// Create the FBO
		int[] frameBuffer = new int[1];

		m_Gl.glGenFramebuffersEXT(1, frameBuffer, 0);
		m_Gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, frameBuffer[0]);

		// Create a TEXTURE_SIZE x TEXTURE_SIZE RGBA texture that will be used as color attachment
		// for the fbo.
		int[] colorBuffer = new int[1];

		// Create 1 Texture
		m_Gl.glGenTextures(1, colorBuffer, 0);

		// Bind The Texture
		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, colorBuffer[0]);
		
		// Build Texture Using Information In data
		m_Gl.glTexImage2D(
				GL.GL_TEXTURE_RECTANGLE_ARB, 
				0, 
				GL.GL_RGBA, 
				m_Texture_Width, 
				m_Texture_Height, 
				0, 
				GL.GL_RGBA, 
				GL.GL_UNSIGNED_BYTE, 
				BufferUtil.newByteBuffer(m_Texture_Width * m_Texture_Height * 4));

//		m_Gl.glTexParameteri(GL.GL_TEXTURE_RECTANGLE_ARB, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
//		m_Gl.glTexParameteri(GL.GL_TEXTURE_RECTANGLE_ARB, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

		// Attach the texture to the frame buffer as the color attachment. This
		// will cause the results of rendering to the FBO to be written in the blur texture.
		m_Gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_RECTANGLE_ARB, colorBuffer[0], 0);

		m_Gl.glBindTexture(GL.GL_TEXTURE_RECTANGLE_ARB, 0);
		
		// Create a 24-bit TEXTURE_SIZE x TEXTURE_SIZE depth buffer for the FBO.
		// We need this to get correct rendering results.
		int[] depthBuffer = new int[1];
		m_Gl.glGenRenderbuffersEXT(1, depthBuffer, 0);
		m_Gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, depthBuffer[0]);
		m_Gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT, GL.GL_DEPTH_COMPONENT24, m_Texture_Width, m_Texture_Height);

		// Attach the newly created depth buffer to the FBO.
		m_Gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT, GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, depthBuffer[0]);

		// Make sure the framebuffer object is complete (i.e. set up correctly)
		int status = m_Gl.glCheckFramebufferStatusEXT(GL.GL_FRAMEBUFFER_EXT);

		if (status == GL.GL_FRAMEBUFFER_COMPLETE_EXT)
		{
			return frameBuffer[0];
		} else
		{
			// No matter what goes wrong, we simply delete the frame buffer object
			// This switch statement simply serves to list all possible error codes
			switch (status)
			{
			// case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
			// One of the attachments is incomplete
			case GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
				// Not all attachments have the same size
			case GL.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
				// The desired read buffer has no attachment
			case GL.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
				// The desired draw buffer has no attachment
			case GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
				// Not all color attachments have the same internal format
			case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
				// No attachments have been attached
			case GL.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
				// The combination of internal formats is not supported
			case GL.GL_FRAMEBUFFER_INCOMPLETE_DUPLICATE_ATTACHMENT_EXT:
				// This value is no longer in the EXT_framebuffer_object specification
			default:
				// Delete the color buffer texture
				m_Gl.glDeleteTextures(1, colorBuffer, 0);
				// Delete the depth buffer
				m_Gl.glDeleteRenderbuffersEXT(1, depthBuffer, 0);
				// Delete the FBO
				m_Gl.glDeleteFramebuffersEXT(1, frameBuffer, 0);
				return -1;
			}
		}
	}
}
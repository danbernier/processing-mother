package onar3d.mothergraphics;

import javax.media.opengl.GL;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;

import onar3d.mothergraphics.GLConstants;

import processing.core.PImage;
import processing.opengl.*;

import javax.media.opengl.*;

import codeanticode.glgraphics.GLTexture;

public class MotherGraphics extends PGraphicsOpenGL //implements GLConstants
{
	public GLContext getGLContext()
	{
		return context;
	}

	protected void renderTriangles(int start, int stop)
	{
		report("render_triangles in");
		
		gl.glDisable(GL.GL_LIGHTING);
		for (int i = start; i < stop; i++)
		{
			float a[] = vertices[triangles[i][VERTEX1]];
			float b[] = vertices[triangles[i][VERTEX2]];
			float c[] = vertices[triangles[i][VERTEX3]];

			// This is only true when not textured.
			// We really should pass specular straight through to triangle rendering.
			float ar = clamp(triangleColors[i][0][TRI_DIFFUSE_R] + triangleColors[i][0][TRI_SPECULAR_R]);
			float ag = clamp(triangleColors[i][0][TRI_DIFFUSE_G] + triangleColors[i][0][TRI_SPECULAR_G]);
			float ab = clamp(triangleColors[i][0][TRI_DIFFUSE_B] + triangleColors[i][0][TRI_SPECULAR_B]);
			float br = clamp(triangleColors[i][1][TRI_DIFFUSE_R] + triangleColors[i][1][TRI_SPECULAR_R]);
			float bg = clamp(triangleColors[i][1][TRI_DIFFUSE_G] + triangleColors[i][1][TRI_SPECULAR_G]);
			float bb = clamp(triangleColors[i][1][TRI_DIFFUSE_B] + triangleColors[i][1][TRI_SPECULAR_B]);
			float cr = clamp(triangleColors[i][2][TRI_DIFFUSE_R] + triangleColors[i][2][TRI_SPECULAR_R]);
			float cg = clamp(triangleColors[i][2][TRI_DIFFUSE_G] + triangleColors[i][2][TRI_SPECULAR_G]);
			float cb = clamp(triangleColors[i][2][TRI_DIFFUSE_B] + triangleColors[i][2][TRI_SPECULAR_B]);

			int textureIndex = triangles[i][TEXTURE_INDEX];
			if (textureIndex != -1)
			{
				report("before enable");
				gl.glEnable(GL.GL_TEXTURE_2D);
				report("after enable");

				float uscale = 1.0f;
				float vscale = 1.0f;

				PImage texture = textures[textureIndex];
				if (texture instanceof GLTexture)
				{
					GLTexture tex = (GLTexture) texture;
					gl.glBindTexture(tex.getTextureTarget(), tex.getTextureID());

					uscale *= tex.getMaxTextureCoordS();
					vscale *= tex.getMaxTextureCoordT();

					float cx = 0.0f;
					float sx = +1.0f;
					if (tex.isFlippedX())
					{
						cx = 1.0f;
						sx = -1.0f;
					}

					float cy = 0.0f;
					float sy = +1.0f;
					if (tex.isFlippedY())
					{
						cy = 1.0f;
						sy = -1.0f;
					}

					gl.glBegin(GL.GL_TRIANGLES);
					gl.glColor4f(ar, ag, ab, a[A]);
					gl.glTexCoord2f((cx + sx * a[U]) * uscale, (cy + sy * a[V]) * vscale);
					gl.glNormal3f(a[NX], a[NY], a[NZ]);
					gl.glEdgeFlag(a[EDGE] == 1);
					gl.glVertex3f(a[VX], a[VY], a[VZ]);

					gl.glColor4f(br, bg, bb, b[A]);
					gl.glTexCoord2f((cx + sx * b[U]) * uscale, (cy + sy * b[V]) * vscale);
					gl.glNormal3f(b[NX], b[NY], b[NZ]);
					gl.glEdgeFlag(a[EDGE] == 1);
					gl.glVertex3f(b[VX], b[VY], b[VZ]);

					gl.glColor4f(cr, cg, cb, c[A]);
					gl.glTexCoord2f((cx + sx * c[U]) * uscale, (cy + sy * c[V]) * vscale);
					gl.glNormal3f(c[NX], c[NY], c[NZ]);
					gl.glEdgeFlag(a[EDGE] == 1);
					gl.glVertex3f(c[VX], c[VY], c[VZ]);

					gl.glEnd();

					gl.glBindTexture(tex.getTextureTarget(), 0);
				}
				else
				{
					// Default texturing using a PImage.

					report("before bind");
					bindTexture(texture);
					report("after bind");

					ImageCache cash = (ImageCache) texture.getCache(this);
					uscale = (float) texture.width / (float) cash.twidth;
					vscale = (float) texture.height / (float) cash.theight;

					gl.glBegin(GL.GL_TRIANGLES);

					// System.out.println(a[U] + " " + a[V] + " " + uscale + " " + vscale);
					// System.out.println(ar + " " + ag + " " + ab + " " + a[A]);
					// ar = ag = ab = 1;
					gl.glColor4f(ar, ag, ab, a[A]);
					gl.glTexCoord2f(a[U] * uscale, a[V] * vscale);
					gl.glNormal3f(a[NX], a[NY], a[NZ]);
					gl.glEdgeFlag(a[EDGE] == 1);
					gl.glVertex3f(a[VX], a[VY], a[VZ]);

					gl.glColor4f(br, bg, bb, b[A]);
					gl.glTexCoord2f(b[U] * uscale, b[V] * vscale);
					gl.glNormal3f(b[NX], b[NY], b[NZ]);
					gl.glEdgeFlag(a[EDGE] == 1);
					gl.glVertex3f(b[VX], b[VY], b[VZ]);

					gl.glColor4f(cr, cg, cb, c[A]);
					gl.glTexCoord2f(c[U] * uscale, c[V] * vscale);
					gl.glNormal3f(c[NX], c[NY], c[NZ]);
					gl.glEdgeFlag(a[EDGE] == 1);
					gl.glVertex3f(c[VX], c[VY], c[VZ]);

					gl.glEnd();

					report("non-binding 6");

					gl.glDisable(GL.GL_TEXTURE_2D);
				}

			}
			else
			{ // no texture
				gl.glBegin(GL.GL_TRIANGLES);

				gl.glColor4f(ar, ag, ab, a[A]);
				gl.glNormal3f(a[NX], a[NY], a[NZ]);
				gl.glEdgeFlag(a[EDGE] == 1);
				gl.glVertex3f(a[VX], a[VY], a[VZ]);

				gl.glColor4f(br, bg, bb, b[A]);
				gl.glNormal3f(b[NX], b[NY], b[NZ]);
				gl.glEdgeFlag(a[EDGE] == 1);
				gl.glVertex3f(b[VX], b[VY], b[VZ]);

				gl.glColor4f(cr, cg, cb, c[A]);
				gl.glNormal3f(c[NX], c[NY], c[NZ]);
				gl.glEdgeFlag(a[EDGE] == 1);
				gl.glVertex3f(c[VX], c[VY], c[VZ]);

				gl.glEnd();
			}

		}

		gl.glEnable(GL.GL_LIGHTING);
		report("render_triangles out");
	}

/*	protected void allocate()
	{
		if (context == null)
		{
			// System.out.println("PGraphicsOpenGL.allocate() for " + width + " " + height);
			// new Exception().printStackTrace(System.out);
			// If OpenGL 2X or 4X smoothing is enabled, setup caps object for them
			GLCapabilities capabilities = new GLCapabilities();
			// Starting in release 0158, OpenGL smoothing is always enabled
			if (!hints[DISABLE_OPENGL_2X_SMOOTH])
			{
				capabilities.setSampleBuffers(true);
				capabilities.setNumSamples(2);
			}
			else if (hints[ENABLE_OPENGL_4X_SMOOTH])
			{
				capabilities.setSampleBuffers(true);
				capabilities.setNumSamples(4);
			}

			capabilities.setAlphaBits(8);

			// get a rendering surface and a context for this canvas
			GLDrawableFactory factory = GLDrawableFactory.getFactory();

			
			 * if (PApplet.platform == PConstants.LINUX) { GraphicsConfiguration pconfig =
			 * parent.getGraphicsConfiguration(); System.out.println("parent config is " + pconfig);
			 * 
			 * // GraphicsDevice device = config.getDevice(); //AbstractGraphicsDevice agd = new
			 * AbstractGraphicsDevice(device); //AbstractGraphicsConfiguration agc =
			 * factory.chooseGraphicsConfiguration(capabilities, null, null);
			 * 
			 * AWTGraphicsConfiguration agc = (AWTGraphicsConfiguration)
			 * factory.chooseGraphicsConfiguration(capabilities, null, null); GraphicsConfiguration config =
			 * agc.getGraphicsConfiguration(); System.out.println("agc config is " + config); }
			 
			drawable = factory.getGLDrawable(parent, capabilities, null);
			context = drawable.createContext(null);

			// need to get proper opengl context since will be needed below
			gl = context.getGL();
			// Flag defaults to be reset on the next trip into beginDraw().
			settingsInited = false;

		}
		else
		{
			// changing for 0100, need to resize rather than re-allocate
			// System.out.println("PGraphicsOpenGL.allocate() again for " + width + " " + height);
			reapplySettings();
		}
	}*/
}
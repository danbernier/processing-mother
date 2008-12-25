package onar3d.mothergraphics;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;

import onar3d.mothergraphics.GLConstants;

import processing.opengl.*;
import javax.media.opengl.*;


public class MotherGraphics extends PGraphicsOpenGL implements GLConstants
{
	public GLContext getGLContext()
	{
		return context;
	}
	
	protected void allocate()
	{
		if (context == null)
		{
//		    System.out.println("PGraphicsOpenGL.allocate() for " + width + " " + height);
//		    new Exception().printStackTrace(System.out);
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

			/*
			 if (PApplet.platform == PConstants.LINUX) {
			 GraphicsConfiguration pconfig = parent.getGraphicsConfiguration();
			 System.out.println("parent config is " + pconfig);

			 //      GraphicsDevice device = config.getDevice();
			 //AbstractGraphicsDevice agd = new AbstractGraphicsDevice(device);
			 //AbstractGraphicsConfiguration agc = factory.chooseGraphicsConfiguration(capabilities, null, null);

			 AWTGraphicsConfiguration agc = (AWTGraphicsConfiguration)
			 factory.chooseGraphicsConfiguration(capabilities, null, null);
			 GraphicsConfiguration config = agc.getGraphicsConfiguration();
			 System.out.println("agc config is " + config);
			 }
			 */
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
			//System.out.println("PGraphicsOpenGL.allocate() again for " + width + " " + height);
			reapplySettings();
		}
	}
}
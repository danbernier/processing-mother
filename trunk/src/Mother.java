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

import processing.core.*; 

import processing.opengl.*;
 
import oscP5.*;
import mpe.config.FileParser;
import netP5.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import java.util.*;

import java.lang.reflect.*;
import foetus.*;

import fullscreen.*;  

import onar3d.mothergraphics.*;

public class Mother extends PApplet
{
	SoftFullScreen fs;
	
	PGraphicsOpenGL pgl;
	GL opengl;
	GLU glu;
	
	// For OSC
	OscP5 oscP5;
	
	/* a NetAddress contains the ip address and port number of a remote location in the network. */
	NetAddress oscBroadcastLocation; 
	int m_osc_send_port;
	int m_osc_receive_port;
	String m_IP;
	
	SynthContainer m_SynthContainer;
	
	String m_Synth_Folder;
	
	int m_Width;
	int m_Height;
		
	FileParser fp;
	
	boolean m_FullScreen;
		
	/**
     * Loads the Settings from the Client INI file
     */
    private void loadIniFile(String fileString)
    {
        fp = new FileParser(fileString);
        
        //parse ini file if it exists
        if (fp.fileExists()) 
        {    		
    		m_IP 				= fp.getStringValue("IP");
            m_osc_receive_port 	= fp.getIntValue("osc_receive_port");
            m_osc_send_port 	= fp.getIntValue("osc_send_port");
            int[] localDim 		= fp.getIntValues("screenSize");
            
            m_Width = localDim[0];
            m_Height = localDim[1];
            
            m_Synth_Folder = fp.getStringValue("SynthFolder");
            
            if(fp.getIntValue("FullScreen")==1)
            {
            	m_FullScreen = true;
            }
            else
            {
            	m_FullScreen = false;
            }
        }
    }

	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() 
	{	
		m_Width 	= 640;
		m_Height 	= 480;
		
		m_FullScreen = true;
		
		// For OSC
		m_IP = "127.0.0.1";
		m_osc_receive_port 		= 7005;
		m_osc_send_port			= 5432;
		m_Synth_Folder 			= "X:\\Lumia Synths";
		
		// Loading setup values from .ini file
		loadIniFile(sketchPath("mother"+".ini"));
		
		if(m_FullScreen)
		{
			/*// From ProcessingHacks, for fullscreen without problems 
			// where window minimizes when focus is lost.		
			frame.dispose();  
			frame.setUndecorated(true);
			frame.setVisible(true); 
			*/
			
			// Create the fullscreen object
			fs = new SoftFullScreen(this);

			// enter fullscreen mode
			fs.enter(); 
			
			m_Width 	= screen.width;
			m_Height 	= screen.height;
		}
		
		size(m_Width, m_Height, GLConstants.MOTHERGRAPHICS/*OPENGL*/);
		
		frameRate(24);
		
		hint( ENABLE_OPENGL_4X_SMOOTH ); // Just to trigger renderer change.
		//hint( ENABLE_OPENGL_2X_SMOOTH ); // Calling this directly doesn't work.
		//hint(DISABLE_OPENGL_2X_SMOOTH);
		
		pgl 	= (PGraphicsOpenGL) g; 
		opengl 	= pgl.gl;
		glu 	= ((PGraphicsOpenGL)g).glu;
		
		m_SynthContainer = new SynthContainer(m_Synth_Folder);
				
		// start oscP5
		oscP5 					= new OscP5(this, m_osc_receive_port);
		oscBroadcastLocation 	= new NetAddress(m_IP, m_osc_send_port);

		// For testing
	//	m_SynthContainer.Add("Grad_02", 		"Gradient", 		m_Width, m_Height, this);
	//	m_SynthContainer.Add("Waltz_02", 		"Waltz", 			m_Width, m_Height, this);
	//	m_SynthContainer.Add("CubeSpine_02", 	"CubeSpine", 		m_Width, m_Height, this);
	//	m_SynthContainer.Add("flight_02", 		"source_particles", m_Width, m_Height, this);
	}
			
	/*
	 * 
	 */
	private void PreDrawChildUpdate(PApplet child)
	{
		child.mouseX 		= this.mouseX;
		child.mouseY 		= this.mouseY;
		child.mousePressed 	= this.mousePressed;
		child.keyPressed 	= this.keyPressed;
		child.key 			= this.key;
	}
	
	/*
	 * (non-Javadoc)
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() 
	{
		ChildWrapper current;
		
		// From ProcessingHacks, for fullscreen without problems 
		// where window minimizes when focus is lost.
		if(m_FullScreen)
			frame.setLocation(0,0);
		
		// If minimized, expand again
		if (frame.getExtendedState()==1)
			frame.setExtendedState(0);  
				
		opengl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Set The Clear Color To Black
		opengl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		for(int i = 0; i < m_SynthContainer.Synths().size(); i++)
		{
			current = (ChildWrapper)m_SynthContainer.Synths().get(i);
			
			PreDrawChildUpdate(current.Child());
		
			opengl.glEnable(GL.GL_BLEND);
//			opengl.glDisable(GL.GL_DEPTH_TEST); // Disables Depth Testing
						
//			//For testing (getting opengl state)		
//			IntBuffer arg1 = IntBuffer.allocate(1);
//			opengl.glGetIntegerv(GL.GL_BLEND_DST, arg1);
//			println(arg1.get(0));

			pgl.colorMode( RGB, 255 );
			
			opengl.glPushMatrix();
					
			opengl.glBlendFunc(current.GetBlending_Source(), current.GetBlending_Destination());
			
			try
			{
			current.draw(i);
			}
			catch(Exception e)
			{
				println("Drawing kid crashed!");
			}
			opengl.glPopMatrix();
			
			opengl.glDisable(GL.GL_BLEND);
		}
	
	}
	
	public void keyPressed()
	{
		PApplet child;
		Method keyMethod;
		
		for(int i = 0; i < m_SynthContainer.Synths().size(); i++)
		{
			child = ((ChildWrapper)m_SynthContainer.Synths().get(i)).Child();

			// Handling messages to synths							
			try
			{
				child.keyEvent 	= this.keyEvent;
				child.key		= this.key;
				child.keyCode   = this.keyCode;
				
				keyMethod = child.getClass().getMethod("keyPressed", new Class[] {});
				keyMethod.invoke(child, new Object[] {});
			}
			catch(Exception e)
			{
				println("CRASH keyPressed" + e.getMessage());
			}
		} 
	}
	
	/*
	 * incoming osc message are forwarded to the oscEvent method.
	 */
	void oscEvent(OscMessage theOscMessage)
	{
		PApplet child;
		Method oscEventMethod;
		
		String 		addrPattern = theOscMessage.addrPattern();
		String 		typetag 	= theOscMessage.typetag();
		String[] 	splits 		= addrPattern.split("/");
		
		println("Mother received an osc message with address pattern " + addrPattern + ", and typetag: " + typetag);
		
		/* check if theOscMessage has the address pattern we are looking for. */
		if ( splits.length >= 2 && (splits[1].compareTo("Mother") == 0))
		{
			if( splits[2].compareTo("Get_synth_names") == 0 )
			{				
				OscMessage oscMessage = new OscMessage("/Synth_names");
							
				for (Enumeration<String> e = m_SynthContainer.get_Synth_Names().keys(); e.hasMoreElements();)
				{
					oscMessage.add( e.nextElement() );
			    }		
				
				oscP5.send(oscMessage, oscBroadcastLocation);
			}
			else if ( splits[2].compareTo("Add_synth") == 0 )
			{
				if (theOscMessage.checkTypetag("ss"))
				{			
					if(!m_SynthContainer.contains(theOscMessage.get(1).stringValue()))
					{
						this.redraw = false;
						noLoop();
						
						ChildWrapper w = m_SynthContainer.Add(	theOscMessage.get(1).stringValue(), 
												theOscMessage.get(0).stringValue(), 
												m_Width, 
												m_Height, 
												this);
						
						sendSupportedMessages(w);
						
						loop();
						this.redraw = true;
					}
				}
			}
			else if ( splits[2].compareTo("Reset") == 0 )
			{
				m_SynthContainer.reset();
			}
			else if ( splits[2].compareTo("Remove_synth") == 0 )
			{
				if (theOscMessage.checkTypetag("s"))
				{				
					m_SynthContainer.Remove( theOscMessage.get(0).stringValue() );
				}
			}
			else if ( splits[2].compareTo("Move_synth") == 0 )
			{
				if (theOscMessage.checkTypetag("si"))
				{				
					m_SynthContainer.Move( theOscMessage.get(0).stringValue(), theOscMessage.get(1).intValue() );
				}
			}
			else if ( splits[2].compareTo("Set_synth_color") == 0 )
			{
				if (theOscMessage.checkTypetag("sffff"))
				{				
					m_SynthContainer.Set_Synth_Color( 	theOscMessage.get(0).stringValue(), 
														theOscMessage.get(1).floatValue(),
														theOscMessage.get(2).floatValue(),
														theOscMessage.get(3).floatValue(),
														theOscMessage.get(4).floatValue());
				}
			}
			else if ( splits[2].compareTo("Set_synth_blending") == 0 )
			{
				if (theOscMessage.checkTypetag("sii"))
				{				
					m_SynthContainer.Set_Synth_Blending(theOscMessage.get(0).stringValue(),
														theOscMessage.get(1).intValue(), 
														theOscMessage.get(2).intValue());
				}
			}
			else if ( splits[2].compareTo("Child") == 0 && splits.length >= 4)
			{
//				System.out.println(theOscMessage.addrPattern());
				
				StringBuffer newAddrPattern = new StringBuffer();
				String childName;
				
				for(int pos = 4; pos < splits.length; pos++)
				{
					newAddrPattern.append("/" + splits[pos]);
				}
				
				for(int i = 0; i < m_SynthContainer.Synths().size(); i++)
				{
					child 		= ((ChildWrapper)m_SynthContainer.Synths().get(i)).Child();
					childName 	= ((ChildWrapper)m_SynthContainer.Synths().get(i)).GetName(); 
					
					if( childName.compareTo(splits[3]) == 0)
					{
						if(splits[4].compareTo("Get_Supported_Messages") == 0)
						{
							sendSupportedMessages((ChildWrapper)m_SynthContainer.Synths().get(i));
						}
						else
						{
							try // Handling messages to synths
							{	
								// removing "/Mother/Child/Synth_Name" from address pattern
								theOscMessage.setAddrPattern(newAddrPattern.toString());
																			
								oscEventMethod = child.getClass().getDeclaredMethod("oscEvent", new Class[] {OscMessage.class});
								
								oscEventMethod.invoke(child, new Object[] {theOscMessage});
							}
							catch(Exception e)
							{
								println("CRASH Child oscEvent" + e.getMessage());
							}
						}
												
						break;
					}
				}
			}
		}
		else // Message not for mother
		{
//			println("Unhandled OSC message: " + theOscMessage.addrPattern());
		}
	}
	
	protected void sendSupportedMessages(ChildWrapper wrapper)
	{
		PApplet child 		= wrapper.Child();
		String childName 	= wrapper.GetName(); 
		
		try
		{
			Foetus			f = null;
			
			try
			{
				f = (Foetus)child.getClass().getField("f").get(child);
			}
			catch (Exception e)
			{
				println("CRASH: Accessing child's foetus failed!" + e.getMessage());
			}
						
			Hashtable<String,String> supportedMessages = f.getSupportedMessages();
																
			OscMessage oscMessage = new OscMessage("/Synth_supported_messages/" + childName);
			
			Enumeration<String> e = supportedMessages.elements();
			
			for (Enumeration<String> ek = supportedMessages.keys(); ek.hasMoreElements();)
			{
				oscMessage.add( (String)ek.nextElement() );
				oscMessage.add( (String)e.nextElement() );
		    }		
			
			oscP5.send(oscMessage, oscBroadcastLocation);
		}
		catch(Exception e)
		{
			println("CRASH Child getSupportedMessages" + e.getMessage());
		}
	}
	
	// -Djava.library.path=src/processing/opengl/library  
	
	// For remote debugging, haven't gotten it right yet though: -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,suspend=n,server=y
	
	
	static public void main(String args[]) 
	{
//		PApplet.main(new String[] { "--present", "Mother"} );	
		PApplet.main(new String[] { "Mother"} );
	}

}

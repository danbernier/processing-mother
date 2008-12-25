

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import processing.core.PApplet;
import processing.core.PApplet.RegisteredMethods;

import foetus.*;

public class SynthContainer
{
	URL[] m_Visual_Synth_urls;
	
	Hashtable m_Visual_Synth_Names;
	
	Hashtable Synth_Names() { return m_Visual_Synth_Names; }
	
	ArrayList m_VisualSynths;
	
	ArrayList Synths() { return m_VisualSynths; } 
	
	Hashtable m_Visual_Synth_Keys;

	String m_Synth_Folder;

	public SynthContainer(String folder)
	{
		m_VisualSynths 			= new ArrayList();
		m_Visual_Synth_Names 	= new Hashtable();
		m_Visual_Synth_Keys 	= new Hashtable();
		
		m_Synth_Folder = folder;
		
		PopulateSynthURLS();
	}
	
	/*
	 * Scans folder containing synths and stores URL for each
	 */
	private void PopulateSynthURLS()
	{
		String[] fileName;
		File oooClassPath 		= new File(m_Synth_Folder);
		File[] files 			= oooClassPath.listFiles();
		m_Visual_Synth_urls 	= new URL[files.length];
		
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				fileName 				= files[i].getName().split(".jar");
				m_Visual_Synth_urls[i] 	= files[i].toURI().toURL();
				System.out.println("Found Synth: " + fileName[0]);
				
				m_Visual_Synth_Names.put(fileName[0], fileName[0]);
			} 
			catch (MalformedURLException ex)
			{
				System.out.println("MalformedURLException: " + ex.getMessage());
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
	public boolean contains(String key)
	{
		if(!m_Visual_Synth_Keys.containsKey(key))
		{
			return false;
		}
		else
			return true;
	}
	
	/*
	 * Create a new synth layer
	 */
	public ChildWrapper Add(String key, String sketchName, int w, int h, PApplet mother)
	{
		ChildWrapper new_Wrapper = null;
		
		if(!m_Visual_Synth_Keys.containsKey(key))
		{			
			try
			{
				new_Wrapper = new ChildWrapper(
													w, 
													h, 
													m_Synth_Folder, 
													sketchName, 
													key, 
													true); // Render Billboard 
				m_VisualSynths.add( new_Wrapper );
				
				InitChild( new_Wrapper.Child(), mother );
				
				m_Visual_Synth_Keys.put(key, sketchName);
			
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return new_Wrapper;
		}	
		
		return new_Wrapper;
	}
	
	public ChildWrapper GetChildWrapper(String key)
	{
		ChildWrapper toReturn = null;
		
		if(m_Visual_Synth_Keys.containsKey(key))
		{
			for(int i = 0; i < m_VisualSynths.size(); i++)
			{
				if( ((ChildWrapper)m_VisualSynths.get(i)).GetName().compareTo(key) == 0)
				{
					return (ChildWrapper)m_VisualSynths.get(i);
				}
			}	
		}	
		
		return toReturn;
	}
	
	public boolean Remove(String key)
	{
		if(m_Visual_Synth_Keys.containsKey(key))
		{
			for(int i = 0; i < m_VisualSynths.size(); i++)
			{
				if( ((ChildWrapper)m_VisualSynths.get(i)).GetName().compareTo(key) == 0)
				{
					((ChildWrapper)m_VisualSynths.get(i)).Child().stop();
					m_VisualSynths.remove(i);
					break;
				}
			}	
			
			m_Visual_Synth_Keys.remove(key);
			
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	public boolean reset()
	{
		m_Visual_Synth_Keys.clear();
		m_VisualSynths.clear();
			
		return true;
	}
	
	public boolean Move(String key, int newLocation)
	{
		if(m_Visual_Synth_Keys.containsKey(key))
		{
			for(int i = 0; i < m_VisualSynths.size(); i++)
			{
				ChildWrapper element = ((ChildWrapper)m_VisualSynths.get(i));
				
				if( (element.GetName().compareTo(key) == 0) 
						&& (m_VisualSynths.size() > newLocation)
						&& (newLocation >= 0))
				{	 
					m_VisualSynths.remove(i);
					
					m_VisualSynths.add(newLocation, element);
					break;
				}
			}	
					
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	public boolean Set_Synth_Color(String key, float r, float g, float b, float a)
	{
		if(m_Visual_Synth_Keys.containsKey(key))
		{
			for(int i = 0; i < m_VisualSynths.size(); i++)
			{
				ChildWrapper element = ((ChildWrapper)m_VisualSynths.get(i));
				
				if( element.GetName().compareTo(key) == 0)
				{	 
					element.Set_Color(r,g,b,a);
					
					break;
				}
			}	
					
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	public boolean Set_Synth_Blending(String key, int source, int dest)
	{
		if(m_Visual_Synth_Keys.containsKey(key))
		{
			for(int i = 0; i < m_VisualSynths.size(); i++)
			{
				ChildWrapper element = ((ChildWrapper)m_VisualSynths.get(i));
				
				if( element.GetName().compareTo(key) == 0)
				{	
					/*					
					GL_ZERO						0
					GL_ONE						1
					GL_SRC_COLOR				768
					GL_ONE_MINUS_SRC_COLOR		769
					GL_DST_COLOR				774
					GL_ONE_MINUS_DST_COLOR		775
					GL_SRC_ALPHA				770
					GL_ONE_MINUS_SRC_ALPHA		771
					GL_DST_ALPHA				772
					GL_ONE_MINUS_DST_ALPHA		773
					GL_SRC_ALPHA_SATURATE		776
					GL_CONSTANT_COLOR			32769
					GL_ONE_MINUS_CONSTANT_COLOR	32770
					GL_CONSTANT_ALPHA			32771
					GL_ONE_MINUS_CONSTANT_ALPHA	32772
					*/
					
					element.SetBlending_Source(source);
					element.SetBlending_Destination(dest);
					
					break;
				}
			}	
					
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	public void Initialize(PApplet mother)
	{
		for(int i = 0; i < m_VisualSynths.size(); i++)
		{
			InitChild( ((ChildWrapper)m_VisualSynths.get(i)).Child(), mother );
		}		
	}
	
	/*
	 * 
	 */
	private void InitChild(PApplet child, PApplet parent)
	{
		Method[] methods = child.getClass().getMethods();
		Method[] declaredMethods = child.getClass().getDeclaredMethods();
	
		child.g = parent.g;
		
		child.setSize(parent.width, parent.height);
		
		/* With this, I'm hoping the child will run in a separate thread, but its timer will not call the draw method.
		 * Instead, ony one timer is running, the one in Mother.
		 */
		child.noLoop();
		
		try
		{	
			for(int i = 0; i < methods.length; i++)
			{
				if(methods[i].getName().equals("init"))
				{
					methods[i].invoke(child, new Object[] {});
					break;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("CRASH PApplet.init: " + e.getMessage());
		}

		child.frameCount	= parent.frameCount;
		child.frameRate		= parent.frameRate;		  
		child.frame			  = parent.frame;		  
		child.screen		  = parent.screen;
		child.recorder		= parent.recorder;
		child.sketchPath	= parent.sketchPath;
		child.pixels		  = parent.pixels;
		  
		child.width 		= parent.width;
		child.height 		= parent.height;
		
		child.noLoop();
		
		Foetus foetusField;
					
		try
		{	
			for(int i = 0; i < declaredMethods.length; i++)
			{
				if(declaredMethods[i].getName().equals("initializeFoetus"))
				{
					declaredMethods[i].invoke(child, new Object[] {});
					
					break;
				}
			}
			
			foetusField = (Foetus)child.getClass().getDeclaredField("f").get(child);

			foetusField.standalone = false;	
		}
		catch(Exception e)
		{
			System.out.println("CRASH standalone: " + e.getMessage());
		}
	}

}

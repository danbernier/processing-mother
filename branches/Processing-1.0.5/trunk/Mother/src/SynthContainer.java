

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import processing.core.PApplet;

import foetus.*;

public class SynthContainer
{
	ArrayList<URL> m_Visual_Synth_urls;
	
	URL[] m_Library_file_URLS;
	
	Hashtable<String,String> m_Visual_Synth_Names;

	ArrayList<ChildWrapper> m_VisualSynths;
	
	ArrayList<ChildWrapper> Synths() { return m_VisualSynths; } 
	
	Hashtable<String,String> m_Visual_Synth_Keys;

	String m_Synth_Folder;

	URL[] get_Library_File_URLS() { return m_Library_file_URLS; }
	
	Hashtable<String,String> get_Synth_Names() { return m_Visual_Synth_Names; }
		
	public SynthContainer(String folder)
	{
		m_VisualSynths 			= new ArrayList<ChildWrapper>();
		m_Visual_Synth_Names 	= new Hashtable<String,String>();
		m_Visual_Synth_Keys 	= new Hashtable<String,String>();
		
		m_Synth_Folder = folder;
		
		PopulateSynthURLS();
		PopulateLibraryURLS();
	}
	
	/*
	 * Scans folder containing synths and stores URL for each
	 */
	private void PopulateSynthURLS()
	{
		String[] fileName;
		File oooClassPath 		= new File(m_Synth_Folder);
		File[] files 			= oooClassPath.listFiles();
		m_Visual_Synth_urls 	= new ArrayList<URL>();
		
		for (int i = 0; i < files.length; i++)
		{
			try
			{
				fileName = files[i].getName().split(".jar");
				
				if(fileName[fileName.length-1].compareTo(".jar")==0)
				{
					m_Visual_Synth_urls.add(files[i].toURI().toURL());
					System.out.println("Found Synth: " + fileName[0]);
					
					m_Visual_Synth_Names.put(fileName[0], fileName[0]);
				}	
			} 
			catch (MalformedURLException ex)
			{
				System.out.println("MalformedURLException: " + ex.getMessage());
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private void PopulateLibraryURLS()
	{
		String[] fileName;
		
		File oooClassPath 		= new File(m_Synth_Folder + "//" + "libraries");
		File[] files 			= oooClassPath.listFiles();
		
		// Check if Libraries folder exists. If not, create empty list.
		if(files != null)
		{
			try
			{
				if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1)
				{
					m_Library_file_URLS		= new URL[] {oooClassPath.toURI().toURL()};
				}
		        else 
		        {
		        	m_Library_file_URLS	= new URL[files.length];
		        	
					for (int i = 0; i < files.length; i++)
					{
						fileName 				= files[i].getName().split(".jar");
						m_Library_file_URLS[i] 	= files[i].toURI().toURL();
						System.out.println("Found library: " + fileName[0]);
					}
		        }
			} 
			catch (MalformedURLException ex)
			{
				System.out.println("MalformedURLException: " + ex.getMessage());
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		else
		{
			m_Library_file_URLS	= new URL[0];
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
	public ChildWrapper Add(String key, String sketchName, int w, int h, Mother mother)
	{
		ChildWrapper new_Wrapper = null;
		
		if(!m_Visual_Synth_Keys.containsKey(key))
		{			
//			try
			{
				new_Wrapper = new ChildWrapper(
													w, 
													h, 
													m_Synth_Folder, 
													m_Library_file_URLS,
													sketchName, 
													key, 
													false, // Render Billboard
													mother);
				m_VisualSynths.add( new_Wrapper );
				
				InitChild( new_Wrapper, mother );
				
				m_Visual_Synth_Keys.put(key, sketchName);
			
			} 
	/*		catch (Exception e)
			{
				e.printStackTrace();
			}*/
			
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
	
	public void Initialize(Mother mother)
	{
		for(int i = 0; i < m_VisualSynths.size(); i++)
		{
			InitChild( ((ChildWrapper)m_VisualSynths.get(i)), mother );
		}		
	}
	
	/*
	 * 
	 */
	private void InitChild(ChildWrapper cw, Mother parent)
	{
		PApplet child = cw.Child();
		
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
		child.frame			= parent.frame;		  
		child.screen		= parent.screen;
		child.recorder		= parent.recorder;
//		child.sketchPath	= parent.sketchPath;
		
		child.sketchPath	=  m_Synth_Folder;
			
		child.pixels		= parent.pixels;
		  
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

			cw.setFoetusField(foetusField);
			
			foetusField.standalone = false;
			
			foetusField.setSpeedFraction(parent.getSpeedFraction());
		}
		catch(Exception e)
		{
			System.out.println("CRASH standalone: " + e.getMessage());
		}
	}

}
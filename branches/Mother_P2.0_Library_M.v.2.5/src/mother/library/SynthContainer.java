package mother.library;

import java.util.ArrayList;
import java.util.Hashtable;

import processing.core.PApplet;

public class SynthContainer
{
	private ArrayList<ChildWrapper> 	m_VisualSynths;
	private Hashtable<String, String> 	m_Visual_Synth_Keys;
	
	public ArrayList<ChildWrapper> Synths() {
		return m_VisualSynths;
	}
	
	public SynthContainer() {
		m_VisualSynths 			= new ArrayList<ChildWrapper>();
		m_Visual_Synth_Keys 	= new Hashtable<String, String>();
	}
	
	/**
	 * Create a new synth layer
	 * @param key
	 * @param sketchName
	 * @param mother
	 * @return
	 */
	public ChildWrapper Add(	String 			key, 
								String 			sketchName, 
								SynthLoader 	container,
								Mother 			mother) {
		ChildWrapper new_Wrapper = null;
		
		if(!m_Visual_Synth_Keys.containsKey(key)) {		
			PApplet child = null;
						
			child = container.LoadSketch(sketchName);
			
			if(child!=null) {
				new_Wrapper = new ChildWrapper(		child,										 
													key, 
													mother.getBillboardFlag(), // Render Billboard
													mother);
				m_VisualSynths.add( new_Wrapper );
					
				container.InitChild( new_Wrapper, mother );
					
				m_Visual_Synth_Keys.put(key, sketchName);
			}
		}	
		
		return new_Wrapper;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		if (!m_Visual_Synth_Keys.containsKey(key)) {
			return false;
		}
		else
			return true;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public ChildWrapper GetChildWrapper(String key)	{
		ChildWrapper toReturn = null;

		if (m_Visual_Synth_Keys.containsKey(key)) {
			for (int i = 0; i < m_VisualSynths.size(); i++)	{
				if (((ChildWrapper) m_VisualSynths.get(i)).GetName().compareTo(key) == 0) {
					return (ChildWrapper) m_VisualSynths.get(i);
				}
			}
		}

		return toReturn;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public ChildWrapper Remove(String key) {
		ChildWrapper toReturn = null;

		if (m_Visual_Synth_Keys.containsKey(key)) {
			for (int i = 0; i < m_VisualSynths.size(); i++)	{
				if (((ChildWrapper) m_VisualSynths.get(i)).GetName().compareTo(key) == 0) {
					toReturn = ((ChildWrapper) m_VisualSynths.get(i));

					((ChildWrapper) m_VisualSynths.get(i)).Child().stop();

					m_VisualSynths.remove(i);
					break;
				}
			}

			m_Visual_Synth_Keys.remove(key);

			return toReturn;
		}
		else {
			return toReturn;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean reset() {
		m_Visual_Synth_Keys.clear();
		m_VisualSynths.clear();

		return true;
	}

	/**
	 * 
	 * @param key
	 * @param newLocation
	 * @return
	 */
	public boolean Move(String key, int newLocation) {
		if (m_Visual_Synth_Keys.containsKey(key)) {
			for (int i = 0; i < m_VisualSynths.size(); i++)	{
				ChildWrapper element = ((ChildWrapper) m_VisualSynths.get(i));

				if ((element.GetName().compareTo(key) == 0) &&
						(m_VisualSynths.size() > newLocation)
						&& (newLocation >= 0)) {
					m_VisualSynths.remove(i);

					m_VisualSynths.add(newLocation, element);
					break;
				}
			}

			return true;
		}
		else {
			return false;
		}
	}	
	
	/**
	 * 
	 * @param key
	 * @param source
	 * @param dest
	 * @return
	 */
	public boolean Set_BlendMode(String key, int mode)	{
		if (m_Visual_Synth_Keys.containsKey(key)) {
			for (int i = 0; i < m_VisualSynths.size(); i++)	{
				ChildWrapper element = ((ChildWrapper) m_VisualSynths.get(i));

				if (element.GetName().compareTo(key) == 0)	{
					/*
					 * GL_ZERO 0 GL_ONE 1 GL_SRC_COLOR 768 GL_ONE_MINUS_SRC_COLOR 769 GL_DST_COLOR 774
					 * GL_ONE_MINUS_DST_COLOR 775 GL_SRC_ALPHA 770 GL_ONE_MINUS_SRC_ALPHA 771 GL_DST_ALPHA 772
					 * GL_ONE_MINUS_DST_ALPHA 773 GL_SRC_ALPHA_SATURATE 776 GL_CONSTANT_COLOR 32769
					 * GL_ONE_MINUS_CONSTANT_COLOR 32770 GL_CONSTANT_ALPHA 32771 GL_ONE_MINUS_CONSTANT_ALPHA 32772
					 */

					element.SetBlendMode(mode);
					
//					element.SetBlending_Source(source);
//					element.SetBlending_Destination(dest);

					break;
				}
			}

			return true;
		}
		else {
			return false;
		}
	}
}

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

package foetus;

import processing.core.*;

import java.util.*;

public class Foetus
{
	PApplet parent;
	
	int[] m_BGColor;
	
	Hashtable<String,String> m_Messages;
	
	public boolean standalone = true;

	float m_SpeedFraction = 1;
	
	/**
	 * Constructor
	 * @param parent
	 */
	public Foetus(PApplet parent)
	{
		this.parent = parent;
		
		m_Messages = new Hashtable<String,String>();
				
		parent.registerDispose(this);
		parent.registerPre(this);
		
		m_BGColor = new int[] {128, 128, 128};
	}
	
	public void setSpeedFraction(float inSF) { m_SpeedFraction = inSF; }
	
	public float getSpeedFraction() { return m_SpeedFraction; }
	
	public int millis() 
	{
		double pm = parent.millis();
	    return (int)(pm/m_SpeedFraction);
	    //return (int)(parent.millis()/m_SpeedFraction);
	}
	
	public void setBGColor(int r, int g, int b)
	{
		m_BGColor[0] = r;
		m_BGColor[1] = g;
		m_BGColor[2] = b;
	}
	
	public void dispose()
	{

	}
		
	public void registerMethod(String address, String typetag)
	{
		m_Messages.put(address, typetag);
	}

	public void unregisterMethod(String address)
	{
		m_Messages.remove(address);
	}
	
	public Hashtable<String,String> getSupportedMessages()
	{
		return m_Messages;
	}
	
	public void pre()
	{
		if (standalone)
			parent.background(m_BGColor[0],m_BGColor[1],m_BGColor[2]);
	}
	
}
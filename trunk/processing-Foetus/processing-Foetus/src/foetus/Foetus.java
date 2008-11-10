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
import java.lang.reflect.Method;

public class Foetus
{
	PApplet parent;
	
	Hashtable m_Messages;
	
	public boolean standalone = true;

	/**
	 * Constructor
	 * @param parent
	 */
	public Foetus(PApplet parent)
	{
		this.parent = parent;
		
		m_Messages = new Hashtable();
				
		parent.registerDispose(this);
		parent.registerPre(this);
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
	
	public Hashtable getSupportedMessages()
	{
		return m_Messages;
	}
	
	public void pre()
	{
		if (standalone)
			parent.background(128);
	}
	
}
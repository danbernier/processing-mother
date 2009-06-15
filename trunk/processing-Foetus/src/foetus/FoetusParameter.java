package foetus;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.*;

import processing.core.PApplet;

public class FoetusParameter
{
	float m_Value;
	float m_LastValue;
	float m_NewValue;
	float m_Factor;
	
	String m_Address;
	
	boolean m_Splerp = true;

	Foetus r_f; 
	
	Animator animation = null;
		
	public float getValue()
	{
		if(m_Splerp)
		{
			m_Value = PApplet.lerp(m_LastValue, m_NewValue, m_Factor);
		}
		
		//System.out.println("Last: " + m_LastValue + " New: " + m_NewValue + " Factor: " + m_Factor);
		
		return m_Value;
	}
	
	/*
	 * 
	 */
	public FoetusParameter(Foetus f, float value, String address, String typetag )
	{
		r_f 					= f;		
		m_Value 				= value;
		m_NewValue				= value;
		m_LastValue				= value;

		m_Address = address;
		
		if(r_f!=null)
			r_f.registerMethod(address, typetag);
		
		float times[] 	= {0, 1};
        Float values[] 	= {new Float(0), new Float(1)};
		
		KeyTimes  keyTimes 	= new KeyTimes(times);
	    KeyValues keyValues = KeyValues.create(values);
		KeyFrames keyFrames = new KeyFrames(keyValues, keyTimes,  new SplineInterpolator(1.00f, 0.00f, 0.00f, 1.00f));
		
		
		animation = new Animator(	(int)(1000/r_f.getSpeedFraction()), 
									1,  
									Animator.RepeatBehavior.LOOP, 
									new PropertySetter(this, "factor", keyFrames) );
		
		//animation.setResolution(42);
		
		//animation.setAcceleration(0.5f);
		//animation.setDeceleration(0.5f);
	}

	
	public void setFactor(Float factor)
	{
		m_Factor = factor;
		
		if(m_Factor>=1.0f)
		{
			r_f.setUpdatingStatus(m_Address, false);
		}
		else
		{
			r_f.setUpdatingStatus(m_Address, true);	
		}
	}
		
	public void setValue(float val)
	{
		long elapsed;
		
	    m_LastValue = m_Value;
	    m_NewValue  = val;

	    try
	    {
		    m_Factor = 0.0f;
		    
		    elapsed = (long)(animation.getTotalElapsedTime()/r_f.getSpeedFraction());
		    		    
		    animation.stop();
		    
		    if(elapsed<(500/r_f.getSpeedFraction()))
		    {
		    	m_Splerp 	= false;
		    	m_LastValue = val;
		    	m_Value 	= val;
		    	r_f.setUpdatingStatus(m_Address, false);
		    }
		    else
		    {
		    	m_Splerp = true;
		    	
		    	if(elapsed>(3000/r_f.getSpeedFraction()))
		    		elapsed = (long)(3000/r_f.getSpeedFraction());
		  
		    	animation.setDuration((int)elapsed);
		    	animation.start();
		    }
		    
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Something exceptional happened in FoetusParameter! " + e.getMessage());
	    }
	}
}

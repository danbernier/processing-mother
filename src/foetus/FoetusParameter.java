package foetus;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.*;

import processing.core.PApplet;

public class FoetusParameter
{
	Foetus r_f; 
	
	float m_Value;
	
	float m_InterpolationPosition;
	float m_LastValue;
	float m_NewValue;
	
	float m_InterpolationSpeed;
	
	float m_Factor;
	long m_LastTimeStamp;
	Animator animation = null;
	
	boolean m_Splerp = true;
	
	public float getValue()
	{
		if(m_Splerp)
			m_Value = PApplet.lerp(m_LastValue, m_NewValue, m_Factor);

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
		m_InterpolationSpeed 	= 0.1f;

		f.registerMethod(address, typetag);
		
		float times[] 	= {0, 1};
        Float values[] 	= {new Float(0), new Float(1)};
		
		KeyTimes  keyTimes 	= new KeyTimes(times);
	    KeyValues keyValues = KeyValues.create(values);
		KeyFrames keyFrames = new KeyFrames(keyValues, keyTimes,  new SplineInterpolator(1.00f, 0.00f, 0.00f, 1.00f));
		
		animation = new Animator(	1000, 
									1,  
									Animator.RepeatBehavior.LOOP, 
									new PropertySetter(this, "factor", keyFrames) );
		
		m_LastTimeStamp = animation.getTotalElapsedTime();
		
		//animation.setAcceleration(0.5f);
		//animation.setDeceleration(0.5f);
	}

	public void setFactor(Float factor)
	{
		m_Factor = factor;
	}
	
	
	/*
	public void setInterpolationSpeed(float speed)
	{
		m_InterpolationSpeed = speed;
	}
	*/
	
	public void interpolateStep()
	{
		/*
		m_InterpolationPosition += m_InterpolationSpeed;
		 
		if(m_InterpolationPosition>1.0)
		{
			m_InterpolationPosition = 1.0f;
		}
		
	    m_Value = PApplet.lerp(m_LastValue, m_NewValue, m_InterpolationPosition);
	    */
	}
	
	
	public void setValue(float val)
	{
		m_InterpolationPosition  	= 0.0f;
	    m_LastValue 				= m_Value;
	    m_NewValue  				= val;

	    try
	    {
		    m_Factor 		= 0.0f;
		    Double toSet 	= Math.abs(new Double(m_LastTimeStamp-animation.getTotalElapsedTime()));
		    m_LastTimeStamp = animation.getTotalElapsedTime();
		    
		    animation.stop();
		    
		    if(toSet<100)
		    {
		    	m_Splerp 	= false;
		    	m_LastValue = val;
		    	m_Value 	= val;
		    }
		    else
		    {
		    	m_Splerp = true;
		    	animation.setDuration(toSet.intValue());
		    	animation.start();
		    }
		    
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e.getMessage());
	    }
	}
}

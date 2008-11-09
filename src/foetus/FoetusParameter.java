package foetus;

import java.awt.Point;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.Interpolator;
import org.jdesktop.animation.timing.interpolation.KeyFrames;
import org.jdesktop.animation.timing.interpolation.KeyTimes;
import org.jdesktop.animation.timing.interpolation.KeyValues;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

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
	
	public float getValue()
	{
		m_Value = PApplet.lerp(m_LastValue, m_NewValue, m_Factor);
		return m_Value;
	}
	
	/*
	 * 
	 */
	public FoetusParameter(Foetus f, float value)
	{
		r_f 					= f;		
		m_Value 				= value;
		m_NewValue				= value;
		m_LastValue				= value;
		m_InterpolationSpeed 	= 0.1f;
		
		float times[] 	= {0, 1};
        Float values[] 	= {new Float(0), new Float(1)};
		
		KeyTimes  keyTimes 	= new KeyTimes(times);
	    KeyValues keyValues = KeyValues.create(values);
		KeyFrames keyFrames = new KeyFrames(keyValues, keyTimes, (Interpolator)null);
		
		animation = new Animator(	1000, 
									1,  
									Animator.RepeatBehavior.LOOP, 
									new PropertySetter(this, "factor", keyFrames) );
		
		animation.setAcceleration(0.5f);
	    animation.setDeceleration(0.5f);
	}

	public void setFactor(Float factor)
	{
		m_Factor = factor;
	}
	
	
	public void setInterpolationSpeed(float speed)
	{
		m_InterpolationSpeed = speed;
	}
	
	
	public void interpolateStep()
	{
		/*m_InterpolationPosition += m_InterpolationSpeed;
		 
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
		    animation.setDuration(toSet.intValue());
		    System.out.println(toSet.intValue() + ", " + toSet);
		    animation.start();
	    }
	    catch(Exception e)
	    {
	    	System.out.println(e.getMessage());
	    }
	}
}

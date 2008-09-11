

import processing.core.PApplet;

public class Mother
{
	static public void main(String args[]) 
	{     
		// Instead of using present mode for fullscreen, I use the fullscreen-hack from here:
		// http://itp.nyu.edu/varwiki/BigScreens/FullScreenHacks
		// This is due to a bug/problem in java that has not yet been dealt with in Processing,
		// where a fullscreen window is minimized as soon as it loses focus.
		// Should revert to present mode when this bug is fixed.
		
//		PApplet.main(new String[] { "--present", "onar3d.mother.Mother"} );	
		PApplet.main(new String[] { "onar3d.mother.Mother"} );
		
		// gurgel
	}
}

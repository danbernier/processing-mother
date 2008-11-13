package SplineInterpolation;

import java.awt.*;

/**
 *  Killable Frame
 */
public class KFrame extends Frame
{
	public static final int KILL_NEVER = 0;

	public static final int KILL_IF_ALONE = 1;

	public static final int KILL_ALWAYS = 2;

	private int killMode;

	private static int nkf = 0; /* Number of KFrames in this application */

	public KFrame(String title, int killMode)
	{
		super(title);
		this.killMode = killMode;
		nkf++;
	}

	public KFrame(String title)
	{
		this(title, KILL_IF_ALONE);
	}

	public KFrame()
	{
		this(null);
	}

	public boolean handleEvent(Event evt)
	{
		if (evt.id == Event.WINDOW_DESTROY)
		{
			hide();
			dispose();
			nkf--;
			if ((killMode == KILL_ALWAYS) || ((killMode == KILL_IF_ALONE) && (nkf == 0)))
				System.exit(0);
			return true;
		}
		return super.handleEvent(evt);
	}

	/*
	 public void invalidate()
	 {
	 super.invalidate();
	 System.out.println( "INVALID" );
	 layout();
	 }
	 */
}

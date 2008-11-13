package SplineInterpolation;

import java.awt.*;
import java.applet.*;

public class SplineDemo extends Applet
{
	private Panel ctlPan;

	private Panel cmdPan;

	private Label lbPoints;

	private Choice chPoints;

	private Checkbox cbLagrange;

	private LagrangePanel lagPan;

	private int nPts = 5;

	public SplineDemo()
	{
		setLayout(new BorderLayout());
		ctlPan = new Panel();
		ctlPan.setLayout(new BorderLayout());
		
		cmdPan = new Panel();
		
		lbPoints = new Label("Number of points");
		
		chPoints = new Choice();
		int i;
		for (i = 2; i < 8; i++)
			chPoints.addItem("" + i);
		
		chPoints.select("" + nPts);
		
		cbLagrange = new Checkbox("Show Lagrange Interpolation", false);
		
		cmdPan.add(lbPoints);
		cmdPan.add(chPoints);
		cmdPan.add(cbLagrange);
		ctlPan.add("Center", cmdPan);
		
		lagPan = new LagrangePanel(500, 500);
		lagPan.init(nPts, true);
		lagPan.showLagrange(false);
		lagPan.showContributions(false);
		lagPan.showSplines(true);
		
		add("North", ctlPan);
		add("Center", lagPan);
	}

	public boolean handleEvent(Event evt)
	{
		if (evt.id == Event.ACTION_EVENT)
		{
			if (evt.target == cbLagrange)
			{
				lagPan.showLagrange(cbLagrange.getState());
			}
			if (evt.target == chPoints)
			{
				int n = Integer.parseInt(chPoints.getSelectedItem());
				if (nPts != n)
				{
					nPts = n;
					lagPan.init(nPts, true);
				}
			}
		}
		return super.handleEvent(evt);
	}

	public static void main(String[] args)
	{
		SplineDemo p = new SplineDemo();
		KFrame fr = new KFrame("Cubic Spline Interpolation");
		fr.add("Center", p);
		fr.pack();
		fr.show();
	}
}

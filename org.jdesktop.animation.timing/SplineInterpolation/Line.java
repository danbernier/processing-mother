package SplineInterpolation;

import java.awt.*;

public class Line implements Drawable
{
	public String name;

	public Dot p0, p1;

	public Color color;

	public Line(String name, Dot p0, Dot p1, Color color)
	{
		this.name = name;
		this.p0 = p0;
		this.p1 = p1;
		this.color = color;
	}

	public void paint(Graphics g)
	{
		g.setColor(color);
		g.drawLine(p0.getX(), p0.getY(), p1.getX(), p1.getY());
	}

	public int getX()
	{
		return (p0.getX() + p1.getX()) / 2;
	}

	public int getY()
	{
		return (p0.getY() + p1.getY()) / 2;
	}
}

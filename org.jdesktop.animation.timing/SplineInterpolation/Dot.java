package SplineInterpolation;

import java.awt.*;

public class Dot implements Drawable
{
	public String name;

	public int x, y;

	public boolean xMovable;

	public boolean yMovable;

	public Color color;

	public Drawable xSrc;

	public Drawable ySrc;

	public Dot(String name, int x, int y, boolean xMovable, boolean yMovable, Color color)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.xMovable = xMovable;
		this.yMovable = yMovable;
		this.color = color;
	}

	public void paint(Graphics g)
	{
		if (color == null)
			return;
		g.setColor(color);
		g.fillOval(getX() - 2, getY() - 2, 5, 5);
	}

	public int getX()
	{
		if (xSrc != null)
			x = xSrc.getX();
		return x;
	}

	public int getY()
	{
		if (ySrc != null)
			y = ySrc.getY();
		return y;
	}

	public void requestMove(int x, int y)
	{
		if (xMovable && (xSrc == null))
			this.x = x;
		if (yMovable && (ySrc == null))
			this.y = y;
	}

	int separation(int x, int y)
	{
		int dx = Math.abs(x - getX());
		int dy = Math.abs(y - getY());
		return Math.max(dx, dy);
	}
}

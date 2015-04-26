package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class RedLine extends BossLaser{
	
	public RedLine(int x, int y, int center) {
		super(x, y, 1, 1000, center);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
	}

	@Override
	public void proceed(int x){
		this.x = x;
	}
}
package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class BossLaser extends Bullet{
	
	public static final int Y_TO_DIE = 800;
	private int step = 20;
	
	public BossLaser(int x, int y) {
		super(x, y, 7, 200);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
		
	}
}
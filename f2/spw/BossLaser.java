package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class BossLaser extends Bullet{
	public static final int Y_TO_DIE = -800;
	private int time = 0;
	protected int step = 0;
	protected int fromCenter = 0;
	
	public BossLaser(int x, int y, int width, int height, int center) {
		super(x, y, width, height);
		fromCenter = center;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	}

	public void proceed(int x){
		y += step;
		this.x = x;
		widthChange();
		if(width <= 0){
			alive = false;
		}
	}

	public int getFromCenter(){
		return fromCenter;
	}

	public void widthChange(){
		if(width < 10 && time < 10){
			width += 2;
			time++;
		}
		else if(time < 26)
			time++;
		else if (time == 26){
			 width--;	
		}
	}
}
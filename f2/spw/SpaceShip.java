package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class SpaceShip extends Sprite{
	private int centerx;
	private int centery;
	int step = 0;

	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		centerx = x + (width/2) - 1;
		centery = y + (height/2) - 10;
	}

	public SpaceShip() {
		super(0, 0, 0, 0);
		centerx = x + (width/2);
		centery = y + (height/2);
	}
	
	public void changeCenter(int x,int y){
		centerx = x + centerx;
		centery = y + centery;
	}

	public int getCenterx(){
		return centerx;
	}

	public int getCentery(){
		return centery;
	}

	@Override
	public abstract void draw(Graphics2D g);
	

	public abstract void move(int direction,char axis);
}

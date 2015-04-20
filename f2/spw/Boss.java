package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Boss extends SpaceShip{
	private Random random = new Random();
	private int fire = 95;
	private boolean shoot = false;
	
	private int step = 3;
	private boolean alive = true;
	private int num = 0;
	private int centerx2;
	private int centery2;
	private int centerx3;
	private int centery3;
	
	public Boss(int x, int y) {
		super(x, y, 15, 15, 500, 500);
		centerx2 = getCenterx() + 30;
		centery2 = centery3 = getCentery();
		centerx3 = getCenterx() - 30;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		
	}

	public void proceed(){
		int i = 0;
		int ran = random.nextInt(100);
		if(num == 0){
			if(ran >= 50){
				move(1,'x');
				num = 1;
			}
			else {
				move(-1,'x');
				num = 1;
			}
		}
		if(num == 1){
			if(x > 385)
				move(-1,'x');
			else move(1,'x');
		}
		if( ran > fire) shoot = true;
		else shoot = false;
	}
    
	@Override
	public void move(int direction,char axis){
		if(axis == 'x'){
			x += step*direction;
			changeCenter(step* direction,0);
			if(x < 0){
				x = 0;
				changeCenter(-step* direction,0);
			}
			if(x > 385 - width){
				x = 385 - width;
			changeCenter(-step* direction,0);
			}
		}
		else{
			y += (step * direction);
			changeCenter(0,step* direction);
			if(y < 0){
				y = 0; 
			changeCenter(0,-step* direction);
			}
			if(y > 600){
				y = 0;
			changeCenter(0,-step* direction);
			}
		}
	}

	public boolean getShoot(){
		return shoot;
	}

}
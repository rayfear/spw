package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Boss extends SpaceShip{
	private Random random = new Random();
	private int fire = 95;
	private int fire2 = 40;
	private boolean shoot = false;
	
	private int step = 2;
	private boolean alive = true;
	private int num = 0;
	private int centerx2;
	private int centery2;
	private int centerx3;
	private int centery3;
	
	public Boss(int x, int y) {
		super(x, y, 200, 100, 100, 100);
		centerx2 = getCenterx() + 70;
		centery2 = centery3 = getCentery();
		centerx3 = getCenterx() - 70;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
		
	}

	public void proceed(){
		int i = 0;
		int ran = random.nextInt(100);
		int ran2 = random.nextInt(100);
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
		if(num != 0){
			if(num == 1)
				move(-1,'x');
			else if(num == 2) move(1,'x');
		}
		if( ran > fire && ran2 > fire2) shoot = true;
		else shoot = false;
	}
    
	@Override
	public void move(int direction,char axis){
		if(axis == 'x'){
			x += step*direction;
			changeCenter(step* direction,0);
			if(x < 0){
				x = 0;
				num = 2;
				changeCenter(-step* direction,0);
			}
			if(x > 385 - width){
				x = 385 - width;
				num = 1;
				changeCenter(-step* direction,0);
			}
			changeAllCenter();
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
			changeAllCenter();
		}
	}

	public boolean getShoot(){
		return shoot;
	}

	public int getCenterx2(){
		return centerx2;
	}

	public int getCenterx3(){
		return centerx3;
	}

	public int getCentery2(){
		return centery2;
	}

	public int getCentery3(){
		return centery3;
	}

	public void changeAllCenter(){
		centerx2 = getCenterx() + 70;
		centerx3 = getCenterx() - 70;
	}
}
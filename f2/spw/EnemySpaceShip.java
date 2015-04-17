package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class EnemySpaceShip extends SpaceShip{
	private Random random = new Random();
	public static final int Y_TO_DIE = 600;
	private int fire = 95;
	private boolean shoot = false;
	
	private int step = 3;
	private boolean alive = true;
	
	public EnemySpaceShip(int x, int y) {
		super(x, y, 15, 15);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		
	}

	public void proceed(){
		int i = 0;
		int ran = random.nextInt(100);
		if(ran >= 60){
			move(1,'x');
			move(1,'x');
			move(1,'y');
		}
		else if(ran >= 20){
			move(-1,'x');
			move(-1,'x');
			move(1,'y');
		}
		else if(ran < 20)
			move(-1,'y');
		if( ran > fire) shoot = true;
		else shoot = false;

		if(y > Y_TO_DIE){
			y = 0;
		}
	}

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
			if(y > 580){
				y = 580;
			changeCenter(0,-step* direction);
			}
		}
	}

	public boolean isAlive(){
		return alive;
	}

	public boolean getShoot(){
		return shoot;
	}

	public void setAlive(boolean alive){
		this.alive = alive;
	}

}
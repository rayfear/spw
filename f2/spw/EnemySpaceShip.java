package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class EnemySpaceShip extends SpaceShip{
	private Random random = new Random();
	public static final int Y_TO_DIE = 600;
	private int fire = 85;
	private boolean shoot = false;
	
	private int step = 3;
	private int num = 0;
	private boolean warp = false;
	
	public EnemySpaceShip(int x, int y) {
		super(x, y, 15, 15, 3, 3);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		
	}

	public void patternMove(int num,int random){
		switch(num){
			case 1:if(random > 10){
						move(1,'x');
						move(1,'y');
						num = 1;
					}
					else if(random > 5){
						move(-1,'x');
						move(1,'y');
						num = 2;
					}
					else{ move(-1,'y');
						  num = 3;
					}
					break;
			case 2:if(random > 10){
						move(-1,'x');
						move(1,'y');
						num = 2;
					}
					else if(random > 5){
						move(1,'x');
						move(1,'y');
						num = 1;
					}
					else{ move(-1,'y');
						  num = 3;
					}
					break;
			case 3:if(random > 70){
						move(-1,'y');
						num = 3;
					}
					else if(random > 35){
						move(1,'x');
						move(1,'y');
						num = 1;
					}
					else{ move(1,'y');
					     move(-1,'x');
						 num = 2;
					}
					break;
		}
	}

	public void proceed(){
		int i = 0;
		int ran = random.nextInt(100);
		if(num == 0){
		if(ran >= 60){
			move(1,'x');
			move(1,'y');
			num = 1;
		}
		else if(ran >= 20){
			move(-1,'x');
			move(1,'y');
			num = 2;
		}
		else if(ran < 20)
			move(-1,'y');
			num = 3;
		}
		else patternMove(num,ran);
		if( ran > fire && random.nextInt(100) > fire) shoot = true;
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
				warp = true;
			changeCenter(0,-step* direction);
			}
		}
	}

	public void setWarp(){
		warp = false;
	}

	public boolean getWarp(){
		return warp;	
	}

	public boolean getShoot(){
		return shoot;
	}
}
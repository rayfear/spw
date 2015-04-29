package f2.spw;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Boss extends SpaceShip{
	private Random random = new Random();
	private int fire = 95;
	private int fire2 = 0;
	private boolean shoot = false;
	private boolean shoot2 = false;
	private int step = 2;
	private boolean alive = true;
	private int num = 0;
	private int centerx2;
	private int centery2;
	private int centerx3;
	private int centery3;
	private int centerx4;
	private int centery4;
	private int centerx5;
	private int centery5;
	private int time = 0;
	private int time2 = 0;
	private int pattern = 0;
	private boolean timestart = false;
	private boolean timestart2 = false;
	private int lv = 1;
	
	public Boss(int x, int y,int hp,int maxHp, int fire2) {
		super(x, y, 200, 100, hp, maxHp);
		centerx2 = getCenterx() + 95;
		centery = getCentery() + 60;
		centery2 = centery3 = centery4 = centery5 = getCentery();
		centerx3 = getCenterx() - 99;
		centerx4 = getCenterx() + 40;
		centerx5 = getCenterx() - 49;
		this.fire2 = fire2;
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
		if(lv > 2){
			pattern = 1;
		}
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
		if( ran > fire && ran2 > fire2) shoot2 = true;
		else shoot2 = false;
		if(shoot2) timestart = true;
		if(timestart) time++;
		if(time >= 25){ 
			shoot = true;
			time = 0;
			timestart = false;
		}
		if(time >= 20){
			timestart2 = true;
		}
		else shoot = false;
		if(timestart2) time2++;
		if(time2 >= 30){
			time2 = 0;
			timestart2 = false;;
		}
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

	public boolean getShoot2(){
		return shoot2;
	}

	public int getCenterx2(){
		return centerx2;
	}

	public int getCenterx3(){
		return centerx3;
	}

	public int getCenterx4(){
		return centerx4;
	}

	public int getCenterx5(){
		return centerx5;
	}

	public int getCentery2(){
		return centery2;
	}

	public int getCentery3(){
		return centery3;
	}

	public int getCentery4(){
		return centery4;
	}

	public int getCentery5(){
		return centery5;
	}

	public void changeAllCenter(){
		centerx2 = getCenterx() + 95;
		centerx3 = getCenterx() - 99;
		centerx4 = getCenterx() + 40;
		centerx5 =getCenterx() - 49;
	}

	public int getPattern(){
		return pattern;
	}

	public void increaseStep(){
		step++;
	}

	public int getTime(){
		return time;
	}

	public int getTime2(){
		return time2;
	}

	public int getLv(){
		return lv;
	}

	public void increaseLv(){
		if(lv < 3){
			lv++;
			this.setMaxHp(this.getMaxHp() + 100);
		}
	}
}
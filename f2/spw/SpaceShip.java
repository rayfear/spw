package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class SpaceShip extends Sprite{
	protected int centerx;
	protected int centery;
	protected int hp = 0;
	protected int hpMax = 0;
	protected boolean alive = true;


	public SpaceShip(int x, int y, int width, int height, int hp, int hpMax) {
		super(x, y, width, height);
		centerx = x + (width/2) - 1;
		centery = y + (height/2) - 10;
		this.hp = hp;
		this.hpMax = hpMax;
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

	public boolean isAlive(){
		return alive;
	}

	@Override
	public abstract void draw(Graphics2D g);

	public int getHp(){
		return hp;
	}

	public void setHp(int hp){
		this.hp = hp;
	}

	public void setFullHp(){
		hp = hpMax;
	}

	public void increaseMaxHp(){
		hpMax++;
	}

	public void checkHp(){
		if(hp > hpMax)
			hp = hpMax;
		if(hp <= 0)
			alive = false;
	}

	public void reduceHp(){
		hp--;
	}
	
	public abstract void move(int direction,char axis);
}

package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class SpaceShip extends Sprite{
	protected int centerx;
	protected int centery;
	protected int hp = 0;
	protected int maxHp = 0;


	public SpaceShip(int x, int y, int width, int height, int hp, int maxHp) {
		super(x, y, width, height);
		centerx = x + (width/2);
		centery = y + (height/2) - 10;
		this.hp = hp;
		this.maxHp = maxHp;
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

	public int getHp(){
		return hp;
	}

	public void setHp(int hp){
		this.hp = hp;
		if(hp >= maxHp)
			setFullHp();
	}

	public void setMaxHp(int maxHp){
		this.maxHp = maxHp;
	}

	public int getMaxHp(){
		return maxHp;
	}

	public void setFullHp(){
		hp = maxHp;
	}

	public void increaseMaxHp(int num){
		maxHp += num;
	}

	public void reduceHp(){
		hp--;
	}

	public void reduceHp(int num){
		hp -= num;
	}
	
	public abstract void move(int direction,char axis);
}

package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlayerSpaceShip extends SpaceShip{
	int step = 1;

	public PlayerSpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height, 50, 50);
		centerx = x + (width/2) - 1;
		centery = y + (height/2) - 10;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width, height);
		
	}
	public void resetPosition(){
		x = 180;
		y = 550;
		width = 20;
		height = 20;
		centerx = x + (width/2) - 1;
		centery = y + (height/2) - 10;
	}

	@Override
	public void move(int direction,char axis){
		if(axis == 'x'){
			x += (step * direction);
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
}

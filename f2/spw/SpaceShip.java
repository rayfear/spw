package f2.spw;

import java.awt.Color;
import java.awt.Graphics2D;

public class SpaceShip extends Sprite{

	int step = 8;
	
	public SpaceShip(int x, int y, int width, int height) {
		super(x, y, width, height);
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width, height);
		
	}

	public void move(int direction,char axis){
		if(axis == 'x'){
			x += (step * direction);
			if(x < 0)
				x = 0;
			if(x > 380 - width)
				x = 380 - width;
		}
		else{
			y += (step * direction);//30
			if(y < 0)
				y = 0; 
			if(y > 580)
				y = 580;
		}
	}
}

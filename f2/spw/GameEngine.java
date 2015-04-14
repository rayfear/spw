package f2.spw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	GamePanel gp;
		
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();	
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();	
	private boolean controll[] = {false,false,false,false,false,false};
	private boolean titleStatus = true;
	private int cursor = 0;
	private SpaceShip v;
	private Timer timer;
	private Timer shiptimer;
	private long score = 0;
	private double difficulty = 0.05;
	private int ms = 0;
	private boolean pauseStatus = false;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);

		timer = new Timer(1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ms++;
				if((ms % 25) == 0 && !titleStatus && cursor == 0)
					process();
				else if(!titleStatus && cursor == 2)
					System.exit(0);
			}
		});
		shiptimer = new Timer(5, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(titleStatus)
					title();
				else shipprocess();
			}
		});
		timer.setRepeats(true);
		shiptimer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
		shiptimer.start();
	}
	
	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30);
		gp.sprites.add(e);
		enemies.add(e);
	}
	
	private void generateBullet(){
		Bullet s = new Bullet(v.getCenterx(),v.getCentery());
		gp.sprites.add(s);
		bullets.add(s);
	}

	private void shipprocess(){
		if(timer.isRunning()){
			if(controll[0])
				v.move(-1,'x');
			if(controll[1])
				v.move(1,'x');
			if(controll[2])
				v.move(-1,'y');
			if(controll[3])
				v.move(1,'y');
			if(controll[4])
				generateBullet();
		}
		if(!timer.isRunning() && controll[5]){
				clear();
				timer.start();
		}
		pause();
	}
	private void pause(){
		if(pauseStatus == true){
			gp.updateGameUIPause(this);
			timer.stop();
		}
		else timer.start();
	}

	private void process(){
		if(Math.random() < difficulty){
			generateEnemy();
		}
		
		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();

			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
				score += 100;
			}
		}
		
		Iterator<Bullet> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			Bullet b = b_iter.next();
			b.proceed();

			if(!b.isAlive()){
				b_iter.remove();
				gp.sprites.remove(b);
			}
		}

		gp.updateGameUI(this);
		
		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				die();

				return;
			}
		}
		Rectangle2D.Double br;
		for(Bullet b : bullets){
			br = b.getRectangle();
			for(Enemy e : enemies){
				er = e.getRectangle();
				if(er.intersects(br)){
						e.setAlive(false);
						b.setAlive(false);
			    }
			}
		}
	}
	
	public void clear(){
		Rectangle2D.Double br;
		Rectangle2D.Double er;
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<Bullet> b_iter = bullets.iterator();
		while(b_iter.hasNext()){
			Bullet b = b_iter.next();
			b_iter.remove();
			br = b.getRectangle();
			gp.sprites.remove(b);
		}
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e_iter.remove();
			er = e.getRectangle();
			gp.sprites.remove(e);
		}
		score = 0;
		v.resetPosition();
	}

	public void die(){
		timer.stop();
		gp.updateGameUIRestart(this);
	}
	
	public void title(){
		gp.titleScreen(cursor);
	}

	public void updateCursor(int i){
		switch(i){
			case 1:
				cursor -= 1;
				break;
			case -1:
				cursor += 1;
			    break;
		}
		if(cursor > 2)
			cursor = 2;
		else if(cursor < 0)
			cursor = 0;
	}
	
	void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:

			controll[0] = true;
			break;
		case KeyEvent.VK_RIGHT:
			controll[1] = true;
			break;
		case KeyEvent.VK_UP:
			if(!titleStatus)
				controll[2] = true;
			else updateCursor(1);
			break;
		case KeyEvent.VK_DOWN:
			if(!titleStatus)
				controll[3] = true;
			else updateCursor(-1);
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		case KeyEvent.VK_SPACE:
			controll[4] = true;
			break;
		case KeyEvent.VK_R:
			controll[5] = true;
			break;
		case KeyEvent.VK_ENTER:
			titleStatus = false;
			break;
		case KeyEvent.VK_P:
			pauseStatus = !pauseStatus;
			break;
		}
	}

	void controlVehiclea(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			controll[0] = false;
			break;
		case KeyEvent.VK_RIGHT:
			controll[1] = false;
			break;
		case KeyEvent.VK_UP:
			controll[2] = false;
			break;
		case KeyEvent.VK_DOWN:
			controll[3] = false;
			break;
		case KeyEvent.VK_SPACE:
			controll[4] = false;
			break;
		case KeyEvent.VK_R:
			controll[5] = false;
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		controlVehiclea(e);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}
}

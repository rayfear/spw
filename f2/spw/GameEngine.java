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
	private boolean controll[] = {false,false,false,false,false};	
	private SpaceShip v;
	private Timer timer;
	private Timer shiptimer;	
	private long score = 0;
	private double difficulty = 0.05;
	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);

		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});

		shiptimer = new Timer(5, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shipprocess();
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
	
	

	public void die(){
		timer.stop();
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
			controll[2] = true;
			break;
		case KeyEvent.VK_DOWN:
			controll[3] = true;
			break;
		case KeyEvent.VK_D:
			difficulty += 0.1;
			break;
		case KeyEvent.VK_SPACE:
			controll[4] = true;
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

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
	private ArrayList<EnemySpaceShip> enemiespaceships = new ArrayList<EnemySpaceShip>();
	private ArrayList<Boss> bossS = new ArrayList<Boss>();
	private ArrayList<BossLaser> bosslaser = new ArrayList<BossLaser>();
	private RedLine redline1;
	private RedLine redline2;
	private RedLine redline3;
	private boolean controll[] = {false,false,false,false,false};
	private boolean titleStatus = true;
	private int cursor = 0;
	private PlayerSpaceShip v;
	private Timer timer;
	private long score = 0;
	private double difficulty = 0.07;
	private int ms10 = 0;
	private boolean pauseStatus = false;
	private int random = 0;
	private boolean readyTofire = false;
	private int maxEnemy = 7;
	private int Enemy = 0;
	private int kill = 0;
	private boolean limitMaxEnemy = true;
	private boolean die = false;
	
	public GameEngine(GamePanel gp, PlayerSpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);

		timer = new Timer(10, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ms10 == 6000) ms10 = 0;
				ms10++;
				if(titleStatus)
					title();
				else shipprocess();
				if((ms10 % 5) == 0 && !titleStatus && cursor == 0){
					process();
				}
				else if(!titleStatus && cursor == 2)
					System.exit(0);
				if(ms10 % 20 == 0) readyTofire = true;
				else readyTofire = false;
				//else if(!titleStatus && cursor == 1)
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	
	private void generateEnemySpaceship(){
		Enemy++;
		EnemySpaceShip es = new EnemySpaceShip((int)(Math.random()*385), 30);
		gp.sprites.add(es);
		enemiespaceships.add(es);
	}

	private void generateEnemy(int x , int y){
		Enemy e = new Enemy(x,y);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBoss(int x, int y){
		Boss boss = new Boss(x,y);
		gp.sprites.add(boss);
		bossS.add(boss);
		redline1 = new RedLine(boss.getCenterx(),boss.getCentery(),1);
		redline2 = new RedLine(boss.getCenterx2(),boss.getCentery2(),2);
		redline3 = new RedLine(boss.getCenterx3(),boss.getCentery3(),3);
		gp.sprites.add(redline1);
		gp.sprites.add(redline2);
		gp.sprites.add(redline3);
		difficulty = 0.02;
	}

	private void generateBossLaser(int x1, int y1, int x2, int y2, int x3, int y3){
		BossLaser bosslaser1 = new BossLaser(x1,y1,10,1000,1);
		BossLaser bosslaser2 = new BossLaser(x2,y2,10,1000,2);
		BossLaser bosslaser3 = new BossLaser(x3,y3,10,1000,3);
		gp.sprites.add(bosslaser1);
		gp.sprites.add(bosslaser2);
		gp.sprites.add(bosslaser3);
		bosslaser.add(bosslaser1);
		bosslaser.add(bosslaser2);
		bosslaser.add(bosslaser3);
	}
	
	private void generateBullet(){
		Bullet s = new Bullet(v.getCenterx(),v.getCentery(), 3, 7);
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
			if(controll[4] && readyTofire)
				generateBullet();
		}
		pause();
	}
	private void pause(){
		if(pauseStatus == true){
			gp.updateGameUIPause(this);
			timer.stop();
		}
		else if(die != true)timer.start();
	}

	private void process(){
		if(score % 200000 == 0 && score != 0 && !limitMaxEnemy){
			maxEnemy += 7;
			limitMaxEnemy = true;
		}
		else if(score % 80000 == 0 && score != 0 && !limitMaxEnemy){
			maxEnemy += 5;
			limitMaxEnemy = true;
		}
		else if(score % 5000 == 0 && score != 0 && !limitMaxEnemy){
			maxEnemy += 2;
			limitMaxEnemy = true;
		}
		if(Math.random() < difficulty){
			if(Enemy < maxEnemy)
				generateEnemySpaceship();
		}
		for(EnemySpaceShip es : enemiespaceships){
			if(es.getShoot())
				generateEnemy(es.getCenterx(),es.getCentery());
		}

		for(Boss bossa: bossS){
			if(bossa.getShoot())
				generateBossLaser(bossa.getCenterx(),bossa.getCentery(), bossa.getCenterx2(), bossa.getCentery2(), bossa.getCenterx3(), bossa.getCentery3());
		}

		
		if(ms10 % 6000 == 0 && ms10 != 0)
			generateBoss(100,50);

		Iterator<Boss> boss_iter = bossS.iterator();
		Iterator<BossLaser> bosslaser_iter = bosslaser.iterator();
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<EnemySpaceShip> es_iter = enemiespaceships.iterator();
		Iterator<Bullet> b_iter = bullets.iterator();

		while(boss_iter.hasNext()){
			Boss boss = boss_iter.next();
			boss.proceed();
			redline1.proceed(boss.getCenterx());
			redline2.proceed(boss.getCenterx2());
			redline3.proceed(boss.getCenterx3());

			if(check(boss)){
				boss_iter.remove();
				gp.sprites.remove(boss);
				gp.sprites.remove(redline1);
				gp.sprites.remove(redline2);
				gp.sprites.remove(redline3);
				maxEnemy -= 5;
				difficulty = 0.07;
			}

			while(bosslaser_iter.hasNext()){
				BossLaser blaser = bosslaser_iter.next();
				switch(blaser.getFromCenter()){
					case 1:blaser.proceed(boss.getCenterx());
						   break;
					case 2:blaser.proceed(boss.getCenterx2());
						   break;
					case 3:blaser.proceed(boss.getCenterx3());
						   break;
				}

				if(!blaser.isAlive()){
					bosslaser_iter.remove();
					gp.sprites.remove(blaser);
				}
			}
		}

		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();

			if(!e.isAlive()){
				e_iter.remove();
				gp.sprites.remove(e);
			}
		}

		while(es_iter.hasNext()){
			EnemySpaceShip es = es_iter.next();
			es.proceed();
			if(es.getWarp()){
				es.setWarp();
				maxEnemy++;
			}

			if(check(es)){
				Enemy--;
				es_iter.remove();
				gp.sprites.remove(es);
				kill++;
				limitMaxEnemy = false;
				if(kill % 40 == 0 && kill != 0) v.increaseMaxHp();
				if(kill % 20 == 0 && kill != 0) v.setHp(v.hp + 1);
				score += 100;
			}
		}
		
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
		Rectangle2D.Double esr;
		Rectangle2D.Double br;
		Rectangle2D.Double bossSr;
		Rectangle2D.Double blaserSr;
		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				v.reduceHp();
				e.setAlive(false);
				if(check(v))
					die();
			}
		}
		for(BossLaser oneblaser : bosslaser){
			blaserSr = oneblaser.getRectangle();
			if(blaserSr.intersects(vr)){
				v.reduceHp();
				if(check(v))
					die();
			}
		}
		for(EnemySpaceShip es : enemiespaceships){
			esr = es.getRectangle();
			if(esr.intersects(vr)){
				v.reduceHp();
				es.reduceHp();
				if(check(v))
					die();
			}
		}
		for(Bullet b : bullets){
			br = b.getRectangle();
			for(EnemySpaceShip es : enemiespaceships){
				esr = es.getRectangle();
				if(esr.intersects(br)){
						es.reduceHp();
						b.setAlive(false);
			    }
			}
			for(Boss bosses : bossS){
				bossSr = bosses.getRectangle();
				if(bossSr.intersects(br)){
						bosses.reduceHp();
						b.setAlive(false);
			    }
			}
		}
	}
	
	public void clear(){
		difficulty = 0.07;
		die = false;
		Rectangle2D.Double br;
		Rectangle2D.Double er;
		Rectangle2D.Double esr;
		Rectangle2D.Double bossSr;
		Rectangle2D.Double blaserSr;
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<Bullet> b_iter = bullets.iterator();
		Iterator<EnemySpaceShip> es_iter = enemiespaceships.iterator();
		Iterator<Boss> boss_iter = bossS.iterator();
		Iterator<BossLaser> bosslaser_iter = bosslaser.iterator();
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
		while(es_iter.hasNext()){
			EnemySpaceShip es = es_iter.next();
			es_iter.remove();
			esr = es.getRectangle();
			gp.sprites.remove(es);
		}
		while(bosslaser_iter.hasNext()){
			BossLaser oneblaser = bosslaser_iter.next();
			bosslaser_iter.remove();
			blaserSr = oneblaser.getRectangle();
			gp.sprites.remove(oneblaser);
		}
		while(boss_iter.hasNext()){
			Boss boss = boss_iter.next();
			boss_iter.remove();
			bossSr = boss.getRectangle();
			gp.sprites.remove(boss);
			gp.sprites.remove(redline1);
			gp.sprites.remove(redline2);
			gp.sprites.remove(redline3);
		}
		score = 0;
		v.resetPosition();
		maxEnemy = 7;
		Enemy = 0;
		v.setMaxHp(5);
		v.setFullHp();
		kill = 0;
	}

	public void die(){
		die = true;
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
			cursor = 0;
		else if(cursor < 0)
			cursor = 2;
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
			if(!timer.isRunning()){
				clear();
				timer.start();
			}
			break;
		case KeyEvent.VK_ENTER:
			titleStatus = false;
			break;
		case KeyEvent.VK_P:
			 pauseStatus = !pauseStatus;
			 break;
		case KeyEvent.VK_B:
			 generateBoss(100,50);
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

	public int getKill(){
		return kill;
	}

	public int getHp(){
		return v.hp;
	}

	public int getMaxHp(){
		return v.maxHp;
	}

	public int getEnemy(){
		return Enemy;
	}

	public int getMaxEnemy(){
		return maxEnemy;
	}

	public boolean check(SpaceShip spaceship){
		if(spaceship.getHp() <= 0) return true;
		return false;
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

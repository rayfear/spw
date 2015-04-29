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
	private RedLine redline4;
	private RedLine redline5;
	private boolean controll[] = {false,false,false,false,false};
	private boolean titleStatus = true;
	private boolean difficultStatus = true;
	private boolean pauseStatus = false;
	private boolean lvUp = false;
	private int cursor = 0;
	private int cursor2 = 0;
	private PlayerSpaceShip v;
	private Timer timer;
	private long score = 0;
	private double difficulty = 0;
	private int ms10 = 0;
	private int random = 0;
	private boolean readyTofire = false;
	private int maxEnemy = 7;
	private int Enemy = 0;
	private int kill = 0;
	private boolean limitMaxEnemy = true;
	private boolean die = false;
	private int gameMode = 0;
	private boolean hasBoss = false;
	private int bossHp = 0;
	private int bossLv = 0;
	private boolean stopCheck = false;
	private boolean noRedLine = true;
	private boolean noRedLine2 = true;
	
	public GameEngine(GamePanel gp, PlayerSpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);

		timer = new Timer(10, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ms10 == 12000) ms10 = 0;
				ms10++;
				if(titleStatus)
					title();
				if(!titleStatus && difficultStatus && cursor == 0 ){
					chooseDifficult();
				}
				if(!titleStatus && !difficultStatus)shipprocess();
				if(cursor2 == 0 && difficultStatus) gameMode = 1;
				else if(cursor2 == 1 && difficultStatus) gameMode = 2;
				else if(cursor2 == 2 && difficultStatus) gameMode = 3;
				if((ms10 % 5) == 0 && !titleStatus && !difficultStatus){
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
		EnemySpaceShip es;
		if(gameMode == 1) es = new EnemySpaceShip((int)(Math.random()*385), 30, 5, 5);
		else if(gameMode == 2) es = new EnemySpaceShip((int)(Math.random()*385), 30, 10, 0);
		else es = new EnemySpaceShip((int)(Math.random()*385), 30, 15, 15);
		gp.sprites.add(es);
		enemiespaceships.add(es);
	}

	private void generateEnemy(int x , int y){
		Enemy e = new Enemy(x,y);
		gp.sprites.add(e);
		enemies.add(e);
	}

	private void generateBoss(){
		Boss boss;
		if(gameMode == 1) boss = new Boss( 100, 50 , 500 , 500 , 90);
		else if(gameMode == 2) boss = new Boss( 100, 50 , 600 , 600 , 85);
		else boss = new Boss( 100, 50, 700 , 700 , 80);
		gp.sprites.add(boss);
		bossS.add(boss);
		if(gameMode == 1)
			difficulty = 0;
		if(gameMode == 2)
			difficulty = 0.01;
		if(gameMode == 3)
			difficulty = 0.02;
		hasBoss = true;
		bossHp = boss.getHp();
		bossLv = boss.getLv();
	}

	private void generateBossLaser(int x1, int y1, int x2, int x3){
		BossLaser bosslaser1 = new BossLaser(x1,y1,1,1000,1);
		BossLaser bosslaser2 = new BossLaser(x2,y1,1,1000,2);
		BossLaser bosslaser3 = new BossLaser(x3,y1,1,1000,3);
		gp.sprites.add(bosslaser1);
		gp.sprites.add(bosslaser2);
		gp.sprites.add(bosslaser3);
		bosslaser.add(bosslaser1);
		bosslaser.add(bosslaser2);
		bosslaser.add(bosslaser3);
		removeRedLine();
	}

	private void generateBossLaser2(int x4, int y4, int x5){
		BossLaser bosslaser4 = new BossLaser(x4,y4,1,1000,4);
		BossLaser bosslaser5 = new BossLaser(x5,y4,1,1000,5);
		gp.sprites.add(bosslaser4);
		gp.sprites.add(bosslaser5);
		bosslaser.add(bosslaser4);
		bosslaser.add(bosslaser5);
		removeRedLine2();
	}

	private void generateBossRedLine(int x1, int y1, int x2, int x3){
		removeRedLine();
		redline1 = new RedLine(x1,y1,1);
		redline2 = new RedLine(x2,y1,2);
		redline3 = new RedLine(x3,y1,3);
		gp.sprites.add(redline1);
		gp.sprites.add(redline2);
		gp.sprites.add(redline3);
		noRedLine = false;
	}

	private void generateBossRedLine2(int x4, int y4, int x5){
		removeRedLine();
		redline4 = new RedLine(x4,y4,4);
		redline5 = new RedLine(x5,y4,5);
		gp.sprites.add(redline4);
		gp.sprites.add(redline5);
		noRedLine2 = false;
	}

	private void removeRedLine(){
		gp.sprites.remove(redline1);
		gp.sprites.remove(redline2);
		gp.sprites.remove(redline3);
		noRedLine = true;
	}

	private void removeRedLine2(){
		gp.sprites.remove(redline4);
		gp.sprites.remove(redline5);
		noRedLine2 = true;
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
	}

	private void pause(){
		if(pauseStatus == true){
			gp.updateGameUIPause(this);
			timer.stop();
		}
		else if(die != true)timer.start();
	}

	private void process(){
		if(!stopCheck){
				if(gameMode == 1 && !difficultStatus) difficulty = 0.04;
				else if(gameMode == 2 && !difficultStatus) difficulty = 0.055;
				else if(gameMode == 3 && !difficultStatus) difficulty = 0.07;
				stopCheck = true;
		}

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
			if(bossa.getShoot2()){
				if(bossa.getPattern() == 0 || bossa.getPattern() == 1)
					generateBossRedLine(bossa.getCenterx(), bossa.getCentery(), bossa.getCenterx2() - 5, bossa.getCenterx3());
			}
			if(bossa.getTime() >= 20 ){ 
				if(bossa.getPattern() == 0 || bossa.getPattern() == 1){
					removeRedLine();
					generateBossLaser(bossa.getCenterx(),bossa.getCentery(), bossa.getCenterx2() - 5, bossa.getCenterx3());
					noRedLine = true;
					if(bossa.getPattern() == 1){
						if(noRedLine2) generateBossRedLine2(bossa.getCenterx4(), bossa.getCentery4(), bossa.getCenterx5());
					}
				}
			}
			
			if(bossa.getPattern() == 1){
				 if(bossa.getTime2() >= 25){
					removeRedLine2();
					noRedLine2 = true;
					generateBossLaser2(bossa.getCenterx4() + 5,bossa.getCentery4(),bossa.getCenterx5());
				}
			}
		}

		
		if(ms10 % 12000 == 0 && ms10 != 0)
			if(!hasBoss) generateBoss();
			else lvUp = true;

		Iterator<Boss> boss_iter = bossS.iterator();
		Iterator<BossLaser> bosslaser_iter = bosslaser.iterator();
		Iterator<Enemy> e_iter = enemies.iterator();
		Iterator<EnemySpaceShip> es_iter = enemiespaceships.iterator();
		Iterator<Bullet> b_iter = bullets.iterator();

		while(boss_iter.hasNext()){
			Boss boss = boss_iter.next();
			boss.proceed();
			if(lvUp){
				boss.increaseLv();
				boss.setFullHp();
				bossHp = boss.getHp();
				bossLv = boss.getLv();
				lvUp = false;
			}
			if(!noRedLine){
				redline1.proceed(boss.getCenterx());
				redline2.proceed(boss.getCenterx2());
				redline3.proceed(boss.getCenterx3());
			}
		
			if(!noRedLine2){
				redline4.proceed(boss.getCenterx4() + 5);
				redline5.proceed(boss.getCenterx5());
			}

			if(check(boss)){
				boss_iter.remove();
				gp.sprites.remove(boss);
				removeRedLine();
				removeRedLine2();
				maxEnemy -= 5;
				bossLv = 0;
				if(gameMode == 3) difficulty = 0.07;
				else if(gameMode == 2) difficulty = 0.055;
				else difficulty = 0.04;
				hasBoss = false;
				while(bosslaser_iter.hasNext()){
				BossLaser blaser = bosslaser_iter.next();
				switch(blaser.getFromCenter()){
					case 1:blaser.proceed(boss.getCenterx());
						   break;
					case 2:blaser.proceed(boss.getCenterx2() - 5);
						   break;
					case 3:blaser.proceed(boss.getCenterx3());
						   break;
					case 4:blaser.proceed(boss.getCenterx4());
						   break;
					case 5:blaser.proceed(boss.getCenterx5());
						   break;
				}
				blaser.setAlive(false);
				if(!blaser.isAlive()){
					bosslaser_iter.remove();
					gp.sprites.remove(blaser);
				}
			}
		}

			while(bosslaser_iter.hasNext()){
				BossLaser blaser = bosslaser_iter.next();
				switch(blaser.getFromCenter()){
					case 1:blaser.proceed(boss.getCenterx());
						   break;
					case 2:blaser.proceed(boss.getCenterx2() - 5);
						   break;
					case 3:blaser.proceed(boss.getCenterx3());
						   break;
					case 4:blaser.proceed(boss.getCenterx4());
						   break;
					case 5:blaser.proceed(boss.getCenterx5());
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
				if(kill % 40 == 0 && kill != 0) v.increaseMaxHp(5);
				if(kill % 20 == 0 && kill != 0) v.setHp(v.hp + 5);
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
				v.reduceHp(5);
				e.setAlive(false);
				if(check(v))
					die();
			}
		}
		for(BossLaser oneblaser : bosslaser){
			blaserSr = oneblaser.getRectangle();
			if(blaserSr.intersects(vr)){
				v.reduceHp(2);
				if(gameMode == 3)
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
		for(Boss bosses : bossS){
			bossSr = bosses.getRectangle();
			if(bossSr.intersects(vr)){
				v.reduceHp();
				bosses.reduceHp();
				bossHp = bosses.getHp();
				if(check(v))
					die();
			}
		}
		for(Bullet b : bullets){
			br = b.getRectangle();
			for(EnemySpaceShip es : enemiespaceships){
				esr = es.getRectangle();
				if(esr.intersects(br)){
						es.reduceHp(5);
						b.setAlive(false);
			    }
			}
			for(Boss bosses : bossS){
				bossSr = bosses.getRectangle();
				if(bossSr.intersects(br)){
						bosses.reduceHp(5);
						bossHp = bosses.getHp();
						b.setAlive(false);
			    }
			}
		}
	}
	
	public void clear(){
		if(gameMode == 1)
			difficulty = 0.04;
		if(gameMode == 2)
			difficulty = 0.055;
		if(gameMode == 3)
			difficulty = 0.07;
		die = false;
		hasBoss = false;
		noRedLine = true;
		noRedLine2 = true;
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
			removeRedLine();
			removeRedLine2();
		}
		score = 0;
		v.resetPosition();
		bossHp = 0;
		bossLv = 0;
		maxEnemy = 7;
		Enemy = 0;
		v.setMaxHp(50);
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

	public void chooseDifficult(){
		gp.difficultScreen(cursor2);
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

	public void updateCursor2(int i){
		switch(i){
			case 1:
				cursor2 -= 1;
				break;
			case -1:
				cursor2 += 1;
			    break;
		}
		if(cursor2 > 2)
			cursor2 = 0;
		else if(cursor2 < 0)
			cursor2 = 2;
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
			if(!titleStatus && !difficultStatus)
				controll[2] = true;
			else if(!titleStatus && difficultStatus)
				updateCursor2(1);
			else updateCursor(1);

			break;
		case KeyEvent.VK_DOWN:
			if(!titleStatus && !difficultStatus)
				controll[3] = true;
			else if(!titleStatus && difficultStatus)
				updateCursor2(-1);
			else updateCursor(-1);
			break;
		case KeyEvent.VK_D:
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
			if(!titleStatus) difficultStatus = false;
			titleStatus = false;
			break;
		case KeyEvent.VK_P:
			 if(!titleStatus){
				pauseStatus = !pauseStatus;
				pause();
			 }
			 break;
		case KeyEvent.VK_B:
			if(!difficultStatus) {
			if(!hasBoss) generateBoss(); 
			else lvUp = true;
			}
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

	public int getBossHp(){
		return bossHp;
	}

	public int getBossLv(){
		return bossLv;
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

package f2.spw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	Graphics2D big;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	public GamePanel() {
		bi = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		big.setBackground(Color.BLACK);
	}

	public void titleScreen(int cursor){
		big.clearRect(0, 0, 400, 600);
		big.setColor(Color.WHITE);	
		if(cursor == 0){
			big.setFont(big.getFont().deriveFont(28F));
			big.drawString("Space war",140,100);
			big.setFont(big.getFont().deriveFont(18F));
			big.drawString("New Game",150,200);
			big.setFont(big.getFont().deriveFont(14F));
			big.drawString("Continue Game",145,270);
			big.drawString("Exit",180,340);
		}
		else if(cursor == 1){
			big.setFont(big.getFont().deriveFont(28F));
			big.drawString("Space war",140,100);
			big.setFont(big.getFont().deriveFont(14F));
			big.drawString("New Game",160,200);
			big.setFont(big.getFont().deriveFont(18F));
			big.drawString("Continue Game",140,270);
			big.setFont(big.getFont().deriveFont(14F));
			big.drawString("Exit",180,340);
		}
		else{
			big.setFont(big.getFont().deriveFont(28F));
			big.drawString("Space war",140,100);
			big.setFont(big.getFont().deriveFont(14F));
			big.drawString("New Game",160,200);
			big.drawString("Continue Game",145,270);
			big.setFont(big.getFont().deriveFont(18F));
			big.drawString("Exit",175,340);
		}
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}

	public void updateGameUI(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		
		big.setColor(Color.WHITE);		
		big.drawString(String.format("%08d", reporter.getScore()), 300, 20);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		repaint();
	}

		public void updateGameUIPause(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		
		big.setColor(Color.WHITE);		
		big.drawString(String.format("%08d", reporter.getScore()), 300, 20);
		for(Sprite s : sprites){
			s.draw(big);
		}
		
		big.setFont(big.getFont().deriveFont(20F));
		big.setColor(Color.WHITE);		
		big.drawString(String.format("Pause"), 170, 280);
		for(Sprite s : sprites){
			s.draw(big);
		}
		big.setFont(big.getFont().deriveFont(12F));
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

	public void updateGameUIRestart(GameReporter reporter){
		big.clearRect(0, 0, 400, 600);
		
		big.setColor(Color.WHITE);		
		big.drawString(String.format("%08d", reporter.getScore()), 300, 20);
		for(Sprite s : sprites){
			s.draw(big);
		}

		big.setFont(big.getFont().deriveFont(20F));
		big.setColor(Color.WHITE);		
		big.drawString(String.format("press R to restart"), 110, 280);
		for(Sprite s : sprites){
			s.draw(big);
		}
		big.setFont(big.getFont().deriveFont(12F));
		repaint();
	}

}
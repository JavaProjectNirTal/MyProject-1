package viewBoard;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

class GameCharacter {
	private int x, y;
	private Image character;
	
	public GameCharacter(int x, int y) {
		this.x = x;
		this.y = y;
		character = new Image(null, "resources/mouse.jpg");
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void paint(PaintEvent e, int w, int h) {
		System.out.println("charcter IS NOW IN: ("+getX()+","+getY()+")");
		e.gc.setBackground(new Color(null, 210, 210, 210));
		e.gc.drawImage(character, 0, 0, character.getImageData().width, character.getImageData().height, x, y, w, h);

	}
}

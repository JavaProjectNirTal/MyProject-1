package viewBoard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

public class MazeGameBoard extends GameBoard {

	private String mazeString;
	private int[][] maze;
	private int mazeSize;
	private Image mazeBackround, imageEnd;
	private GameCharacter character;

	
	
	public MazeGameBoard(Composite parent, int style, String description) {
		super(parent, style, description);
		mazeBackround = new Image(getDisplay(), "resources/wall3.jpg");
		imageEnd = new Image(getDisplay(), "resources/cheese.jpg");
		mazeString = description;
		if (mazeString != null) {
			buildMaze();
			createBoardGame();
			character  = new GameCharacter(0,0);
			redraw();
		}
	}

	@Override
	public void createBoardGame() {
		
		setLayout(new GridLayout(2, false));									//Layout of Board itself (in side)
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));		//Layout (out Side)
		setBackgroundImage(mazeBackround);
		
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				Image tmp = new Image(getDisplay(), "resources/wall2.jpg");
				
				double width = (getSize().x / mazeSize) - 1;				//(double)
				double height = getSize().y / mazeSize;
				int widthI = (int)width;
				int heightI = (int)height;
				int end = mazeSize-1;
						if (maze != null) {
							for (int i = 0; i < mazeSize; i++)
								for (int j = 0; j < mazeSize; j++) {		
									if (maze[i][j] == 0)
									e.gc.drawImage(tmp, 0, 0, tmp.getImageData().width, tmp.getImageData().height, j*widthI, i*heightI, widthI, heightI);
									if (i == end && j == end )
										e.gc.drawImage(imageEnd, 0, 0,imageEnd.getImageData().width, imageEnd.getImageData().height, j*widthI, i*heightI, widthI, heightI);
								}
							character.paint(e, widthI, heightI);
							checkWin(getGameCharacterCordinate());
						}
					}
				});
	}

	public int getRowNumber(String s) {
		int sum=0;
		for (int i=1; i<s.length(); i++) {
			if (s.charAt(i) == ',')
				break;
			sum = sum*10 + Character.getNumericValue(s.charAt(i));
		}
		return sum;
	}
	
	public int getColumnNumber(String s) {
		int sum=0;
		int lastChar = s.length()-1;
		int firstChar = s.indexOf(',');
		for (int i=firstChar+1; i<lastChar; i++) {
			sum = sum*10 + Character.getNumericValue(s.charAt(i));
		}
		return sum;
	}
	
	public void extractSizeOfMaze(String s) {
		//stage #1 - extract size out of the description
		String[] a = s.split("Size ");
		String[] b = a[1].split("X");
		
		//stage #2 set the size on the Maze
		mazeSize = Integer.parseInt(b[0]);
	}
	
	public void setWallsOfMaze(String s) {
		//stage #1 - extract walls out of the description
		String[] a = s.split(": ");
		String b = a[1];
		String[] arrWalls = b.split(" ");

		//stage #2 set the walls on the Maze
		for(int k=0; k<arrWalls.length; k++) {
			int row =getRowNumber(arrWalls[k]); 
			int col =getColumnNumber(arrWalls[k]);
			maze[row][col] = 0;
		}
		
	}
	
	public void buildMaze() {
		//#1: set size
		extractSizeOfMaze(mazeString);
		
		//#2: building maze
		maze = new int[mazeSize][mazeSize];
		for(int i=0; i<mazeSize; i++)
			for(int j=0; j<mazeSize; j++) {
				maze[i][j] = 1;
			}
		System.out.println();
		
		//#3: setting walls
		setWallsOfMaze(mazeString);
/*		
		//#4: printing for check
		for(int i=0; i<mazeSize; i++) {
			System.out.println();
			for(int j=0; j<mazeSize; j++) {
				System.out.print(maze[i][j]+" ");
			}
		}*/
	}
	
	public String getGameCharacterCordinate() {
		
		double width = (getSize().x / mazeSize) - 1;				//(double)
		double height = getSize().y / mazeSize;
		
		int y = (int)(character.getX() / width);
		int x = (int)(character.getY() / height);
		return ("("+x+","+y+")");
	}

	public void setGameCharacter(String cordinate) {
		int y = getRowNumber(cordinate);  		//x of the new regular Move
		int x = getColumnNumber(cordinate);		//y of the new regular Move
		
		double width = (getSize().x / mazeSize) - 1;				//(double)
		double height = getSize().y / mazeSize;
		
		character.setX(x * (int)width);
		character.setY(y * (int)height);
		redraw();
	}
	
	public void checkWin(String cordinate) {
		int y = getRowNumber(cordinate);  		//x of the new regular Move
		int x = getColumnNumber(cordinate);		//y of the new regular Move
		
		int end = mazeSize-1;
		boolean gotToEnd = false;
		
		if (x == end && y == end && !gotToEnd) {
			gotToEnd = true;
			MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			messageBox.setMessage("Congratulations You Win!!! ");
			messageBox.open();
			this.setEnabled(false);
		}
		
	}
}
		

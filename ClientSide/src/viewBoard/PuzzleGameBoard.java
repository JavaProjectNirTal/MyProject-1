package viewBoard;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class PuzzleGameBoard extends GameBoard {

	String puzzleString;
	int[][] board;
	private static final int size = 3;
	int puzzleStateSize;
	private GameCharacter myPlayer;

	Image pic0, pic1, pic2, pic3, pic4, pic5, pic6, pic7, pic8;

	private HashMap<Integer, Image> ImageToInt;

	public PuzzleGameBoard(Composite parent, int style, String description) {
		super(parent, style, description);
		puzzleString = description;
		puzzleStateSize = size * size;
		board = new int[size][size];
		if (puzzleString != null) {
			buildPuzzle();
			createBoardGame();
		}
	}

	// #1:
	private void buildPuzzle() {
		String[] a = puzzleString.split(" ");
		if(a.length > 1)
			puzzleString = a[1];
		else
			puzzleString = a[0];
		char c;
		int k = 0;
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				c = puzzleString.charAt(k);
				board[i][j] = Character.getNumericValue(c);
				k++;
			}
	}

	public void setImages() {
		pic0 = new Image(getDisplay(), "resources/0.jpg");
		pic1 = new Image(getDisplay(), "resources/1.jpg");
		pic2 = new Image(getDisplay(), "resources/2.jpg");
		pic3 = new Image(getDisplay(), "resources/3.jpg");
		pic4 = new Image(getDisplay(), "resources/4.jpg");
		pic5 = new Image(getDisplay(), "resources/5.jpg");
		pic6 = new Image(getDisplay(), "resources/6.jpg");
		pic7 = new Image(getDisplay(), "resources/7.jpg");
		pic8 = new Image(getDisplay(), "resources/8.jpg");

		ImageToInt = new HashMap<Integer, Image>();

		ImageToInt.put(0, pic0);
		ImageToInt.put(1, pic1);
		ImageToInt.put(2, pic2);
		ImageToInt.put(3, pic3);
		ImageToInt.put(4, pic4);
		ImageToInt.put(5, pic5);
		ImageToInt.put(6, pic6);
		ImageToInt.put(7, pic7);
		ImageToInt.put(8, pic8);
	}

	@Override
	public void createBoardGame() {

		setImages();
		myPlayer = new GameCharacter(0, 0);

		setLayout(new GridLayout(3, true));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

		setBackground(new Color(null, 255, 255, 255));
		// redraw();

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (board != null) {
					int width = getSize().x;
					int height = getSize().y;
					int w = width / board[0].length;
					int h = height / board.length;
					for (int i = 0; i < board.length; i++)
						for (int j = 0; j < board[0].length; j++) {
							int x = j * w;
							int y = i * h;
							Image tmp = ImageToInt.get(board[i][j]);
							e.gc.drawImage(tmp, 0, 0, tmp.getImageData().width,
									tmp.getImageData().height, x, y, w - 5,
									h - 5);
							e.gc.drawRectangle(x, y, w - 5, h - 5);
						}
				}
			}
		});
	}

	protected GameCharacter getMyPlayer() {
		return myPlayer;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public String getPuzzleString() {
		return puzzleString;
	}

	public void setNewState(String description) {
		puzzleString = description;
		puzzleStateSize = size * size;
		board = new int[size][size];
		if (puzzleString != null) {
			buildPuzzle();
			createBoardGame();
		}
		redraw();
	}

}

package viewNwindow;

import model.Solution;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;

import viewBoard.GameBoard;
import viewBoard.PuzzleGameBoard;

public class PuzzleGameWindow extends GameWindow {

	Canvas startGameCanvas;
	String puzzleString;
	Button btnStart;
	Image puzzleInstruction;
	Button instruction;
	
	public PuzzleGameWindow(int width, int height, String title) {
		super(width, height, title);

	}

	@Override
	void initWidgets() { // change!!!!!! without all if!!! use FACTORY
		// setting the Window:
		puzzleInstruction = new Image(display, "resources/insructionPuzzle.jpg");
		
		shell.setSize(700, 700);
		shell.setLayout(new GridLayout(2, false));

		Label lblHeadLine = new Label(shell, SWT.CENTER); // headLine
		lblHeadLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		lblHeadLine.setText("Welcome To The Puzzle Game");

		// Group #1:
		Group gameAlgorithmGroup = new Group(shell, SWT.CENTER); // for Game
																	// Algorithm
		gameAlgorithmGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));
		gameAlgorithmGroup.setText("Game Algorithm");
		gameAlgorithmGroup.setLayout(new GridLayout(2, true));
		Button b4 = new Button(gameAlgorithmGroup, SWT.RADIO);
		b4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1)); //
		b4.setText("BFS");
		Button b5 = new Button(gameAlgorithmGroup, SWT.RADIO);
		b5.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,
				1)); //
		b5.setText("Astar");

		// start Button:
		btnStart = new Button(shell, SWT.PUSH);
		btnStart.setText("Start Game");
		btnStart.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));

		btnStart.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				setCommandChange("selectDomain: Puzzle");
				if (b4.getSelection())
					setCommandChange("selectAlgorithm: BFS");
				if (b5.getSelection())
					setCommandChange("selectAlgorithm: Astar");
				
				setCommandChange("getDescription");
				String s = getDescription();
				if (board != null)
					board.dispose();
				setBoard(new PuzzleGameBoard(shell, SWT.DOUBLE_BUFFERED, s));
				startGameCanvas.dispose();
				instruction.dispose();
				buildBoard();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// the Canvas before the Game starts
		startGameCanvas = new Canvas(shell, SWT.PUSH);
		startGameCanvas.setSize(500, 500);
		startGameCanvas.setLayout(new GridLayout(1, false));
		startGameCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		startGameCanvas.moveBelow(btnStart);
		//startGameCanvas.setBackground(new Color(null, 0, 0, 255));
		//startGameCanvas.setBackgroundImage(puzzleInstruction);
		
		instruction = new Button(shell, SWT.PUSH);
		instruction.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
		instruction.setImage(puzzleInstruction);
		
		// solution Button:
		Button btnDisplaysolution = new Button(shell, SWT.PUSH);
		btnDisplaysolution.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				true, false, 2, 1));
		btnDisplaysolution.setText("Display Solution");
		btnDisplaysolution.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				//***checking server connection
				setCommandChange("CheckConnection");
				
				if (getPossibleToConnect() == false) {
					MessageBox messageBox = new MessageBox(shell, SWT.ERROR);
					messageBox.setMessage("Server is down, connect to get a Solution");
					messageBox.open();
					board.setFocus();
				}
				else
				{
				setCommandChange("solveDomain");
				while (getSolutionRecivied() == false) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					setCommandChange("presentSolution: 1");
				}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		// exit Button:
		Button btnExit = new Button(shell, SWT.PUSH);
		btnExit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false,
				2, 1));
		btnExit.setText("Exit Game");
		btnExit.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCommandChange("exit");
				shell.dispose();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void start() {
		run();
	}

	@Override
	public void displayCurrentState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displaySolution(Solution solution) {
		List lstActions;
		setSolutionRecivied(true);
		if (solution.getActions().get(0).getDescription().equals("no solution"))
			System.out
					.println("SORRY, A valid solution can not be found for this specific problem. please Run again!");
		else {

			lstActions = new List(shell, SWT.BORDER | SWT.V_SCROLL);
			lstActions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
					false, 2, 1));
			for (int i = 0; i < solution.getActions().size(); i++) {
				String a = solution.getActions().get(i).getDescription();
				String[] arr = a.split(" ");
				this.shell.getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						lstActions.add("Move " + arr[0]);
					}
				});
			}
			lstActions.setVisible(true);
			shell.setSize(400, 750);
			shell.layout();
			board.setFocus();
		}
	}

	@Override
	public String getUserAction() {
		return getCommand();
	}

	@Override
	public void solutionFoundOrNot(boolean answer) {
		if (answer == false)
			System.out.println("NO! Still searching for solution");
		else
			System.out.println("YES! we have reached a solution");
	}

	@Override
	public void doTask() {
		start();
	}

	public GameBoard getBoard() {
		return board;
	}

	public void setBoard(GameBoard board) {
		this.board = board;
	}

	public void updatePuzzle() {
		board.dispose();
		setBoard(new PuzzleGameBoard(shell, SWT.DOUBLE_BUFFERED, getDescription()));
		buildBoard();
		if(this.getDescription().equals("123456780")){
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
			messageBox.setMessage("Congratulation! You Win!!!!! ");
			messageBox.open();
			
		}
	}

	private void buildBoard() {
		board.moveBelow(btnStart);
		board.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		shell.layout();
		board.setFocus();

		board.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent k) {}
			@Override
			public void keyPressed(KeyEvent k) {
				puzzleString = ((PuzzleGameBoard) board).getPuzzleString();

				switch (k.keyCode) {
				case SWT.ARROW_RIGHT:
					setCommandChange("selectMoves: right " + puzzleString);
					updatePuzzle(); // ////////////////////////////////////
				break;
				case SWT.ARROW_LEFT:
					setCommandChange("selectMoves: left " + puzzleString);
					updatePuzzle(); // ////////////////////////////////////
				break;
				case SWT.ARROW_UP:
					setCommandChange("selectMoves: up " + puzzleString);
					updatePuzzle(); // ////////////////////////////////////
				break;
				case SWT.ARROW_DOWN:
					setCommandChange("selectMoves: down " + puzzleString);
					updatePuzzle(); // ////////////////////////////////////
				break;
				}

			}
		});
	}
	
	public void setCommandChange(String command) {
		setCommand(command);
		PuzzleGameWindow.this.setChanged();
		PuzzleGameWindow.this.notifyObservers();
	}

}

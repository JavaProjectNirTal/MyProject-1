package viewNwindow;


import model.Solution;
import model.algorithm.Action;

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
import org.eclipse.swt.widgets.MessageBox;

import viewBoard.GameBoard;
import viewBoard.MazeGameBoard;

public class MazeGameWindow extends GameWindow {

	private Canvas startGameCanvas;
	private String mazeCharacterState;
	Button instruction;
	Image mazeInstruction;

	public MazeGameWindow(int width, int height, String title) {
		super(width, height, title);
	}

	@Override
	void initWidgets() { // change!!!!!! without all if!!! use FACTORY
		// setting the Window:
		mazeInstruction = new Image(display, "resources/instructionMaze.jpg");

		shell.setSize(750, 750);
		shell.setLayout(new GridLayout(2, false));
		
		Label lblHeadLine = new Label(shell, SWT.CENTER); // headLine
		lblHeadLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				2, 1));
		lblHeadLine.setText("Welcome To The Maze Game");

		// Group #1:
		Group gameLevelGroup = new Group(shell, SWT.CENTER); // for Game Level
		gameLevelGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true,false, 2, 1));
		gameLevelGroup.setText("Game Level");
		gameLevelGroup.setLayout(new GridLayout(3, true));
		Button b1 = new Button(gameLevelGroup, SWT.RADIO);
		b1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,1)); //
		b1.setText("Amateur");
		b1.setSelection(true);
		Button b2 = new Button(gameLevelGroup, SWT.RADIO);
		b2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,1));//
		b2.setText("Intermediate");
		Button b3 = new Button(gameLevelGroup, SWT.RADIO);
		b3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,1));//
		b3.setText("Professional");

		// Group #2:
		Group gameAlgorithmGroup = new Group(shell, SWT.CENTER); // for Game Algorithm
		gameAlgorithmGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL,true, false, 2, 1));
		gameAlgorithmGroup.setText("Game Algorithm");
		gameAlgorithmGroup.setLayout(new GridLayout(2, true));
		Button b4 = new Button(gameAlgorithmGroup, SWT.RADIO);
		b4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,1)); //
		b4.setText("BFS");
		b4.setSelection(true);
		Button b5 = new Button(gameAlgorithmGroup, SWT.RADIO);
		b5.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1,1)); //
		b5.setText("Astar");

		// start Button:
		Button btnStart = new Button(shell, SWT.PUSH);
		btnStart.setText("Start Game");
		btnStart.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,false, 2, 1));

		btnStart.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// 1: choosing game Level
				if (b1.getSelection())
					setCommandChange("selectDomain: Maze-(0,0) (10,10) 11 25");
				if (b2.getSelection())
					setCommandChange("selectDomain: Maze-(0,0) (12,12) 13 40");
				if (b3.getSelection())
					setCommandChange("selectDomain: Maze-(0,0) (14,14) 15 50");
				// 2: choosing algorithm
				if (b4.getSelection())
					setCommandChange("selectAlgorithm: BFS");
				if (b5.getSelection())
					setCommandChange("selectAlgorithm: Astar");

				setCommandChange("getDescription");
				String s = getDescription();
				if (board != null)
					board.dispose();
				setBoard(new MazeGameBoard(shell, SWT.DOUBLE_BUFFERED, s));
				startGameCanvas.dispose();
				instruction.dispose();
				board.moveBelow(btnStart);
				board.setFocus();
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
		
		instruction = new Button(shell, SWT.PUSH);
		instruction.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
		instruction.setImage(mazeInstruction);
		
		// solution Button:
		Button btnDisplaysolution = new Button(shell, SWT.PUSH);
		btnDisplaysolution.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
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
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});

		// exit Button:
		Button btnExit = new Button(shell, SWT.PUSH);
		btnExit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		btnExit.setText("Exit Game");
		btnExit.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCommandChange("exit");
				shell.dispose();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
	}
	
	public void buildBoard() {
			board.addKeyListener(new KeyListener() {

				@Override
				public void keyReleased(KeyEvent k) {
				}

				@Override
				public void keyPressed(KeyEvent k) {
					mazeCharacterState = ((MazeGameBoard) board).getGameCharacterCordinate(); // (x,y) before move
					switch (k.keyCode) {
					case SWT.ARROW_RIGHT:
						setCommandChange("selectMoves: right "+ mazeCharacterState);
						((MazeGameBoard) board).setGameCharacter(getDescription());
						System.out.println("move to right: "+((MazeGameBoard) board).getGameCharacterCordinate());
						board.redraw();
						shell.layout();
						break;
					case SWT.ARROW_LEFT:
						setCommandChange("selectMoves: left "+ mazeCharacterState);
						((MazeGameBoard) board).setGameCharacter(getDescription());
						System.out.println("move to left: "+((MazeGameBoard) board).getGameCharacterCordinate());
						board.redraw();
						shell.layout();
						break;
					case SWT.ARROW_UP:
						setCommandChange("selectMoves: up "+ mazeCharacterState);
						((MazeGameBoard) board).setGameCharacter(getDescription());
						System.out.println("move to up: "+((MazeGameBoard) board).getGameCharacterCordinate());
						board.redraw();
						shell.layout();
						break;
					case SWT.ARROW_DOWN:
						setCommandChange("selectMoves: down "+ mazeCharacterState);
						((MazeGameBoard) board).setGameCharacter(getDescription());
						System.out.println("move to down: "+((MazeGameBoard) board).getGameCharacterCordinate());
						board.redraw();
						shell.layout();
						break;
					}

				}
			});
			board.redraw();
			shell.layout();
	}
	
	@Override
	public void start() {
		run();
	}

	@Override
	public void displaySolution(Solution solution) {
		setSolutionRecivied(true);
		if (solution.getActions().get(0).getDescription().equals("no solution"))
			System.out
					.println("SORRY, A valid solution can not be found for this specific problem. please Run again!");
		else {
			System.out.println("Path from Start to Goal:");
			for (Action a : solution.getActions())
				System.out.println(a);
			System.out.println("End of Game !");
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

	public void setCommandChange(String command) {
		setCommand(command);
		MazeGameWindow.this.setChanged();
		MazeGameWindow.this.notifyObservers();
	}
}

package viewNwindow;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class WelcomeWindow extends BasicWindow {

	private GameWindow gameWindow;
	private String selectedFilePath;
	
	public WelcomeWindow(int width, int height, String title) {
		super(width, height, title);
	}

	public GameWindow getGameWindow() {
		return gameWindow;
	}

	@Override
	void initWidgets() {
		//setting the Window:
		shell.setSize(900, 500);
		shell.setLayout(new GridLayout(2, true));
		
		Button xmlLoad = new Button(shell, SWT.PUSH);
		xmlLoad.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 2, 1));
		xmlLoad.setText("Load Client's Properties");
		
			
		xmlLoad.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// open a file dialog
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setText("open");
				dialog.setFilterPath("resources");				//default path is resources
				String[] filterExt = { "*.xml", "*.*" };		//ext: *.xml , *.*
				dialog.setFilterExtensions(filterExt);
				selectedFilePath = dialog.open();
				//System.out.println("selectedFilePath: "+selectedFilePath);
				if (selectedFilePath != null) {
					setChanged();
					notifyObservers(selectedFilePath);
					Text text = new Text(shell, SWT.NONE);
					text.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 2, 1));
					text.moveBelow(xmlLoad);
					text.setText("XML File was Loaded Successfully");
					shell.layout();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
		
		//Game #1:
		Image gameImage1 = new Image(display,"resources/maze0.png");
		Button game1maze = new Button(shell, SWT.PUSH);
		game1maze.setImage(gameImage1);
		game1maze.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
		game1maze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedFilePath != null) {
					display.dispose();
					gameWindow = new MazeGameWindow(500, 500, "MazeGameWindow");
					WelcomeWindow.this.setChanged();
					WelcomeWindow.this.notifyObservers();
					gameWindow.run();
				}
				else {
					MessageBox messageBox = new MessageBox(shell, SWT.ERROR);
					messageBox.setMessage("Please Choose an XML File.");
					messageBox.open();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		//Game #2:
		Image gameImage2 = new Image(display,"resources/Puzzle0.png");
		Button game2puzzle = new Button(shell, SWT.PUSH);
		game2puzzle.setImage(gameImage2);
		game2puzzle.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
		game2puzzle.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedFilePath != null) {
					display.dispose();
					gameWindow = new PuzzleGameWindow(500, 500, "PuzzleGameWindow");
					WelcomeWindow.this.setChanged();
					WelcomeWindow.this.notifyObservers();
					gameWindow.run();
				}
				else {
					MessageBox messageBox = new MessageBox(shell, SWT.ERROR);
					messageBox.setMessage("Please Choose an XML File.");
					messageBox.open();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	
	
	
	
}
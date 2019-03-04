package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Minesweeper extends JPanel implements ActionListener, MouseListener {

	/*
	 * Timer
	 * Fix Map Expansion (does not expand completely/expands too much)
	 */

	JFrame frame;

	JMenuBar menuBar;
	JMenu game, display, control;
	JMenuItem[] levels;
	JMenuItem beginner, intermediate, expert;
	JMenuItem[] icons;
	JMenuItem mDefault, sweet, savory;
	JLabel[] directions;
	int currentTheme;

	JPanel panel, topPanel, scoreBoard;
	JToggleButton[][] togglers;
	int[][] numbers;
	Color[] numberColors;
	int width = 9, height = 9;
	ImageIcon[] flag, mine, centerImage;
	JButton center;
	JLabel timer, flagsRemaining;
	int startingFlag, flagCount;
	String spacing = "     ";
	boolean gameOver = false;
	HashSet<Point> minePositions;
	boolean firstClick = true;

	public Minesweeper() {
		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(40 * width, 40 * height);

		// Center, Mine, Flag Images
		mine = new ImageIcon[4];
		flag = new ImageIcon[4];
		centerImage = new ImageIcon[5];
		mine[0] = new ImageIcon("dMine.png");
		flag[0] = new ImageIcon("dFlag.png");
		centerImage[0] = new ImageIcon("dCenter.png");
		mine[1] = new ImageIcon("cMine.png");
		flag[1] = new ImageIcon("cFlag.png");
		centerImage[1] = new ImageIcon("cCenter.png");
		mine[2] = new ImageIcon("sMine.png");
		flag[2] = new ImageIcon("sFlag.png");
		centerImage[2] = new ImageIcon("sCenter.png");
		mine[3] = new ImageIcon(mine[0].getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		flag[3] = new ImageIcon(flag[0].getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		centerImage[3] = new ImageIcon(centerImage[0].getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		centerImage[4] = new ImageIcon("wCenter.png");;
		// Menu Bar
		menuBar = new JMenuBar();
		game = new JMenu("Game");
		display = new JMenu("Icons");
		control = new JMenu("Control");
		beginner = new JMenuItem("Beginner");
		intermediate = new JMenuItem("Intermediate");
		expert = new JMenuItem("Expert");
		levels = new JMenuItem[3];
		levels[0] = beginner;
		levels[1] = intermediate;
		levels[2] = expert;
		for (JMenuItem item : levels) {
			item.addActionListener(this);
			game.add(item);
		}

		currentTheme = 0;
		mDefault = new JMenuItem("Default");
		sweet = new JMenuItem("Candyland");
		savory = new JMenuItem("Fair");
		icons = new JMenuItem[3];
		icons[0] = mDefault;
		icons[1] = sweet;
		icons[2] = savory;
		for (JMenuItem item : icons) {
			item.addActionListener(this);
			display.add(item);
		}
		directions = new JLabel[2];
		directions[0] = new JLabel(" Left-click an empty square to reveal it ");
		directions[1] = new JLabel(" Right-click an empty square to flag it ");
		for (JLabel label : directions) {
			control.add(label);
		}

		menuBar.add(game);
		menuBar.add(display);
		menuBar.add(control);

		// Panel Board
		togglers = new JToggleButton[width][height];
		numbers = new int[width][height];
		numberColors = new Color[9];
		panel = new JPanel();
		panel.setLayout(new GridLayout(togglers.length, togglers[0].length));
		for (int i = 0; i < togglers.length; i++) {
			for (int j = 0; j < togglers[0].length; j++) {
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		}
		minePositions = new HashSet<>();

		// Setting Colors for Numbers
		numberColors[0] = Color.BLUE; // Standard Blue
		numberColors[1] = new Color(0, 100, 0); // Dark Green
		numberColors[2] = Color.RED; // Standard Red
		numberColors[3] = new Color(25, 25, 112); // Midnight Blue
		numberColors[4] = new Color(255, 140, 0); // Dark Orange
		numberColors[5] = new Color(139, 69, 19); // Saddle Brown
		numberColors[6] = new Color(128, 0, 128); // Purple
		numberColors[7] = new Color(128, 0, 0); // Maroon
		numberColors[8] = new Color(255, 20, 147); // Deep Pink

		// Score Board
		scoreBoard = new JPanel(new FlowLayout(FlowLayout.CENTER));
		startingFlag = 10;
		flagsRemaining = new JLabel(spacing + (startingFlag - flagCount) + spacing);
		flagsRemaining.setFont(new Font("Lucida Sans", Font.PLAIN, 24));
		center = new JButton(centerImage[3]);
		center.setPreferredSize(new Dimension(50, 50));
		center.addActionListener(this);
		timer = new JLabel(spacing + "0" + spacing);
		timer.setFont(new Font("Lucida Sans", Font.PLAIN, 24));
		scoreBoard.add(flagsRemaining);
		scoreBoard.add(center);
		scoreBoard.add(timer);

		// Combine Panels
		topPanel = new JPanel(new BorderLayout());
		topPanel.add(menuBar, BorderLayout.NORTH);
		topPanel.add(scoreBoard, BorderLayout.SOUTH);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == beginner) {
			width = 9;
			height = 9;
			startingFlag = 10;
			changeLevel();
		}
		if (e.getSource() == intermediate) {
			width = 16;
			height = 16;
			startingFlag = 40;
			changeLevel();
		}
		if (e.getSource() == expert) {
			width = 30;
			height = 16;
			startingFlag = 99;
			changeLevel();
		}
		if (e.getSource() == mDefault) {
			changeIcon("default");
			changeLevel();
		}
		if (e.getSource() == sweet) {
			changeIcon("sweet");
			changeLevel();
		}
		if (e.getSource() == savory) {
			changeIcon("savory");
			changeLevel();
		}
		if (e.getSource() == center) {
			changeLevel();
			centerImage[3] = new ImageIcon(centerImage[currentTheme].getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			center.setIcon(centerImage[3]);
		}
		revalidate();
	}

	public void gameSetup(int r, int c) {
		int minesRemaining = 0;
		if (width == 9)
			minesRemaining = 10;
		if (width == 16)
			minesRemaining = 40;
		if (width == 30)
			minesRemaining = 90;
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[0].length; j++) {
				numbers[i][j] = 0;
			}
		}

		// Randomly assign the mine positions
		minePositions = new HashSet<>();
		while (minePositions.size() != minesRemaining) {
			int row;
			int col;
			do {
				row = (int) (Math.random() * height);
				col = (int) (Math.random() * width);
			} while (togglers[row][col].getIcon() != null);
			if (row != r && col != c) {
				minePositions.add(new Point(row, col));
				numbers[row][col] = -1;
			}
		}
		System.out.println(minePositions);

		// Fill buttons with values based on mine positions
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[0].length; j++) {
				if (numbers[i][j] == -1) {
					for (int x = -1; x < 2; x++) {
						for (int y = -1; y < 2; y++) {
							try {
								if (numbers[i + x][j + y] != -1) {
									numbers[i + x][j + y] += 1;
								}
							} catch (Exception e) {
							}
						}
					}
				}
			}
		}
	}

	public void changeIcon(String type) {
		if (type.equals("default")) {
			currentTheme = 0;

		} else if (type.equals("sweet")) {
			currentTheme = 1;

		} else if (type.equals("savory")) {
			currentTheme = 2;
		}
		mine[3] = new ImageIcon(mine[currentTheme].getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		flag[3] = new ImageIcon(flag[currentTheme].getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		centerImage[3] = new ImageIcon(centerImage[currentTheme].getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		center.setIcon(centerImage[3]);
	}

	public void changeLevel() {
		frame.remove(panel);
		frame.setSize(40 * width, 40 * height);
		togglers = new JToggleButton[height][width];
		numbers = new int[height][width];
		panel = new JPanel();
		panel.setLayout(new GridLayout(togglers.length, togglers[0].length));
		for (int i = 0; i < togglers.length; i++) {
			for (int j = 0; j < togglers[0].length; j++) {
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		}
		flagCount = 0;
		flagsRemaining.setText(spacing + (startingFlag - flagCount) + spacing);
		gameOver = false;
		firstClick = true;
		frame.add(panel, BorderLayout.CENTER);
	}

	public void gameOver() {
		for (int i = 0; i < togglers.length; i++) {
			for (int j = 0; j < togglers[0].length; j++) {
				togglers[i][j].setEnabled(false);
				if (minePositions.contains(new Point(i, j))) {
					togglers[i][j].setIcon(mine[3]);
					togglers[i][j].setDisabledIcon(mine[3]);
				}
				gameOver = true;
			}
		}
	}

	public void expandMap(int row, int col) {
		togglers[row][col].setSelected(true);
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				try {
					if (numbers[row + x][col + y] == 0 && !togglers[row + x][col + y].isSelected())
						expandMap(row + x, col + y);
					else if (numbers[row + x][col + y] != -1 && togglers[x][y].getIcon() != flag[3]) {
						togglers[row + x][col + y].setSelected(true);
						togglers[row + x][col + y].setMargin(new Insets(0, 0, 0, 0));
						togglers[row + x][col + y].setFont(new Font("Lucida Sans", Font.PLAIN, 20));
						togglers[row + x][col + y].setForeground(numberColors[numbers[row + x][col + y] - 1]);
						togglers[row + x][col + y].setText(numbers[row + x][col + y] + "");
					}
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void checkWinning() {
		int flagCheckCount = 0;
		int openBoxCount = 0;
		for (int i = 0; i < togglers.length; i++) {
			for (int j = 0; j < togglers[0].length; j++) {
				if (togglers[i][j].isSelected() == false) {
					if (minePositions.contains(new Point(i, j))) {
						flagCheckCount++;
					}
				} else {
					openBoxCount++;
				}
			}
		}
		if (flagCheckCount == startingFlag && openBoxCount == height*width-startingFlag) {
			gameOver = true;
			System.out.println ("game over--winning");
			centerImage[3] = new ImageIcon(centerImage[4].getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
			center.setIcon(centerImage[3]);
			gameOver();
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (firstClick) {
				for (int i = 0; i < togglers.length; i++) {
					for (int j = 0; j < togglers[0].length; j++) {
						if (e.getSource() == togglers[i][j]) {
							gameSetup(i, j);
						}
					}
				}
				firstClick = false;
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				for (int i = 0; i < togglers.length; i++) {
					for (int j = 0; j < togglers[0].length; j++) {
						if (e.getSource() == togglers[i][j] && togglers[i][j].isSelected() == false) {
							if (togglers[i][j].getIcon() == null) {
								togglers[i][j].setIcon(flag[3]);
								togglers[i][j].setDisabledIcon(flag[3]);
								flagCount++;
							} else {
								togglers[i][j].setIcon(null);
								flagCount--;
							}
							flagsRemaining.setText(spacing + (startingFlag - flagCount) + spacing);
						}
					}
				}
			}
			if (e.getButton() == MouseEvent.BUTTON1) {
				for (int i = 0; i < togglers.length; i++) {
					for (int j = 0; j < togglers[0].length; j++) {
						if (e.getSource() == togglers[i][j]) {
							togglers[i][j].setSelected(false);
							if (togglers[i][j].getIcon() == null) {
								if (minePositions.contains(new Point(i, j))) {
									togglers[i][j].setIcon(mine[3]);
									togglers[i][j].setDisabledIcon(mine[3]);
									gameOver();
								} else {
									if (togglers[i][j].isSelected() == false)
										expandMap(i, j);
								}
							} 
						}
					}
				}
			}
			if (startingFlag - flagCount == 0) {
				System.out.println("zero");
				checkWinning();
			}
		}
	}

	public static void main(String[] args) {
		Minesweeper game = new Minesweeper();
	}

}

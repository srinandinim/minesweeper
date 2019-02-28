package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Minesweeper extends JPanel implements ActionListener, MouseListener {

	/*
	*Ensure first click is not a mine
	*/

	JFrame frame;

	JMenuBar menuBar;
	JMenu game, display, control;
	JMenuItem[] levels;
	JMenuItem beginner, intermediate,expert;
	JMenuItem[] icons;
	JMenuItem mDefault, sweet, savory;
	JLabel[] directions;

	JPanel panel, topPanel, scoreBoard;
	JToggleButton[][] togglers;
	int width = 9;
	int height = 9;
	ImageIcon[] flag, mine, centerImage;
	JButton center;
	JLabel timer, flagsRemaining;
	int startingFlag;
	int flagCount = 0;
	String spacing = "     ";
	
	ArrayList<Point> minePositions;
	
	public Minesweeper(){
		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(40*width,40*height);
		mine = new ImageIcon[4];
		flag = new ImageIcon[4];
		centerImage = new ImageIcon[4];

		mine[0] = new ImageIcon ("dMine.png");
		flag[0] = new ImageIcon ("dFlag.png");
		centerImage[0] = new ImageIcon ("dCenter.png");
		mine[1] = new ImageIcon ("cMine.png");
		flag[1] = new ImageIcon ("cFlag.png");
		centerImage[1] = new ImageIcon ("cCenter.png");
		mine[2] = new ImageIcon ("sMine.png");
		flag[2] = new ImageIcon ("sFlag.png");
		centerImage[2] = new ImageIcon ("sCenter.png");
		mine[3] = new ImageIcon(mine[0].getImage().getScaledInstance(25,25, Image.SCALE_SMOOTH));
		flag[3] = new ImageIcon(flag[0].getImage().getScaledInstance(25,25, Image.SCALE_SMOOTH));
		centerImage[3] = new ImageIcon(centerImage[0].getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

		menuBar = new JMenuBar();
		game = new JMenu ("Game");
		display = new JMenu ("Icons");
		control = new JMenu ("Control");

		beginner = new JMenuItem ("Beginner");
		intermediate = new JMenuItem ("Intermediate");
		expert = new JMenuItem ("Expert");
		levels = new JMenuItem[3];
		levels[0] = beginner;
		levels[1] = intermediate;
		levels[2] = expert;
		for (JMenuItem item: levels) {
			item.addActionListener(this);
			game.add(item);
		}
		mDefault = new JMenuItem ("Default");
		sweet = new JMenuItem ("Candyland");
		savory = new JMenuItem ("Fair");
		icons = new JMenuItem[3];
		icons[0] = mDefault;
		icons[1] = sweet;
		icons[2] = savory;
		for (JMenuItem item: icons) {
			item.addActionListener(this);
			display.add(item);
		}
		directions = new JLabel[2];
		directions[0] = new JLabel (" Left-click an empty square to reveal it ");
		directions[1] = new JLabel (" Right-click an empty square to flag it ");
		for (JLabel label: directions){
			control.add(label);
		}

		menuBar.add(game);
		menuBar.add(display);
		menuBar.add(control);

		togglers = new JToggleButton[width][height];
		panel = new JPanel();
		panel.setLayout(new GridLayout(togglers.length, togglers[0].length));
		for (int i = 0; i < togglers.length; i++){
			for (int j = 0; j < togglers[0].length; j++){
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		}
		minePositions = new ArrayList<>();
		gameSetup();
		
		scoreBoard = new JPanel(new FlowLayout(FlowLayout.CENTER));
		startingFlag = 10;
		flagsRemaining = new JLabel(spacing + (startingFlag-flagCount) + spacing);
		flagsRemaining.setFont(new Font("Serif", Font.PLAIN, 24));
		center = new JButton (centerImage[3]);
		center.setPreferredSize(new Dimension(50, 50));
		center.addActionListener(this);
		timer = new JLabel(spacing + "10" + spacing);
		timer.setFont(new Font("Serif", Font.PLAIN, 24));
		scoreBoard.add(flagsRemaining);
		scoreBoard.add(center);
		scoreBoard.add(timer);

		topPanel = new JPanel (new BorderLayout());
		topPanel.add(menuBar, BorderLayout.NORTH);
		topPanel.add(scoreBoard, BorderLayout.SOUTH);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed (ActionEvent e){
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
		if (e.getSource() == mDefault){
			changeIcon("default");
			changeLevel();
		}
		if (e.getSource() == sweet){
			changeIcon("sweet");
			changeLevel();
		}
		if (e.getSource() == savory){
			changeIcon("savory");
			changeLevel();
		}
		if (e.getSource() == center) {
			changeLevel();
		}
		revalidate();
	}

	public void gameSetup (){
		int minesRemaining = 0;
		if (width == 9)
			minesRemaining = 10;
		if (width == 16)
			minesRemaining = 40;
		if (width == 30)
			minesRemaining = 90;
		
		minePositions = new ArrayList<>();
		while (minesRemaining > 0){
			int row;
			int col;
			do{
				row = (int)(Math.random() * height);
				col = (int)(Math.random() * width);
			} while (togglers[row][col].getIcon()!=null);
			minePositions.add(new Point(row, col));			
			minesRemaining--;
		}
		System.out.println(minePositions);
	}

	public void changeIcon(String type) {
		int number = 0;
		if (type.equals("default")) {
			number = 0;

		} else if (type.equals("sweet")) {
			number = 1;

		} else if (type.equals("savory")) {
			number = 2;
		}
		mine[3] = new ImageIcon(mine[number].getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		flag[3] = new ImageIcon(flag[number].getImage().getScaledInstance(25,25, Image.SCALE_SMOOTH));
		centerImage[3] = new ImageIcon(centerImage[number].getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		center.setIcon(centerImage[3]);
	}

	public void changeLevel(){
		frame.remove(panel);
		frame.setSize(40*width,40*height);
		togglers = new JToggleButton[height][width];
		panel = new JPanel();
		panel.setLayout(new GridLayout(togglers.length, togglers[0].length));
		for (int i = 0; i < togglers.length; i++){
			for (int j = 0; j < togglers[0].length; j++){
				togglers[i][j] = new JToggleButton();
				togglers[i][j].addMouseListener(this);
				panel.add(togglers[i][j]);
			}
		}
		flagCount = 0;
		flagsRemaining.setText(spacing + (startingFlag-flagCount) + spacing);
		gameSetup();
		frame.add(panel,BorderLayout.CENTER);
	}
	
	public void gameOver() {
		for (int i = 0; i < togglers.length; i++) {
			for (int j = 0; j < togglers[0].length; j++) {
				if (minePositions.contains(new Point(i, j))) {
					togglers[i][j].setIcon (mine[3]);
				}
				togglers[i][j].setEnabled(false);
			}
		}
	}

	public void mousePressed (MouseEvent e){
		if (e.getButton() == MouseEvent.BUTTON3){
			for (int i = 0; i < togglers.length; i++){
				for (int j = 0; j < togglers[0].length; j++){
					if (e.getSource() == togglers[i][j]){
						if (togglers[i][j].getIcon() == null){
							togglers[i][j].setIcon (flag[3]);
							flagCount++;
							flagsRemaining.setText(spacing + (startingFlag-flagCount) + spacing);
						} else {
							togglers[i][j].setIcon (null);
							flagCount--;
							flagsRemaining.setText(spacing + (startingFlag-flagCount) + spacing);
						}
					}
				}
			}
		}
		if (e.getButton() == MouseEvent.BUTTON1){
			for (int i = 0; i < togglers.length; i++){
				for (int j = 0; j < togglers[0].length; j++){
					if (e.getSource() == togglers[i][j]){
						//disable toggling feature
						if (minePositions.contains(new Point(i, j))) {
							togglers[i][j].setIcon (mine[3]);
							gameOver();
						}
					}
				}
			}
		}
	}

	public void mouseEntered (MouseEvent e){
	}

	public void mouseExited (MouseEvent e){
	}

	public void mouseReleased (MouseEvent e){
	}

	public void mouseClicked (MouseEvent e){
	}

	public static void main (String[] args){
		Minesweeper app = new Minesweeper();
	}

}

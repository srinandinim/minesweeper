import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Minesweeper extends JPanel implements ActionListener, MouseListener {

	JFrame frame;

	JMenuBar menuBar;
	JMenu game;
	JMenu display;
	JMenu control;
	JMenuItem[] levels;
	JMenuItem beginner;
	JMenuItem intermediate;
	JMenuItem expert;
	JMenuItem[] icons;
	JMenuItem mDefault;
	JMenuItem sweet;
	JMenuItem savory;
	JLabel[] directions;

	JPanel panel;
	int width = 9;
	int height = 9;
	JToggleButton[][] togglers;
	ImageIcon flag;
	ImageIcon mine;

	JPanel topPanel;
	JPanel scoreBoard;
	JButton center;
	ImageIcon centerImage;

	public Minesweeper(){
		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(850,700);

		centerImage = new ImageIcon ("dCenter.png");
		centerImage = new ImageIcon(centerImage.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		mine = new ImageIcon ("dMine.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance(frame.getWidth()/width, frame.getHeight()/height, Image.SCALE_SMOOTH));
		flag = new ImageIcon ("dFlag.png");
		flag = new ImageIcon(flag.getImage().getScaledInstance(frame.getWidth()/width, frame.getHeight()/height, Image.SCALE_SMOOTH));

		menuBar = new JMenuBar();
		game = new JMenu ("Game");
		display = new JMenu ("Display");
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

		scoreBoard = new JPanel();
		center = new JButton (centerImage);
		center.setPreferredSize(new Dimension(50, 50));
		scoreBoard.add(center);

		topPanel = new JPanel (new GridLayout (2, 1));
		topPanel.add(menuBar);
		topPanel.add(scoreBoard);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed (ActionEvent e){
		if (e.getSource() == beginner) {
			width = 9;
			height = 9;
			changeLevel();
		}
		if (e.getSource() == intermediate) {
			width = 16;
			height = 16;
			changeLevel();
		}
		if (e.getSource() == expert) {
			width = 30;
			height = 16;
			changeLevel();
		}
		if (e.getSource() == mDefault){
		}
		if (e.getSource() == sweet){
		}
		if (e.getSource() == savory){
		}
		revalidate();
	}

	public void changeLevel(){
		frame.remove(panel);
		mine = new ImageIcon(mine.getImage().getScaledInstance(frame.getWidth()/width, frame.getHeight()/height, Image.SCALE_SMOOTH));
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
		frame.add(panel,BorderLayout.CENTER);
	}

	public void mousePressed (MouseEvent e){
		if (e.getButton() == MouseEvent.BUTTON3){
			if (!togglers[0][0].isSelected()){
				togglers[0][0].setSelected (true);
				togglers[0][0].setIcon (mine);
			} else {
				togglers[0][0].setSelected (false);
				togglers[0][0].setIcon (null);
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
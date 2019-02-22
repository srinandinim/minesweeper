package minesweeper;
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

	JPanel panel;
	int width = 9;
	int height = 9;
	JToggleButton[][] togglers;
	ImageIcon mine;

	public Minesweeper(){
		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(850,700);

		mine = new ImageIcon ("mine.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance(frame.getWidth()/width, frame.getHeight()/height, Image.SCALE_SMOOTH));

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

		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed (ActionEvent e){
		if (e.getSource() == beginner) {
			width = 9;
			height = 9;
		}
		if (e.getSource() == intermediate) {
			width = 16;
			height = 16;
		}
		if (e.getSource() == expert) {
			width = 30;
			height = 16;
		}
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

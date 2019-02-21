import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Minesweeper extends JPanel implements ActionListener, MouseListener {

	JFrame frame;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem beginner;
	JMenuItem intermediate;
	JMenuItem expert;

	JPanel panel;
	int dimension = 20;
	JToggleButton[][] togglers;
	ImageIcon mine;

	public Minesweeper(){
		frame = new JFrame("Minesweeper");
		frame.add(this);
		frame.setSize(1000,800);

		mine = new ImageIcon ("mine.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance(frame.getWidth()/dimension, frame.getHeight()/dimension, Image.SCALE_SMOOTH));

		menuBar = new JMenuBar();
		menu = new JMenu ("Game");
		beginner = new JMenuItem ("Beginner");
		intermediate = new JMenuItem ("Intermediate");
		expert = new JMenuItem ("Expert");

		menu.add(beginner);
		menu.add(intermediate);
		menu.add(expert);
		menuBar.add(menu);
		beginner.addActionListener(this);
		intermediate.addActionListener(this);
		expert.addActionListener(this);

		togglers = new JToggleButton[dimension][dimension];
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
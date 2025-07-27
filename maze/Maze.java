import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Maze {
	static int R = 13;
	static int C = 13;
	static ImageIcon icon = new ImageIcon("dude.png");
	static MazeGen.coord player;
	static JPanel maze_panel;
	static JPanel[][] maze_panel_grid;
	static int[][] maze_grid;

	public static void main(String[] args) {
		System.setProperty("java.home", ".");
		maze_panel_grid = new JPanel[R][C];

		JFrame frame = new JFrame();
		frame.setTitle("Maze Game");
		frame.setIconImage(icon.getImage());
		frame.addKeyListener(new moveListener());
		frame.setResizable(false);
		frame.setLayout(new BorderLayout(10, 10));
		frame.getContentPane().setBackground(Color.blue);

		addMazePanel(frame);
		
		maze_grid = MazeGen.generateMazeGrid(R, C);
		load_grid();

		addControlPanel(frame);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	static void addMazePanel(JFrame frame) {
		maze_panel = new JPanel();
		maze_panel.setLayout(new GridLayout(R, C));
		maze_panel.setBackground(Color.black);
		maze_panel.setPreferredSize(new Dimension(500, 500));
		maze_panel.setBorder(BorderFactory.createLineBorder(Color.black, 10));

		for (int r = 0; r < R; r++) {
		for (int c = 0; c < C; c++) {
			JPanel pane = new JPanel();

			int panelSide = 500 / R;
			int dudeSide = panelSide - 4;

			pane.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

			JLabel dude = new JLabel();
			ImageIcon dudeIcon = new ImageIcon(icon.getImage().getScaledInstance(dudeSide, dudeSide, Image.SCALE_DEFAULT));
			dude.setIcon(dudeIcon);

			dude.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			dude.setVisible(false);
			
			pane.add(dude);
			maze_panel_grid[r][c] = pane;
			maze_panel.add(pane);
		}
		}

		frame.add(maze_panel, BorderLayout.CENTER);
	}

	static void addControlPanel(JFrame frame) {
		JPanel control_panel = new JPanel();
		control_panel.setPreferredSize(new Dimension(200, 40));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEADING);
		control_panel.setLayout(flowLayout);

		JButton reset_btn = new JButton("Reset");
		reset_btn.setFocusable(false);
		reset_btn.addActionListener((e) -> {
			if (player != null) {
				maze_panel_grid[player.x()][player.y()].getComponents()[0].setVisible(false);
				player = null;
			}
			maze_grid = MazeGen.generateMazeGrid(R, C);
			load_grid();
		});

		JButton play_btn = new JButton("Play");
		play_btn.setFocusable(false);
		play_btn.addActionListener((e) -> {
			player = new MazeGen.coord(0, 0);
			maze_panel_grid[player.x()][player.y()].getComponents()[0].setVisible(true);
		});

		JButton solve_btn = new JButton("Solve");
		solve_btn.setFocusable(false);
		solve_btn.addActionListener((e) -> {
			MazeSolve.solveMaze(maze_grid);
			load_grid();
		});

		flowLayout.setHgap(5);
		int top_pad = (control_panel.getPreferredSize().height - play_btn.getPreferredSize().height) / 2;
		flowLayout.setVgap(top_pad);

		control_panel.add(play_btn);
		control_panel.add(solve_btn);
		control_panel.add(reset_btn);

		frame.add(control_panel, BorderLayout.NORTH);
	}

	static class moveListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			movePlayer(e.getKeyCode());
		}
	}

	static void movePlayer(int direction) {
		if (player == null) return;

		MazeGen.coord newCoord;
		switch (direction) {
			case 87, 38:
				newCoord = new MazeGen.coord(player.x() - 1, player.y());
				break;
			case 83, 40:
				newCoord = new MazeGen.coord(player.x() + 1, player.y());
				break;
			case 65, 37:
				newCoord = new MazeGen.coord(player.x(), player.y() - 1);
				break;
			case 68, 39:
				newCoord = new MazeGen.coord(player.x(), player.y() + 1);
				break;
			default:
				newCoord = player;
		}

		if (newCoord.x() >= maze_grid.length || newCoord.x() < 0) return;
		if (newCoord.y() >= maze_grid[0].length || newCoord.y() < 0) return;
		if (maze_grid[newCoord.x()][newCoord.y()] == 0) return;

		maze_panel_grid[player.x()][player.y()].getComponents()[0].setVisible(false);
		maze_panel_grid[newCoord.x()][newCoord.y()].getComponents()[0].setVisible(true);
		player = newCoord;

		if (player.x() == R - 1 && player.y() == C - 1) {
			JOptionPane.showMessageDialog(null, "You Win!");
			maze_panel_grid[player.x()][player.y()].getComponents()[0].setVisible(false);
			player = null;
		}
	}

	static void load_grid() {
		for (int r = 0; r < R; r++) {
		for (int c = 0; c < C; c++) {
			JPanel pane = maze_panel_grid[r][c];

			int rc = maze_grid[r][c];
			if (r == maze_grid.length - 1 && c == maze_grid[r].length - 1) {
				pane.setBackground(Color.red);
			} else if (rc == 1) {
				pane.setBackground(Color.white);
			} else if (rc == 2) {
				pane.setBackground(Color.green);
			} else {
				pane.setBackground(Color.black);
			}
		}
		}
	}
}

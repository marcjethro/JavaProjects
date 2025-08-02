import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Sliding {
	static final int GRID_SIDE = 3;
	static List<Tile> tiles;
	static JPanel puzzle_pnl;
	static Tile moveTile;
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(5, 5));
		frame.getContentPane().setBackground(Color.blue);
		frame.setTitle("Sliding Puzzle");

		JPanel control_pnl = new JPanel();
		control_pnl.setPreferredSize(new Dimension(400, 40));
		control_pnl.setLayout(new FlowLayout(FlowLayout.LEADING));

		JButton shuffle_btn = new JButton("SHUFFLE");
		shuffle_btn.setFocusable(false);
		shuffle_btn.addActionListener((e) -> {
			char[] directions = {'a', 'w', 's', 'd'};
			for (int i = 0; i <= 10000; i++) {
				char direction = directions[new Random().nextInt(directions.length)];
				shiftTile(direction);
			}
			renderTiles();
		});
		JButton reset_btn = new JButton("RESET");
		reset_btn.setFocusable(false);
		reset_btn.addActionListener((e) -> {
			for (Tile tile : tiles) {
				tile.reset();
			}
			renderTiles();
		});

		control_pnl.add(shuffle_btn);
		control_pnl.add(reset_btn);

		puzzle_pnl = new JPanel();
		puzzle_pnl.setPreferredSize(new Dimension(400, 400));
		puzzle_pnl.setLayout(new GridLayout(GRID_SIDE, GRID_SIDE));

		tiles = new ArrayList<>();

		for (int r = 0; r <= GRID_SIDE-1; r++) {
		for (int c = 0; c <= GRID_SIDE-1; c++) {
			Tile tile = new Tile(Integer.toString(1 + (r*GRID_SIDE) + c), r, c);
			tiles.add(tile);
		}
		}

		moveTile = tiles.get(tiles.size()-1);
		moveTile.setText("");

		renderTiles();

		frame.addKeyListener(new ArrowListener());

		frame.add(control_pnl, BorderLayout.NORTH);
		frame.add(puzzle_pnl, BorderLayout.CENTER);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	static void shiftTile(char direction) {
		Tile[] moveSet = {null, null, null, null};
		for (Tile tile : tiles) {
			if (tile.x == moveTile.x && tile.y == moveTile.y + 1) {
				moveSet[0] = tile;
			} else if (tile.x == moveTile.x + 1 && tile.y == moveTile.y) {
				moveSet[1] = tile;
			} else if (tile.x == moveTile.x - 1 && tile.y == moveTile.y) {
				moveSet[2] = tile;
			} else if (tile.x == moveTile.x && tile.y == moveTile.y - 1) {
				moveSet[3] = tile;
			}
		}
		switch (direction) {
			case 'a':
				moveTile.swap(moveSet[0]);
				break;
			case 'w':
				moveTile.swap(moveSet[1]);
				break;
			case 's':
				moveTile.swap(moveSet[2]);
				break;
			case 'd':
				moveTile.swap(moveSet[3]);
				break;
		}
	}

	static class ArrowListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent k) {
			shiftTile(k.getKeyChar());
			renderTiles();
		}
	}

	static void renderTiles() {
		puzzle_pnl.removeAll();
		for (int r = 0; r <= GRID_SIDE-1; r++) {
		for (int c = 0; c <= GRID_SIDE-1; c++) {
			for (Tile tile : tiles) {
				if (tile.x == r && tile.y == c) {
					puzzle_pnl.add(tile);
				}
			
			}
		}
		}
		puzzle_pnl.revalidate();
		puzzle_pnl.repaint();
	}

	static class Tile extends JLabel {
		String text;
		int origX;
		int origY;
		int x;
		int y;
		Tile(String text, int x, int y) {
			this.text = text;
			this.origX = x;
			this.origY = y;
			this.x = x;
			this.y = y;
			this.setText(text);
			this.setFont(new Font("MV Boli", Font.BOLD, 30));
			this.setVerticalAlignment(JLabel.CENTER);
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		}

		void swap(Tile target) {
			if (target == null) {
				return;
			}
			int r = this.x;
			int c = this.y;
			this.x = target.x;
			this.y = target.y;
			target.x = r;
			target.y = c;
		}

		void reset() {
			this.x = this.origX;
			this.y = this.origY;
		}
	}
}

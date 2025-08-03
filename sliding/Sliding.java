import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Sliding {
	static final int GRID_DIMENSION = 3;
	static final int GRID_SIDE = 400;
	static List<Tile> tiles;
	static JPanel puzzle_pnl;
	static Tile moveTile;
	static JColorChooser colorChooser  = new JColorChooser(Color.black);
	static JCheckBox seeCheckBox;
	static boolean shuffled = false;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(5, 5));
		frame.getContentPane().setBackground(Color.black);
		frame.setTitle("Sliding Puzzle");

		JPanel control_pnl = new JPanel();
		control_pnl.setPreferredSize(new Dimension(0, 40));
		control_pnl.setLayout(new FlowLayout(FlowLayout.LEADING));

		JButton open_btn = new JButton("OPEN");
		open_btn.setFocusable(false);
		open_btn.addActionListener((e) -> {
			JFileChooser chooser = new JFileChooser(".");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"PNG or JPG", "png", "jpg");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File imageFile = chooser.getSelectedFile();
				loadImage(imageFile);
				renderTiles();
			}
		});

		JButton shuffle_btn = new JButton("SHUFFLE");
		shuffle_btn.setFocusable(false);
		shuffle_btn.addActionListener((e) -> {
			char[] directions = {'a', 'w', 's', 'd'};
			for (int i = 0; i <= 10000; i++) {
				char direction = directions[new Random().nextInt(directions.length)];
				shiftTile(direction);
			}
			renderTiles();
			shuffled = true;
		});
		JButton reset_btn = new JButton("RESET");
		reset_btn.setFocusable(false);
		reset_btn.addActionListener((e) -> {
			for (Tile tile : tiles) {
				tile.reset();
			}
			renderTiles();
		});

		JButton color_btn = new JButton();
		int color_btn_side = reset_btn.getPreferredSize().height;
		color_btn.setFocusable(false);
		color_btn.setPreferredSize(new Dimension(color_btn_side, color_btn_side));
		color_btn.setBackground(Color.black);
		color_btn.addActionListener((e) -> {
			Color color = colorChooser.showDialog(frame, "Pick color", Color.black);
			color_btn.setBackground(color);
			for (Tile tile : tiles) {
				tile.setForeground(color);
			}

		});

		seeCheckBox = new JCheckBox("SHOW NUMBER");
		seeCheckBox.setSelected(true);
		seeCheckBox.setFocusable(false);
		seeCheckBox.addActionListener((e) -> {
			for (Tile tile : tiles) {
				tile.setText((seeCheckBox.isSelected()) ? tile.text : null);
			}
			renderTiles();
		});

		control_pnl.add(open_btn);
		control_pnl.add(shuffle_btn);
		control_pnl.add(reset_btn);
		control_pnl.add(color_btn);
		control_pnl.add(seeCheckBox);

		puzzle_pnl = new JPanel();
		puzzle_pnl.setPreferredSize(new Dimension(GRID_SIDE, GRID_SIDE));
		puzzle_pnl.setLayout(new GridLayout(GRID_DIMENSION, GRID_DIMENSION));

		tiles = new ArrayList<>();

		for (int r = 0; r <= GRID_DIMENSION-1; r++) {
		for (int c = 0; c <= GRID_DIMENSION-1; c++) {
			Tile tile = new Tile(Integer.toString(1 + (r*GRID_DIMENSION) + c), r, c);
			tiles.add(tile);
		}
		}

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

			if (isSolved() && shuffled && tiles.get(0).icon != null) {
				seeCheckBox.setSelected(false);
				for (Tile tile : tiles) {
					tile.setText((seeCheckBox.isSelected()) ? tile.text : null);
				}
			}

			renderTiles();

			if (isSolved() && shuffled) {
				JOptionPane.showMessageDialog(null, "You Win!");
				shuffled = false;
			}

		}
	}

	static void loadImage(File imageFile) {
		try {
			Image originalImage = ImageIO.read(imageFile);
			Image scaledImage = originalImage.getScaledInstance(GRID_SIDE, GRID_SIDE, Image.SCALE_DEFAULT);
			BufferedImage scaledBufferedImage = new BufferedImage(GRID_SIDE, GRID_SIDE, BufferedImage.TYPE_INT_ARGB);
			int CELL_SIDE = GRID_SIDE/GRID_DIMENSION;
			scaledBufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);
			for (Tile tile : tiles) {
				BufferedImage croppedImage = scaledBufferedImage.getSubimage(tile.origY*CELL_SIDE, tile.origX*CELL_SIDE, CELL_SIDE, CELL_SIDE);
				ImageIcon icon = new ImageIcon(croppedImage);
				tile.icon = icon;
				tile.setIcon(icon);
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}

	static void renderTiles() {
		puzzle_pnl.removeAll();
		for (int r = 0; r <= GRID_DIMENSION-1; r++) {
		for (int c = 0; c <= GRID_DIMENSION-1; c++) {
			for (Tile tile : tiles) {
				if (tile.x == r && tile.y == c) {
					puzzle_pnl.add(tile);
				}
			
			}
		}
		}

		moveTile = tiles.get(tiles.size()-1);
		if (isSolved()) {
			moveTile.setIcon(moveTile.icon);
		} else {
			moveTile.setIcon(null);
		}
		moveTile.setText(null);

		puzzle_pnl.revalidate();
		puzzle_pnl.repaint();
	}

	static boolean isSolved() {
		for (Tile tile : tiles) {
			if (tile.origX != tile.x || tile.origY != tile.y) return false;
		}
		return true;
	}

	static class Tile extends JLabel {
		String text;
		ImageIcon icon = null;
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
			this.setFont(new Font("MV Boli", Font.BOLD, 50));
			this.setVerticalAlignment(JLabel.CENTER);
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setVerticalTextPosition(JLabel.CENTER);
			this.setHorizontalTextPosition(JLabel.CENTER);
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

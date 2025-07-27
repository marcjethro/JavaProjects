import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class Puppy {
	static JFrame frame;
	static JLabel label;
	static ImageIcon icon = new ImageIcon("puppy.png");
	static String name = "Puppy";

	public static void main(String[] args) {
		System.setProperty("java.home", ".");
		frame = new JFrame();
		frame.setTitle("Puppy Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.magenta);
		frame.setLayout(new BorderLayout(10, 10));
		frame.setIconImage(icon.getImage());

		addLabel();
		addPanels();

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	static void addPanels() {
		JPanel btn_pane = createBtnPane();
		JPanel paneWest = new JPanel();
		JPanel paneEast = new JPanel();
		JPanel paneSouth = new JPanel();

		btn_pane.setBackground(Color.green);
		paneWest.setBackground(Color.red);
		paneEast.setBackground(Color.red);
		paneSouth.setBackground(Color.green);

		btn_pane.setPreferredSize(new Dimension(0, 35));
		paneWest.setPreferredSize(new Dimension(25, 0));
		paneEast.setPreferredSize(new Dimension(25, 0));
		paneSouth.setPreferredSize(new Dimension(0, 25));

		frame.add(btn_pane, BorderLayout.NORTH);
		frame.add(paneWest, BorderLayout.WEST);
		frame.add(paneEast, BorderLayout.EAST);
		frame.add(paneSouth, BorderLayout.SOUTH);
	}

	static JPanel createBtnPane() {
		JPanel btn_pane = new JPanel();

		JButton bark_btn = new JButton("Bark");
		bark_btn.setFocusable(false);
		bark_btn.addActionListener((e) -> JOptionPane.showMessageDialog(frame, "Woof! Woof!", name + " says:", JOptionPane.INFORMATION_MESSAGE));

		JButton rename_btn = new JButton("Rename");
		rename_btn.setFocusable(false);
		rename_btn.addActionListener((e) -> {
			name = JOptionPane.showInputDialog(frame, "What's the puppy's name?");
			label.setText("This is " + name);
			frame.pack();
		});

		btn_pane.add(bark_btn);
		btn_pane.add(rename_btn);

		return btn_pane;
	}

	static void addLabel() {
		label = new JLabel();
		label.setText("This is puppy");
		label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT)));
		label.setVerticalTextPosition(JLabel.TOP);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(new Font("MV Boli", Font.BOLD, 30));
		label.setForeground(Color.blue);
		label.setBackground(Color.yellow);
		label.setOpaque(true);

		Border lineBorder = BorderFactory.createLineBorder(Color.blue, 5); 
		Border padding = BorderFactory.createEmptyBorder(15, 15, 15, 15);
		Border compound = BorderFactory.createCompoundBorder(lineBorder, padding); 
		label.setBorder(compound);

		frame.add(label);
	}
}

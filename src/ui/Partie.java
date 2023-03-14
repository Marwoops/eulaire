import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Partie extends JPanel {
	VueGraphePartie g;
	Image background;
	JButton regenerer = new JButton(new ImageIcon("../files/textures/retry.png"));
	JButton solution = new JButton(new ImageIcon("../files/textures/solve.png"));

	public Partie(Image bg) {
		background = bg;
		g = new VueGraphePartie(this);
		add(g);
		regenerer.setBorderPainted(false);
		regenerer.setContentAreaFilled(false);
		regenerer.setFocusPainted(false);
		add(regenerer);
		regenerer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Je regenère la team");
				g.regen();
			}
		});
		solution.setBorderPainted(false);
		solution.setContentAreaFilled(false);
		solution.setFocusPainted(false);
		add(solution);
		solution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Voyons voir pour résoudre ça bg");
				g.solution();
			}
		});
		repaint();
	}

	public Partie(VueGraphe vg, Image bg) {
		background = bg;
		g = new VueGraphePartie(this);
		g.setGraphe(vg.getGraphe());
		g.setCoordonnes(vg.getCoordonnees());
		g.setOrigin();
		add(g);
		regenerer.setBorderPainted(false);
		regenerer.setContentAreaFilled(false);
		regenerer.setFocusPainted(false);
		add(regenerer);
		regenerer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Je regenère la team");
				g.regen();
			}
		});
		solution.setBorderPainted(false);
		solution.setContentAreaFilled(false);
		solution.setFocusPainted(false);
		add(solution);
		solution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Voyons voir pour résoudre ça bg");
				g.solution();
			}
		});
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		regenerer.setBounds(getWidth() - 105, getHeight() / 2, 80, 50);
		solution.setBounds(getWidth() - 110, getHeight() / 3, 102, 60);
	}
}

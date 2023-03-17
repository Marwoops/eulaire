import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class AideEditeur extends JPanel {

	private JButton editeur;
	private Image background;
	private LinkedList<String> text;

	private static LinkedList<String> loadText() {
		LinkedList<String> res = new LinkedList<String>();
		String tmp;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("../texte_aide.mzr"));
			tmp = reader.readLine();
			while (tmp != null) {
				res.add(tmp);
				tmp = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public AideEditeur(JFrame frame, Image bg, String pack, VueGraphe vg, int level) {

		background = bg;
		text = loadText();

		editeur = Utils.generate_button("jeu-editeur", e -> {
			frame.setContentPane(new Editeur(frame, bg, pack, vg, level));
			frame.revalidate();
			frame.repaint();
		});

		add(editeur);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 850, 850);
		g.setColor(Color.BLACK);
		for (int i = 0; i < text.size(); ++i) {
			g.drawString(text.get(i), 10, 13 * (i + 1));
		}
		editeur.setBounds(getWidth() - 120, 800, 90, 50);
	}
}

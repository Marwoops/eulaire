import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class VueGraphe extends JComponent {
	private final Color color_bg = Color.WHITE;
	private final Color color_default = Color.BLUE;
	private final Color color_unsolvable = Color.RED;
	private final Color color_selected = Color.GREEN;
	private final int point_radius = 8;

	private ModeGraphique modeGraphique;

	private Graphe graphe;
	private LinkedList<Point> coords;
	private int selected = -1;

	public VueGraphe(ModeGraphique mg) {
		modeGraphique = mg;
		setPreferredSize(new Dimension(850, 850));
		setBorder(BorderFactory.createLineBorder(Color.black));
		effacer();
	}

	public void setModeGraphique(ModeGraphique mg) {
		modeGraphique = mg;
	}

	public Graphe getGraphe() {
		return graphe;
	}
	public LinkedList<Point> getCoords() {
		return coords;
	}
	// g.taille() doit valoir coords.size()
	public void setGraphe(Graphe graphe, LinkedList<Point> coords) {
		this.graphe = graphe;
		this.coords = coords;
		repaint();
	}

	public void ajouteSommet(Point p) {
		graphe.addSommet();
		coords.add(p);
		repaint();
	}

	public Point getCoord(int i) {
		return coords.get(i);
	}
	public void setCoord(int i, int x, int y) {
		coords.set(i, new Point(x, y));
		repaint();
	}

	public void effacer() {
		graphe = new Graphe();
		coords = new LinkedList<Point>();
		select(-1);
		repaint();
	}

	public void supprSommet(int id) {
		coords.set(id, coords.get(coords.size() - 1));
		coords.remove(coords.size() - 1);
		graphe.supprSommet(id);
		repaint();
	}

	// select(-1) pour déselectionner
	public void select(int i) {
		selected = i;
		repaint();
	}
	// -1 si aucun sélectionné
	public int get_selected() {
		return selected;
	}

	public int getId(int x, int y) {
		for (int i = 0; i < coords.size(); i++) {
			final var point = coords.get(i);
			final var dist_sq = Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2);
			if (dist_sq < Math.pow(point_radius, 2)) {
				return i;
			}
		}
		return -1;
	}

	public void paintComponent(Graphics g_) {
		final var g = ((Graphics2D) g_);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(color_bg);
		g.fillRect(0, 0, 850, 850);
		g.setColor(color_default);
		g.setStroke(new BasicStroke(5));

		modeGraphique.render_sommets(g, coords, selected);
		modeGraphique.render_aretes(g, graphe, coords);
	}


	public String serialise() {
		String res = "";

		if (coords.size() == 0) {
			return res;
		}

		for (Point pos : coords) {
			res += " " + pos.getX() + ";" + pos.getY();
		}
		res = res.substring(1, res.length()) + "\n"; // retrait du premier espace

		return res + graphe.toString();
	}

	public LinkedList<Point> deserialiseCoords(String inp) {
		LinkedList<Point> resultat = new LinkedList<>();

		String[] tab = inp.split(" ");
		for (String coord : tab) {
			String[] point = coord.split(";");
			int x = (int) Double.parseDouble(point[0]);
			int y = (int) Double.parseDouble(point[1]);
			resultat.add(new Point(x, y));
		}

		return resultat;
	}

	public Graphe deserialiseConnections(BufferedReader reader) {
		Graphe g = new Graphe();
		try {
			reader.mark(0);
			String[] ligne = (reader.readLine()).split(" ");
			reader.reset();
			int taille = ligne.length;

			for (int i = 0; i < taille; i++) {
				g.addSommet();
			}

			for (int i = 0; i < taille; i++) {
				ligne = (reader.readLine()).split(" ");
				for (int j = 0; j <= i; j++) {
					g.addConnections(j, i, Integer.parseInt(ligne[j]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return g;
	}

	public void exporter(String pack, int n) {
		try {
			BufferedWriter writer;
			if (pack == null) {
				writer = new BufferedWriter(new FileWriter("../packless/" + n + ".mzr"));
			} else {
				writer = new BufferedWriter(new FileWriter("../packs/" + pack + "/" + n + ".mzr"));
			}
			writer.write(serialise());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void importer(String pack, int n) {
		try {
			BufferedReader reader;
			if (pack == null) {
				reader = new BufferedReader(new FileReader("../packless/" + n + ".mzr"));
			} else {
				reader = new BufferedReader(new FileReader("../packs/" + pack + "/" + n + ".mzr"));
			}
			final var line = reader.readLine();
			if (line == null) {
				effacer();
				reader.close();
				return;
			}
			coords = deserialiseCoords(line);
			graphe = deserialiseConnections(reader);
			reader.close();
			repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

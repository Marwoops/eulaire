import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class Editeur extends JPanel {
	private static Image background;

	private VueGraphe vuegraphe;
	private InputHandler input_handler;

	private String pack;
	private int graphe_actuel;

	private JButton supprimer;
	private JButton suppr_all;

	private JButton exporter;
	private JButton importer;

	private JButton generer_random;
	private static Random r = new Random();
	private boolean en_generation = false;

	private JButton ajoute_sommet;
	private JButton ajoute_arete;
	private JButton enleve_sommet;
	private JButton enleve_arete;
	private JLabel sommet = new JLabel("Sommets");
	private JLabel arete = new JLabel("Arêtes");

	private JButton jouer;

	private JButton aide;

	private JFrame frame;

	public Editeur(JFrame frame, Image bg, String pack, VueGraphe vg, int level) {
		background = bg;
		this.frame = frame;
		this.pack = pack;

		if (vg != null) {
			vuegraphe = vg;
			vuegraphe.set_editing(true);
			for (var l : vuegraphe.getMouseListeners()) {
				vuegraphe.removeMouseListener(l);
			}
			for (var l : vuegraphe.getMouseMotionListeners()) {
				vuegraphe.removeMouseMotionListener(l);
			}
		} else {
			vuegraphe = new VueGraphe(true);
		}
		vuegraphe.select(-1);
		add(vuegraphe);
		final var ml = new ControleurSourisEditeur();
		vuegraphe.addMouseListener(ml);
		vuegraphe.addMouseMotionListener(ml);

		input_handler = new PlacingInputHandler();
		graphe_actuel = level >= 0 ? level : get_graphe_nb();
		if (vg == null) {
			if (graphe_actuel < get_graphe_nb()) {
				vuegraphe.importer(pack, graphe_actuel);
			} else {
				vuegraphe.effacer();
			}
		}

		supprimer = Utils.generate_button("suppr_sommet", e -> {
			if (input_handler instanceof DeletingInputHandler) {
				input_handler = new PlacingInputHandler();
			} else {
				input_handler = new DeletingInputHandler();
			}
			repaint();
		});
		add(supprimer);

		suppr_all = Utils.generate_button("suppr_tout", e -> {
			graphe_actuel = get_graphe_nb();
			vuegraphe.effacer();
		});
		add(suppr_all);

		exporter = Utils.generate_button("exporter", e -> {
			vuegraphe.exporter(pack, graphe_actuel);
		});
		add(exporter);

		importer = Utils.generate_button("importer", e -> {
			graphe_actuel = (graphe_actuel + 1) % (get_graphe_nb() + 1);
			if (graphe_actuel < get_graphe_nb()) {
				vuegraphe.importer(pack, graphe_actuel);
			} else {
				vuegraphe.effacer();
			}
		});
		add(importer);

		generer_random = Utils.generate_button("random", e -> {
			en_generation = !en_generation;
			if (en_generation) {
				final int nb_sommets = 5 + r.nextInt(20);
				final int nb_aretes = r.nextInt(nb_sommets);
				vuegraphe.setGraphe(getNRandomSom(nb_sommets, nb_aretes), getNRandomCoord(nb_sommets));
			}
			graphe_actuel = get_graphe_nb();
		});
		add(generer_random);

		ajoute_sommet = Utils.generate_button("ajoute", e -> {
			vuegraphe.ajouteSommet(getRandomCoord());
		});
		add(ajoute_sommet);

		add(sommet);

		enleve_sommet = Utils.generate_button("enleve", e -> {
			if (vuegraphe.getGraphe().taille() >= 1) {
				vuegraphe.supprSommet(r.nextInt(vuegraphe.getGraphe().taille()));
			}
		});
		add(enleve_sommet);

		ajoute_arete = Utils.generate_button("ajoute", e -> {
			if (vuegraphe.getGraphe().taille() < 2) {
				return;
			}
			int v1 = r.nextInt(vuegraphe.getGraphe().taille());
			int v2 = r.nextInt(vuegraphe.getGraphe().taille() - 1);
			if (v1 == v2) {
				v2 = vuegraphe.getGraphe().taille() - 1;
			}
			vuegraphe.getGraphe().addConnections(v1, v2, 1);
			repaint();
		});
		add(ajoute_arete);

		add(arete);

		enleve_arete = Utils.generate_button("enleve", e -> {
			if (!vuegraphe.getGraphe().hasConnexions()) {
				return;
			}
			int v1;
			int v2;
			do {
				v1 = r.nextInt(vuegraphe.getGraphe().taille());
				v2 = r.nextInt(vuegraphe.getGraphe().taille());
			} while (v1 == v2 || vuegraphe.getGraphe().getConnexion(v1, v2) == 0);
			vuegraphe.getGraphe().addConnections(v1, v2, -1);
			repaint();
		});
		add(enleve_arete);

		jouer = Utils.generate_button("jouer_editeur", e -> {
			frame.setContentPane(new Partie(frame, background, pack, vuegraphe, graphe_actuel));
			frame.revalidate();
			frame.repaint();
		});
		add(jouer);

		aide = Utils.generate_button("aide", e -> {
			frame.setContentPane(new AideEditeur(frame, background, pack, vuegraphe, graphe_actuel));
			frame.revalidate();
			frame.repaint();
		});
		add(aide);
	}

	public static Point getRandomCoord() {
		return new Point(30 + r.nextInt(800), 30 + r.nextInt(800));
	}

	public static LinkedList<Point> getNRandomCoord(int n) {
		LinkedList<Point> res = new LinkedList<Point>();
		for (int i = 0; i < n; i++) {
			res.add(getRandomCoord());
		}
		return res;
	}

	public static Graphe getNRandomSom(int nb_sommets, int nb_aretes) {
		Graphe graphe = new Graphe();
		for (int i = 0; i < nb_sommets; i++) {
			graphe.addSommet();
		}
		for (int j = 0; j < nb_aretes; j++) {
			int id1 = r.nextInt(nb_sommets);
			int id2 = r.nextInt(nb_sommets);
			while (id2 == id1) {
				id2 = r.nextInt(nb_sommets);
			}
			graphe.addConnections(id1, id2, 1);
		}
		return graphe;
	}

	public int get_graphe_nb() {
		if (pack == null) {
			return (new java.io.File("../packless")).listFiles().length - 1;
		} else {
			return (new java.io.File("../packs/" + pack)).listFiles().length;
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		if (input_handler instanceof DeletingInputHandler) {
			g.setColor(Color.green);
			((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 49, 51, 51));
		}

		supprimer.setBounds(getWidth() - 100, 50, 50, 50);
		suppr_all.setBounds(getWidth() - 100, 110, 50, 50);

		generer_random.setBounds(getWidth() - 100, 170, 50, 50);
		ajoute_sommet.setBounds(getWidth() - 100, 230, 50, 24);
		sommet.setBounds(getWidth() - 115, 255, 75, 24);
		enleve_sommet.setBounds(getWidth() - 100, 280, 50, 24);
		ajoute_arete.setBounds(getWidth() - 100, 330, 50, 24);
		arete.setBounds(getWidth() - 105, 355, 75, 24);
		enleve_arete.setBounds(getWidth() - 100, 380, 50, 24);

		exporter.setBounds(getWidth() - 100, 420, 50, 50);
		importer.setBounds(getWidth() - 100, 480, 50, 50);
		jouer.setBounds(getWidth() - 120, 800, 100, 50);
		aide.setBounds(getWidth() - 120, 700, 100, 50);
	}


	public class ControleurSourisEditeur implements MouseInputListener {
		int current_vertex = -1;
		public void mouseClicked(MouseEvent e) {
			if (current_vertex == -1) {
				input_handler.on_point_click(e.getPoint());
			} else {
				input_handler.on_vertex_click(current_vertex);
			}
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
			current_vertex = vuegraphe.getId(e.getX(), e.getY());
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseDragged(MouseEvent e) {
			if (current_vertex != -1) {
				input_handler.on_vertex_drag(current_vertex, e.getPoint());
			}
		}
		public void mouseMoved(MouseEvent e) {
		}
	}

	private abstract class InputHandler {
		public abstract void on_vertex_click(int vertex);
		public abstract void on_point_click(Point p);
		public abstract void on_vertex_drag(int vertex, Point p);
	}
	private class PlacingInputHandler extends InputHandler {
		public void on_vertex_click(int vertex) {
			if (vuegraphe.get_selected() == -1) {
				vuegraphe.select(vertex);
			} else if (vuegraphe.get_selected() == vertex) {
				vuegraphe.select(-1);
			} else {
				vuegraphe.getGraphe().addConnections(vuegraphe.get_selected(), vertex, 1);
				vuegraphe.select(vertex);
			}
		}
		public void on_point_click(Point p) {
			vuegraphe.ajouteSommet(p);
		}
		public void on_vertex_drag(int vertex, Point p) {
			vuegraphe.setCoord(vertex, p.x, p.y);
		}
	}
	private class DeletingInputHandler extends InputHandler {
		int last_deleted = -1;
		public void on_vertex_click(int vertex) {
			vuegraphe.supprSommet(vertex);
		}
		public void on_point_click(Point p) {
		}
		public void on_vertex_drag(int vertex, Point p) {
			if (last_deleted == -1) {
				last_deleted = vertex;
			}
			final var other_p = vuegraphe.getId(p.x, p.y);
			if (other_p == -1 || last_deleted == other_p) {
				return;
			}
			vuegraphe.getGraphe().addConnections(last_deleted, other_p, -1);
			last_deleted = other_p;
			repaint();
		}
	}
}

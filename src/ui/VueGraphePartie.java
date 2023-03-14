import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import javax.swing.*;

public class VueGraphePartie extends VueGraphe {
	int oujesuis = -1;
	Graphe origin;
	Partie partie;
	boolean toSolve = false;

	public VueGraphePartie(Partie p) {
		super(Color.black, 30, null);
		partie = p;
		addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {

				if (oujesuis < 0) {
					if (getId(e.getX(), e.getY()) != -1) {
						oujesuis = getId(e.getX(), e.getY());
						repaint();
					}
				} else {
					int clicked = getId(e.getX(), e.getY());
					if (getGraphe().getConnexions(oujesuis).contains(clicked)) {
						getGraphe().setConnexion(oujesuis, clicked, false);
						oujesuis = clicked;
						repaint();
					}
				}
			}
		});
		super.importer(id_graphe);
		setOrigin();
		regen();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 850, 850);
		g.setColor(getCouleur());

		if (getGraphe().nbConnexions() == 0) {
			if (id_graphe < n_graphes) {
				id_graphe++;
			} else {
				id_graphe = 1;
			}
			super.importer(id_graphe);
			setOrigin();
			regen();
			System.out.println(super.id_graphe);
			return;
		}

		LinkedList<Point> coord = getCoordonnees();
		int diam = getDiametre();

		for (int i = 0; i < getGraphe().taille(); ++i) {
			if (i == oujesuis) {
				g.setColor(Color.green);
			}

			((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(i).getX(), coord.get(i).getY(), diam,
			                                           diam));

			if (i == oujesuis) {
				g.setColor(getCouleur());
			}

			for (int j = i; j < getGraphe().taille(); ++j) {
				if (getGraphe().getConnexion(i, j) != 0) {
					int coord_i_x = (int) (coord.get(i).getX() + diam / 2);
					int coord_i_y = (int) (coord.get(i).getY() + diam / 2);
					int coord_j_x = (int) (coord.get(j).getX() + diam / 2);
					int coord_j_y = (int) (coord.get(j).getY() + diam / 2);
					g.drawLine(coord_i_x, coord_i_y, coord_j_x, coord_j_y);
					if (getGraphe().getConnexion(i, j) > 1) {
						g.drawString(getGraphe().getConnexion(i, j) + "", // uncrustify,
						             (coord_i_x + coord_j_x) / 2,    // what are you doing.
						             (coord_i_y + coord_j_y) / 2);
					}
				}
			}
		}

		if (toSolve) {
			System.out.println("Ce graphe est eulérien.");
			ArrayList<Integer> sol = getGraphe().hierholzer();

			for (int i = 0; i < index; ++i) {
				g.setColor(Color.WHITE);
				int coord_i_x = (int) (coord.get(sol.get(i)).getX() + diam / 2);
				int coord_i_y = (int) (coord.get(sol.get(i)).getY() + diam / 2);
				int coord_j_x = (int) (coord.get(sol.get(i + 1)).getX() + diam / 2);
				int coord_j_y = (int) (coord.get(sol.get(i + 1)).getY() + diam / 2);
				if (getGraphe().getConnexion(sol.get(i), sol.get(i + 1)) > 1) {
					g.drawString(getGraphe().getConnexion(sol.get(i), sol.get(i + 1)) - cpt + "", // uncrustify,
					             (coord_i_x + coord_j_x) / 2,            // what are you doing.
					             (coord_i_y + coord_j_y) / 2);
					cpt++;
					if (getGraphe().getConnexion(sol.get(i), sol.get(i + 1)) - cpt > 1) {
						g.setColor(Color.black);
						g.drawString(getGraphe().getConnexion(sol.get(i), sol.get(
											      i + 1)) - cpt + "",           // uncrustify,
						             (coord_i_x + coord_j_x) / 2,    // what are you doing.
						             (coord_i_y + coord_j_y) / 2);
					} else if (getGraphe().getConnexion(sol.get(i), sol.get(i + 1)) - cpt == 1) {
						g.setColor(Color.black);
						g.drawLine(coord_i_x, coord_i_y, coord_j_x, coord_j_y);
					} else {
						g.setColor(Color.WHITE);
						g.drawLine(coord_i_x, coord_i_y, coord_j_x, coord_j_y);
					}
				} else {
					g.drawLine(coord_i_x, coord_i_y, coord_j_x, coord_j_y);
					cpt = 0;
				}
				g.setColor(Color.black);
				((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(sol.get(i)).getX(),
				                                           coord.get(sol.get(i)).getY(), diam, diam));
				((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(sol.get(i + 1)).getX(),
				                                           coord.get(sol.get(i + 1)).getY(), diam,
				                                           diam));
			}
			index++;
		}
	}

	public void regen() {
		try {
			setGraphe(origin.clone());
			oujesuis = -1;
			repaint();
		} catch (CloneNotSupportedException e) {
			System.out.println("This shouldn't happen.");
			// This won't happen, java's just being a dick.
		}
	}

	public void setOrigin() {
		try {
			origin = getGraphe().clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("This shouldn't happen.");
			// This won't happen, java's just being a dick.
		}
	}

	int cpt = 0;
	int index = 1;
	public void solution() {
		if (getGraphe().estEulerien() && getGraphe().hierholzer() != null) {
			toSolve = true;
			final Timer timer = new Timer();
			final TimerTask task = new TimerTask() {
				public void run() {
					if (index >= getGraphe().hierholzer().size() - 1) {
						timer.cancel();
						this.cancel();
						timer.purge();
					}
					if (index == getGraphe().hierholzer().size()) {
						cpt = 0;
						index = 1;
					}
					System.out.println(index + "Task started");
					repaint();
					System.out.println(index + "Task ended");
				}
			};
			timer.scheduleAtFixedRate(task, 1000, 1000);
		} else {
			System.out.println("Ce graphe n'a pas de solution.");
		}
	}
}

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class VueGrapheEditeur extends VueGraphe {
	boolean peut_poser_sommet = false;
	boolean peut_lier = false;

	Editeur editeur;

	public VueGrapheEditeur(Color c, int d, Editeur e, MouseListener controleur) {
		super(c, d, controleur);
		editeur = e;
	}


	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 850, 850);

		g.setColor(super.getCouleur());

		LinkedList<Point> coord = super.getCoordonnees();
		int diam = super.getDiametre();

		for (int i = 0; i < getGraphe().taille(); ++i) {
			if (editeur.getALier() != i || !getPeutLier()) {
				((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(i).getX(), coord.get(i).getY(),
				                                           diam, diam));
			} else {
				g.setColor(Color.GREEN);
				((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(i).getX(), coord.get(i).getY(),
				                                           diam, diam));
				g.setColor(super.getCouleur());
			}
			for (int j = i; j < getGraphe().taille(); ++j) {
				if (getGraphe().getConnexion(i, j) != 0) {
					int coord_i_x = (int) (coord.get(i).getX() + diam / 2);
					int coord_i_y = (int) (coord.get(i).getY() + diam / 2);
					int coord_j_x = (int) (coord.get(j).getX() + diam / 2);
					int coord_j_y = (int) (coord.get(j).getY() + diam / 2);
					g.drawLine(coord_i_x, // Make formatter happy
					           coord_i_y, // until I figure out
					           coord_j_x, // the setting I need.
					           coord_j_y);
					if (getGraphe().getConnexion(i, j) > 1) {
						g.drawString(getGraphe().getConnexion(i, j)+"", (coord_i_x + coord_j_x) / 2, (coord_i_y + coord_j_y) / 2);
					}
				}
			}
		}
	}

	public boolean getPeutLier() {
		return editeur.getPeutLier();
	}

	public void ajouteSommet(Point p) {
		super.ajouteSommet(p);
	}

	public int getId(int x, int y) {
		return super.getId(x, y);
	}

	public int supprSommet(int id) {
		return super.supprSommet(id);
	}
}

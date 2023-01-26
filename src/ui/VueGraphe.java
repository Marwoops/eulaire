import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public abstract class VueGraphe extends JComponent{

	private LinkedList<Sommet> sommets;
	private LinkedList<Point> coordonnees;

	private Color COULEUR = Color.BLUE;
	public int DIAMETRE = 15;

	public VueGraphe(Color c, int d, MouseListener controleur){
		setPreferredSize(new Dimension(800,800));
		COULEUR = c;
		DIAMETRE = d;
		sommets = new LinkedList<Sommet>();
		coordonnees = new LinkedList<Point>();
		addMouseListener(controleur);
	}


	public LinkedList<Point> getCoordonnees(){
		return coordonnees;
	}

	public LinkedList<Sommet> getSommets(){
		return sommets;
	}

	public void ajouteSommet(Sommet s){
		sommets.add(s);
	}

	public void ajouteCoordonnees(Point p){
		coordonnees.add(p);
	}

	public Color getCouleur(){
		return COULEUR;
	}

	public int getDiametre(){
		return DIAMETRE;
	}	

	public int getId(int x, int y){
		for(int i = 0;i<coordonnees.size();i++){
			if( (x <= coordonnees.get(i).getX() + 3*DIAMETRE/2) && (x >= coordonnees.get(i).getX() - DIAMETRE/2) && (y <= coordonnees.get(i).getY() + 3*DIAMETRE/2) && (y >= coordonnees.get(i).getY() - DIAMETRE/2) ){
				return i;
			}
		}
		return -1;
	}

	


	

}

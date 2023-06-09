import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Classement extends JComponent {

	private String packname;
	private LinkedList<Double> scores = new LinkedList<Double>();
	private LinkedList<String> noms = new LinkedList<String>();
	private Font font;

	public Classement(String packname, Font font) {
		this.font = font;
		this.packname = packname;
		deserialise(loadClassement(packname));
		setPreferredSize(new Dimension(300, 500));
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public void changePack(String nom) {
		packname = nom;
		scores.clear();
		noms.clear();
		deserialise(loadClassement(packname));
		repaint();
	}

	public String getNom() {
		return packname;
	}

	public String serialise() {
		//transforme noms et scores en string au "format" mizair
		String res = "";
		if (scores.size() == 0) {
			return res;
		}
		for (int i = 0; i < scores.size(); ++i) {
			res += noms.get(i) + " " + scores.get(i) + "\n";
		}
		return res;
	}

	public void exporter(String nom, String classement) {
		//exporte classement (au format mizair) dans ../classement/nom.mizair
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("../classements/" + nom + ".mizair"));
			writer.write(serialise());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<String> loadClassement(String name) {
		//retourne une liste des lignes du fichier name.mizair
		LinkedList<String> res = new LinkedList<String>();
		String tmp;
		try {
			File f = new File("../classements/" + name + ".mizair");
			f.createNewFile();
			BufferedReader reader =
				new BufferedReader(new FileReader(f));
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

	public void deserialise(LinkedList<String> classement) {
		//depuis une liste au format de celle retournée par loadClassement, initialise noms et scores
		String[] tmp;
		for (int i = 0; i < classement.size(); ++i) {
			tmp = classement.get(i).split(" ");
			if (tmp.length > 1) {
				noms.add(tmp[0]);
				scores.add(Double.parseDouble(tmp[1]));
			}
		}
	}

	public void ajouteScore(String nom, Double score) {
		for (int i = 0; i < scores.size(); ++i) {
			if (score < scores.get(i)) {
				scores.add(i, score);
				noms.add(i, nom);
				return;
			}
		}
		scores.add(score);
		noms.add(nom);
	}

	public void paintComponent(Graphics g) {
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 400, 500);
		g.setColor(Color.BLACK);
		for (int i = 0; i < noms.size(); ++i) {
			g.drawString(noms.get(i) + " : ", 10, 25 * (i + 1));
			g.drawString(scores.get(i) + "", 200, 25 * (i + 1));
		}
	}


}

import java.awt.*;
import javax.swing.*;

class AideEditeur extends JTextArea {

	public AideEditeur() {
		super();
		setEditable(false);
		setOpaque(true);
		setBackground(Color.white);
		append("Pour poser un sommet il suffit de cliquer la où vous voulez\n");
		append(
			"Pour placer une arete cliquez sur le sommet de départ puis le sommet d'arrivée, ensuite si vous cliquez sur de nouveaux sommets, c'est le dernier cliqué qui lui sera relié");
	}

}

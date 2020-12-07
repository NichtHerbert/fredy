import javax.swing.JScrollPane;

import gui.Hauptfenster;
import gui.JPanelEditor;
import gui.toolbar.JtbWerkzeugleiste;
import verwaltung.ZentraleVerschraenkung;
import wfnmodell.WFNModell;

/**
 * Die Klasse mit der main-Methode, von der aus der Workflownetz-Editor gestartet wird. 
 *
 */
public class Start {

	/**
	 * Erzeugt Modell, Toolbar, EditorPanel (in einem Scrollpane) 
	 * und übergibt sie einem neu erzeugtem Hauptfenster 
	 * und der neue erzeugten Zentralen-Verschränkung.
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		WFNModell modell = new WFNModell();
		JPanelEditor editor = new JPanelEditor();
		JScrollPane sp = new JScrollPane(editor);
		JtbWerkzeugleiste toolbar = new JtbWerkzeugleiste();
		ZentraleVerschraenkung zV = new ZentraleVerschraenkung(modell, editor, toolbar);
		new Hauptfenster(sp, toolbar, zV);

	}

}

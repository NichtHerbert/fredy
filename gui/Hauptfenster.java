package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import control.ControlCentral;

/**
 * Das Hauptfenster des Workflownetz-Editors.
 * Es besteht aus einem Panel, in dem das Workflownetz dargestellt wird,
 * und einer Toolbar. 
 */
public class Hauptfenster extends JFrame {

	private static final long serialVersionUID = -7063560430530422635L;

	/**
	 * Initialisiert das Fenster und setzt die Toolbar an die rechte Seite.
	 * @param editorBereich Komponente mit der das WFN dargestellt werden soll
	 * @param toolbarBereich Komponente mit Werkzeugen zur Veränderung des WFN und/oder seiner Darstellung
	 * @param zV die Mutter aller Steuerungsklassen
	 */
	public Hauptfenster(JComponent editorBereich, JComponent toolbarBereich, ControlCentral zV) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				zV.programmBeenden();
			}
		});
		add(editorBereich, BorderLayout.CENTER);
		add(toolbarBereich, BorderLayout.EAST);
		setPreferredSize(new Dimension(800, 600));
		setTitle("***REMOVED***");
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}

package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import control.ControlCentral;

/**
 * Das MainWindow des Workflownetz-Editors.
 * Es besteht aus einem Panel, in dem das Workflownetz dargestellt wird,
 * und einer Toolbar. 
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = -7063560430530422635L;
	
	private final String windowTitle = "fredy - the Workflow-Net-Editor";

	/**
	 * Initialisiert das Fenster und setzt die Toolbar an die rechte Seite.
	 * @param editorArea Komponente mit der das WFN dargestellt werden soll
	 * @param toolbarArea Komponente mit Werkzeugen zur Veränderung des WFN und/oder seiner Darstellung
	 * @param control die Mutter aller Steuerungsklassen
	 */
	public MainWindow(JComponent editorArea, JComponent toolbarArea, ControlCentral control) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				control.exitProgram();
			}
		});
		add(editorArea, BorderLayout.CENTER);
		add(toolbarArea, BorderLayout.EAST);
		setPreferredSize(new Dimension(800, 600));
		setTitle(windowTitle);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}

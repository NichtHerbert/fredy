import javax.swing.JScrollPane;

import control.ControlCentral;
import gui.MainWindow;
import gui.EditorPanel;
import gui.toolbar.JtbWerkzeugleiste;
import wfnmodel.WfnModel;

/**
 * Die Klasse mit der main-Methode, von der aus der Workflownetz-Editor gestartet wird. 
 *
 */
public class Start {

	/**
	 * Erzeugt Modell, Toolbar, EditorPanel (in einem Scrollpane) 
	 * und übergibt sie einem neu erzeugtem MainWindow 
	 * und der neue erzeugten Zentralen-Verschränkung.
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		WfnModel modell = new WfnModel();
		EditorPanel editor = new EditorPanel();
		JScrollPane sp = new JScrollPane(editor);
		JtbWerkzeugleiste toolbar = new JtbWerkzeugleiste();
		ControlCentral zV = new ControlCentral(modell, editor, toolbar);
		new MainWindow(sp, toolbar, zV);

	}

}

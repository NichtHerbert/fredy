import javax.swing.JScrollPane;

import control.ControlCentral;
import gui.MainWindow;
import gui.EditorPanel;
import gui.toolbar.JtbToolBar;
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
		WfnModel model = new WfnModel();
		EditorPanel editor = new EditorPanel();
		JScrollPane scrollPane = new JScrollPane(editor);
		JtbToolBar toolbar = new JtbToolBar();
		ControlCentral control = new ControlCentral(model, editor, toolbar);
		new MainWindow(scrollPane, toolbar, control);

	}

}

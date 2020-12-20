package gui.toolbar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import gui.EIcons;
import gui.EWfnEditModus;
import listeners.ISelectionEditingListener;
import listeners.ISelectionChangingListener;
import listeners.IEditModusListener;
import wfnmodel.interfaces.IWfnElement;

/**
 * Panel, in welchem alles versammelt ist, was mit der Bearbeitung einzelner bzw. ausgewählter
 * Elemente des WFN zu tun hat. Zur Bearbeitung gehören Hinzufügen, Löschen, Namensänderung.
 * Hinzufügen wird realisiert duch das Setzen des @see {@link gui.EWfnEditModus}.
 * Löschen und Namensänderung durch ein @see {@link JPanelSelection}.
 */
class JPanelElementHandling extends JPanel implements ISelectionChangingListener{
	
	private static final long serialVersionUID = 3117004156020700423L;
	
	/** Zur Realisierung von Löschen und Namensänderung. */
	private JPanelSelection jpSelectionInfo;
	/** Der aktuelle Editormodus. */
	private EWfnEditModus modus;
	/** Liste der Horcher, die über eine Änderung des Editormodus informiert werden wollen. */
	private ArrayList<IEditModusListener> editorModusListeners;

	/**
	 * Initialisiert das JPanel mit einem {@link JPanelSelection} und 4 {@link JToggleButton}
	 * zur Setzung des {@link EWfnEditModus}. 
	 */
	JPanelElementHandling() {
		modus = EWfnEditModus.SELECT;
		editorModusListeners = new ArrayList<>(2);
		
		JToggleButton jtbSelect = new JToggleButton(EIcons.CURSOR.getIcon(), true);
		JToggleButton jtbAddPlace = new JToggleButton(EIcons.PLACE.getIcon());
		JToggleButton jtbAddTransition = new JToggleButton(EIcons.TRANSITION.getIcon());
		JToggleButton jtbAddArc = new JToggleButton(EIcons.ARC.getIcon());
		
		jtbSelect.setToolTipText("Select Elements");
		jtbAddPlace.setToolTipText("Add Places");
		jtbAddTransition.setToolTipText("Add Transitions");
		jtbAddArc.setToolTipText("Add Arcs");

		JPanel jpModusSelect = new JPanel();
		ButtonGroup btg = new ButtonGroup();

		btg.add(jtbSelect);
		jpModusSelect.add(jtbSelect);
		btg.add(jtbAddPlace);
		jpModusSelect.add(jtbAddPlace);
		btg.add(jtbAddTransition);
		jpModusSelect.add(jtbAddTransition);
		btg.add(jtbAddArc);
		jpModusSelect.add(jtbAddArc);

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == jtbSelect)
					modus = EWfnEditModus.SELECT;
				if (e.getSource() == jtbAddPlace)
					modus = EWfnEditModus.ADD_PLACE;
				if (e.getSource() == jtbAddTransition)
					modus = EWfnEditModus.ADD_TRANSITION;
				if (e.getSource() == jtbAddArc)
					modus = EWfnEditModus.ADD_ARC;
				
				fireEditorModusChanged(modus);
				
			}
		};

		jtbSelect.addActionListener(al);
		jtbAddPlace.addActionListener(al);
		jtbAddTransition.addActionListener(al);
		jtbAddArc.addActionListener(al);
		
		
		jpSelectionInfo = new JPanelSelection();
		this.setLayout(new BorderLayout());
		
		add(jpSelectionInfo, BorderLayout.CENTER);
		add(jpModusSelect, BorderLayout.SOUTH);
		setBorder(new TitledBorder("Element Handling"));
	}

	/**
	 * Informiert alle in {@link #editorModusListeners} gespeicherten Horcher 
	 * über eine Modus-Änderung. 
	 * @param modus Neuer Editormodus, der an alle Horcher weitergeleitet wird.
	 */
	private void fireEditorModusChanged(EWfnEditModus modus) {
		for (IEditModusListener listener : editorModusListeners)
			listener.editModusChanged(modus);
	}
	
	/**
	 * Fügt der {@link #editorModusListeners} einen Horcher hinzu.
	 * @param listener Wird der @see {@link #editorModusListeners} hinzugefügt.
	 */
	void addEditorModusListener(IEditModusListener listener) {
		editorModusListeners.add(listener);
	}

	/**
	 * Löscht einen Horcher aus der {@link #editorModusListeners}.
	 * @param listener Wird von der {@link #editorModusListeners} entfernt.
	 */
	void removeEditorModusListener(IEditModusListener listener) {
		if (editorModusListeners.contains(listener)) 
			editorModusListeners.remove(listener);
	}

	@Override
	public void selectionChangeOccurred(int auswahlArt, ArrayList<? extends IWfnElement> ausgewaehlteElemente) {
		jpSelectionInfo.selectionChangeOccurred(auswahlArt, ausgewaehlteElemente);	
	}

	/**
	 * @param listener Wird an das {@link JPanelSelection} 
	 * {@link JPanelElementHandling#jpSelectionInfo} weitergeleitet. 
	 */
	void addSelectionEditingListener(ISelectionEditingListener listener) {
		jpSelectionInfo.addSelectionEditingListener(listener);
	}

}

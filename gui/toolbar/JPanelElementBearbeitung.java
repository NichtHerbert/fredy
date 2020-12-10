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
import gui.EWFNEditorModus;
import horcherschnittstellen.IAuswahlBearbeitetHorcher;
import horcherschnittstellen.IAuswahlVeraenderungsHorcher;
import horcherschnittstellen.IEditorModusHorcher;
import wfnmodel.interfaces.IWfnElement;

/**
 * Panel, in welchem alles versammelt ist, was mit der Bearbeitung einzelner bzw. ausgewählter
 * Elemente des WFN zu tun hat. Zur Bearbeitung gehören Hinzufügen, Löschen, Namensänderung.
 * Hinzufügen wird realisiert duch das Setzen des @see {@link gui.EWFNEditorModus}.
 * Löschen und Namensänderung durch ein @see {@link JPanelAuswahl}.
 */
class JPanelElementBearbeitung extends JPanel implements IAuswahlVeraenderungsHorcher{
	
	private static final long serialVersionUID = 3117004156020700423L;
	
	/**
	 * Zur Realisierung von Löschen und Namensänderung.
	 */
	private JPanelAuswahl jpAuswahlInfo;
	/**
	 * Der aktuelle Editormodus. 
	 */
	private EWFNEditorModus modus;
	/**
	 * Liste der Horcher, die über eine Änderung des Editormodus informiert werden wollen.
	 */
	private ArrayList<IEditorModusHorcher> editorModusHorcherListe;

	/**
	 * Initialisiert das JPanel mit einem {@link JPanelAuswahl} und 4 {@link JToggleButton}
	 * zur Setzung des {@link EWFNEditorModus}. 
	 */
	JPanelElementBearbeitung() {
		modus = EWFNEditorModus.AUSWAHL;
		editorModusHorcherListe = new ArrayList<>(2);
		
		JToggleButton jtb_Auswahl = new JToggleButton(EIcons.CURSOR.getIcon(), true);
		JToggleButton jtb_Stelle = new JToggleButton(EIcons.STELLE.getIcon());
		JToggleButton jtb_Transition = new JToggleButton(EIcons.TRANSITION.getIcon());
		JToggleButton jtb_Kante = new JToggleButton(EIcons.KANTE.getIcon());
		
		jtb_Auswahl.setToolTipText("Elemente auswählen");
		jtb_Stelle.setToolTipText("Stelle hinzufügen");
		jtb_Transition.setToolTipText("Transition hinzufügen");
		jtb_Kante.setToolTipText("Kante hinzufügen");

		JPanel jp_modusAuswahl = new JPanel();
		ButtonGroup btg = new ButtonGroup();

		btg.add(jtb_Auswahl);
		jp_modusAuswahl.add(jtb_Auswahl);
		btg.add(jtb_Stelle);
		jp_modusAuswahl.add(jtb_Stelle);
		btg.add(jtb_Transition);
		jp_modusAuswahl.add(jtb_Transition);
		btg.add(jtb_Kante);
		jp_modusAuswahl.add(jtb_Kante);

		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == jtb_Auswahl)
					modus = EWFNEditorModus.AUSWAHL;
				if (e.getSource() == jtb_Stelle)
					modus = EWFNEditorModus.STELLE_HINZU;
				if (e.getSource() == jtb_Transition)
					modus = EWFNEditorModus.TRANSITION_HINZU;
				if (e.getSource() == jtb_Kante)
					modus = EWFNEditorModus.KANTE_HINZU;
				
				fireEditorModusGeaendert(modus);
				
			}
		};

		jtb_Auswahl.addActionListener(al);
		jtb_Stelle.addActionListener(al);
		jtb_Transition.addActionListener(al);
		jtb_Kante.addActionListener(al);
		
		
		jpAuswahlInfo = new JPanelAuswahl();
		this.setLayout(new BorderLayout());
		
		add(jpAuswahlInfo, BorderLayout.CENTER);
		add(jp_modusAuswahl, BorderLayout.SOUTH);
		setBorder(new TitledBorder("Element Bearbeitung"));
	}

	/**
	 * Informiert alle in {@link #editorModusHorcherListe} gespeicherten Horcher 
	 * über eine Modus-Änderung. 
	 * @param modus Neuer Editormodus, der an alle Horcher weitergeleitet wird.
	 */
	private void fireEditorModusGeaendert(EWFNEditorModus modus) {
		for (IEditorModusHorcher horcher : editorModusHorcherListe)
			horcher.editorModusGeaendert(modus);
	}
	
	/**
	 * Fügt der {@link #editorModusHorcherListe} einen Horcher hinzu.
	 * @param horcher Wird der @see {@link #editorModusHorcherListe} hinzugefügt.
	 */
	void addEditorModusHorcher(IEditorModusHorcher horcher) {
		editorModusHorcherListe.add(horcher);
	}

	/**
	 * Löscht einen Horcher aus der {@link #editorModusHorcherListe}.
	 * @param horcher Wird von der {@link #editorModusHorcherListe} entfernt.
	 */
	void removeEditorModusHorcher(IEditorModusHorcher horcher) {
		if (editorModusHorcherListe.contains(horcher)) 
			editorModusHorcherListe.remove(horcher);
	}

	@Override
	public void auswahlAenderungEingetreten(int auswahlArt, ArrayList<? extends IWfnElement> ausgewaehlteElemente) {
		jpAuswahlInfo.auswahlAenderungEingetreten(auswahlArt, ausgewaehlteElemente);	
	}

	/**
	 * @param horcher Wird an das {@link JPanelAuswahl} 
	 * {@link JPanelElementBearbeitung#jpAuswahlInfo} weitergeleitet. 
	 */
	void addAuswahlBearbeitetHorcher(IAuswahlBearbeitetHorcher horcher) {
		jpAuswahlInfo.addAuswahlBearbeitetHorcher(horcher);
	}

}

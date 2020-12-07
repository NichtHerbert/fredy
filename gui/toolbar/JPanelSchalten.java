package gui.toolbar;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import gui.EIcons;
import horcherschnittstellen.ITransitionsSchaltungsHorcher;
import horcherschnittstellen.IWFNModellStatusHorcher;
import wfnmodell.WFNStatusInfo;
import wfnmodell.elemente.EWFNElement;
import wfnmodell.elemente.WFNElementTransition;
import wfnmodell.schnittstellen.IWFNElementTransition;

/**
 * Panel zuständig zur Anzeige der Steuerung des Schaltens von Transitionen und des Zurücksetzens
 * aller Marken auf die Ausgangsposition. 
 */
class JPanelSchalten extends JPanel implements IWFNModellStatusHorcher {

	private static final long serialVersionUID = 424304809500193863L;
	
	/**
	 * Liste der Horcher, die über das Schalten oder Zurücksetzen informiert werden möchten.
	 */
	private ArrayList<ITransitionsSchaltungsHorcher> transSchaltHorcherListe;
	
	/**
	 * Der letztübermittelte Zustand/Status des Workflownetzes.
	 */
	private WFNStatusInfo statusInfo;
	
	/**
	 * Leeres Füllelement mit der {@link #jcbModell} gefüllt wird, wenn es ansonsten leer wäre, damit
	 * {@link #jcbAktivierteTransitionen} seine Form behält.
	 */
	private WFNElementTransition leeresElement;
	
	/**
	 * ComboBox, welche alle momentan aktivierten Transitionen anzeigt.
	 */
	private JComboBox<IWFNElementTransition> jcbAktivierteTransitionen;
	
	/**
	 * Datenmodell für {@link #jcbAktivierteTransitionen}.
	 */
	private DefaultComboBoxModel<IWFNElementTransition> jcbModell;
	
	
	/**
	 * Initialisiert das Panel mit nebeneinander einem Button zum Zurücksetzen,
	 * eine ComboBox zur Auswahl der zu schaltenden Transition und einem Butten zum Schalten.
	 */
	JPanelSchalten() {
		transSchaltHorcherListe = new ArrayList<>(2);
		statusInfo = new WFNStatusInfo();
		leeresElement = new WFNElementTransition("", 0, new Point(0, 0));
		leeresElement.setName(" ");
		
		JButton jbAufStart = new JButton(EIcons.AUF_START.getIcon());
		jbAufStart.setToolTipText("Zurück auf Start");
		jbAufStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireZurueckAufStart();
			}
		});
		
		JButton jbSchalten = new JButton(EIcons.PLAY.getIcon());
		jbSchalten.setToolTipText("Schalten");
		jbSchalten.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireSchalteTransition();
			}
		});
		
		jcbAktivierteTransitionen = new JComboBox<>();
		jcbAktivierteTransitionen.setPreferredSize(new Dimension(
				8*EWFNElement.URGROESSE, jcbAktivierteTransitionen.getPreferredSize().height));
		jcbModell = (DefaultComboBoxModel<IWFNElementTransition>) jcbAktivierteTransitionen.getModel();
		aktualisiereComboBoxModell();
		
		add(jbAufStart);
		add(jcbAktivierteTransitionen);
		add(jbSchalten);
		setBorder(new TitledBorder("Transition Schalten"));
	}

	/**
	 * Aktualisiert {@link #jcbModell} mit Daten von {@link #statusInfo}.
	 * Gibt es keine aktivierten Transitionen, fügt es stattdessen {@link #leeresElement} ein.
	 */
	private void aktualisiereComboBoxModell() {
		jcbModell.removeAllElements();
		if ((statusInfo.getAktivierteTransitionen() != null)
				&& ( !statusInfo.getAktivierteTransitionen().isEmpty())) 
			for (IWFNElementTransition transition : statusInfo.getAktivierteTransitionen())
				jcbModell.addElement(transition);
		else 
			jcbModell.addElement(leeresElement);
	}
	
	/**
	 * Informiert alle in {@link #transSchaltHorcherListe} gespeicherten Horcher, dass die 
	 * Marken zum Schalten auf die Anfangsposition zurückgesetzt werden sollen. 
	 */
	private void fireZurueckAufStart() {
		for (ITransitionsSchaltungsHorcher horcher : transSchaltHorcherListe)
			horcher.allesZurueckAufStart();
	}
	
	/**
	 * Informiert alle in {@link #transSchaltHorcherListe} gespeicherten Horcher, dass die 
	 * momentan in {@link #jcbModell} ausgewählte Transition geschaltet werden soll. 
	 */
	private void fireSchalteTransition() {
		for (ITransitionsSchaltungsHorcher horcher : transSchaltHorcherListe)
			horcher.schalteTransition((IWFNElementTransition) jcbModell.getSelectedItem());
	}

	/**
	 * Fügt der {@link #transSchaltHorcherListe} einen Horcher hinzu.
	 * @param horcher Wird der {@link #transSchaltHorcherListe} hinzugefügt.
	 */
	void addTransitionsSchaltungsHorcher(ITransitionsSchaltungsHorcher horcher) {
		transSchaltHorcherListe.add(horcher);
	}

	@Override
	public void modellStatusAenderung(WFNStatusInfo statusInfo) {
		if (statusInfo.getAktivierteTransitionen() != null) {
			this.statusInfo = statusInfo;
			aktualisiereComboBoxModell();
		}
		
	}
}

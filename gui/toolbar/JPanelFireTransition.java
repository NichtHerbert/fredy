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
import listeners.ITransitionFireListener;
import listeners.IWfnStatusListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.elements.EWfnElement;
import wfnmodel.elements.WfnElementTransition;
import wfnmodel.interfaces.IWfnTransition;

/**
 * Panel zuständig zur Anzeige der Steuerung des Schaltens von Transitionen und des Zurücksetzens
 * aller Marken auf die Ausgangsposition. 
 */
class JPanelFireTransition extends JPanel implements IWfnStatusListener {

	private static final long serialVersionUID = 424304809500193863L;
	
	/** Liste der Horcher, die über das Schalten oder Zurücksetzen informiert werden möchten. */
	private ArrayList<ITransitionFireListener> transitionFireListeners;
	/** Der letztübermittelte Zustand/Status des Workflownetzes. */
	private WfnStatusInfo statusInfo;
	/** Leeres Füllelement mit der {@link #jcbModel} gefüllt wird, wenn es ansonsten leer wäre, damit
	 * {@link #jcbEnabledTransitions} seine Form behält. */
	private WfnElementTransition emptyElem;
	/** ComboBox, welche alle momentan aktivierten Transitionen anzeigt.*/
	private JComboBox<IWfnTransition> jcbEnabledTransitions;
	/** Datenmodell für {@link #jcbEnabledTransitions}. */
	private DefaultComboBoxModel<IWfnTransition> jcbModel;
	
	
	/**
	 * Initialisiert das Panel mit nebeneinander einem Button zum Zurücksetzen,
	 * eine ComboBox zur Auswahl der zu schaltenden Transition und einem Butten zum Schalten.
	 */
	JPanelFireTransition() {
		transitionFireListeners = new ArrayList<>(2);
		statusInfo = new WfnStatusInfo();
		emptyElem = new WfnElementTransition("", 0, new Point(0, 0));
		emptyElem.setName(" ");
		
		JButton jbBackToStart = new JButton(EIcons.AUF_START.getIcon());
		jbBackToStart.setToolTipText("Back To Start");
		jbBackToStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEverythingBackToStart();
			}
		});
		
		JButton jbFire = new JButton(EIcons.PLAY.getIcon());
		jbFire.setToolTipText("Fire");
		jbFire.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireFireTransition();
			}
		});
		
		jcbEnabledTransitions = new JComboBox<>();
		jcbEnabledTransitions.setPreferredSize(new Dimension(
				8*EWfnElement.BASEFACTOR, jcbEnabledTransitions.getPreferredSize().height));
		jcbModel = (DefaultComboBoxModel<IWfnTransition>) jcbEnabledTransitions.getModel();
		updateComboBoxModell();
		
		add(jbBackToStart);
		add(jcbEnabledTransitions);
		add(jbFire);
		setBorder(new TitledBorder("Fire Transition"));
	}

	/**
	 * Aktualisiert {@link #jcbModel} mit Daten von {@link #statusInfo}.
	 * Gibt es keine aktivierten Transitionen, fügt es stattdessen {@link #emptyElem} ein.
	 */
	private void updateComboBoxModell() {
		jcbModel.removeAllElements();
		if ((statusInfo.getEnabledTransitions() != null)
				&& ( !statusInfo.getEnabledTransitions().isEmpty())) 
			for (IWfnTransition transition : statusInfo.getEnabledTransitions())
				jcbModel.addElement(transition);
		else 
			jcbModel.addElement(emptyElem);
	}
	
	/**
	 * Informiert alle in {@link #transitionFireListeners} gespeicherten Horcher, dass die 
	 * Marken zum Schalten auf die Anfangsposition zurückgesetzt werden sollen. 
	 */
	private void fireEverythingBackToStart() {
		for (ITransitionFireListener listener : transitionFireListeners)
			listener.everythingBackToStart();
	}
	
	/**
	 * Informiert alle in {@link #transitionFireListeners} gespeicherten Horcher, dass die 
	 * momentan in {@link #jcbModel} ausgewählte Transition geschaltet werden soll. 
	 */
	private void fireFireTransition() {
		for (ITransitionFireListener listener : transitionFireListeners)
			listener.fireTransition((IWfnTransition) jcbModel.getSelectedItem());
	}

	/**
	 * Fügt der {@link #transitionFireListeners} einen Horcher hinzu.
	 * @param listener Wird der {@link #transitionFireListeners} hinzugefügt.
	 */
	void addTransitionFireListener(ITransitionFireListener listener) {
		transitionFireListeners.add(listener);
	}

	@Override
	public void newWfnStatus(WfnStatusInfo statusInfo) {
		if (statusInfo.getEnabledTransitions() != null) {
			this.statusInfo = statusInfo;
			updateComboBoxModell();
		}
		
	}
}

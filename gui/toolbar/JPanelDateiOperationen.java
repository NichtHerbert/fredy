package gui.toolbar;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import gui.EIcons;
import horcherschnittstellen.IWFNModellStatusHorcher;
import verwaltung.DateiVerwaltung;
import verwaltung.IDateiVerwaltung;
import wfnmodel.WfnStatusInfo;

/**
 * JPanel zur Anzeige des Dateinamens und 4 nebeneinanderliegenden Buttons. 
 * Die Buttons sollen zum Öffnen, Speichern(-Unter) und Leeren 
 * des Datenmodells führen.
 */
class JPanelDateiOperationen extends JPanel implements IWFNModellStatusHorcher {

	private static final long serialVersionUID = -4237254650736397557L;
	
	/**
	 * Referenz auf die für Dateioperationen zuständige Verwaltung.
	 */
	private IDateiVerwaltung dialogVerwaltung;

	/**
	 * JPanel, in welchem der Dateiname angezeigt wird.
	 */
	private JPanel jpDateiName;
	
	/**
	 * JPanel, in welchem die JButtons zum Leeren, Öffnen, Speichern und Speichern-Unter,
	 * nebeneinander angeordnet, befinden.
	 */
	private JPanel jpDateiButtons;
	
	/**
	 * Zeigt den Dateinamen, sitzt im @see {@link #jpDateiName}.
	 */
	private JLabel jlDateiName;
	
	JPanelDateiOperationen() {
		this(null);
	}
	
	JPanelDateiOperationen(IDateiVerwaltung dateiDialogVerwaltung) {
		this.dialogVerwaltung = dateiDialogVerwaltung;
		jpDateiName = new JPanel();
		jpDateiName.setLayout(new GridLayout(0, 1));
		jlDateiName = new JLabel("Dateiname:   unbenannt1.pnml");
		jpDateiName.add(jlDateiName);
		
		JButton jb_neu = new JButton(EIcons.NEU.getIcon());
		JButton jb_öffnen = new JButton(EIcons.OEFFNEN.getIcon());
		JButton jb_speichern = new JButton(EIcons.SPEICHERN.getIcon());
		JButton jb_speichern_unter = new JButton(EIcons.SPEICHERN_UNTER.getIcon());
		
		jb_neu.setToolTipText("Neu");
		jb_öffnen.setToolTipText("Datei öffnen");
		jb_speichern.setToolTipText("Datei speichern");
		jb_speichern_unter.setToolTipText("Datei speichern unter");
		
		jb_neu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dialogVerwaltung.dateiNeu();
					jlDateiName.setText("Dateiname:   " + dialogVerwaltung.getDateiName());
				} catch (NullPointerException e1) {}
			}
		});
		jb_öffnen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dialogVerwaltung.dateiOeffnen(JPanelDateiOperationen.this);
					jlDateiName.setText("Dateiname:   " + dialogVerwaltung.getDateiName());
				} catch (NullPointerException e1) {}
			}
		});
		jb_speichern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (dialogVerwaltung.dateiSpeichern(JPanelDateiOperationen.this))
						jlDateiName.setText("Dateiname:   " + dialogVerwaltung.getDateiName());
				} catch (NullPointerException e1) {}
			}
		});
		jb_speichern_unter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (dialogVerwaltung.dateiSpeichernUnter(JPanelDateiOperationen.this))
						jlDateiName.setText("Dateiname:   " + dialogVerwaltung.getDateiName());
				} catch (NullPointerException e1) {}
			}
		});
		
		jpDateiButtons = new JPanel();
		jpDateiButtons.add(jb_neu);
		jpDateiButtons.add(jb_öffnen);
		jpDateiButtons.add(jb_speichern);
		jpDateiButtons.add(jb_speichern_unter);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(jpDateiName);
		add(jpDateiButtons);
		setBorder(new TitledBorder("Datei Operationen"));
	}

	/**
	 * Setzt das Attribut {@link #dialogVerwaltung}.
	 * @param dateiVerwaltung zu setzende DateiVerwaltung
	 */
	void setDateiVerwaltung(DateiVerwaltung dateiVerwaltung) {
		dialogVerwaltung = dateiVerwaltung;
	}

	@Override
	public void modellStatusAenderung(WfnStatusInfo statusInfo) {
		jlDateiName.setText("Dateiname:   " + dialogVerwaltung.getDateiName());
	}


}

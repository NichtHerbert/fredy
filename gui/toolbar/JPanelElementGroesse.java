package gui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import gui.EIcons;
import verwaltung.ElementGroessenVerwaltung;

/**
 * Panel zur Anzeige der Buttons zur Steuerung der Elementgröße.
 *
 */
public class JPanelElementGroesse extends JPanel {

	private static final long serialVersionUID = 9106561939176271978L;
	
	/**
	 * Referenz auf die für den Zoom zuständige Verwaltung. 
	 */
	private ElementGroessenVerwaltung eGVerwaltung;
	
	/**
	 * Initialisiert das Panel mit zwei nebeneinanderliegenden Buttons für ZoomIn und ZoomOut.
	 */
	public JPanelElementGroesse() {
	 
		super();
		JButton btnEGroesser = new JButton(EIcons.PLUS.getIcon());
		JButton btnEKleiner = new JButton(EIcons.MINUS.getIcon());
		
		btnEGroesser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eGVerwaltung != null) 
					eGVerwaltung.elementGroesser();
			}
		});
		btnEKleiner.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (eGVerwaltung != null) 
					eGVerwaltung.elementKleiner();
			}
		});
		
		add(btnEGroesser);
		add(btnEKleiner);
		setBorder(new TitledBorder("Element-Größe"));
	}
	
	/**
	 * Setzt das Attribut {@link #ElementGroessenVerwaltung}.
	 * @param zoomVerwaltung die neue {@link #ElementGroessenVerwaltung}
	 */
	void setElementGroessenVerwaltung(ElementGroessenVerwaltung eGVerwaltung) {
		this.eGVerwaltung = eGVerwaltung;
	}
	
}

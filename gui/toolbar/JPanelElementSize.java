package gui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import control.ElementSizeManagement;
import gui.EIcons;

/**
 * Panel zur Anzeige der Buttons zur Steuerung der Elementgröße.
 *
 */
public class JPanelElementSize extends JPanel {

	private static final long serialVersionUID = 9106561939176271978L;
	
	/** Referenz auf die für den Zoom zuständige Verwaltung. */
	private ElementSizeManagement elementSizeManagement;
	
	/**
	 * Initialisiert das Panel mit zwei nebeneinanderliegenden Buttons für ZoomIn und ZoomOut.
	 */
	public JPanelElementSize() {
	 
		super();
		JButton btnElemBigger = new JButton(EIcons.PLUS.getIcon());
		JButton btnElemSmaller = new JButton(EIcons.MINUS.getIcon());
		
		btnElemBigger.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (elementSizeManagement != null) 
					elementSizeManagement.bigger();
			}
		});
		btnElemSmaller.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (elementSizeManagement != null) 
					elementSizeManagement.smaller();
			}
		});
		
		add(btnElemBigger);
		add(btnElemSmaller);
		setBorder(new TitledBorder("Element Size"));
	}
	
	/**
	 * Setzt das Attribut {@link #ElementSizeManagement}.
	 * @param zoomVerwaltung die neue {@link #ElementSizeManagement}
	 */
	void setElementSizeManagement(ElementSizeManagement elementSizeManagement) {
		this.elementSizeManagement = elementSizeManagement;
	}
	
}

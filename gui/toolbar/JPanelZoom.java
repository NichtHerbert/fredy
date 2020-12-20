package gui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import control.ZoomManagement;
import gui.EIcons;

/**
 * Panel zur Anzeige der Zoom-Steuer-Instrumente.
 */
class JPanelZoom extends JPanel {
	
	private static final long serialVersionUID = 4118112168211344115L;
	
	/** Referenz auf die für den Zoom zuständige Verwaltung. */
	private ZoomManagement zoomManagement;
	
	
	/** Initialisiert das Panel mit zwei nebeneinanderliegenden Buttons für ZoomIn und ZoomOut. */
	JPanelZoom() {
		super();
		JButton btnZoomIn = new JButton(EIcons.ZOOM_IN.getIcon());
		JButton btnZoomOut = new JButton(EIcons.ZOOM_OUT.getIcon());
		
		btnZoomIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (zoomManagement != null) 
					zoomManagement.zoomIn();
			}
		});
		btnZoomOut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (zoomManagement != null) 
					zoomManagement.zoomOut();
			}
		});
		
		add(btnZoomIn);
		add(btnZoomOut);
		setBorder(new TitledBorder("Zoom In - Out"));
	}
	
	/**
	 * Setzt das Attribut {@link #zoomManagement}.
	 * @param zoomManagement die neue {@link #zoomManagement}
	 */
	void setZoomManagement(ZoomManagement zoomVerwaltung) {
		this.zoomManagement = zoomVerwaltung;
	}
	
}

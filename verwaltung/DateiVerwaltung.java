package verwaltung;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.IZentraleKonstanten;
import wfnmodel.importexport.ExportManagement;
import wfnmodel.importexport.IWfnExport;
import wfnmodel.importexport.IWfnImport;
import wfnmodel.importexport.ImportManagement;
import wfnmodel.interfaces.IWfnElement;

/**
 * Verwaltung und Steuerung der durch den Benutzer angestoßenen Import- und Exportvorgänge
 *
 */
public class DateiVerwaltung implements IDateiVerwaltung,
										IZentraleKonstanten {
	/**
	 * Import-Schnittstelle zum Datenmodell.
	 */
	private IWfnImport impModell;
	/**
	 * Export-Schnittstelle zum Datenmodell.
	 */
	private IWfnExport expModell;
	/** Die aktuelle AuswahlVerwaltung.*/
	private AuswahlVerwaltung<IWfnElement> auswahlVerwaltung;
	/** zur Übersicht was als dateiname anzuzeigen ist.*/
	private boolean letzteDateiAktionWarDateiNeu;
	
	
	/**
	 * @param impModell Import-Schnittstelle zum Datenmodell
	 * @param expModell Export-Schnittstelle zum Datenmodell
	 * @param auswahlVerwaltung die aktuelle Auswahlverwaltung
	 */
	public DateiVerwaltung(IWfnImport impModell, IWfnExport expModell, AuswahlVerwaltung<IWfnElement> auswahlVerwaltung) {
		super();
		this.impModell = impModell;
		this.expModell = expModell;
		this.auswahlVerwaltung = auswahlVerwaltung;
	}

	@Override
	public void dateiNeu() {
		impModell.clear();
		letzteDateiAktionWarDateiNeu = true;
		auswahlVerwaltung.clearAndFire(NEUE_AUSWAHL);
	}

	@Override
	public void dateiOeffnen(JComponent ausloeser) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Petri-Netze: *.pnml", "pnml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(impModell.getWfnFile());
		int returnVal = chooser.showOpenDialog(ausloeser.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ImportManagement iV = new ImportManagement(chooser.getSelectedFile(), impModell);
			iV.startImport();
			auswahlVerwaltung.clearAndFire(NEUE_AUSWAHL);
		}
		letzteDateiAktionWarDateiNeu = false;
	}

	@Override
	public boolean dateiSpeichern(JComponent ausloeser) {
		if (!impModell.isCurrentWfnSaved()) {
			if (impModell.getWfnFile() == null)
				return dateiSpeichernUnter(ausloeser);
			else {
				(new ExportManagement(impModell.getWfnFile(), expModell)).startExport();
				impModell.setIsCurrentWfnSaved(true);
				letzteDateiAktionWarDateiNeu = false;
			}
		}
		return false;
	}

	@Override
	public boolean dateiSpeichernUnter(JComponent ausloeser) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Petri-Netze: *.pnml", "pnml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(impModell.getWfnFile());
		int returnVal = chooser.showSaveDialog(ausloeser.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File pnml = chooser.getSelectedFile();
			if ( !pnml.getPath().toLowerCase().endsWith(".pnml"))
				pnml = new File(pnml.getPath() + ".pnml");
			if (pnml.exists()) {
				int dateiUeberschreiben = JOptionPane.showConfirmDialog(null,
			            "Die Datei " + pnml.getName() + " existiert bereits.\n"
			            		+ "Soll die Datei überschrieben werden?", 
			            "Datei Überschreiben?", JOptionPane.YES_NO_OPTION, 
			            JOptionPane.QUESTION_MESSAGE);
			    if (dateiUeberschreiben != JOptionPane.YES_OPTION) 
			        return false;
			}
			(new ExportManagement(pnml, expModell)).startExport();
			impModell.setWfnFile(pnml);
			impModell.setIsCurrentWfnSaved(true);
			letzteDateiAktionWarDateiNeu = false;
			return true;
		}
		return false;
	}

	@Override
	public String getDateiName() {
		return (( !letzteDateiAktionWarDateiNeu)
				&& (impModell.getWfnFile() != null)) 
					? impModell.getWfnFile().getName()
					: "unbenannt";
	}

}

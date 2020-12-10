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
public class FileManagement implements IFileManagement,
										IZentraleKonstanten {
	/**
	 * Import-Schnittstelle zum Datenmodell.
	 */
	private IWfnImport impModel;
	/**
	 * Export-Schnittstelle zum Datenmodell.
	 */
	private IWfnExport expModel;
	/** Die aktuelle AuswahlVerwaltung.*/
	private AuswahlVerwaltung<IWfnElement> selectionManagement;
	/** zur Übersicht was als dateiname anzuzeigen ist.*/
	private boolean isFileNew;
	
	
	/**
	 * @param impModel Import-Schnittstelle zum Datenmodell
	 * @param expModel Export-Schnittstelle zum Datenmodell
	 * @param selectionManagement die aktuelle Auswahlverwaltung
	 */
	public FileManagement(IWfnImport impModel, IWfnExport expModel, AuswahlVerwaltung<IWfnElement> selectionManagement) {
		super();
		this.impModel = impModel;
		this.expModel = expModel;
		this.selectionManagement = selectionManagement;
	}

	@Override
	public void fileNew() {
		impModel.clear();
		isFileNew = true;
		selectionManagement.clearAndFire(NEUE_AUSWAHL);
	}

	@Override
	public void fileOpen(JComponent trigger) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Petri-Nets: *.pnml", "pnml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(impModel.getWfnFile());
		int returnVal = chooser.showOpenDialog(trigger.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ImportManagement iM = new ImportManagement(chooser.getSelectedFile(), impModel);
			iM.startImport();
			selectionManagement.clearAndFire(NEUE_AUSWAHL);
		}
		isFileNew = false;
	}

	@Override
	public boolean fileSaved(JComponent trigger) {
		if (!impModel.isCurrentWfnSaved()) {
			if (impModel.getWfnFile() == null)
				return fileSaveAs(trigger);
			else {
				(new ExportManagement(impModel.getWfnFile(), expModel)).startExport();
				impModel.setIsCurrentWfnSaved(true);
				isFileNew = false;
			}
		}
		return false;
	}

	@Override
	public boolean fileSaveAs(JComponent trigger) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Petri-Nets: *.pnml", "pnml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(impModel.getWfnFile());
		int returnVal = chooser.showSaveDialog(trigger.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File pnmlFile = chooser.getSelectedFile();
			if ( !pnmlFile.getPath().toLowerCase().endsWith(".pnml"))
				pnmlFile = new File(pnmlFile.getPath() + ".pnml");
			if (pnmlFile.exists()) {
				int fileOverwrite = JOptionPane.showConfirmDialog(null,
			            "The file " + pnmlFile.getName() + " already exists.\n"
			            		+ "Do you want to overwrite the file?", 
			            "Overwrite file?", JOptionPane.YES_NO_OPTION, 
			            JOptionPane.QUESTION_MESSAGE);
			    if (fileOverwrite != JOptionPane.YES_OPTION) 
			        return false;
			}
			(new ExportManagement(pnmlFile, expModel)).startExport();
			impModel.setWfnFile(pnmlFile);
			impModel.setIsCurrentWfnSaved(true);
			isFileNew = false;
			return true;
		}
		return false;
	}

	@Override
	public String getFileName() {
		return (!isFileNew && impModel.getWfnFile() != null) 
					? impModel.getWfnFile().getName()
					: "unknown";
	}

}

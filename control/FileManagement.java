package control;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.ICentralConstants;
import wfnmodel.importexport.Export;
import wfnmodel.importexport.IWfnExport;
import wfnmodel.importexport.IWfnImport;
import wfnmodel.importexport.Import;
import wfnmodel.interfaces.IWfnElement;

/**
 * Verwaltung und Steuerung der durch den Benutzer angestoßenen Import- und Exportvorgänge
 *
 */
public class FileManagement implements IFileManagement,
										ICentralConstants {
	/** Import-Schnittstelle zum Datenmodell.*/
	private IWfnImport impModel;
	/** Export-Schnittstelle zum Datenmodell.*/
	private IWfnExport expModel;
	/** Die aktuelle SelectionManagement.*/
	private SelectionManagement<IWfnElement> selectionManagement;
	/** zur Übersicht was als dateiname anzuzeigen ist.*/
	private boolean isFileNew;
	
	
	/**
	 * @param impModel Import-Schnittstelle zum Datenmodell
	 * @param expModel Export-Schnittstelle zum Datenmodell
	 * @param selectionManagement die aktuelle Auswahlverwaltung
	 */
	public FileManagement(IWfnImport impModel, IWfnExport expModel, SelectionManagement<IWfnElement> selectionManagement) {
		super();
		this.impModel = impModel;
		this.expModel = expModel;
		this.selectionManagement = selectionManagement;
	}

	@Override
	public void clear() {
		impModel.clear();
		isFileNew = true;
		selectionManagement.clearAndFire(NEW_SELECTION);
	}

	@Override
	public void open(JComponent trigger) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Petri-Nets: *.pnml", "pnml");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(impModel.getWfnFile());
		int returnVal = chooser.showOpenDialog(trigger.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			Import.execute(chooser.getSelectedFile(), impModel);
			selectionManagement.clearAndFire(NEW_SELECTION);
		}
		isFileNew = false;
	}

	@Override
	public boolean save(JComponent trigger) {
		if (!impModel.isCurrentWfnSaved()) {
			if (impModel.getWfnFile() == null)
				return saveAs(trigger);
			else {
				Export.execute(impModel.getWfnFile(), expModel);
				impModel.setIsCurrentWfnSaved(true);
				isFileNew = false;
			}
		}
		return false;
	}

	@Override
	public boolean saveAs(JComponent trigger) {
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
			Export.execute(pnmlFile, expModel);
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

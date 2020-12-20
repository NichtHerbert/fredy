package gui.toolbar;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import control.FileManagement;
import control.IFileManagement;
import gui.EIcons;
import listeners.IWfnStatusListener;
import wfnmodel.WfnStatusInfo;

/**
 * JPanel zur Anzeige des Dateinamens und 4 nebeneinanderliegenden Buttons. 
 * Die Buttons sollen zum Öffnen, Speichern(-Unter) und Leeren 
 * des Datenmodells führen.
 */
class JPanelFileOperations extends JPanel implements IWfnStatusListener {

	private static final long serialVersionUID = -4237254650736397557L;
	
	/** Referenz auf die für Dateioperationen zuständige Verwaltung. */
	private IFileManagement fileManagement;
	/** JPanel, in welchem der Dateiname angezeigt wird. */
	private JPanel jpFileName;
	/** JPanel, in welchem die JButtons zum Leeren, Öffnen, Speichern und Speichern-Unter,
	 * nebeneinander angeordnet, befinden. */
	private JPanel jpFileButtons;
	/** Zeigt den Dateinamen, sitzt im @see {@link #jpFileName}.*/
	private JLabel jlFileName;
	
	JPanelFileOperations() {
		this(null);
	}
	
	JPanelFileOperations(IFileManagement filesManagement) {
		this.fileManagement = filesManagement;
		jpFileName = new JPanel();
		jpFileName.setLayout(new GridLayout(0, 1));
		jlFileName = new JLabel("Filename:   unknown1.pnml");
		jpFileName.add(jlFileName);
		
		JButton jbNew = new JButton(EIcons.NEU.getIcon());
		JButton jbOpen = new JButton(EIcons.OEFFNEN.getIcon());
		JButton jbSave = new JButton(EIcons.SPEICHERN.getIcon());
		JButton jbSaveAs = new JButton(EIcons.SPEICHERN_UNTER.getIcon());
		
		jbNew.setToolTipText("File new");
		jbOpen.setToolTipText("File open");
		jbSave.setToolTipText("File save");
		jbSaveAs.setToolTipText("File save as");
		
		jbNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fileManagement.clear();
					jlFileName.setText("Filename:   " + fileManagement.getFileName());
				} catch (NullPointerException e1) {}
			}
		});
		jbOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fileManagement.open(JPanelFileOperations.this);
					jlFileName.setText("Filename:   " + fileManagement.getFileName());
				} catch (NullPointerException e1) {}
			}
		});
		jbSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (fileManagement.save(JPanelFileOperations.this))
						jlFileName.setText("Filename:   " + fileManagement.getFileName());
				} catch (NullPointerException e1) {}
			}
		});
		jbSaveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (fileManagement.saveAs(JPanelFileOperations.this))
						jlFileName.setText("Filename:   " + fileManagement.getFileName());
				} catch (NullPointerException e1) {}
			}
		});
		
		jpFileButtons = new JPanel();
		jpFileButtons.add(jbNew);
		jpFileButtons.add(jbOpen);
		jpFileButtons.add(jbSave);
		jpFileButtons.add(jbSaveAs);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(jpFileName);
		add(jpFileButtons);
		setBorder(new TitledBorder("File Operations"));
	}

	/**
	 * Setzt das Attribut {@link #fileManagement}.
	 * @param fileManagement zu setzende FileManagement
	 */
	void setFileManagement(IFileManagement fileManagement) {
		this.fileManagement = fileManagement;
	}

	@Override
	public void newWfnStatus(WfnStatusInfo statusInfo) {
		jlFileName.setText("Filename:   " + fileManagement.getFileName());
	}


}

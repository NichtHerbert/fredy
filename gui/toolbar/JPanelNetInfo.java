package gui.toolbar;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import control.CircleTest;
import listeners.IWfnStatusListener;
import wfnmodel.WfnStatusInfo;

/**
 * Panel zur Anzeige, ob die aktuell existierenden WFN-Elemente
 * ein Workflownetz ergeben. Und wenn nicht, eine Begründung dafür anzeigen. 
 *
 */
class JPanelNetInfo extends JPanel implements IWfnStatusListener {

	private static final long serialVersionUID = 4974014779936004767L;

	/** Label-Array , über welches der WFN-Zustand ausgegeben wird. */
	private JLabel[] jlInfo;
	/** Der letztübermittelte Zustand/Status des Workflownetzes. */
	private WfnStatusInfo statusInfo;
	private CircleTest circleTest;

	/**
	 * Initialisiert das Panel mit {@link #jlInfo} als Array der Größe/Länge 4,
	 * mit den einzelnen Labels zeilenartig übereinander angeordnet.
	 */
	JPanelNetInfo() {
		setLayout(new GridLayout(0,1));
		setAlignmentY(LEFT_ALIGNMENT);
		statusInfo = new WfnStatusInfo();
		JButton jbCircleTest = new JButton("Circle-Test");
		jbCircleTest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (circleTest != null) 
					jlInfo[3].setText(circleTest.execute());
			}
		});
		add(jbCircleTest);
		jlInfo = new JLabel[4];
		for (int i = 0; i < jlInfo.length; i++) {
			jlInfo[i] = new JLabel(" ", SwingConstants.LEFT);
		}
		getGruendeZuLabel();
		add(jlInfo[0]);
		add(jlInfo[1]);
		add(jlInfo[2]);
		add(jlInfo[3]);
		setBorder(new TitledBorder("Workflow-Net Information"));
	}

	/**
	 * Holt sich Zustand bzw. Begründung des Zustandes von {@link #statusInfo}
	 * und schreibt ihn in {@link #jlInfo}.
	 */
	private void getGruendeZuLabel() {
		if (!statusInfo.getNotWfnExplanatoryStatements().isEmpty()) {
			for (int i = 0; i < jlInfo.length; i++)
				if (i < statusInfo.getNotWfnExplanatoryStatements().size())
					jlInfo[i].setText(statusInfo.getNotWfnExplanatoryStatements().get(i));
				else
					jlInfo[i].setText("");
		} else {
			for (JLabel label : jlInfo)
				label.setText("");
		}
	}

	@Override
	public void newWfnStatus(WfnStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
		getGruendeZuLabel();
	}
	
	void setCircleTest(CircleTest circleTest) {
		this.circleTest = circleTest;
	}

}

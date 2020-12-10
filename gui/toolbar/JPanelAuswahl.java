package gui.toolbar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import gui.EIcons;
import horcherschnittstellen.IAuswahlBearbeitetHorcher;
import horcherschnittstellen.IAuswahlVeraenderungsHorcher;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * JPanel zur Anzeige der momentan ausgewählten Elemente des Editors.
 * Über dieses Panel kann der Benutzer die Namen der ausgewählten Elemente ändern,
 * sowie auch alle ausgewählten Elemente löschen.
 */
class JPanelAuswahl extends JPanel implements IAuswahlVeraenderungsHorcher {

	private static final long serialVersionUID = 6151200173301427145L;
	
	/**
	 * Liste der ausgewählten Elemente.
	 */
	private ArrayList<? extends IWfnElement> ausgewaehlteElemente;
	
	/**
	 * Liste derjenigen IAuswahlVeraenderungsHorcher, die informiert werden wollen, 
	 * wenn ein Elementname geändert oder die Auswahl gelöscht werden soll.
	 */
	private ArrayList<IAuswahlBearbeitetHorcher> auswahlBearbeitetHorcherListe;
	
	/**
	 * Datenmodell für das JTable jt_auswahl.
	 */
	private AbstractTableModel t_modell;
	
	/**
	 * Tabelle, die die ausgewählten WFN-Elemente mit ihrem Namen anzeigt.
	 */
	private JTable jt_auswahl;
	
	/**
	 * Initialisiert das JPanel mit einer Tabelle ({@link JTable}) und einem Button ({@link JButton}.
	 * Datenmodell der Tabelle, wie Actionlistener des Buttons 
	 * werden innerhalb des Kostruktors als anonyme Klasse umgesetzt.
	 */
	JPanelAuswahl() {
		ausgewaehlteElemente = new ArrayList<>();
		auswahlBearbeitetHorcherListe = new ArrayList<>(1);
		
		t_modell = new AbstractTableModel() {
			
			private static final long serialVersionUID = -2513122092612453996L;

			@Override
			public String getValueAt(int rowIndex, int columnIndex) {
				if (ausgewaehlteElemente.size() >= rowIndex) {
					switch (columnIndex) {
					case 0:	return ausgewaehlteElemente.get(rowIndex).getWfnElementType().toString(); 
					case 1:	if (ausgewaehlteElemente.get(rowIndex).getWfnElementType() != EWfnElement.ARC)
								return ((IWfnTransitionAndPlace) ausgewaehlteElemente.get(rowIndex)).getName();
							break;
					default:break;
					}
				}
				return null;
			}
			
			@Override
			public int getRowCount() {
				return ausgewaehlteElemente.size();
			}
			
			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 0: 	return "Element";
				case 1: 	return "Name";
				default: 	return super.getColumnName(column);
				}
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				try {
					return Class.forName("java.lang.String");
				} catch (ClassNotFoundException e) {
					return null;
				}
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if ((columnIndex == 1)
						&& (ausgewaehlteElemente.get(rowIndex).getWfnElementType() != EWfnElement.ARC))
					return true;
				return super.isCellEditable(rowIndex, columnIndex);
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				if (ausgewaehlteElemente.get(rowIndex).getWfnElementType() != EWfnElement.ARC) {
					fireElementSollNamenAendern((IWfnTransitionAndPlace) ausgewaehlteElemente.get(rowIndex),
							(String) aValue);
				}
			}

		};
		
		jt_auswahl = new JTable(t_modell);
		jt_auswahl.setRowSelectionAllowed(false);
		jt_auswahl.getColumnModel().getColumn(0).setPreferredWidth(8*EWfnElement.BASEFACTOR);
		jt_auswahl.getColumnModel().getColumn(1).setPreferredWidth(8*EWfnElement.BASEFACTOR);
		JScrollPane scrollPane = new JScrollPane(jt_auswahl);
		jt_auswahl.setFillsViewportHeight(true);
		scrollPane.setPreferredSize(new Dimension(16*EWfnElement.BASEFACTOR, 4*EWfnElement.BASEFACTOR));
		
		JButton jbAuswahlLoeschen = new JButton(EIcons.MUELLEIMER.getIcon());
		jbAuswahlLoeschen.setToolTipText("Auswahl Löschen");
		jbAuswahlLoeschen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireAuswahlSollGeloeschtWerden();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(jbAuswahlLoeschen, BorderLayout.SOUTH);
		this.setBorder(new TitledBorder("Auswahl"));
	}
	
	@Override
	public void auswahlAenderungEingetreten(int auswahlArt, ArrayList<? extends IWfnElement> ausgewaehlteElemente) {
		if ((auswahlArt == NEUE_AUSWAHL)
				&& (ausgewaehlteElemente != null))
			this.ausgewaehlteElemente = ausgewaehlteElemente;
		else 
			this.ausgewaehlteElemente.clear();
		if (jt_auswahl.isEditing()) {
			jt_auswahl.getCellEditor().cancelCellEditing();
		}
		t_modell.fireTableDataChanged();
		jt_auswahl.revalidate();

	}

	
	/**
	 * Fügt den übergebenen Horcher der {@link #auswahlBearbeitetHorcherListe} hinzu.
	 * @param horcher wird {@link #auswahlBearbeitetHorcherListe} hinzugefügt
	 */
	public void addAuswahlBearbeitetHorcher(IAuswahlBearbeitetHorcher horcher) {
		auswahlBearbeitetHorcherListe.add(horcher);
	}

	
	/**
	 * Entfernt den übergebenen Horcher von der {@link #auswahlBearbeitetHorcherListe}.
	 * @param horcher wird von {@link #auswahlBearbeitetHorcherListe} entfernt
	 */
	public void removeAuswahlBearbeitetHorcher(IAuswahlBearbeitetHorcher horcher) {
		if (auswahlBearbeitetHorcherListe.contains(horcher)) 
			auswahlBearbeitetHorcherListe.remove(horcher);
	}

	
	/**
	 * Informiert alle Horcher der {@link #auswahlBearbeitetHorcherListe},
	 * dass die Elemente der Liste {@link #ausgewaehlteElemente} gelöscht werden sollen.
	 */
	public void fireAuswahlSollGeloeschtWerden() {
		if (!ausgewaehlteElemente.isEmpty())
			for (IAuswahlBearbeitetHorcher horcher : auswahlBearbeitetHorcherListe)
				horcher.auswahlSollGeloeschtWerden(ausgewaehlteElemente);
	}

	
	/**
	 * Informiert alle Horcher der {@link #auswahlBearbeitetHorcherListe},
	 * ein Element einen neuen Namen bekommen soll.
	 * @param element Element, dessen Name geändert werden soll
	 * @param name zu setzender Name
	 */
	public void fireElementSollNamenAendern(IWfnTransitionAndPlace element, String name) {
		for (IAuswahlBearbeitetHorcher horcher : auswahlBearbeitetHorcherListe)
			horcher.elementSollNameAendern(element, name);
	}
	
	
}

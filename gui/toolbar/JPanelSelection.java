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
import listeners.ISelectionEditingListener;
import listeners.ISelectionChangingListener;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * JPanel zur Anzeige der momentan ausgewählten Elemente des Editors.
 * Über dieses Panel kann der Benutzer die Namen der ausgewählten Elemente ändern,
 * sowie auch alle ausgewählten Elemente löschen.
 */
class JPanelSelection extends JPanel implements ISelectionChangingListener {

	private static final long serialVersionUID = 6151200173301427145L;
	
	/** Liste der ausgewählten Elemente. */
	private ArrayList<? extends IWfnElement> selectedElements;
	/** Liste derjenigen ISelectionChangingListener, die informiert werden wollen, 
	 * wenn ein Elementname geändert oder die Auswahl gelöscht werden soll. */
	private ArrayList<ISelectionEditingListener> selectionEditingListeners;
	/** Datenmodell für das JTable jtSelectedElements.*/
	private AbstractTableModel tableModel;
	/** Tabelle, die die ausgewählten WFN-Elemente mit ihrem Namen anzeigt. */
	private JTable jtSelectedElements;
	
	/**
	 * Initialisiert das JPanel mit einer Tabelle ({@link JTable}) und einem Button ({@link JButton}.
	 * Datenmodell der Tabelle, wie Actionlistener des Buttons 
	 * werden innerhalb des Kostruktors als anonyme Klasse umgesetzt.
	 */
	JPanelSelection() {
		selectedElements = new ArrayList<>();
		selectionEditingListeners = new ArrayList<>(1);
		
		tableModel = new AbstractTableModel() {
			
			private static final long serialVersionUID = -2513122092612453996L;

			@Override
			public String getValueAt(int rowIndex, int columnIndex) {
				if (selectedElements.size() >= rowIndex) {
					switch (columnIndex) {
					case 0:	return selectedElements.get(rowIndex).getWfnElementType().toString(); 
					case 1:	if (selectedElements.get(rowIndex).getWfnElementType() != EWfnElement.ARC)
								return ((IWfnTransitionAndPlace) selectedElements.get(rowIndex)).getName();
							break;
					default:break;
					}
				}
				return null;
			}
			
			@Override
			public int getRowCount() {
				return selectedElements.size();
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
						&& (selectedElements.get(rowIndex).getWfnElementType() != EWfnElement.ARC))
					return true;
				return super.isCellEditable(rowIndex, columnIndex);
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				if (selectedElements.get(rowIndex).getWfnElementType() != EWfnElement.ARC) {
					fireElementToSetName((IWfnTransitionAndPlace) selectedElements.get(rowIndex),
							(String) aValue);
				}
			}

		};
		
		jtSelectedElements = new JTable(tableModel);
		jtSelectedElements.setRowSelectionAllowed(false);
		jtSelectedElements.getColumnModel().getColumn(0).setPreferredWidth(8*EWfnElement.BASEFACTOR);
		jtSelectedElements.getColumnModel().getColumn(1).setPreferredWidth(8*EWfnElement.BASEFACTOR);
		JScrollPane scrollPane = new JScrollPane(jtSelectedElements);
		jtSelectedElements.setFillsViewportHeight(true);
		scrollPane.setPreferredSize(new Dimension(16*EWfnElement.BASEFACTOR, 4*EWfnElement.BASEFACTOR));
		
		JButton jbSelectionDelete = new JButton(EIcons.MUELLEIMER.getIcon());
		jbSelectionDelete.setToolTipText("Delete Selection");
		jbSelectionDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireSelectedElementsToDelete();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(jbSelectionDelete, BorderLayout.SOUTH);
		this.setBorder(new TitledBorder("Selection"));
	}
	
	@Override
	public void selectionChangeOccurred(int selectionType, ArrayList<? extends IWfnElement> selectedElements) {
		if ((selectionType == NEW_SELECTION)
				&& (selectedElements != null))
			this.selectedElements = selectedElements;
		else 
			this.selectedElements.clear();
		if (jtSelectedElements.isEditing()) {
			jtSelectedElements.getCellEditor().cancelCellEditing();
		}
		tableModel.fireTableDataChanged();
		jtSelectedElements.revalidate();

	}

	
	/**
	 * Fügt den übergebenen Horcher der {@link #selectionEditingListeners} hinzu.
	 * @param listener wird {@link #selectionEditingListeners} hinzugefügt
	 */
	public void addSelectionEditingListener(ISelectionEditingListener listener) {
		selectionEditingListeners.add(listener);
	}

	
	/**
	 * Entfernt den übergebenen Horcher von der {@link #selectionEditingListeners}.
	 * @param listener wird von {@link #selectionEditingListeners} entfernt
	 */
	public void removeSelectionEditingListener(ISelectionEditingListener listener) {
		if (selectionEditingListeners.contains(listener)) 
			selectionEditingListeners.remove(listener);
	}

	
	/**
	 * Informiert alle Horcher der {@link #selectionEditingListeners},
	 * dass die Elemente der Liste {@link #selectedElements} gelöscht werden sollen.
	 */
	public void fireSelectedElementsToDelete() {
		if (!selectedElements.isEmpty())
			for (ISelectionEditingListener listener : selectionEditingListeners)
				listener.elementsToDelete(selectedElements);
	}

	
	/**
	 * Informiert alle Horcher der {@link #selectionEditingListeners},
	 * ein Element einen neuen Namen bekommen soll.
	 * @param element Element, dessen Name geändert werden soll
	 * @param name zu setzender Name
	 */
	public void fireElementToSetName(IWfnTransitionAndPlace element, String name) {
		for (ISelectionEditingListener listener : selectionEditingListeners)
			listener.elementToSetName(element, name);
	}
	
	
}

package wfnmodell;

import java.util.BitSet;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Klasse zur Verwaltung der Identifier, mit deren Hilfe die WFN-Elemente des Datenmodells
 * eindeutig unterscheidbar sein sollen.
 * Desweiteren stellt die Klasse Methoden zur Verfügung, mit deren Hilfe WFN-Elemente ihre ID
 * aus *.pnml Dateien auch nach Veränderungen des Datenmodells beim erneuten Speichern behalten können
 * (was vor Allem dann zu Komplikationen führen könnte, wenn die ID der pnml-Datei 
 * eine niedrige natürliche Zahl ist).
 */
class Identifier {
	/**
	 * Wird von 0 auf hochgezählt: Ist also entweder 0 oder entspricht dem Wert der letzten ID-Zuweisung.
	 */
	private int index;
	
	/**
	 * Liste der "PNML"-ID's, die ein Integer-Wert sind, von WFN-Elementen, 
	 * die aus einer pnml-Datei importiert wurden.
	 */
	private TreeSet<Integer> importedPnmlIDs;
	
	/**
	 * Liste der ID's, die nicht in Benutzung sind 
	 * (und deren Wert niedriger ist, als der von {@link #index}).
	 */
	private BitSet unusedIDs;
	
	/**
	 * HashMap zur verknüpften Speicherung von IDs. Wenn eine ID und eine PNML-ID sich gleichen, 
	 * wird in der HashMap die alte ID und eine neu generierte ID abgelegt.
	 */
	private HashMap<Integer, Integer> idsDifferentFromPnmlID;

	Identifier() {
		index = 0;
		unusedIDs = new BitSet(20);
		importedPnmlIDs = new TreeSet<Integer>();
		idsDifferentFromPnmlID = new HashMap<>();
	}

	/**
	 * Überprüft, ob der übergebene String einen Integer-Wert darstellt, und wenn ja, 
	 * wird er der Liste {@link #importedPnmlIDs} hinzugefügt.
	 * @param pnmlID zu überprüfender String
	 */
	void pnmlIDMonitoring(String pnmlID) {
		if (pnmlID.matches("\\d+")) importedPnmlIDs.add(Integer.parseInt(pnmlID));
	}
	
	/**
	 * Gibt die niedrigste freie ID zurück, welche nicht in der Liste {@link #importedPnmlIDs} steht.
	 * @return die niedrigste freie ID
	 */
	int get() {
		int result = -1;
		int i = 0;
		while ((unusedIDs.nextSetBit(i) != -1) && (unusedIDs.nextSetBit(i) <= index)) 
			if (!importedPnmlIDs.contains(unusedIDs.nextSetBit(i))) {
				result = unusedIDs.nextSetBit(i);
				i = index + 1;
			} else
				i = unusedIDs.nextSetBit(i+1);
		if (result == -1) { 	
			index++;
			while (importedPnmlIDs.contains(index)) index++;
			result = index;
		}
		return result;
	}
	

	@Override
	public String toString() {
		return "IdentifierVerwaltung [identifier=" + index 
				+ ", intPNMLIDs=" + importedPnmlIDs 
				+ ", freieIDs=" + unusedIDs + "]";
	}

	/**
	 * Methode zur Verhinderung, dass sich ID und PNML-ID in die Quere kommen, wenn das Datenmodell 
	 * in eine pnml-Datei exportiert werden soll. Sollte die ID des Elements nicht als PNML-ID
	 * verwendet werden können, da sie schon die PNML-ID eines anderen Elements ist, wird eine zweite
	 * ID generiert (via {@link #get()}) und beide, alte wie neue ID, werden in der 
	 * HashMap {@link #idsDifferentFromPnmlID} gespeichert.
	 * @param id die festzugewiesene ID eines WFN-Elements
	 * @return diese oder eine neue ID in String-Form
	 */
	String convertIDintoPnmlID(int id) {
		int result = id;
		if (importedPnmlIDs.contains(id)) {
			if (idsDifferentFromPnmlID.containsKey(id))
				result = idsDifferentFromPnmlID.get(id);
			else {
				result = get();
				idsDifferentFromPnmlID.put(id, result);
			}	
		}
		return String.valueOf(result);
	}

	/**
	 * Gibt der IdentifierVerwaltung eine ID zur Wiederverwendung zurück.
	 * @param id wird der Liste {@link #unusedIDs} hinzugefügt
	 */
	void passBack(int id) {
		unusedIDs.set(id);
	}
	

}

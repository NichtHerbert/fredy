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
class IdentifierVerwaltung {
	/**
	 * Wird von 0 auf hochgezählt: Ist also entweder 0 oder entspricht dem Wert der letzten ID-Zuweisung.
	 */
	private int identifier;
	
	/**
	 * Liste der "PNML"-ID's, die ein Integer-Wert sind, von WFN-Elementen, 
	 * die aus einer pnml-Datei importiert wurden.
	 */
	private TreeSet<Integer> intPNMLIDs;
	
	/**
	 * Liste der ID's, die nicht in Benutzung sind 
	 * (und deren Wert niedriger ist, als der von {@link #identifier}).
	 */
	private BitSet freieIDs;
	
	/**
	 * HashMap zur verknüpften Speicherung von IDs. Wenn eine ID und eine PNML-ID sich gleichen, 
	 * wird in der HashMap die alte ID und eine neu generierte ID abgelegt.
	 */
	private HashMap<Integer, Integer> idAndersAlsPNMLID;

	IdentifierVerwaltung() {
		identifier = 0;
		freieIDs = new BitSet(20);
		intPNMLIDs = new TreeSet<Integer>();
		idAndersAlsPNMLID = new HashMap<>();
	}

	/**
	 * Überprüft, ob der übergebene String einen Integer-Wert darstellt, und wenn ja, 
	 * wird er der Liste {@link #intPNMLIDs} hinzugefügt.
	 * @param pnmlID zu überprüfender String
	 */
	void pnmlIDUeberwachung(String pnmlID) {
		if (pnmlID.matches("\\d+")) intPNMLIDs.add(Integer.parseInt(pnmlID));
	}
	
	/**
	 * Gibt die niedrigste freie ID zurück, welche nicht in der Liste {@link #intPNMLIDs} steht.
	 * @return die niedrigste freie ID
	 */
	int getNextFreeIdentifier() {
		int ergebnis = -1;
		int i = 0;
		while ((freieIDs.nextSetBit(i) != -1) && (freieIDs.nextSetBit(i) <= identifier)) 
			if (!intPNMLIDs.contains(freieIDs.nextSetBit(i))) {
				ergebnis = freieIDs.nextSetBit(i);
				i = identifier + 1;
			} else
				i = freieIDs.nextSetBit(i+1);
		if (ergebnis == -1) { 	
			identifier++;
			while (intPNMLIDs.contains(identifier)) identifier++;
			ergebnis = identifier;
		}
		return ergebnis;
	}
	

	@Override
	public String toString() {
		return "IdentifierVerwaltung [identifier=" + identifier 
				+ ", intPNMLIDs=" + intPNMLIDs 
				+ ", freieIDs=" + freieIDs + "]";
	}

	/**
	 * Methode zur Verhinderung, dass sich ID und PNML-ID in die Quere kommen, wenn das Datenmodell 
	 * in eine pnml-Datei exportiert werden soll. Sollte die ID des Elements nicht als PNML-ID
	 * verwendet werden können, da sie schon die PNML-ID eines anderen Elements ist, wird eine zweite
	 * ID generiert (via {@link #getNextFreeIdentifier()}) und beide, alte wie neue ID, werden in der 
	 * HashMap {@link #idAndersAlsPNMLID} gespeichert.
	 * @param id die festzugewiesene ID eines WFN-Elements
	 * @return diese oder eine neue ID in String-Form
	 */
	String idZuPNMLID(int id) {
		int ergebnis = id;
		if (intPNMLIDs.contains(id)) {
			if (idAndersAlsPNMLID.containsKey(id))
				ergebnis = idAndersAlsPNMLID.get(id);
			else {
				ergebnis = getNextFreeIdentifier();
				idAndersAlsPNMLID.put(id, ergebnis);
			}	
		}
		return String.valueOf(ergebnis);
	}

	/**
	 * Gibt der IdentifierVerwaltung eine ID zur Wiederverwendung zurück.
	 * @param id wird der Liste {@link #freieIDs} hinzugefügt
	 */
	void idWiederFrei(int id) {
		freieIDs.set(id);
	}
	

}

package verwaltung;

import java.util.ArrayList;

import gui.IZentraleKonstanten;
import horcherschnittstellen.IWFNVeraenderungsHorcher;
import wfnmodell.WfnStatusInfo;
import wfnmodell.schnittstellen.IWFNElement;
import wfnmodell.schnittstellen.IWFNElementOK;

public class KreisTestVerwaltung implements IWFNVeraenderungsHorcher,
											IZentraleKonstanten {
	
	/*Die aktuelle StatusInfo, ohne die Informationen der Markierungsverwaltung.*/
	private WfnStatusInfo statusInfo;
	
	private AuswahlVerwaltung<IWFNElement> auswahlVerwaltung;
	
	private ArrayList<IWFNElementOK> besuchteElemente;
	
	private ArrayList<IWFNElementOK> inBearbeitungElemente;
	
	KreisTestVerwaltung(AuswahlVerwaltung<IWFNElement> auswahlVerwaltung2) {
		this.auswahlVerwaltung = auswahlVerwaltung2;
		statusInfo = new WfnStatusInfo();
		besuchteElemente = new ArrayList<>();
		inBearbeitungElemente = new ArrayList<>();
	}
	
	public String kreisTest() {
		if (statusInfo.isWfn()) {
			besuchteElemente.clear();
			inBearbeitungElemente.clear();
			if (istKreis(statusInfo.getStartPlace())) {
				int idxEnd = inBearbeitungElemente.size()-1;
				IWFNElementOK verbindungsElement = inBearbeitungElemente.get(idxEnd);
				int idxStart = inBearbeitungElemente.indexOf(verbindungsElement);
				ArrayList<IWFNElement> ergebnisliste = new ArrayList<>();
				for (int i = idxStart; i<= idxEnd; i++) {
					ergebnisliste.add(inBearbeitungElemente.get(i));
				}
				auswahlVerwaltung.clearAndAddALLAndFire(ergebnisliste, NEUE_AUSWAHL);
				return "Mindestens ein Kreis im WFN";
			}
			return "Kein Kreis im WFN";	
		}
		return "";
	}
		
	private boolean istKreis(IWFNElementOK element) {
		if (!besuchteElemente.contains(element)) {
			besuchteElemente.add(element);
			inBearbeitungElemente.add(element);	
			for (IWFNElementOK elementDanach : element.getKantenZu()) {
				if (istKreis(elementDanach)) 
					return true;
				else
					if (inBearbeitungElemente.contains(elementDanach)) {
						inBearbeitungElemente.add(elementDanach);
						return true;
					}
			}
			inBearbeitungElemente.remove(element);
		}
		return false;
	}

	@Override
	public void modellAenderungEingetreten(WfnStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

}

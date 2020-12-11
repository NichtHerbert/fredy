package verwaltung;

import java.util.ArrayList;

import gui.ICentralConstants;
import listeners.IWfnNetListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

public class KreisTestVerwaltung implements IWfnNetListener,
											ICentralConstants {
	
	/*Die aktuelle StatusInfo, ohne die Informationen der Markierungsverwaltung.*/
	private WfnStatusInfo statusInfo;
	
	private AuswahlVerwaltung<IWfnElement> auswahlVerwaltung;
	
	private ArrayList<IWfnTransitionAndPlace> besuchteElemente;
	
	private ArrayList<IWfnTransitionAndPlace> inBearbeitungElemente;
	
	KreisTestVerwaltung(AuswahlVerwaltung<IWfnElement> auswahlVerwaltung2) {
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
				IWfnTransitionAndPlace verbindungsElement = inBearbeitungElemente.get(idxEnd);
				int idxStart = inBearbeitungElemente.indexOf(verbindungsElement);
				ArrayList<IWfnElement> ergebnisliste = new ArrayList<>();
				for (int i = idxStart; i<= idxEnd; i++) {
					ergebnisliste.add(inBearbeitungElemente.get(i));
				}
				auswahlVerwaltung.clearAndAddALLAndFire(ergebnisliste, NEW_SELECTION);
				return "Mindestens ein Kreis im WFN";
			}
			return "Kein Kreis im WFN";	
		}
		return "";
	}
		
	private boolean istKreis(IWfnTransitionAndPlace element) {
		if (!besuchteElemente.contains(element)) {
			besuchteElemente.add(element);
			inBearbeitungElemente.add(element);	
			for (IWfnTransitionAndPlace elementDanach : element.getOutputElements()) {
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
	public void netChangeOccurred(WfnStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

}

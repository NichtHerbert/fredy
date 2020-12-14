package verwaltung;

import java.util.ArrayList;
import gui.ICentralConstants;
import listeners.IWfnNetListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

public class CircleTest implements IWfnNetListener, ICentralConstants {
	
	/*Die aktuelle StatusInfo, ohne die Informationen der Markierungsverwaltung.*/
	private WfnStatusInfo statusInfo;
	
	private SelectionManagement<IWfnElement> selectionManagement;
	
	private ArrayList<IWfnTransitionAndPlace> visitedElements;
	
	private ArrayList<IWfnTransitionAndPlace> inProcessElements;
	
	private final String thereIsACircle = "This WFN has at least one circle!";
	private final String thereIsNoCircle = "This WFN has no circle!";
	
	CircleTest(SelectionManagement<IWfnElement> selectionManagement) {
		this.selectionManagement = selectionManagement;
		statusInfo = new WfnStatusInfo();
		visitedElements = new ArrayList<>();
		inProcessElements = new ArrayList<>();
	}
	
	public String execute() {
		if (!statusInfo.isWfn()) return "";
		visitedElements.clear();
		inProcessElements.clear();
		if (!isCircle(statusInfo.getStartPlace())) return thereIsNoCircle;
		
		final int idxEnd = inProcessElements.size()-1;
		final IWfnTransitionAndPlace circleStartEndElement = inProcessElements.get(idxEnd);
		final int idxStart = inProcessElements.indexOf(circleStartEndElement);
		final ArrayList<IWfnElement> circleElements = new ArrayList<>(inProcessElements.subList(idxStart, idxEnd));
		selectionManagement.clearAndAddALLAndFire(circleElements, NEW_SELECTION);
		return thereIsACircle;
	}
		
	private boolean isCircle(IWfnTransitionAndPlace element) {
		if (visitedElements.contains(element)) return false;
		visitedElements.add(element);
		inProcessElements.add(element);	
		for (IWfnTransitionAndPlace elementForwards : element.getOutputElements()) {
			if (isCircle(elementForwards)) 
				return true;
			else
				if (inProcessElements.contains(elementForwards)) {
					inProcessElements.add(elementForwards);
					return true;
				}
		}
		inProcessElements.remove(element);
		return false;
	}

	@Override
	public void netChangeOccurred(WfnStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

}

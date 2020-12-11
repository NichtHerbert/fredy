package wfnmodel;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import listeners.IWfnNetListener;
import wfnmodel.elements.EWfnElement;
import wfnmodel.elements.WfnElementPlace;
import wfnmodel.elements.WfnElementTransition;
import wfnmodel.importexport.IWfnExport;
import wfnmodel.importexport.IWfnImport;
import wfnmodel.importexport.PnmlElement;
import wfnmodel.interfaces.IWfnArc;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnModelChanging;
import wfnmodel.interfaces.IWfnModelPresentation;
import wfnmodel.interfaces.IWfnPlace;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Das Datenmodell des Workflownetzes.
 *
 */
public class WfnModel implements 	IWfnModelChanging, 
									IWfnModelPresentation, 
									IWfnImport, 
									IWfnExport {
	
	/**
	 * Die aktuelle {@link IDManagement}.
	 */
	private IDManagement idManagement;
	/**
	 * Liste aller Transitionen und Stellen des aktuellen Datenmodells.
	 */
	private ArrayList<IWfnTransitionAndPlace> transitionsAndPlaces;
	/**
	 * Liste der Horcher, die über eine Modell-Änderung informiert werden möchten.
	 */
	private ArrayList<IWfnNetListener> changingListeners;
	/**
	 * Die aktuelle {@link StartEndManagement}.
	 */
	private StartEndManagement startEndManagement;
	/**
	 * Die aktuelle {@link ConnectionManagement}.
	 */
	private ConnectionManagement connectionManagement;
	/**
	 * Die aktuelle {@link ArcManagement}.
	 */
	private ArcManagement arcManagement;
	/**
	 * Referenz auf die letzte importierte oder exportierte pnml-Datei.
	 */
	private File wfnFile;
	/**
	 * true, solange es seit dem letzten Öffnen oder Speichern des Datenmodells zu keiner Änderung des Models kam
	 */
	private boolean isSaved;

	public WfnModel() {
		newInit();
		changingListeners = new ArrayList<IWfnNetListener>();
	}

	/**
	 * Trennt Initialisierung und Konstruktor, so kann das Datenmodell durch newInit() auf einfachste
	 * Art zurückgesetzt werden.
	 */
	private void newInit() {
		idManagement = new IDManagement();
		transitionsAndPlaces = new ArrayList<IWfnTransitionAndPlace>();
		connectionManagement = new ConnectionManagement();
		startEndManagement = new StartEndManagement(connectionManagement);
		arcManagement = new ArcManagement(idManagement, startEndManagement,
				connectionManagement);
		isSaved = false;
	}
	
	@Override
	public void addChangingListener(IWfnNetListener listener) {
		changingListeners.add(listener);
	}
	
	@Override
	public void removeChangingListener(IWfnNetListener listener) {
		if (changingListeners.contains(listener)) 
			changingListeners.remove(listener);
	}
	
	/**
	 * Erstellt ein neues {@link WfnStatusInfo} und sendet es an alle Horcher der Liste {@link #changingListeners}.
	 */
	private void fireModelChange() {
		WfnStatusInfo statusInfo = WfnStatusInfo.getInfo(transitionsAndPlaces, arcManagement.getAllArcs(), startEndManagement);
		for (IWfnNetListener listener : changingListeners)
			listener.netChangeOccurred(statusInfo);
		isSaved = false;
	}
	
	@Override
	public void setWfnFile(File file) {
		wfnFile = file;
	}
	
	@Override
	public File getWfnFile() {
		return wfnFile;
	}


	@Override
	public void clear() {
		newInit();
		fireModelChange();
	}
	
	@Override
	public void delete(ArrayList<? extends IWfnElement> elements) {
		for (IWfnElement element : elements)
			deleteWithoutFire(element);
		fireModelChange();
	}
	
	@Override
	public void delete(IWfnElement element) {
		deleteWithoutFire(element);
		fireModelChange();
	}
	
	/**
	 * Private Methode zur Löschung eines Elements aus dem Datenmodell, ohne eine Modelländerung bekannt zu machen.
	 * Handelt es sich bei dem Element um eine Stelle oder Transition, werden auch alle Kanten, die in diesem Element
	 * enden oder beginnen, gelöscht.
	 * @param element das zu löschende Element
	 */
	private void deleteWithoutFire(IWfnElement element) {
		if (element.getWfnElementType() != EWfnElement.ARC) {
			IWfnTransitionAndPlace elementButNoArc = (IWfnTransitionAndPlace)element; 
			if (transitionsAndPlaces.contains(elementButNoArc)) {
				for (IWfnTransitionAndPlace elemBackwards : elementButNoArc.getInputElements()) {
					arcManagement.deleteArc(elemBackwards, elementButNoArc);
				}
				for (IWfnTransitionAndPlace elemForwards : elementButNoArc.getOutputElements()) {
					arcManagement.deleteArc(elementButNoArc, elemForwards);
				}
				idManagement.passBack(elementButNoArc.getID());
				if (elementButNoArc.getWfnElementType() == EWfnElement.PLACE) 
					startEndManagement.remove((WfnElementPlace) elementButNoArc);
			    transitionsAndPlaces.remove(elementButNoArc);
			}
		} else 
			arcManagement.deleteArc((IWfnArc) element);
	}
	
	@Override
	public void createPlace(Point position) {
		createPlace("", "", position, false);
	}

	@Override
	public void createPlace(String pnmlID, String name, Point position, boolean marking) {
		idManagement.pnmlIDMonitoring(pnmlID);
		final int newID = idManagement.get();
		WfnElementPlace place = new WfnElementPlace(pnmlID, newID, position);
		if (!name.equals("")) place.setName(name);
		if (marking) place.setMarking(marking);
		transitionsAndPlaces.add(place);
		startEndManagement.add(place);
		fireModelChange();
	}

	@Override
	public void createTransition(Point position) {
		createTransition("", "", position);
	}

	@Override
	public void createTransition(String pnmlID, String name, Point position) {
		idManagement.pnmlIDMonitoring(pnmlID);
		final int newID = idManagement.get();
		WfnElementTransition transition = new WfnElementTransition(pnmlID, newID, position);
		if ( !name.equals("")) transition.setName(name);
		transitionsAndPlaces.add(transition);
		fireModelChange();
	}
	
	@Override
	public void createArc(String pnmlIDArc, String pnmlIDSource, String pnmlIDTarget) {
		try {
			IWfnTransitionAndPlace source = transitionsAndPlaces.parallelStream()
												.filter(elem -> elem.getPnmlID().contentEquals(pnmlIDSource))
												.findFirst().get();
			IWfnTransitionAndPlace target = transitionsAndPlaces.parallelStream()
												.filter(elem -> elem.getPnmlID().contentEquals(pnmlIDTarget))
												.findFirst().get();
			createArc(pnmlIDArc, source, target);
		} catch (NoSuchElementException e) {}
//		, target;
//		for (IWfnTransitionAndPlace elem : transitionsAndPlaces) {
//			
//		}
//		boolean sourceNotFound = true;
//		boolean targetŃotFound = true;
//		Iterator<IWfnTransitionAndPlace> it = transitionsAndPlaces.iterator();
//		IWfnTransitionAndPlace source, target;
//		while ((it.hasNext()) && (sourceNotFound || targetŃotFound)) {
//			IWfnTransitionAndPlace elem = it.next();
//			String pnmlID = elem.getPnmlID();
//			if (pnmlID.equals(pnmlIDSource)) {
//				source = elem;
//				sourceNotFound = false;
//			} else 
//				if (pnmlID.equals(pnmlIDTarget)) {
//					target = elem;
//					targetŃotFound = false;
//				}
//		}
//		if (( !sourceNotFound) && ( !targetŃotFound)) 
//			createArc(pnmlIDArc, source, target);
	}
	
	@Override
	public void createArc(IWfnTransitionAndPlace source, IWfnTransitionAndPlace target) {
		createArc("", source, target);
	}
	
	/**
	 * Übergibt das Erschaffen der neuen Kante der {@link #arcManagement}, und löst Informationsweitergabe
	 * zur Modelländerung aus.
	 * @param pnmlID ID aus der pnml-Datei oder ""
	 * @param source Element, von dem die neue Kante ausgeht
	 * @param target Element, in dem die neue Kante endet
	 */
	private void createArc(String pnmlID, IWfnTransitionAndPlace source, IWfnTransitionAndPlace target) {
		arcManagement.createArc(pnmlID, source, target);
		fireModelChange();
	}
	
	@Override
	public void setElementName(IWfnTransitionAndPlace element, String name) {
		element.setName(name);
		fireModelChange();
	}
	
	@Override
	public ArrayList<IWfnTransitionAndPlace> getAllElementsForDrawing() {
		return transitionsAndPlaces;
	}
	
	/**
	 * Gibt alle Ellemente, also auch Kanten, zurück.
	 * @return Liste aller Stellen, Transitionen und Kanten des aktuellen Datenmodells.
	 */
	private ArrayList<? extends IWfnElement> getAllElements() {
		ArrayList<IWfnElement> result = new ArrayList<IWfnElement>(transitionsAndPlaces);
		result.addAll(arcManagement.getAllArcs());
		return result;
	}

	@Override
	public boolean isCurrentWfnSaved() {
		return isSaved;
	}
	
	@Override
	public void setIsCurrentWfnSaved(boolean b) {
		isSaved = b;
	}

	@Override
	public String toString() {
		String result = "";
		if (wfnFile != null) 
			result = "Geöffnete Datei = " + wfnFile.getName() + "\n";
		result += idManagement.toString() + "\n"
 					+ connectionManagement.toString() + "\n"
					+ startEndManagement.toString() + "\n"
					+ arcManagement.toString() + "\n"
					+ (getAllElements()).toString();
		return result;
	}

	@Override
	public ArrayList<PnmlElement> getAllElementsForExport() {
		ArrayList<? extends IWfnElement> allElements = getAllElements();
		ArrayList<PnmlElement> result = new ArrayList<>(allElements.size());
		for (IWfnElement elem : allElements) {
			String pnmlID = elem.getPnmlID();
			if (pnmlID.equals("")) pnmlID = idManagement.convertIDintoPnmlID(elem.getID());
			String marking = "";
			EWfnElement type = elem.getWfnElementType();
			switch (type) {
    		case PLACE: 	if (((IWfnPlace) elem).hasMarking()) marking = "1"; 
    						else marking = "0";	
    						// no break on purpose
    		case TRANSITION:String name = ((IWfnTransitionAndPlace) elem).getName();
    						String x = String.valueOf((((IWfnTransitionAndPlace) elem).getPosition()).x);
    						String y = String.valueOf((((IWfnTransitionAndPlace) elem).getPosition()).y);
    						result.add(new PnmlElement(type,pnmlID,name,x,y,marking));
    						break;
    		case ARC:		String sourcePnmlID = (((IWfnArc) elem).getSource()).getPnmlID();
    						String targetPnmlID = (((IWfnArc) elem).getTarget()).getPnmlID();
    						if (sourcePnmlID.equals("")) 
    							sourcePnmlID = idManagement.convertIDintoPnmlID((((IWfnArc) elem).getSource()).getID());
    						if (targetPnmlID.equals("")) 
    							targetPnmlID = idManagement.convertIDintoPnmlID((((IWfnArc) elem).getTarget()).getID());
    							result.add(new PnmlElement(type,pnmlID,sourcePnmlID,targetPnmlID));
    						break;
			}
		}
		return result;
	}

	
}

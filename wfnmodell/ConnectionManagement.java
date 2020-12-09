package wfnmodell;

import java.util.ArrayList;

import wfnmodell.elements.EWfnElement;
import wfnmodell.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse, mit deren Hilfe der Überblick behalten werden soll, ob das vorhandene Netz zusammenhängend auf einem Pfad von der
 * Startstelle zur Endstelle liegt. Sie benutzt dafür die Methoden der Stellen und Transitionen
 * {@link wfnmodell.interfaces.IWfnTransitionAndPlace#hasStartPath()} und
 * {@link wfnmodell.interfaces.IWfnTransitionAndPlace#setHasStartPath(boolean)}, bzw.
 * {@link wfnmodell.interfaces.IWfnTransitionAndPlace#hasEndPath()} und
 * {@link wfnmodell.interfaces.IWfnTransitionAndPlace#setHasEndPath(boolean)}.
 */
class ConnectionManagement {

	/**
	 * Aktualisiert wenn notwendig den PfadVomStart bzw. PfadZumEnde der übergebenen Stelle und Transition
	 * und ihrer möglichen Folge-Elemente durch Aufruf von {@link #setHasStartPath(IWfnTransitionAndPlace)}
	 * und {@link #setHasEndPath(IWfnTransitionAndPlace)}.
	 * @param origin das Element, von dem die neue Kante ausgeht
	 * @param ending das Element, in dem die neue Kante endet
	 */
	void infoCreatedArc(IWfnTransitionAndPlace origin, IWfnTransitionAndPlace ending) {
		if (origin.hasStartPath()) setHasStartPath(ending);
		if (ending.hasEndPath()) setHasEndPath(origin);
	}
	
	/**
	 * Aktualisiert wenn notwendig den PfadVomStart bzw. PfadZumEnde der übergebenen Stelle und Transition
	 * und ihrer möglichen Folge-Elemente durch Aufruf von {@link #setLostStartPath(IWfnTransitionAndPlace)}
	 * und {@link #setLostEndPath(IWfnTransitionAndPlace)}.
	 * @param origin das Element, von dem die gelöschte Kante ausging
	 * @param ending das Element, in dem die gelöschte Kante endete
	 */
	void infoDeletedArc(IWfnTransitionAndPlace origin, IWfnTransitionAndPlace ending) {
		if (ending.hasStartPath()) setLostStartPath(ending);
		if (origin.hasEndPath()) setLostEndPath(origin);
	}
	
	/**
	 * Rekursive Methode, bei der überpüft wird, ob das übergebene Element, wenn es bis jetzt auf einem 
	 * Pfad vom Start lag, dies jetzt immer noch tut, und wenn nein, diese Methode auf alle Folge-Elemente
	 * anwendet.
	 * @param elem zu überprüfendes Element
	 */
	void setLostStartPath(IWfnTransitionAndPlace elem) {
		if (elem.hasStartPath()) {
			ArrayList<IWfnTransitionAndPlace> starts = getPotentialStarts(elem);
			if ((starts == null)
					|| (starts.isEmpty())) {
				setAllElemOnStartPath(elem, false);
				for (IWfnTransitionAndPlace elementForwards : elem.getOutputElements())
					setLostStartPath(elementForwards);
			}
		}
	}

	/**
	 * Rekursive Methode, sie setzt {@link wfnmodell.interfaces.IWfnTransitionAndPlace#setHasStartPath(boolean)} des übergebenen 
	 * WFN-Elements und aller seiner Vorläufer-Elemente auf den übergebenen boolschen Wert.
	 * @param elem Element, dessen Attribut gesetzt werden soll, und die Attribute aller seiner Vorläufer
	 * @param b zu setzender boolscher Wert
	 */	
	private void setAllElemOnStartPath(IWfnTransitionAndPlace elem, boolean b) {
		if (!elem.getRecursiveMethodFlag()) {
			elem.setHasStartPath(b);
			if (elem.hasIncomingArcs()) {
				elem.setRecursiveMethodFlag(true);
				for (IWfnTransitionAndPlace elementBackwards : elem.getInputElements())
					setAllElemOnStartPath(elementBackwards, b);
				elem.setRecursiveMethodFlag(false);
			}
		}
	}

	/**
	 * Rekursive Methode, die aus dem Zirkelproblem des Zusammenhangs resultiert. Sie überprüft,
	 * welche der vorherigen Elemente eine mögliche Startstelle sein könnte.
	 * @param element das zu überprüfende Element
	 * @return Liste aller vorherigen Elemente, die keine eingehenden Kanten haben 
	 */
	private ArrayList<IWfnTransitionAndPlace> getPotentialStarts(IWfnTransitionAndPlace element) {
		ArrayList<IWfnTransitionAndPlace> result = new ArrayList<>(4);
		if (element.hasStartPath()) {
			if ((!element.hasIncomingArcs())
					&& (element.getWfnElementType() == EWfnElement.PLACE))
				result.add(element);
			else 
				if (!element.getRecursiveMethodFlag()) {
					element.setRecursiveMethodFlag(true);
					for (IWfnTransitionAndPlace elementBackwards: element.getInputElements()) {
						ArrayList<IWfnTransitionAndPlace> partResult = getPotentialStarts(elementBackwards);
						if (partResult != null)
							result.addAll(partResult);
					}
					element.setRecursiveMethodFlag(false);
				}
			return result;
		} else
			return result;
	}

	/**
	 * Rekursive Methode, bei der überpüft wird, ob das übergebene Element, wenn es bis jetzt auf einem 
	 * Pfad zum Ende lag, dies jetzt immer noch tut, und wenn nein, diese Methode auf alle Elemente
	 * anwendet, von denen Kanten ausgehen, die in dem übergebenen Element enden.
	 * @param elem zu überprüfendes Element
	 */
	void setLostEndPath(IWfnTransitionAndPlace elem) {
		if (elem.hasEndPath()) {
			ArrayList<IWfnTransitionAndPlace> ends = getPotentialEnds(elem);
			if ((ends == null)
					|| (ends.isEmpty())) {
				setAllElemOnEndPath(elem, false);
				for (IWfnTransitionAndPlace elementBackwards : elem.getInputElements())
					setLostEndPath(elementBackwards);
			}
		}
	}

	/**
	 * Rekursive Methode, sie setzt {@link wfnmodell.interfaces.IWfnTransitionAndPlace#setHasEndPath(boolean)} des übergebenen 
	 * WFN-Elements und aller seiner Folge-Elemente auf den übergebenen boolschen Wert.
	 * @param elem Element, dessen Attribut gesetzt werden soll, und die Attribute aller seiner Nachfolger
	 * @param b zu setzender boolscher Wert
	 */
	private void setAllElemOnEndPath(IWfnTransitionAndPlace elem, boolean b) {
		if (!elem.getRecursiveMethodFlag()) {
			elem.setHasEndPath(b);
			if (elem.hasOutgoingArcs()) {
				elem.setRecursiveMethodFlag(true);
				for (IWfnTransitionAndPlace elementForwards : elem.getOutputElements())
					setAllElemOnEndPath(elementForwards, b);
				elem.setRecursiveMethodFlag(false);
			}
		}
	}

	/**
	 * Rekursive Methode, die aus dem Zirkelproblem des Zusammenhangs resultiert. Sie überprüft,
	 * welche der nachfolgenden Elemente eine mögliche Endstelle sein könnte.
	 * @param element das zu überprüfende Element
	 * @return Liste aller nachfolgenden Elemente, die keine ausgehenden Kanten haben 
	 */
	private ArrayList<IWfnTransitionAndPlace> getPotentialEnds(IWfnTransitionAndPlace element) {
		ArrayList<IWfnTransitionAndPlace> result = new ArrayList<>(4);
		if (element.hasEndPath()) {
			if ((!element.hasOutgoingArcs())
					&& (element.getWfnElementType() == EWfnElement.PLACE))
				result.add(element);
			else 
				if (!element.getRecursiveMethodFlag()) {
					element.setRecursiveMethodFlag(true);
					for (IWfnTransitionAndPlace elementForwards: element.getOutputElements()) {
						ArrayList<IWfnTransitionAndPlace> partResult = getPotentialEnds(elementForwards);
						if (partResult != null)
							result.addAll(partResult);
					}
					element.setRecursiveMethodFlag(false);
				}
			return result;
		} else
			return result;
	}

	/**
	 * Rekursive Methode, welche {@link wfnmodell.interfaces.IWfnTransitionAndPlace#setHasStartPath(boolean)}
	 * des übergebenenen Elements auf true setzt, und sollten von dem übergebenen Element Kanten ausgehen,
	 * die in Elementen enden, welche bis jetzt noch keinen Pfad vom Start hatten, werden jene Elemente mit dieser 
	 * Methode aufgerufen.
	 * @param elem Element, welches jetzt einen Pfad vom Start hat
	 */
	void setHasStartPath(IWfnTransitionAndPlace elem) {
		elem.setHasStartPath(true);
		for (IWfnTransitionAndPlace elementForwards : elem.getOutputElements()) {
			if (!elementForwards.hasStartPath())
				setHasStartPath(elementForwards);
		}
	}

	/**
	 * Rekursive Methode, welche {@link wfnmodell.interfaces.IWfnTransitionAndPlace#setHasEndPath(boolean)}
	 * des übergebenenen Elements auf true setzt, und sollten von dem übergebenen Element Kanten enden,
	 * die von Elementen ausgehen, welche bis jetzt noch keinen Pfad zum Ende hatten, werden jene Elemente mit dieser 
	 * Methode aufgerufen.
	 * @param elem Element, welches jetzt einen Pfad zum Ende hat
	 */
	void setHasEndPath(IWfnTransitionAndPlace elem) {
		elem.setHasEndPath(true);
		for (IWfnTransitionAndPlace elementBackwards : elem.getInputElements()) {
			if (elementBackwards.hasEndPath() == false)
				setHasEndPath(elementBackwards);
		}
	}
		
}

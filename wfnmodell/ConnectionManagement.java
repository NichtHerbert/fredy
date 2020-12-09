package wfnmodell;

import java.util.ArrayList;

import wfnmodell.elemente.EWFNElement;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse, mit deren Hilfe der Überblick behalten werden soll, ob das vorhandene Netz zusammenhängend auf einem Pfad von der
 * Startstelle zur Endstelle liegt. Sie benutzt dafür die Methoden der Stellen und Transitionen
 * {@link wfnmodell.schnittstellen.IWFNElementOK#istAufPfadVomStart()} und
 * {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadVomStart(boolean)}, bzw.
 * {@link wfnmodell.schnittstellen.IWFNElementOK#istAufPfadZumEnde()} und
 * {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadZumEnde(boolean)}.
 */
class ConnectionManagement {

	/**
	 * Aktualisiert wenn notwendig den PfadVomStart bzw. PfadZumEnde der übergebenen Stelle und Transition
	 * und ihrer möglichen Folge-Elemente durch Aufruf von {@link #setHasStartPath(IWFNElementOK)}
	 * und {@link #setHasEndPath(IWFNElementOK)}.
	 * @param origin das Element, von dem die neue Kante ausgeht
	 * @param ending das Element, in dem die neue Kante endet
	 */
	void infoCreatedArc(IWFNElementOK origin, IWFNElementOK ending) {
		if (origin.istAufPfadVomStart()) setHasStartPath(ending);
		if (ending.istAufPfadZumEnde()) setHasEndPath(origin);
	}
	
	/**
	 * Aktualisiert wenn notwendig den PfadVomStart bzw. PfadZumEnde der übergebenen Stelle und Transition
	 * und ihrer möglichen Folge-Elemente durch Aufruf von {@link #setLostStartPath(IWFNElementOK)}
	 * und {@link #setLostEndPath(IWFNElementOK)}.
	 * @param origin das Element, von dem die gelöschte Kante ausging
	 * @param ending das Element, in dem die gelöschte Kante endete
	 */
	void infoDeletedArc(IWFNElementOK origin, IWFNElementOK ending) {
		if (ending.istAufPfadVomStart()) setLostStartPath(ending);
		if (origin.istAufPfadZumEnde()) setLostEndPath(origin);
	}
	
	/**
	 * Rekursive Methode, bei der überpüft wird, ob das übergebene Element, wenn es bis jetzt auf einem 
	 * Pfad vom Start lag, dies jetzt immer noch tut, und wenn nein, diese Methode auf alle Folge-Elemente
	 * anwendet.
	 * @param elem zu überprüfendes Element
	 */
	void setLostStartPath(IWFNElementOK elem) {
		if (elem.istAufPfadVomStart()) {
			ArrayList<IWFNElementOK> starts = getPotentialStarts(elem);
			if ((starts == null)
					|| (starts.isEmpty())) {
				setAllElemOnStartPath(elem, false);
				for (IWFNElementOK elementForwards : elem.getKantenZu())
					setLostStartPath(elementForwards);
			}
		}
	}

	/**
	 * Rekursive Methode, sie setzt {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadVomStart(boolean)} des übergebenen 
	 * WFN-Elements und aller seiner Vorläufer-Elemente auf den übergebenen boolschen Wert.
	 * @param elem Element, dessen Attribut gesetzt werden soll, und die Attribute aller seiner Vorläufer
	 * @param b zu setzender boolscher Wert
	 */	
	private void setAllElemOnStartPath(IWFNElementOK elem, boolean b) {
		if (!elem.hatRekursiveMethodeAufgerufen()) {
			elem.setPfadVomStart(b);
			if (elem.hatEingehendeKanten()) {
				elem.setHatRekursiveMethodeAufgerufen(true);
				for (IWFNElementOK elementBackwards : elem.getKantenVon())
					setAllElemOnStartPath(elementBackwards, b);
				elem.setHatRekursiveMethodeAufgerufen(false);
			}
		}
	}

	/**
	 * Rekursive Methode, die aus dem Zirkelproblem des Zusammenhangs resultiert. Sie überprüft,
	 * welche der vorherigen Elemente eine mögliche Startstelle sein könnte.
	 * @param element das zu überprüfende Element
	 * @return Liste aller vorherigen Elemente, die keine eingehenden Kanten haben 
	 */
	private ArrayList<IWFNElementOK> getPotentialStarts(IWFNElementOK element) {
		ArrayList<IWFNElementOK> result = new ArrayList<>(4);
		if (element.istAufPfadVomStart()) {
			if ((!element.hatEingehendeKanten())
					&& (element.getTyp() == EWFNElement.STELLE))
				result.add(element);
			else 
				if (!element.hatRekursiveMethodeAufgerufen()) {
					element.setHatRekursiveMethodeAufgerufen(true);
					for (IWFNElementOK elementBackwards: element.getKantenVon()) {
						ArrayList<IWFNElementOK> partResult = getPotentialStarts(elementBackwards);
						if (partResult != null)
							result.addAll(partResult);
					}
					element.setHatRekursiveMethodeAufgerufen(false);
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
	void setLostEndPath(IWFNElementOK elem) {
		if (elem.istAufPfadZumEnde()) {
			ArrayList<IWFNElementOK> ends = getPotentialEnds(elem);
			if ((ends == null)
					|| (ends.isEmpty())) {
				setAllElemOnEndPath(elem, false);
				for (IWFNElementOK elementBackwards : elem.getKantenVon())
					setLostEndPath(elementBackwards);
			}
		}
	}

	/**
	 * Rekursive Methode, sie setzt {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadZumEnde(boolean)} des übergebenen 
	 * WFN-Elements und aller seiner Folge-Elemente auf den übergebenen boolschen Wert.
	 * @param elem Element, dessen Attribut gesetzt werden soll, und die Attribute aller seiner Nachfolger
	 * @param b zu setzender boolscher Wert
	 */
	private void setAllElemOnEndPath(IWFNElementOK elem, boolean b) {
		if (!elem.hatRekursiveMethodeAufgerufen()) {
			elem.setPfadZumEnde(b);
			if (elem.hatAusgehendeKanten()) {
				elem.setHatRekursiveMethodeAufgerufen(true);
				for (IWFNElementOK elementForwards : elem.getKantenZu())
					setAllElemOnEndPath(elementForwards, b);
				elem.setHatRekursiveMethodeAufgerufen(false);
			}
		}
	}

	/**
	 * Rekursive Methode, die aus dem Zirkelproblem des Zusammenhangs resultiert. Sie überprüft,
	 * welche der nachfolgenden Elemente eine mögliche Endstelle sein könnte.
	 * @param element das zu überprüfende Element
	 * @return Liste aller nachfolgenden Elemente, die keine ausgehenden Kanten haben 
	 */
	private ArrayList<IWFNElementOK> getPotentialEnds(IWFNElementOK element) {
		ArrayList<IWFNElementOK> result = new ArrayList<>(4);
		if (element.istAufPfadZumEnde()) {
			if ((!element.hatAusgehendeKanten())
					&& (element.getTyp() == EWFNElement.STELLE))
				result.add(element);
			else 
				if (!element.hatRekursiveMethodeAufgerufen()) {
					element.setHatRekursiveMethodeAufgerufen(true);
					for (IWFNElementOK elementForwards: element.getKantenZu()) {
						ArrayList<IWFNElementOK> partResult = getPotentialEnds(elementForwards);
						if (partResult != null)
							result.addAll(partResult);
					}
					element.setHatRekursiveMethodeAufgerufen(false);
				}
			return result;
		} else
			return result;
	}

	/**
	 * Rekursive Methode, welche {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadVomStart(boolean)}
	 * des übergebenenen Elements auf true setzt, und sollten von dem übergebenen Element Kanten ausgehen,
	 * die in Elementen enden, welche bis jetzt noch keinen Pfad vom Start hatten, werden jene Elemente mit dieser 
	 * Methode aufgerufen.
	 * @param elem Element, welches jetzt einen Pfad vom Start hat
	 */
	void setHasStartPath(IWFNElementOK elem) {
		elem.setPfadVomStart(true);
		for (IWFNElementOK elementForwards : elem.getKantenZu()) {
			if (!elementForwards.istAufPfadVomStart())
				setHasStartPath(elementForwards);
		}
	}

	/**
	 * Rekursive Methode, welche {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadZumEnde(boolean)}
	 * des übergebenenen Elements auf true setzt, und sollten von dem übergebenen Element Kanten enden,
	 * die von Elementen ausgehen, welche bis jetzt noch keinen Pfad zum Ende hatten, werden jene Elemente mit dieser 
	 * Methode aufgerufen.
	 * @param elem Element, welches jetzt einen Pfad zum Ende hat
	 */
	void setHasEndPath(IWFNElementOK elem) {
		elem.setPfadZumEnde(true);
		for (IWFNElementOK elementBackwards : elem.getKantenVon()) {
			if (elementBackwards.istAufPfadZumEnde() == false)
				setHasEndPath(elementBackwards);
		}
	}
		
}

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
class ZusammenhangsVerwaltung {

	/**
	 * Aktualisiert wenn notwendig den PfadVomStart bzw. PfadZumEnde der übergebenen Stelle und Transition
	 * und ihrer möglichen Folge-Elemente durch Aufruf von {@link #setHatJetztPfadVomStart(IWFNElementOK)}
	 * und {@link #setHatJetztPfadZumEnde(IWFNElementOK)}.
	 * @param von das Element, von dem die neue Kante ausgeht
	 * @param zu das Element, in dem die neue Kante endet
	 */
	void infoNeueKante(IWFNElementOK von, IWFNElementOK zu) {
		if (von.istAufPfadVomStart()) setHatJetztPfadVomStart(zu);
		if (zu.istAufPfadZumEnde()) setHatJetztPfadZumEnde(von);
	}
	
	/**
	 * Aktualisiert wenn notwendig den PfadVomStart bzw. PfadZumEnde der übergebenen Stelle und Transition
	 * und ihrer möglichen Folge-Elemente durch Aufruf von {@link #setHatEinenPfadVomStartVerloren(IWFNElementOK)}
	 * und {@link #setHatEinenPfadZumEndeVerloren(IWFNElementOK)}.
	 * @param von das Element, von dem die gelöschte Kante ausging
	 * @param zu das Element, in dem die gelöschte Kante endete
	 */
	void infoGeloeschteKante(IWFNElementOK von, IWFNElementOK zu) {
		if (zu.istAufPfadVomStart()) setHatEinenPfadVomStartVerloren(zu);
		if (von.istAufPfadZumEnde()) setHatEinenPfadZumEndeVerloren(von);
	}
	
	/**
	 * Rekursive Methode, bei der überpüft wird, ob das übergebene Element, wenn es bis jetzt auf einem 
	 * Pfad vom Start lag, dies jetzt immer noch tut, und wenn nein, diese Methode auf alle Folge-Elemente
	 * anwendet.
	 * @param elem zu überprüfendes Element
	 */
	void setHatEinenPfadVomStartVerloren(IWFNElementOK elem) {
		if (elem.istAufPfadVomStart()) {
			ArrayList<IWFNElementOK> startListe = getStartPfadStartElemente(elem);
			if ((startListe == null)
					|| (startListe.isEmpty())) {
				setAlleVorgaengerAufPfadVomStart(elem, false);
				for (IWFNElementOK elementDanach : elem.getKantenZu())
					setHatEinenPfadVomStartVerloren(elementDanach);
			}
		}
	}

	/**
	 * Rekursive Methode, sie setzt {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadVomStart(boolean)} des übergebenen 
	 * WFN-Elements und aller seiner Vorläufer-Elemente auf den übergebenen boolschen Wert.
	 * @param elem Element, dessen Attribut gesetzt werden soll, und die Attribute aller seiner Vorläufer
	 * @param b zu setzender boolscher Wert
	 */	
	private void setAlleVorgaengerAufPfadVomStart(IWFNElementOK elem, boolean b) {
		if (!elem.hatRekursiveMethodeAufgerufen()) {
			elem.setPfadVomStart(b);
			if (elem.hatEingehendeKanten()) {
				elem.setHatRekursiveMethodeAufgerufen(true);
				for (IWFNElementOK elementDavor : elem.getKantenVon())
					setAlleVorgaengerAufPfadVomStart(elementDavor, b);
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
	private ArrayList<IWFNElementOK> getStartPfadStartElemente(IWFNElementOK element) {
		ArrayList<IWFNElementOK> ergebnis = new ArrayList<>(4);
		if (element.istAufPfadVomStart()) {
			if ((!element.hatEingehendeKanten())
					&& (element.getTyp() == EWFNElement.STELLE))
				ergebnis.add(element);
			else 
				if (!element.hatRekursiveMethodeAufgerufen()) {
					element.setHatRekursiveMethodeAufgerufen(true);
					for (IWFNElementOK elementDavor: element.getKantenVon()) {
						ArrayList<IWFNElementOK> teilErgebnis = getStartPfadStartElemente(elementDavor);
						if (teilErgebnis != null)
							ergebnis.addAll(teilErgebnis);
					}
					element.setHatRekursiveMethodeAufgerufen(false);
				}
			return ergebnis;
		} else
			return ergebnis;
	}

	/**
	 * Rekursive Methode, bei der überpüft wird, ob das übergebene Element, wenn es bis jetzt auf einem 
	 * Pfad zum Ende lag, dies jetzt immer noch tut, und wenn nein, diese Methode auf alle Elemente
	 * anwendet, von denen Kanten ausgehen, die in dem übergebenen Element enden.
	 * @param elem zu überprüfendes Element
	 */
	void setHatEinenPfadZumEndeVerloren(IWFNElementOK elem) {
		if (elem.istAufPfadZumEnde()) {
			ArrayList<IWFNElementOK> endListe = getEndPfadEndElemente(elem);
			if ((endListe == null)
					|| (endListe.isEmpty())) {
				setAlleNachfolgerAufPfadZumEnde(elem, false);
				for (IWFNElementOK elementDavor : elem.getKantenVon())
					setHatEinenPfadZumEndeVerloren(elementDavor);
			}
		}
	}

	/**
	 * Rekursive Methode, sie setzt {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadZumEnde(boolean)} des übergebenen 
	 * WFN-Elements und aller seiner Folge-Elemente auf den übergebenen boolschen Wert.
	 * @param elem Element, dessen Attribut gesetzt werden soll, und die Attribute aller seiner Nachfolger
	 * @param b zu setzender boolscher Wert
	 */
	private void setAlleNachfolgerAufPfadZumEnde(IWFNElementOK elem, boolean b) {
		if (!elem.hatRekursiveMethodeAufgerufen()) {
			elem.setPfadZumEnde(b);
			if (elem.hatAusgehendeKanten()) {
				elem.setHatRekursiveMethodeAufgerufen(true);
				for (IWFNElementOK elementDanach : elem.getKantenZu())
					setAlleNachfolgerAufPfadZumEnde(elementDanach, b);
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
	private ArrayList<IWFNElementOK> getEndPfadEndElemente(IWFNElementOK element) {
		ArrayList<IWFNElementOK> ergebnis = new ArrayList<>(4);
		if (element.istAufPfadZumEnde()) {
			if ((!element.hatAusgehendeKanten())
					&& (element.getTyp() == EWFNElement.STELLE))
				ergebnis.add(element);
			else 
				if (!element.hatRekursiveMethodeAufgerufen()) {
					element.setHatRekursiveMethodeAufgerufen(true);
					for (IWFNElementOK elementDanach: element.getKantenZu()) {
						ArrayList<IWFNElementOK> teilErgebnis = getEndPfadEndElemente(elementDanach);
						if (teilErgebnis != null)
							ergebnis.addAll(teilErgebnis);
					}
					element.setHatRekursiveMethodeAufgerufen(false);
				}
			return ergebnis;
		} else
			return ergebnis;
	}

	/**
	 * Rekursive Methode, welche {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadVomStart(boolean)}
	 * des übergebenenen Elements auf true setzt, und sollten von dem übergebenen Element Kanten ausgehen,
	 * die in Elementen enden, welche bis jetzt noch keinen Pfad vom Start hatten, werden jene Elemente mit dieser 
	 * Methode aufgerufen.
	 * @param elem Element, welches jetzt einen Pfad vom Start hat
	 */
	void setHatJetztPfadVomStart(IWFNElementOK elem) {
		elem.setPfadVomStart(true);
		for (IWFNElementOK elementDanach : elem.getKantenZu()) {
			if (!elementDanach.istAufPfadVomStart())
				setHatJetztPfadVomStart(elementDanach);
		}
	}

	/**
	 * Rekursive Methode, welche {@link wfnmodell.schnittstellen.IWFNElementOK#setPfadZumEnde(boolean)}
	 * des übergebenenen Elements auf true setzt, und sollten von dem übergebenen Element Kanten enden,
	 * die von Elementen ausgehen, welche bis jetzt noch keinen Pfad zum Ende hatten, werden jene Elemente mit dieser 
	 * Methode aufgerufen.
	 * @param elem Element, welches jetzt einen Pfad zum Ende hat
	 */
	void setHatJetztPfadZumEnde(IWFNElementOK elem) {
		elem.setPfadZumEnde(true);
		for (IWFNElementOK elementDavor : elem.getKantenVon()) {
			if (elementDavor.istAufPfadZumEnde() == false)
				setHatJetztPfadZumEnde(elementDavor);
		}
	}
		
}

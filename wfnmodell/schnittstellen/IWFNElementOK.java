package wfnmodell.schnittstellen;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Schnittstelle aller Elemente außer Kanten, also was Transitionen und Stellen gemein haben.
 */
public interface IWFNElementOK extends IWFNElement {
	/**
	 * Setzt den Namen des Elements.
	 * @param name der zu setzende Name
	 */
	void setName(String name);

	/**
	 * Gibt den Namen des Elements zurück.
	 * @return der Name des Elements, oder "", wenn es keinen hat
	 */
	String getName();

	/**
	 * Legt die Position des Elements fest.
	 * @param point die festzulegende Position
	 */
	void setPosition(Point point);

	/**
	 * Gibt die Position des Elements zurück.
	 * @return die Position des Elements
	 */
	Point getPosition();
	
	/**
	 * Gibt zurück, ob das Element eingehende Kanten hat.
	 * @return true, wenn das Element eingehende Kanten hat
	 */
	boolean hatEingehendeKanten();
	
	/**
	 * Gibt zurück, ob das Element ausgehende Kanten hat.
	 * @return true, wenn das Element ausgehende Kanten hat
	 */
	boolean hatAusgehendeKanten();

	/**
	 * Fügt dem Element eine eingehende Kante hinzu.
	 * @param elem Element, von dem diese Kante ausgeht
	 * @return true, wenn das Hinzufügen erfolgreich war
	 */
	boolean addKanteVon(IWFNElementOK elem);

	/** Gibt eine Liste aller Ausgangs-Elemente zurück, deren Kanten in diesem Element enden. 
	 * @return Liste aller Elemente, von denen Kanten zu diesem Element führen
	 */
	ArrayList<IWFNElementOK> getKantenVon();
	
	/**
	 * Fügt dem Element eine ausgehende Kante hinzu.
	 * @param elem Element, bei dem diese Kante endet
	 * @return true, wenn das Hinzufügen erfolgreich war
	 */
	boolean addKanteZu(IWFNElementOK elem);

	/**
	 * Gibt eine Liste aller Elemente zurück, in welchen Kanten enden, die von diesem Element ausgehen.
	 * @return Liste aller Elemente, in denen Kanten enden, die von diesem Element ausgehen
	 */
	ArrayList<IWFNElementOK> getKantenZu();

	/**
	 * Gibt an, ob sich das Element auf mindestens einem gerichteten Pfad von mindestens 
	 * einer möglichen Startstelle befindet.
	 * @return true, wenn sich das Element auf einem gerichteten Pfad vom Start befindet
	 */
	boolean istAufPfadVomStart();

	/**
	 * Gibt an, ob sich das Element auf mindestens einem gerichteten Pfad zu mindestens einer 
	 * möglichen Endstelle befindet.
	 * @return true, wenn sich das Element auf einem gerichteten Pfad zum Ende befindet
	 */
	boolean istAufPfadZumEnde();
	
	/**
	 * Legt fest, ob sich das Element auf mindestens einem gerichteten Pfad von mindestens 
	 * einer möglichen Startstelle befindet.
	 * @param pfadVomStart zu setzender boolescher Wert
	 */
	void setPfadVomStart(boolean pfadVomStart);
	
	/**
	 * Legt fest, ob sich das Element auf mindestens einem gerichteten Pfad zu mindestens einer 
	 * möglichen Endstelle befindet.
	 * @param pfadZumEnde zu setzender boolescher Wert
	 */
	void setPfadZumEnde(boolean pfadZumEnde);
	
	/** 
	 * Entfernt eine ausgehende Kante.
	 * @param zu Element, zu dem die zu entfernende Kante führt
	 */
	void removeKanteZu(IWFNElementOK zu);

	/**
	 * Entfernt eine eingehende Kante.
	 * @param von Element, von dem die eingehende Kante ausgeht
	 */
	void removeKanteVon(IWFNElementOK von);
}

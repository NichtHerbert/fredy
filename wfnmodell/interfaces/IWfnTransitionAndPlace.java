package wfnmodell.interfaces;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Schnittstelle aller Elemente außer Kanten, also was Transitionen und Stellen gemein haben.
 */
public interface IWfnTransitionAndPlace extends IWfnElement {
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
	boolean hasIncomingArcs();
	
	/**
	 * Gibt zurück, ob das Element ausgehende Kanten hat.
	 * @return true, wenn das Element ausgehende Kanten hat
	 */
	boolean hasOutgoingArcs();

	/**
	 * Fügt dem Element eine eingehende Kante hinzu.
	 * @param elem Element, von dem diese Kante ausgeht
	 * @return true, wenn das Hinzufügen erfolgreich war
	 */
	boolean addInputElement(IWfnTransitionAndPlace elem);

	/** Gibt eine Liste aller Ausgangs-Elemente zurück, deren Kanten in diesem Element enden. 
	 * @return Liste aller Elemente, von denen Kanten zu diesem Element führen
	 */
	ArrayList<IWfnTransitionAndPlace> getInputElements();
	
	/**
	 * Fügt dem Element eine ausgehende Kante hinzu.
	 * @param elem Element, bei dem diese Kante endet
	 * @return true, wenn das Hinzufügen erfolgreich war
	 */
	boolean addOutputElements(IWfnTransitionAndPlace elem);

	/**
	 * Gibt eine Liste aller Elemente zurück, in welchen Kanten enden, die von diesem Element ausgehen.
	 * @return Liste aller Elemente, in denen Kanten enden, die von diesem Element ausgehen
	 */
	ArrayList<IWfnTransitionAndPlace> getOutputElements();

	/**
	 * Gibt an, ob sich das Element auf mindestens einem gerichteten Pfad von mindestens 
	 * einer möglichen Startstelle befindet.
	 * @return true, wenn sich das Element auf einem gerichteten Pfad vom Start befindet
	 */
	boolean hasStartPath();

	/**
	 * Gibt an, ob sich das Element auf mindestens einem gerichteten Pfad zu mindestens einer 
	 * möglichen Endstelle befindet.
	 * @return true, wenn sich das Element auf einem gerichteten Pfad zum Ende befindet
	 */
	boolean hasEndPath();
	
	/**
	 * Legt fest, ob sich das Element auf mindestens einem gerichteten Pfad von mindestens 
	 * einer möglichen Startstelle befindet.
	 * @param hasStartPath zu setzender boolescher Wert
	 */
	void setHasStartPath(boolean b);
	
	/**
	 * Legt fest, ob sich das Element auf mindestens einem gerichteten Pfad zu mindestens einer 
	 * möglichen Endstelle befindet.
	 * @param hasEndPath zu setzender boolescher Wert
	 */
	void setHasEndPath(boolean b);
	
	/** 
	 * Entfernt eine ausgehende Kante.
	 * @param target Element, zu dem die zu entfernende Kante führt
	 */
	void removeOutputElements(IWfnTransitionAndPlace target);

	/**
	 * Entfernt eine eingehende Kante.
	 * @param source Element, von dem die eingehende Kante ausgeht
	 */
	void removeInputElement(IWfnTransitionAndPlace source);
}

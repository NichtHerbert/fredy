package wfnmodel.importexport;

import wfnmodel.elements.EWfnElement;

/**
 * Hilfsklasse zum Zwischenspeichern der Elemente eines Workflownetzes, 
 * während das WFN importiert oder exportiert wird.
 */
public class PnmlElement {
	/** Typ des Elements */
	private EWfnElement type;
	/** pnml-Datei-ID des Elements*/
	private String pnmlID;
	/** Name des Elements*/
	private String name;
	/** X-Position des Elements*/
	private String x;
	/** Y-Position des Elements*/
	private String y;
	/** Markierung des Elements (relevant, sollte es vom Typ Stelle sein)*/
	private String marking;
	/** pnml-Datei-ID des Ausgangselements, von dem die Kante ausgeht 
	 * (natürlich nur relevant, sollte dass Element vom Typ Kante sein)
	 */
	private String pnmlIDSource;
	/** pnml-Datei-ID des Endelements, in welchem die Kante endet 
	 * (natürlich nur relevant, sollte dass Element vom Typ Kante sein)
	 */
	private String pnmlIDTarget;


	/**
	 * Konstruktor zum instanzieren einer Stelle oder einer Transition.
	 * Der Konstruktor instanziert ein neues Objekt dieser Klasse mit den übergebenen Parametern.
	 * Alle anderen Attribute des Objekts haben den Wert "".
	 * @param type Typ des zu instanzierenden Elements
	 * @param pnmlID pnml-ID des zu instanzierenden Elements
	 */
	public PnmlElement(EWfnElement type, String pnmlID) {
		this(type, pnmlID, "", "", "");
	}

	/**
	 * Konstruktor zum instanzieren einer Kante.
	 * Der Konstruktor instanziert ein neues Objekt dieser Klasse mit den übergebenen Parametern.
	 * Alle anderen Attribute des Objekts haben den Wert "".
	 * @param type Typ des zu instanzierenden Elements
	 * @param pnmlID pnml-ID des zu instanzierenden Elements
	 * @param pnmlIDSource pnml-Datei-ID des Ausgangselements, von dem die Kante ausgeht 
	 * @param pnmlIDTarget pnml-Datei-ID des Endelements, in welchem die Kante endet
	 */
	public PnmlElement(EWfnElement type, String pnmlID, String pnmlIDSource, String pnmlIDTarget) {
		super();
		this.type = type;
		this.pnmlID = pnmlID;
		this.pnmlIDSource = pnmlIDSource;
		this.pnmlIDTarget = pnmlIDTarget;
		name = "";
		x = "";
		y = "";
		marking = "";
	}

	/**
	 * Konstruktor zum instanzieren einer Stelle oder einer Transition.
	 * Der Konstruktor instanziert ein neues Objekt dieser Klasse mit den übergebenen Parametern.
	 * Alle anderen Attribute des Objekts haben den Wert "".
	 * @param type Typ des zu instanzierenden Elements
	 * @param pnmlID pnml-ID des zu instanzierenden Elements
	 * @param name Name des zu instanzierenden Elements
	 * @param x X-Position des Elements
	 * @param y Y-Position des Elements
	 */
	public PnmlElement(EWfnElement type, String pnmlID, String name, String x, String y) {
		this(type, pnmlID, name, x, y, "");
	}

	/**
	 * Konstruktor zum instanzieren einer Stelle oder einer Transition. 
	 * Der Konstruktor instanziert ein neues Objekt dieser Klasse mit den übergebenen Parametern.
	 * @param marking wenn nicht "" muss es sich um eine Stelle handeln.
	 * @param type Typ des zu instanzierenden Elements
	 * @param pnmlID pnml-ID des zu instanzierenden Elements
	 * @param name Name des zu instanzierenden Elements
	 * @param x X-Position des Elements
	 * @param y Y-Position des Elements
	 */
	public PnmlElement(EWfnElement type, String pnmlID, String name, String x, String y, String marking) {
		super();
		this.type = type;
		this.pnmlID = pnmlID;
		this.name = name;
		this.x = x;
		this.y = y;
		this.marking = marking;
		pnmlIDSource = "";
		pnmlIDTarget = "";
	}

	/**
	 * Gibt den Typ des Elements zurück.
	 * @return Typ des Elements
	 */
	public EWfnElement getType() {
		return type;
	}

	/**
	 * Gibt die pnml-Datei-ID eines Elements zurück
	 * @return pnml-ID des Elements
	 */
	public String getPNMLID() {
		return pnmlID;
	}

	/**
	 * Gibt den Namen eines Elements zurück.
	 * @return Name des Elements
	 */
	public String getName() {
		return name;
	}

	/**
	 * Legt den Namen eines Elements fest.
	 * @param name festzulegender Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt die X-Position eines Elements zurück.
	 * @return die X-Position eines Elements
	 */
	public String getX() {
		return this.x;
	}

	/**
	 * Gibt die Y-Position eines Elements zurück.
	 * @return die Y-Position eines Elements
	 */
	public String getY() {
		return this.y;
	}

	/**
	 * Legt die Position eines Elements fest.
	 * @param x die X-Position des Elements
	 * @param y die Y-Position des Elements
	 */
	public void setPosition(String x, String y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gibt die Markierung des Elements zurück.
	 * @return die Markierung des Elements
	 */
	public String getMarking() {
		return marking;
	}

	/**
	 * Legt die Markierung eines Elements fest.
	 * @param marking die festzulegende Markierung des Elements
	 */
	public void setMarking(String marking) {
		this.marking = marking;
	}

	/**
	 * Gibt die pnml-ID des KantenausgangsElements zurück.
	 * @return pnml-ID des KantenausgangsElements oder "" wenn das Element keine Kante ist
	 */
	public String getPnmlIDSource() {
		return pnmlIDSource;
	}

	/**
	 * Legt die pnml-ID des Kantenausganselements fest.
	 * @param pnmlIDSource die festzulegende pnml-ID des Kantenausgangselements
	 */
	public void setPnmlIDSource(String pnmlIDSource) {
		this.pnmlIDSource = pnmlIDSource;
	}

	/**
	 * Gibt die pnml-ID des Kantenendelements zurück.
	 * @return pnml-ID des Kantenendelements oder "" wenn das Element keine Kante ist
	 */
	public String getPnmlIDTarget() {
		return pnmlIDTarget;
	}

	/**
	 * Legt die pnml-ID des KantenEndelements fest.
	 * @param pnmlIDTarget die festzulegende pnml-ID des Kantenendelements
	 */
	public void setPnmlIDTarget(String pnmlIDTarget) {
		this.pnmlIDTarget = pnmlIDTarget;
	}

}

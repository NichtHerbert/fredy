package verwaltung;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.MouseInputAdapter;

import gui.EWfnEditModus;
import gui.IZentraleKonstanten;
import horcherschnittstellen.IEditorModusHorcher;
import horcherschnittstellen.IZeichnungBenoetigtHorcher;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnModelChanging;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse zur Verwaltung der möglichen Mausaktionen im {@link gui.JPanelEditor}.
 *
 */
class MausVerwaltung extends MouseInputAdapter implements IEditorModusHorcher,
														  IZentraleKonstanten {

	/**Schnittstelle um Veränderungen am Datenmodell vorzunehmen*/
	private IWfnModelChanging wfnModell;
	/**Die aktuelle {@link ZoomFaktorVerwaltung}. */
	private ZoomFaktorVerwaltung zoom;
	/** Liste der ausgewählten Elemente */
	private AuswahlVerwaltung<IWfnElement> auswahl;
	/**Die aktuelle {@link PositionsVerwaltung} */
	private PositionsVerwaltung koordinaten;
	/**Die aktuelle {@link MarkierungsVerwaltung} */
	private MarkierungsVerwaltung markierungsVerwaltung;
	
	/**(Für den Editormodus SELECT) Gibt an ob es ein {@link #startElement} gibt.*/
	private boolean gibtEsEinStartElement;
	/**(Für den Editormodus SELECT) Dasjenige ausgewählte Element, über dem sich der Mauszeiger befand,
	 * als eine Maustaste gedrückt wurde.*/
	private IWfnElement startElement;
	/**(Für den Editormodus SELECT) Die Mausposition, wenn die Maustaste gedrückt wurde.*/
	private Point startMausPosition;
	/** (Für den Editormodus SELECT) Zum Speichern (bei einer Auswahl größer 1) 
	 * der Abstandsverhältnisse der ausgewählten Elemente zum {@link #startElement}. */
	private HashMap<IWfnElement, Point> auswahlElementAbstand;
	/**(Für den Editormodus SELECT) Zum Speichern (bei einer Auswahl größer 1) 
	 * der größten x und der größten y Verschiebung der ausgewählten Elemente zum {@link #startElement}
	 * (so dass man die Auswahl nicht über den Rand hinaus schieben kann).*/
	private int xNiedrigerAlsStartElement, yNiedrigerAlsStartElement;
	/** (Für den Editormus ADD_ARC) Gibt an, ob schon ein Kanteausgangselement gewählt wurde.*/
	private boolean kanteAusgangsElementAusgewaehlt;
	/**(Für den Editormodus ADD_ARC) Das für die hinzuzufügende Kante gewählte Ausgangselement.*/
	private IWfnTransitionAndPlace kantenAusgangsElement;
	/**Der aktuelle Editormodus */
	private EWfnEditModus editorModus;
	/**Liste der Horcher, die informiert werden möchten, wenn die Zeichnung einer Linie oder eines Rechtecks benötigt wird*/
	private ArrayList<IZeichnungBenoetigtHorcher> zeichnungBenoetigtHorcherListe;

	/**
	 * Instanziert eine MausVerwaltung und stellt den Editormodus auf SELECT. 
	 * @param wfnModell Schnittstelle um Veränderungen am Datenmodell vorzunehmen
	 * @param koordinaten Die aktuelle {@link PositionsVerwaltung} 
	 * @param auswahl Die aktuelle {@link AuswahlVerwaltung}
	 * @param markierungsVerwaltung Die aktuelle {@link MarkierungsVerwaltung}
	 * @param zoom Die aktuelle {@link ZoomFaktorVerwaltung}
	 */
	MausVerwaltung(IWfnModelChanging wfnModell,
						PositionsVerwaltung koordinaten,
						AuswahlVerwaltung<IWfnElement> auswahl,
						MarkierungsVerwaltung markierungsVerwaltung,
						ZoomFaktorVerwaltung zoom) {
		this.wfnModell = wfnModell;
		this.koordinaten = koordinaten;
		this.auswahl = auswahl;
		this.markierungsVerwaltung = markierungsVerwaltung;
		this.zoom = zoom;
		gibtEsEinStartElement = false;
		kanteAusgangsElementAusgewaehlt = false;
		auswahlElementAbstand = new HashMap<>(5);
		xNiedrigerAlsStartElement = 0;
		yNiedrigerAlsStartElement = 0;
		editorModus = EWfnEditModus.SELECT;
		zeichnungBenoetigtHorcherListe = new ArrayList<>(1);	
	}
	
	/**
	 * Fügt den übergebenen Horcher der {@link #zeichnungBenoetigtHorcherListe} hinzu.
	 * @param horcher wird der {@link #zeichnungBenoetigtHorcherListe} hinzugefügt
	 */
	void addZeichnungBenoetigtHorcher(IZeichnungBenoetigtHorcher horcher) {
		zeichnungBenoetigtHorcherListe.add(horcher);
	}
	
	/**
	 * Entfernt den übergebenen Horcher von der {@link #zeichnungBenoetigtHorcherListe}.
	 * @param horcher wird von der {@link #zeichnungBenoetigtHorcherListe} enfernt
	 */
	void removeZeichnungBenoetigtHorcher(IZeichnungBenoetigtHorcher horcher) {
		if (zeichnungBenoetigtHorcherListe.contains(horcher))
			zeichnungBenoetigtHorcherListe.remove(horcher);
	}
	
	/**
	 * Informiert alle Horcher der {@link #zeichnungBenoetigtHorcherListe} über eine benötigte Zeichnung.
	 * @param form LINIE oder RECHTECK
	 * @param startMausPosition Eckpunkt/Startpunkt der Zeichnung
	 * @param jetztMausPosition Eckpunkt/Endpunkt der Zeichnung
	 */
	private void fireZeichnungBenoetigt(int form, Point startMausPosition, Point jetztMausPosition) {
		for (IZeichnungBenoetigtHorcher horcher : zeichnungBenoetigtHorcherListe)
			horcher.zeichnungBenoetigt(form, startMausPosition, jetztMausPosition);
	}

	/* (non-Javadoc)
	 * @see horcherschnittstellen.IEditorModusHorcher#editorModusGeaendert(gui.EWfnEditModus)
	 * Falls durch die Modus-Änderung nötig, wird die derzeitige Auswahl geleert oder die Kanten-Auswahl abgebrochen
	 * und jeweils ein neu-zeichnen ausgelöst.
	 */
	@Override
	public void editorModusGeaendert(EWfnEditModus neuerModus) {
		if (editorModus != neuerModus) {
			switch (editorModus) {
			case SELECT:
				auswahl.clearAndAddAndFire(null, NEUE_AUSWAHL);
				break;
			case ADD_ARC:
				if (kanteAusgangsElementAusgewaehlt) {
					fireZeichnungBenoetigt(NICHTS, null, null);
					kanteAusgangsElementAusgewaehlt = false;
					kantenAusgangsElement = null;
				}
				break;
			default:break;
			}
			editorModus = neuerModus;
		}
	}
	
	/* (non-Javadoc)
	 * Falls im Editormodus SELECT eine Maustaste gedrückt wird
	 * und sich an dieser Stelle ein Element befindet,
	 * wird die auswahl aktualisiert, 
	 * und gegebenenfalls die Abstände der ausgewählten Elemente zueinander.
	 * Falls im Editormodus SELECT eine Maustaste gedrückt wird
	 * und es befindet sich an dieser Stelle kein Element,
	 * wird die Mausposition in #startMausPosition gespeichert,
	 * für das mögliche Zeichnen eines Rechtecks bei einer Mausbewegung.
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (editorModus == EWfnEditModus.SELECT) {
			IWfnElement elem = koordinaten.getWasDaIst(e.getPoint());
			if (elem != null) {
				gibtEsEinStartElement = true;
				startElement = elem;
				xNiedrigerAlsStartElement = 0;
				yNiedrigerAlsStartElement = 0;
				if (!auswahl.contains(startElement)) {
					if (!e.isControlDown()) 
						auswahl.clear();
					auswahl.addAndFire(startElement, NEUE_AUSWAHL);
				}
				if (auswahl.size() > 1) {
					int x, y;
					if (startElement.getWfnElementType() != EWfnElement.ARC ) {
						x = ((IWfnTransitionAndPlace)startElement).getPosition().x;
						y = ((IWfnTransitionAndPlace)startElement).getPosition().y;
					} else {
						Point p = zoom.ohne(e.getPoint());
						x = p.x;
						y = p.y;
					}
					auswahlElementAbstand.clear();
					for (IWfnElement auswahlElem : auswahl) {
						if ((auswahlElem.getWfnElementType() != EWfnElement.ARC)
								&& (auswahlElem != startElement)) {
							Point abstandZuStart = new Point(
									((IWfnTransitionAndPlace)auswahlElem).getPosition().x - x,
									((IWfnTransitionAndPlace)auswahlElem).getPosition().y - y);
							if (abstandZuStart.x < xNiedrigerAlsStartElement) 
								xNiedrigerAlsStartElement = abstandZuStart.x;
							if (abstandZuStart.y < yNiedrigerAlsStartElement)
								yNiedrigerAlsStartElement = abstandZuStart.y;
							auswahlElementAbstand.put(auswahlElem, abstandZuStart);
						}
					}
					xNiedrigerAlsStartElement *= -1;
					yNiedrigerAlsStartElement *= -1;
				}
			} else {
				startMausPosition = zoom.ohne(e.getPoint());
			}
			
		}
		super.mousePressed(e);
	}

	/* (non-Javadoc)
	 * 
	 * Falls im Editormodus SELECT die Maus mit gedrückter Maustaste bewegt wird, 
	 * und es ausgewählte Elemente gibt,
	 * werden diese Elemente der Mausbewegung entsprechend verschoben,
	 * aber in ihrer Entfernung zum Element, über welchem sich die Maus befindet (dem #startElement), 
	 * bleiben die Elemente gleich (gespeichert in #auswahlElementAbstand).
	 * 
	 * Falls im Editormodus SELECT die Maus mit gedrückter Maustaste bewegt wird, 
	 * und es gibt keine ausgewählten Elemente, 
	 * dann wird ein Rechteck gezeichnet zwischen der jetzigen Mausposition
	 * und der Position, an der die Maustaste gedrückt wurde. (#startMausPosition)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (editorModus == EWfnEditModus.SELECT) {
			if (gibtEsEinStartElement) {
				Point mausPunktOhneZoom = zoom.ohne(e.getPoint());
				if  ((startElement.getWfnElementType() != EWfnElement.ARC)
						&& (mausPunktOhneZoom.x > (xNiedrigerAlsStartElement + EWfnElement.BASEFACTOR))
						&& (mausPunktOhneZoom.y > (yNiedrigerAlsStartElement + EWfnElement.BASEFACTOR))) {
					((IWfnTransitionAndPlace)startElement).setPosition(mausPunktOhneZoom);
					if (auswahl.size() > 1) {
						for (IWfnElement auswahlElem : auswahl) {
							if ((auswahlElem.getWfnElementType() != EWfnElement.ARC)
									&& (auswahlElem != startElement)) {
								Point position = ((IWfnTransitionAndPlace)auswahlElem).getPosition();
								position.setLocation(mausPunktOhneZoom.getX() + auswahlElementAbstand.get(auswahlElem).x,
										mausPunktOhneZoom.getY() + auswahlElementAbstand.get(auswahlElem).y);
							}
						}
					}
					auswahl.fireAuswahlAenderungEingetreten(NEUE_AUSWAHL);
				}
			} else {
				fireZeichnungBenoetigt(RECHTECK, startMausPosition, zoom.ohne(e.getPoint()));
			}
		}
		super.mouseDragged(e);
	}

	/* (non-Javadoc)
	 * Falls im Editormodus ADD_ARC die Maus bewegt wird,
	 * und schon eine Kante oder Transition als Kantenausgangselement ausgewählt wurde,
	 * wird eine Linie vom #kantenAusgangsElement zur Cursorposition gezeichnet. 
	 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if ((editorModus == EWfnEditModus.ADD_ARC) 
				&& (kanteAusgangsElementAusgewaehlt)) {
			fireZeichnungBenoetigt(
					LINIE,
					kantenAusgangsElement.getPosition(),
					zoom.ohne(e.getPoint()));
		}
		super.mouseMoved(e);
	}

	/* (non-Javadoc)
	 * 
	 * Falls die linke Maustaste losgelassen wird im Editormodus SELECT,
	 *   wird die Auswahl aktualisiert.
	 * Falls die linke Maustaste losgelassen wird im Editormodus ADD_PLACE,
	 * 	 dann wird via Datenmodell eine neue Stelle instanziert. 
	 * Falls die linke Maustaste losgelassen wird im Editormodus ADD_TRANSITION,
	 * 	 dann wird via Datenmodell eine neue Transition instanziert.
	 * Falls die linke Maustaste losgelassen wird im Editormodus ADD_ARC,
	 * 	 und es befindet sich an der Mausposition ein Element,
	 * 	 so wird dieses ein Kantenausgangselement,
	 *   oder falls ein solches schon gibt, 
	 *   und der Elementtyp des neuen Elements anders ist als der des Kantenausgangselements,
	 *   so wird via Datenmodell eine neue Kante instanziert.  	 
	 * 
	 * Falls die rechte Maustaste losgelassen wird,
	 *   und es befindet sich an dieser Stelle eine aktivierte Transition,
	 *   so wird diese geschaltet.
	 *    
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton()== MouseEvent.BUTTON1)
			switch (editorModus) {
			case SELECT:
				if (gibtEsEinStartElement) {
					IWfnElement neueAuswahl = koordinaten.getWasDaIst(e.getPoint());
					if (neueAuswahl != null) {
						if (!auswahl.contains(neueAuswahl)) {
							if (!e.isControlDown()) auswahl.clear();
							auswahl.addAndFire(neueAuswahl, NEUE_AUSWAHL);
						}
					} else 
						if (auswahl.size() != 0) {
							auswahl.clearAndAddAndFire(null, NEUE_AUSWAHL);
						}
				} else {
					auswahl.clearAndAddALLAndFire(koordinaten.getWasDaIst(startMausPosition, zoom.ohne(e.getPoint())),NEUE_AUSWAHL);
				}
				gibtEsEinStartElement = false;
				break;
			case ADD_PLACE:
				wfnModell.createPlace(zoom.ohne(e.getPoint()));
				break;
			case ADD_TRANSITION:
				wfnModell.createTransition(zoom.ohne(e.getPoint()));
				break;
			case ADD_ARC:
				IWfnElement element = koordinaten.getWasDaIst(e.getPoint());
				if ((element != null)
					&& (element.getWfnElementType() != EWfnElement.ARC)) {
						if (!kanteAusgangsElementAusgewaehlt) {
							kantenAusgangsElement = (IWfnTransitionAndPlace) element;
							kanteAusgangsElementAusgewaehlt = true;
							auswahl.clearAndAddAndFire(element, KANTEN_AUSWAHL);
						} else {
							if (element == kantenAusgangsElement) {
								kantenAusgangsElement = null;
								kanteAusgangsElementAusgewaehlt = false;
								auswahl.clearAndFire(KANTEN_AUSWAHL);
							} else {
								if (element.getWfnElementType() != kantenAusgangsElement.getWfnElementType()) {
									wfnModell.createArc(kantenAusgangsElement, (IWfnTransitionAndPlace) element);
									kanteAusgangsElementAusgewaehlt = false;
									kantenAusgangsElement = null;
								}
							}
					}
				} 
				default:break;
			}
		else 
			if (e.getButton()== MouseEvent.BUTTON3) {
				markierungsVerwaltung.schalteWennElementTransition(
						koordinaten.getWasDaIst(e.getPoint()));
			}
		super.mouseReleased(e);
	}


}	

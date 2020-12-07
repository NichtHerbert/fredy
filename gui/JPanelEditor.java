package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import horcherschnittstellen.IAuswahlVeraenderungsHorcher;
import horcherschnittstellen.IElementGroessenHorcher;
import horcherschnittstellen.IWFNModellStatusHorcher;
import horcherschnittstellen.IZeichnungBenoetigtHorcher;
import horcherschnittstellen.IZoomFaktorVeraenderungsHorcher;
import wfnmodell.WFNStatusInfo;
import wfnmodell.elemente.EWFNElement;
import wfnmodell.schnittstellen.IWFNElement;
import wfnmodell.schnittstellen.IWFNElementKante;
import wfnmodell.schnittstellen.IWFNElementTransition;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Panel zur Darstellung des Workflownetzes.
 *
 */
public class JPanelEditor extends JPanel implements IWFNModellStatusHorcher, 
													IAuswahlVeraenderungsHorcher,
													IZeichnungBenoetigtHorcher,
													IZoomFaktorVeraenderungsHorcher,
													IElementGroessenHorcher {

	private static final long serialVersionUID = -5582213274116886884L;
	
	/**
	 * Der letztübermittelte Zustand/Status des Workflownetzes.
	 */
	private WFNStatusInfo statusInfo;
	
	/**
	 *  Liste der momentan ausgewählten WFN-Elemente.
	 */
	private ArrayList<? extends IWFNElement> ausgewaehlteElemente;
	private boolean istZeichnungBenoetigt;
	private int benoetigteForm;
	private Point zeichnungPunkt1, zeichnungPunkt2;
	private double zoomFaktor;
	private int auswahlArt;
	/* Die Darstellungsgröße von Stellen, Transitionen und Pfeilspitzen*/
	private int elementGroesse;
	

	public JPanelEditor() {
		
		ausgewaehlteElemente = null;
		auswahlArt = NEUE_AUSWAHL;
		istZeichnungBenoetigt = false;
		benoetigteForm = NICHTS;
		zoomFaktor = 1d;
		statusInfo = new WFNStatusInfo(); 
		elementGroesse = EWFNElement.URGROESSE;

		setBackground(Color.WHITE);
	} 

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		zoomFaktorEinberechnung(g2);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		zeichneAuswahl(g2);
		zeichneMarkierungenUndAktivierteTransitionen(g2);
		zeichneAlleElemente(g2);
		zeichneFormBeiMausbewegung(g2);
	}

	/**
	 * Zeichnet die in der Liste {@link #ausgewaehlteElemente} gespeicherten WFN-Elemente
	 * durch Aufruf der Methode {@link wfnmodell.elemente.EWFNElement#zeichneAlsAusgewaehlt(Graphics2D, Point, Color)},
	 * wobei {@link #auswahlArt} Einfluss auf die Farbe hat.
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	 */
	private void zeichneAuswahl(Graphics2D g2) {
		if ((ausgewaehlteElemente != null) 
				&& (!ausgewaehlteElemente.isEmpty())) {
			switch (auswahlArt) {
			case NEUE_AUSWAHL:
				for (IWFNElement elem : ausgewaehlteElemente) {
						if (elem.getTyp() != EWFNElement.KANTE) {
							elem.getTyp().zeichneAlsAusgewaehlt(
									g2, 
									((IWFNElementOK) elem).getPosition(), 
									EEditorFarben.AUSWAHL, 
									elementGroesse);
						} else {
							elem.getTyp().zeichneAlsAusgewaehlt(
									g2, 
									((IWFNElementKante) elem).getVon().getTyp(),
									((IWFNElementKante) elem).getVon().getPosition(),
									((IWFNElementKante) elem).getZu().getPosition(),
									EEditorFarben.AUSWAHL, 
									elementGroesse);
						}
				}	
				break;
			case KANTEN_AUSWAHL:
				IWFNElementOK kantenStart = (IWFNElementOK) ausgewaehlteElemente.get(0);
				kantenStart.getTyp().zeichneAlsAusgewaehlt(
						g2, 
						kantenStart.getPosition(), 
						EEditorFarben.KANTEN_ANFANG_AUSGEWAEHLT, 
						elementGroesse);
				break;
			default: break;
			}
		}
	}

	/**
	 * Zeichnet, wenn {@link #istZeichnungBenoetigt} wahr ist, je nach {@link #benoetigteForm}
	 * ein Rechteck oder eine Linie zwischen {@link #zeichnungPunkt1} und {@link #zeichnungPunkt2}.
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	 */
	private void zeichneFormBeiMausbewegung(Graphics2D g2) {
		if (istZeichnungBenoetigt) {
			switch(benoetigteForm) {
			case RECHTECK:
				int x, y, hoehe, breite;
				if (zeichnungPunkt1.x < zeichnungPunkt2.x) {
					x = zeichnungPunkt1.x;
					breite = zeichnungPunkt2.x - zeichnungPunkt1.x;
				} else {
					x = zeichnungPunkt2.x;
					breite = zeichnungPunkt1.x - zeichnungPunkt2.x;
				}
				if (zeichnungPunkt1.y < zeichnungPunkt2.y) {
					y = zeichnungPunkt1.y;
					hoehe = zeichnungPunkt2.y - zeichnungPunkt1.y;
				} else {
					y = zeichnungPunkt2.y;
					hoehe = zeichnungPunkt1.y - zeichnungPunkt2.y;
				}
				g2.drawRect(x,y,breite,hoehe);
				break;
			case LINIE:
				g2.drawLine(zeichnungPunkt1.x, zeichnungPunkt1.y, 
							zeichnungPunkt2.x, zeichnungPunkt2.y);
				break;
			default: break;	
			}
			istZeichnungBenoetigt = false;
		}
	}

	/**
	 * Einberechnet {@link #zoomFaktor} zur Skalierung sämtlicher Zeichenoperationen auf dem 
	 * Graphics2D-Object.
	 * @param g2 Objekt auf dem die Skalierung ausgeführt wird
	 */
	private void zoomFaktorEinberechnung(Graphics2D g2) {
		AffineTransform at = AffineTransform.getScaleInstance(zoomFaktor, zoomFaktor);
        g2.transform(at);
	}

	/**
	 * Zeichnet sämtliche WFN-Elemente des aktuellen Datenmodels, die {@link #statusInfo} entnommen
	 * werden. Gegebenenfalls wird {@link #setPreferredSize(Dimension)} des Panels angepasst.
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	 */
	private void zeichneAlleElemente(Graphics2D g2) {
		double maxX = 0d;
		double maxY = 0d;
		for (IWFNElementOK elem : statusInfo.getAlleElementeOK()) {
			Point position = elem.getPosition();
			EWFNElement typ = elem.getTyp();
			if ((position.x*zoomFaktor) > maxX) maxX = position.x * zoomFaktor;
			if ((position.y*zoomFaktor) > maxY) maxY = position.y * zoomFaktor;
			typ.zeichne(g2, position, elementGroesse);
			zeichneName(elem.getName(), position, g2);
			for (IWFNElementOK nextElem :  elem.getKantenZu())
				EWFNElement.KANTE.zeichne(g2, typ, position, nextElem.getPosition(), elementGroesse);
		}
		Dimension panelGroesse = new Dimension((int)maxX + (2 * elementGroesse) +10, (int)maxY + (2 * elementGroesse) +10);
		setPreferredSize(panelGroesse);
		revalidate();
	}

	/**
	 * Zeichnet, wenn laut {@link #statusInfo} das Netz alle Bedingungen für ein Workflownetz erfüllt,
	 * die Start- und die Endstelle, alle Marken, sowie alle aktivierten Transitionen.
	 * @param g2 Objekt, auf dem die Zeichnung ausgeführt wird
	 */
	private void zeichneMarkierungenUndAktivierteTransitionen(Graphics2D g2) {
		if ((statusInfo.istWFN())
				&& (statusInfo.getStartStelle() != statusInfo.getEndStelle())
				) {
			EWFNElement.STELLE.zeichneAlsAusgewaehlt(g2, statusInfo.getStartStelle().getPosition(), EEditorFarben.START, elementGroesse);
			EWFNElement.STELLE.zeichneAlsAusgewaehlt(g2, statusInfo.getEndStelle().getPosition(), EEditorFarben.ENDE, elementGroesse);
			for (IWFNElementOK stelle: statusInfo.getMarkierungsListe())
				g2.fillOval(stelle.getPosition().x - (elementGroesse/2), 
							stelle.getPosition().y - (elementGroesse/2), 
							elementGroesse, elementGroesse);
			if (statusInfo.getAktivierteTransitionen() != null)
				for (IWFNElementTransition transition : statusInfo.getAktivierteTransitionen())
					EWFNElement.TRANSITION.zeichneAlsAusgewaehlt(g2, transition.getPosition(), EEditorFarben.AKTIVIERT, elementGroesse); 
			if (statusInfo.getKontaktTransitionen() != null)
				for (IWFNElementTransition transition : statusInfo.getKontaktTransitionen())
					EWFNElement.TRANSITION.zeichneAlsAusgewaehlt(g2, transition.getPosition(), EEditorFarben.KONTAKT, elementGroesse);
		}
	}

	/**
	 * Zeichnet Worte auf das Panel.
	 * @param name zu schreibende/zeichnende Worte
	 * @param position Position, unterhalb welcher der Wortanfang liegt
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	  */
	private void zeichneName(String name, Point position, Graphics2D g2) {
		if (!name.equals("")) {
			g2.drawString(name, position.x, position.y + elementGroesse + EWFNElement.URGROESSE);
		}
	}
	
	/* 
	 * Bei Aufruf wird #statusInfo aktualisiert und die Liste #ausgewahlteElemente auf null gesetzt.
	 * Und repaint() aufgerufen.
	 * @see horcherschnittstellen.IWFNModellStatusHorcher#modellStatusAenderung(wfnmodell.WFNStatusInfo)
	 */
	@Override
	public void modellStatusAenderung(WFNStatusInfo statusInfo) {
		this.ausgewaehlteElemente = null;
		this.statusInfo = statusInfo;
		repaint();
	}

	@Override
	public void auswahlAenderungEingetreten(int auswahlArt, ArrayList<? extends IWFNElement> ausgewaehlteElemente) {
		this.auswahlArt = auswahlArt;
		this.ausgewaehlteElemente = ausgewaehlteElemente;
		repaint();	
	}
	
	@Override
	public void zeichnungBenoetigt(int form, Point startMausPosition, Point jetztMausPosition) {
		if ((form != NICHTS) 
				&& (startMausPosition != null) 
				&& (jetztMausPosition != null)) {
			istZeichnungBenoetigt = true;
			benoetigteForm = form;
			zeichnungPunkt1 = startMausPosition;
			zeichnungPunkt2 = jetztMausPosition;
		} else {
			benoetigteForm = NICHTS;
			istZeichnungBenoetigt = false;
		}
		repaint();
	}

	@Override
	public void zoomFaktorGeaendert(double neuerZoomFaktor) {
		if (zoomFaktor != neuerZoomFaktor) {
			zoomFaktor = neuerZoomFaktor;
			repaint();
		}
	}

	@Override
	public void elementGroesseGeaendert(int neueGroesse) {
		if (elementGroesse != neueGroesse) {
			elementGroesse = neueGroesse;
			repaint();
		}
		
	}
	
}

package wfnmodell.elemente;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

/**
 * Enum zur Typunterscheidung der Elemente eines Workflownetzes.
 * Desweiteren werden den Typen des Enums ihre Zeichen-Methoden mitgegeben.
 *
 */
public enum EWFNElement {
	STELLE() {

		@Override
		public void zeichne(Graphics2D g2, Point position, int elementGroesse) {
			g2.drawOval(position.x - elementGroesse, position.y - elementGroesse, elementGroesse * 2, elementGroesse * 2);
		}

		@Override
		public void zeichneAlsAusgewaehlt(Graphics2D g2, Point position, Color auswahlFarbe, int elementGroesse) {
 		   	Color aktuelleFarbe = g2.getColor();
			g2.setColor(auswahlFarbe);
			g2.fillOval(position.x - elementGroesse, position.y - elementGroesse, elementGroesse * 2, elementGroesse * 2);
			g2.setColor(aktuelleFarbe);
		}
		
	},
	TRANSITION() {

		@Override
		public void zeichne(Graphics2D g2, Point position, int elementGroesse) {
			g2.drawRect(position.x - elementGroesse, position.y - elementGroesse, elementGroesse * 2, elementGroesse * 2);
		}
		
		@Override
		public void zeichneAlsAusgewaehlt(Graphics2D g2, Point position, Color auswahlFarbe, int elementGroesse) {
 			Color aktuelleFarbe = g2.getColor();
			g2.setColor(auswahlFarbe);
			g2.fillRect(position.x - elementGroesse, position.y - elementGroesse, elementGroesse * 2, elementGroesse * 2);
			g2.setColor(aktuelleFarbe);
		}
		
	},
	KANTE() {
		BasicStroke dickeLinie = new BasicStroke(URGROESSE/2);
		Stroke aktuelleLinie;
		Color aktuelleFarbe;

		@Override
		public void zeichne(Graphics2D g2, EWFNElement typ, Point von, Point zu, int elementGroesse) {
			Point vonRand = getVerschiebung(typ, von, zu, elementGroesse);
			Point zuRand = getVerschiebung(getGGTElementTyp(typ), zu, von, elementGroesse);
			drawArrowLine(g2, vonRand.x, vonRand.y, zuRand.x, zuRand.y, elementGroesse, elementGroesse / 2);
		}
		
		@Override
		public void zeichneAlsAusgewaehlt(Graphics2D g2, EWFNElement typ, Point von, Point zu, Color auswahlFarbe, int elementGroesse) {
			Point vonRand = getVerschiebung(typ, von, zu, elementGroesse);
			Point zuRand = getVerschiebung(getGGTElementTyp(typ), zu, von, elementGroesse);
			aktuelleLinie = g2.getStroke();
			aktuelleFarbe = g2.getColor();
			g2.setStroke(dickeLinie);
			g2.setColor(auswahlFarbe);
			g2.drawLine(vonRand.x, vonRand.y, zuRand.x, zuRand.y);
			g2.setStroke(aktuelleLinie);
			g2.setColor(aktuelleFarbe);
		}

	};

	/**
	 * Basisgröße und Maßeinheit bei der Darstellung des WFN.
	 */
	public final static int URGROESSE = 15;
	

	/**
	 * Zeichnet je nach Typ, auf der sie aufgerufen wird, eine Stelle oder eine Transition.
	 * Im Falle des Aufrufs von einer Kante passiert nichts. 
	 * @param g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param position Position, an welcher die Zeichnung erfolgen soll
	 */
	public void zeichne(Graphics2D g2, Point position, int elementGroesse) {
		// wird überschrieben für die Typen STELLE und TRANSITION.
		// im Fall von Kante passiert nichts.
	}

	/**
	 * Zeichnet, falls sie von einer Kante aufgerufen wird, eine Kante.
	 * Im Falle des Aufrufs von einer Stelle oder einer Transition passiert nichts.
	 * @param g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param typ Typ des Elements, von dem die Kante ausgeht
	 * @param von Position des Elements, von dem die Kante ausgeht
	 * @param zu Position des Elements, in dem die Kante endet
	 */
	public void zeichne(Graphics2D g2, EWFNElement typ, Point von, Point zu, int elementGroesse) {
		// wird überschrieben für den Typ KANTE.
		// im Fall von STELLE und TRANSITION passiert nichts.
	}
	
	/**
	 * Zeichnet je nach Typ, auf der sie aufgerufen wird, eine mit Farbe gefüllte
	 * Stelle oder Transition.
	 * Im Falle des Aufrufs von einer Kante passiert nichts. 
	 * @param g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param position Position, an welcher die Zeichnung erfolgen soll
	 * @param auswahlFarbe Farbe, mit der die Füllung vorgenommen werden soll
	 */
	public void zeichneAlsAusgewaehlt(Graphics2D g2, Point position, Color auswahlFarbe, int elementGroesse) {
		// wird überschrieben für die Typen STELLE und TRANSITION.
		// im Fall von Kante passiert nichts.
	}

	/**
	 * Zeichnet eine ausgewählte Kante.
	 * Im Falle des Aufrufs von einer Stelle oder einer Transition passiert nichts.
	 * @param g2 g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param typ Typ des Elements, von dem die Kante ausgeht
	 * @param von Position des Elements, von dem die Kante verläuft
	 * @param zu Position des Elements, zu dem die Kante verläuft
	 * @param auswahlFarbe Farbe, mit welcher die Füllung vorgenommen werden soll
	 */
	public void zeichneAlsAusgewaehlt(Graphics2D g2, EWFNElement typ, Point von, Point zu, Color auswahlFarbe, int elementGroesse) {
		// wird überschrieben für den Typ KANTE.
		// im Fall von STELLE und TRANSITION passiert nichts.
	}
	
	/**
	 * Gibt den anderen Elementtyp zurück, der keine Kante ist.
	 * @param typ Ausgangstyp
	 * @return der andere Elementtyp, der keine Kante ist
	 */
	private static EWFNElement getGGTElementTyp(EWFNElement typ) {
		EWFNElement ergebnis = (typ == STELLE) ? TRANSITION : STELLE;
		return ergebnis;
	}

	
	
	/**
	 * Methode zur Berechnung des Punktes auf dem Rand der Stelle oder Transition,
	 * von wo aus die Kante gezeichnet werden soll.
	 * @param typ Typ des Elements, dessen Verschiebung zum Rand berechnet werden soll
	 * @param von Position des Elements, von welchem die Verschiebung berechnet werden soll
	 * @param zu Position des Elements, zu welchem die Kante verlaufen soll
	 * @return die Verschiebung zum Mittelpunkt ("der Position") des Elements, von wo aus die Kante gezeichnet werden soll
	 */
	private static Point getVerschiebung(EWFNElement typ, Point von, Point zu, int elementGroesse) {
		int wegAufXAchse = Math.abs(zu.x - von.x);
		int wegAufYAchse = Math.abs(zu.y - von.y);
		double entfernungVonZu = von.distance(zu);

		double xErgebnis = wegAufXAchse * elementGroesse / entfernungVonZu;
		double yErgebnis = wegAufYAchse * elementGroesse / entfernungVonZu;

		Point vonRand = von;
		switch (typ) {
		case STELLE:
			if (von.x < zu.x) {
				// zu befindet sich rechts von von
				if (von.y < zu.y) {
					// zu befindet sich unter von
					vonRand = new Point(von.x + ((int) xErgebnis), von.y + ((int) yErgebnis));
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					vonRand = new Point(von.x + ((int) xErgebnis), von.y - ((int) yErgebnis));
				}
			} else {
				// zu befindet sich links oder auf gleicher Höhe von von
				if (von.y < zu.y) {
					// zu befindet sich unter von
					vonRand = new Point(von.x - ((int) xErgebnis), von.y + ((int) yErgebnis));
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					vonRand = new Point(von.x - ((int) xErgebnis), von.y - ((int) yErgebnis));
				}
			}
			break;
		case TRANSITION:
			if (von.x < zu.x) {
				// zu befindet sich rechts von von
				if (von.y < zu.y) {
					// zu befindet sich unter von
					if (wegAufXAchse <= wegAufYAchse) {
						// schneidet auf der Unterseite rechts oder im Eckpunkt
						vonRand = new Point(von.x + ((int) xErgebnis), von.y + elementGroesse); 
					} else {
						// schneidet auf der rechten Seite unten
						vonRand = new Point(von.x + elementGroesse, von.y + ((int) yErgebnis));
					}
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					if (wegAufXAchse <= wegAufYAchse) {
						// schneidet auf der Oberseite rechts oder im Eckpunkt
						vonRand = new Point(von.x + ((int) xErgebnis), von.y - elementGroesse);
					} else {
						// schneidet auf der rechten Seite oben
						vonRand = new Point(von.x + elementGroesse, von.y - ((int) yErgebnis));
					}
				}
			} else {
				// zu befindet sich links oder auf gleicher Höhe von von
				if (von.y < zu.y) {
					// zu befindet sich unter von
					if (wegAufXAchse <= wegAufYAchse) {
						// schneidet auf der Unterseite links oder im Eckpunkt
						vonRand = new Point(von.x - ((int) xErgebnis), von.y + elementGroesse); //
					} else {
						// schneidet auf der linken Seite unten
						vonRand = new Point(von.x - elementGroesse, von.y + ((int) yErgebnis));
					}
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					if (wegAufXAchse <= wegAufYAchse) {
						// schneidet auf der Oberseite links oder im Eckpunkt
						vonRand = new Point(von.x - ((int) xErgebnis), von.y - elementGroesse); //
					} else {
						// schneidet auf der rechten Seite
						vonRand = new Point(von.x - elementGroesse, von.y - ((int) yErgebnis));
					}
				}
			}
			break;
			default:
				break;
		}
		return vonRand;
	}
	



	/**
	 * Die Methode zeichnet einen Pfeil zwischen zwei Punkten, und die Methode ist in ihrer ganzen Klarheit
	 * und Effizienz entliehen beim User phibao237, auf stackoverflow am 14.12.2017.
	 * https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java#2027641
	 * @param g the graphics component.
	 * @param x1 x-position of first point.
	 * @param y1 y-position of first point.
	 * @param x2 x-position of second point.
	 * @param y2 y-position of second point.
	 * @param d  the width of the arrow.
	 * @param h  the height of the arrow.
	 */
	private static void drawArrowLine(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;
	
	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;
	
	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;
	
	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};
	
	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);

	}


}

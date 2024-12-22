package obstacles.ventilateur;

import java.awt.Graphics2D;

import interfaces.Dessinable;
import interfaces.Selectionnable;

public class Ventilateur implements Dessinable, Selectionnable {

	@Override
	public boolean contient(double xPix, double yPix) {
		return false ;
	}

	@Override
	public void dessiner(Graphics2D g2d) {
		
	}
}

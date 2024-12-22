package application.appliSatellite;

import java.awt.Color;

import math.vecteurs.Vecteur3D;
import obstacles.Obstacle;
import obstacles.TrouNoir;
import obstacles.plaqueMagnetique.ZoneDeChampMagnetique;

public class TestTrouNoir {



	public static void main(String[] args) {
		Obstacle trouNoir = new TrouNoir(new Vecteur3D(0,0,0),100,100, Color.GRAY);		
		System.out.println(	trouNoir.toString());
	}

}

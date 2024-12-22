package physique;

import math.vecteurs.Vecteur3D;

public class TestPhysique {

	public TestPhysique() {
		
	}

	public static void main(String[] args) throws Exception {
		MoteurPhysique.calculForceGrav(2, -90);
		Vecteur3D vitesse = new Vecteur3D(5,0,0);
		Vecteur3D reponse = new Vecteur3D ( -5.88399,0, 0);
		
		System.out.println("Test Force frottement : "+ MoteurPhysique.calculForceFrottement(2, 0.3,vitesse).equals(reponse));
	}

}

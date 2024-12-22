package math.vecteurs;

import java.io.Serializable;

/**
 * La classe Vecteur permet de realiser les operations de base sur un vecteur Euclidien en trois dimensions 
 * (x,y), où x,y et z sont les composantes du vecteur.
 * 
 * **ATTENTION***
 * Les identifiants x,y et z sont utilisés dans cette classe non pas pour représenter
 * des positions, mais bien pour représenter des composantes !!
 * 
 * Cette classe est une version 3d modifiée et augmentée de la classe SVector3d écrite par Simon Vezina 
 * dans le cadre du cours de physique.
 * 
 * @author Simon Vézina
 * @author Caroline Houle
 * @author Aimé Melançon
 */
public class Vecteur3D implements Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L;
	//champs de base
	/**tolérance utilisée dans les comparaisons réeles avec zero **/
	private static final double EPSILON = 1e-10; 
	/**composante x du vecteur 3d **/
	protected double x;	
	/**composante y du vecteur 3d **/
	protected double y;	
	/**composante z du vecteur 3d**/
	protected double z; 

	/**
	 * Constructeur representant un vecteur 3d aux composantes nulles
	 */
	//Aimé Melançon
	public Vecteur3D(){
		x = 0;
		y = 0;
		z = 0 ;
	}
	
	/**
	 * Constructeur avec 3 composantes x,y,z
	 * @param x La composante x du vecteur
	 * @param y La composante y du vecteur
	 * @param z La composante z du vecteur
	 */
	//Aimé Melançon
	public Vecteur3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z=z;
	}
	
	/**
	 * Constructeur avec 2 composantes x,y
	 * @param x La composante x du vecteur.
	 * @param y La composante y du vecteur.
	 */
	//Aimé Melançon
	public Vecteur3D(double x, double y){
		this.x = x;
		this.y = y;
		this.z=0;
	}

	/**
	 * Constructeur qui cr�e un nouveau vecteur qui contient les m�mes composantes que celui pass� en param�tre
	 * @param v Le vecteur � reproduire
	 */
	//Aimé Melançon
	public Vecteur3D(Vecteur3D v){
		this.x = v.x;
		this.y = v.y;
		this.z= v.z;
		
	}

	/**
	 * Methode qui donne acces a la composante x du vecteur.
	 * @return La composante x
	 */
	//Caroline Houle
	public double getX(){ 
		return x;
	}

	/**
	 * Methode qui donne acces a la composante y du vecteur.
	 * @return La composante y
	 */
	//Caroline Houle
	public double getY(){ 
		return y;
	}
	/**
	 * Methode qui donne acces a la composante z du vecteur.
	 * @return La composante z
	 */
	//Aimé Melançon
	public double getZ(){ 
		return z;
	}

	/**
	 * Methode qui permet de modifier la composante x du vecteur.
	 * @param x La nouvelle composante x
	 */
	//Caroline Houle
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Methode qui permet de modifier la composante y du vecteur.
	 * @param y La nouvelle composante y
	 */
	//Caroline Houle
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * Methode qui permet de modifier la composante z du vecteur.
	 * @param z La nouvelle composante z
	 */
	//Aimé Melançon
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Methode qui permet de modifier les deux composantes du vecteur.
	 * @param x La nouvelle composante x
	 * @param y La nouvelle composante y
	 */
	//Caroline Houle
	public void setComposantes(double x, double y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Methode qui permet de modifier les trois composantes du vecteur.
	 * @param x La nouvelle composante x
	 * @param y La nouvelle composante y
	 * @param z La nouvelle composante z
	 */
	//Aimé Melançon
	public void setComposantes(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z=z;
	}

	/**
	 * Methode qui permet de modifier les deux composantes du vecteur en reproduisant celles
	 * du vecteur pass� en param�tre
	 * @param v Le vecteur dont on d�sire copier les composantes
	 */
	//Aimé Melançon
	public void setComposantes(Vecteur3D v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/**
	 * Genere une chaine de caractere avec les informations  du vecteur
	 */
	@Override
	//Aimé Melançon
	public String toString(){
		if(z==0) {
		return "[ x = " + x + ", y = " + y +"]";	
		}else {
			return "[ x = " + x + ", y = " + y + ", z = "+z +"]";	
		}
	}	

	/**
	 * Genere une chaine de caractere avec les informations du vecteur, avec un
	 * nombre de decimales restreint
	 * @param nbDecimales Nombre de chiffres significatifs d�sir�s
	 * @return Retourne les composants du vecteur
	 */
	//Aimé Melançon
	public String toString(int nbDecimales){
		return "[ x = " + String.format("%."+nbDecimales+"f",x) +
				", y = " + String.format("%."+nbDecimales+"f",y)+
				", z = " + String.format("%."+nbDecimales+"f",z)+ "]";		
	}

	/**
	 * Determine si le vecteur courant est egal ou non a un autre vecteur, a EPSILON pres
	 */
	@Override
	//Aimé Melançon
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(obj == null)
			return false;

		if(!(obj instanceof Vecteur3D))
			return false;

		Vecteur3D other = (Vecteur3D) obj;

		//Comparaison des valeurs x,y et z en utilisant la precision de EPSILON modulee par la valeur a comparer
		if(Math.abs(x - other.x) > Math.abs(x)*EPSILON)
			return false;

		if(Math.abs(y - other.y) > Math.abs(y)*EPSILON)
			return false;
		if(Math.abs(z - other.z) > Math.abs(z)*EPSILON)
			return false;

		return true;
	}

	/**
	 * Methode qui calcule et retourner l'addition du vecteur courant et d'un autre vecteur. Le vecteur courant reste inchang�.
	 * @param v Le vecteur a ajouter au vecteur courant
	 * @return Un nouveau vecteur qui represente la somme des deux vecteurs
	 */
	//Aimé Melançon
	public Vecteur3D additionne(Vecteur3D v){	
		return new Vecteur3D(x + v.x, y + v.y, z +v.z);
	}

	/**
	 * Methode de classe qui retourne l'addition de deux vecteurs quelconques.
	 * @param v1 Le premier vecteur
	 * @param v2 Le deuxieme vecteur
	 * @return Un nouveau vecteur qui represente la somme des deux vecteurs
	 */
	//Caroline Houle
	public static Vecteur3D additionne(Vecteur3D v1, Vecteur3D v2)
	{ 
		return v1.additionne(v2);
	}

	/**
	 * Methode qui calcule et retourne le vecteur resultant de la soustraction d'un vecteur quelconque du vecteur courant. Le vecteur courant reste inchang�.
	 * @param v Le vecteur a soustraire au vecteur courant.
	 * @return Un nouveau vecteur qui represente la soustraction des deux vecteurs.
	 */
	//Aimé Melançon
	public Vecteur3D soustrait(Vecteur3D v){
		return new Vecteur3D(x - v.x, y - v.y, z-v.z);
	}

	/**
	 * Methode de classe qui retourne la soustraction entre deux vecteurs quelconques.
	 * @param v1 Le premier vecteur
	 * @param v2 Le deuxieme vecteur, a soustraire du premier
	 * @return Un nouveau vecteur qui represente la diffrence entre les deux vecteurs
	 */
	//Caroline Houle
	public static Vecteur3D soustrait(Vecteur3D v1, Vecteur3D v2)
	{ 
		return v1.soustrait(v2);
	}


	/**
	 * Methode qui effectue la multiplication du vecteur courant par une scalaire.Le vecteur courant reste inchang�.
	 * @param m - Le muliplicateur
	 * @return Un nouveau vecteur qui represente le resultat de la multiplication par un scalaire m sur le vecteur.
	 */
	//Aimé Melançon
	public Vecteur3D multiplie(double m){
		return new Vecteur3D(m*x, m*y, m*z);
	}

	/**
	 * Methode de classe qui effectue la multiplication d'un vecteur quelconque par une scalaire.
	 * @param v Le vecteur
	 * @param m Le muliplicateur
	 * @return Un nouveau vecteur qui represente le resultat de la multiplication par un scalaire m sur le vecteur.
	 */
	//Caroline Houle
	public static Vecteur3D multiplie(Vecteur3D v, double m)
	{ 
		return v.multiplie(m);
	}

	/**
	 * Methode pour obtenir le module du vecteur courant.
	 * @return Le module du vecteur courant.
	 */
	//Aimé Melançon
	public double module(){
		return Math.sqrt((x*x) + (y*y)+ (z*z));
	}

	/**
	 * Methode de classe pour obtenir le module d'un vecteur quelconque.
	 * @param v Le vecteur
	 * @return Le module du vecteur.
	 */
	//Caroline Houle
	public static double module(Vecteur3D v){
		return v.module();
	}

	/**
	 * Methode pour normaliser le vecteur courant.
	 * Un vecteur normalise possede la meme orientation, mais possede une longeur unitaire.
	 * Si le module du vecteur est nul, le vecteur normalise sera le vecteur nul (0.0, 0.0).
	 * 
	 * @return Le vecteur normalise.
	 * @throws Exception Si le vecteur ne peut pas etre normalise etant trop petit ou de longueur nulle.
	 */
	//Aime Melancon
	public Vecteur3D normalise() throws Exception
	{
		double mod = module();			//obtenir le module du vecteur

		//Verification du module. S'il est trop petit, nous ne pouvons pas numeriquement normaliser ce vecteur
		if(mod < EPSILON) 
			throw new Exception("Erreur Vecteur: Le vecteur " + this.toString() + " �tant nul ou presque nul ne peut pas etre normalis�.");
		else
			return new Vecteur3D(x/mod, y/mod, z/mod);
	}

	/**
	 * Methode de classe pour normaliser un vecteur quelconque.
	 * Un vecteur normalise possede la meme orientation, mais possede une longeur unitaire.
	 * Si le module du vecteur est nul, le vecteur normalise sera le vecteur nul (0.0, 0.0).
	 * 
	 * @param v Le vecteur
	 * @return Le vecteur normalis�.
	 * @throws Exception Si le vecteur ne peut pas etre normalise etant trop petit ou de longueur nulle.
	 */
	//Caroline Houle
	public static Vecteur3D normalise(Vecteur3D v) throws Exception
	{
		return v.normalise();
	}

	/**
	 * Methode pour effectuer le produit scalaire du vecteur courant avec un autre vecteur.
	 * @param v L'autre vecteur.
	 * @return Le produit scalaire entre les deux vecteurs.
	 */
	//Aimé Melançon
	public double prodScalaire(Vecteur3D v){
		return (x*v.x + y*v.y+z*v.z);
	}

	/**
	 * Methode de classe pour effectuer le produit scalaire entre deux vecteurs quelconques.
	 * @param v1 Le premier vecteur
	 * @param v2 Le deuxieme vecteur
	 * @return Le produit scalaire entre les deux vecteurs.
	 */
	//Caroline Houle
	public static double prodScalaire(Vecteur3D v1, Vecteur3D v2){
		return (v1.prodScalaire(v2));
	}
	/**
	 * Methode de classe pour effectuer le produit Vectoriel entre deux vecteurs quelconques.
	 * @param v1 Le premier vecteur
	 * @param v2 Le deuxième vecteur
	 * @return Le produit vectoriel entre les deux vecteurs.
	 */
	//Aimé Melançon
	public static Vecteur3D prodVectoriel(Vecteur3D v1, Vecteur3D v2) {

		double x = (v1.getY()*v2.getZ())-(v1.getZ()*v2.getY());
		double y = (v1.getX()*v2.getZ())-(v1.getZ()*v2.getX());
		double z = (v1.getX()*v2.getY())-(v1.getY()*v2.getX());

		return new Vecteur3D(x,y,z);

	}

	/**
	 * Méthode de classe pour effectuer le produit Vectoriel entre deux vecteurs quelconques.
	 * @param v2 Le deuxième vecteur
	 * @return Le produit vectoriel entre les deux vecteurs.
	 */
	//Aimé Melançon
	public  Vecteur3D prodVectoriel( Vecteur3D v2) {

		double x = (this.y*v2.getZ())-(this.z*v2.getY());
		double y = -((this.x*v2.getZ())-(this.z*v2.getX()));
		double z = (this.x*v2.getY())-(this.y*v2.getX());

		return new Vecteur3D(x,y,z);

	}
	/**Méthode permettant de faire la projection orthogonale avec deux vecteur. 
	 * 
	 * @param vecA Le vecteur sélectionné
	 * @param vecB Le vecteur destination
	 * @return La projection orthogonale vecA_b
	 */
	//Aimé Melançon
	public static Vecteur3D projectionOrthogonale(Vecteur3D vecA, Vecteur3D vecB) {
		
		Vecteur3D vecAvecProjectionOrthogonale = vecB.multiplie(vecA.prodScalaire(vecB)/vecB.prodScalaire(vecB));
		
		return vecAvecProjectionOrthogonale;
	}

}//fin classe Vecteur

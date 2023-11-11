package es.florida.books;

public class Principal {

	public static void main(String[] args) {
		Vista vista = new Vista();
		Model model = new Model();
		Controlador controlador = new Controlador(vista, model);
	}
}

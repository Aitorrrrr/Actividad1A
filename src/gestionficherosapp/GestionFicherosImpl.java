package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileTime;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		
		//que se pueda escribir -> lanzará una excepción
		if (!file.getParentFile().canWrite() || !file.getParentFile().exists())
		{
			throw new GestionFicherosException("Parece que no la vas a crear");
		}
			
		
		//que no exista -> lanzará una excepción
		if (file.exists())
		{
			throw new GestionFicherosException("Vaya parece que el fichero existe");
		}
		
		//crear la carpeta -> lanzará una excepción
		if (!file.mkdir())
		{
			throw new GestionFicherosException("Has faileao, tú sabras");
		}
		
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file =new File(carpetaDeTrabajo,arg0);
		
		if (!file.getParentFile().canWrite() || !file.getParentFile().exists())
		{
			throw new GestionFicherosException("Parece que no la vas a crear, el padre no existe o no posees permiso en él");
		}
					
		if (file.exists())
		{
			throw new GestionFicherosException("Vaya parece que el fichero existe");
		}
				
		try
		{
			file.createNewFile();
		}
		catch (IOException ioe)
		{
			System.out.println("El archivo no se ha podido crear "+ioe.getMessage());
		}
		
		actualiza();
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file =new File(carpetaDeTrabajo,arg0);
		
		if (!file.getParentFile().canWrite() || !file.getParentFile().exists())
		{
			throw new GestionFicherosException("Parece que no se puede eliminar, el padre no existe o no posees permiso en él");
		}
					
		if (!file.exists())
		{
			throw new GestionFicherosException("Vaya parece que el fichero a eliminar no existe");
		}
		
		if (!file.delete())
		{
			throw new GestionFicherosException("El fichero no ha podido ser eliminado");
		}
		
		actualiza();
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción
		try
		{
			if (file.exists())
			{
				strBuilder.append("¡El fichero seleccionado existe!");
			}
			else
			{
				strBuilder.append("Vaya... ¿Parece que el fichero no existe?");
			}
		}
		catch (SecurityException se)
		{
			strBuilder.append("Vaya parece que no tienes permisos de lectura en el directorio padre: "+file.getParent());
		}
		
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
		if (file.canRead())
		{
			strBuilder.append("Posees permiso de lectura en este fichero");
		}
		else
		{
			throw new GestionFicherosException("Vaya parece que no tienes permiso de lectura en "+file.getAbsolutePath());
		}
		
		//Título
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n\n");
		
		//Tipo: fichero o directorio
		if (file.isDirectory())
		{
			strBuilder.append("Es un directorio");
		}
		else
		{
			strBuilder.append("Es un fichero");
		}
		strBuilder.append("\n\n");
		
		//Ubicación
		strBuilder.append(file.getAbsoluteFile());
		strBuilder.append("\n\n");
		
		//Fecha de última modificación
		strBuilder.append(FileTime.fromMillis(file.lastModified()));
		strBuilder.append("\n\n");
		
		//Si es un fichero oculto o no
		if (file.isHidden())
		{
			strBuilder.append("Esta oculto");
		}
		else
		{
			strBuilder.append("No esta oculto");
		}
		strBuilder.append("\n\n");
		
		//Si es un fichero: Tamaño en bytes
		if (file.isFile())
		{
			strBuilder.append("Tamaño en bytes: "+file.length());
			strBuilder.append("\n\n");
		}
		
		//Si es directorio: Número de elementos que contiene,
		if (file.isDirectory())
		{
			strBuilder.append("Elementos que contiene: "+file.list().length);
			strBuilder.append("\n\n");
		}
		
		//Si es directorio: Espacio libre, espacio disponible, espacio total (bytes)
		if (file.isDirectory())
		{
			strBuilder.append("Espacio libre: "+file.getFreeSpace());
			strBuilder.append("Espacio disponible: "+file.getUsableSpace());
			strBuilder.append("Espacio total: "+file.getTotalSpace());
			strBuilder.append("\n\n");
		}
				
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file =new File(carpetaDeTrabajo,arg0);
		
		if (!file.getParentFile().canWrite() || !file.getParentFile().exists())
		{
			throw new GestionFicherosException("Parece que no se puede renombrar, el padre no existe o no posees permiso en él");
		}
					
		if (!file.exists())
		{
			throw new GestionFicherosException("Vaya parece que el fichero a renombrar no existe");
		}
		
		File fileAux=new File(carpetaDeTrabajo,arg1);
		if(fileAux.exists())
		{
			throw new GestionFicherosException("Cuidado, ya existe un fichero con el nuevo nombre introducido");
		}
		
		if (!file.renameTo(fileAux))
		{
			throw new GestionFicherosException("Ups, el fichero no ha podido ser renombrado");
		}
		
		actualiza();
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}

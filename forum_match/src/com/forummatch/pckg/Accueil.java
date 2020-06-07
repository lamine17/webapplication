package com.forummatch.pckg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Servlet implementation class Accueil
 */
@WebServlet("/Accueil")
public class Accueil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String FILE_NAME = GetRessources.FILE_NAME;
	private static final String SPLIT_STRING = GetRessources.SPLIT_STRING;
	
	//decrit le nombre de topic apparaissant sur la page d'acceuil
	private static final int SIZE_MAIN_PAGE = 12;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Accueil() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = Integer.parseInt(request.getParameter("page"));
		String[] topic_names = new String[SIZE_MAIN_PAGE];
		String[] topic_ids = new String[SIZE_MAIN_PAGE];
		int j=0;
		int k;
		JSONObject jdoc = new JSONObject();
		//Mettre dans topic_names et topic_ids les noms et les identifiants des topics
		//depuis la ressource
		String[] tab;
		
		int i = 0;
		try {
			File myF = new File(FILE_NAME);
			Scanner myReader = new Scanner(myF);
			while (myReader.hasNextLine() && (i<(page-1)*SIZE_MAIN_PAGE)) {
				myReader.nextLine();
			  	i++;
			}
			while (myReader.hasNextLine() && (i<SIZE_MAIN_PAGE+(page-1)*SIZE_MAIN_PAGE)) {
				String line = myReader.nextLine();
			  	tab = line.split(SPLIT_STRING);
				topic_ids[j] 	= tab[0];
			  	topic_names[j] 	= tab[1]+" "+tab[2]+" "+tab[3];
			  	i++;
			  	j++;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		//Incorporer les tableaux dans la reponse
		
		try {
		for(k=1;k<j+1;k++) {
				jdoc.accumulate(""+k, topic_names[k-1]);
				jdoc.accumulate(""+k+"h", topic_ids[k-1]);	
		}
		
		if(page>1) {
			jdoc.accumulate("prec", "submit");
		}
		else {
			jdoc.accumulate("prec", "hidden");
		}
		if(page<(i/SIZE_MAIN_PAGE)+1) {
			jdoc.accumulate("suiv", "submit");
		}
		else {
			jdoc.accumulate("suiv", "hidden");
		}
		
		} catch (JSONException e) {e.printStackTrace();}
		response.getWriter().write(jdoc.toString());
		response.setContentType("application/json");
	    response.setHeader("Cache-Control", "no-cache");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

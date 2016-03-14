// A class that allows the other classes to create Publication objects

import java.io.Serializable;

public class Publication implements Serializable{
	long publication_code;
	String publication_name;
	int publication_year;
	String publication_authorname;
	double publication_cost;
	int publication_nbpages;
	
	public Publication(long pCode, String pName, int pYear, String pAuthorName, double pCost, int pPages) {
		publication_code = pCode;
		publication_name = pName;
		publication_year = pYear;
		publication_authorname = pAuthorName;
		publication_cost = pCost;
		publication_nbpages = pPages;
	}
}

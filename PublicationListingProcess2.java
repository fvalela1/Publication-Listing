// Allows the user to add data entries into an assorted .txt file. Puts entries into an array and creates a serialized, binary .dat file of the publication array.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

public class PublicationListingProcess2 implements Serializable {
	private static Publication[] PublicationArray;
	public enum PublicationTypes 
		{PUBLICATIONCODE, PUBLICATIONNAME, PUBLICATIONYEAR, 
		PUBLICATIONAUTHORNAME, PUBLICATIONCOST, PUBLICATIONNBPAGES
		}

	//Prompts user for new entries into file
	public static void insertRowsToFile(String outputFileName) throws FileNotFoundException {
		Scanner keyIn = new Scanner(System.in);
		String add = null;
		PrintWriter out = new PrintWriter(new FileOutputStream("PublicationData_Output.txt", true));
		
		do {
			System.out.println("What would you like to add? type \"no\" to stop adding");
			add = keyIn.nextLine();
				if (add.equals("no"))
					break;
				else {
					out.println(add);
				} 
		} while (add.equals("no"));
		out.close();
		
	}
	
	// Accepts input file name and displays contents on screen
	public static void printFileItems(String inputFileName) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(inputFileName));

		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			}
	
		in.close();
	}
	
	// Parses the .txt file to determine how many indexes are needed for the array
	private static int publicationSize(String inputFileName) throws IOException {
		int rowCount = 0;
		BufferedReader in = new BufferedReader(new FileReader(inputFileName));

		String line;
		while ((line = in.readLine()) != null) {
			rowCount++;
			}
		in.close();
		return rowCount;
	}
	
	// Parses .txt file and copy's content into array
	private static void toArray(String inputFileName) throws IOException {
		long publication_code = 0;
		String publication_name = null;
		int publication_year = 0;
		String publication_authorname = null;
		double publication_cost = 0.0;
		int publication_nbpages = 0;
		int i = 0, columnCount = 0;
		
		Scanner read = new Scanner(new FileReader(inputFileName));
		
		//reads each cell and puts entry into variable to then be put into array index
		while (i < publicationSize(inputFileName)) {
			if (columnCount < 6) {
				switch (columnCount) {
					case 0: publication_code = read.nextLong();
							break;
					case 1: publication_name = read.next();
							break;
					case 2: publication_year = read.nextInt();
							break;
					case 3: publication_authorname = read.next();
							break;
					case 4: publication_cost = read.nextDouble();
							break;
					case 5: publication_nbpages = read.nextInt();
							break;
				}
				
				columnCount++;
			}
			else {
				PublicationArray[i] = new Publication(publication_code, publication_name, publication_year, publication_authorname, publication_cost, publication_nbpages);
				columnCount = 0;
				i++;
			}	
		}
		read.close();
	}
	
	// Through binary search, finds if the user inputed passed publication code is in the array. If so, displays how many iterations it takes to find it.
	public static int binaryPublicationSearch(Publication[] a, int startIndex, int endIndex, long pCode) {
		int first = startIndex, last = endIndex, mid, result = 0, count = 1;
		boolean found = false;
		
		while ((first <= last) && !found) {
			mid = (first+last)/2;			
			if (pCode == a[mid].publication_code) {
				result = mid;
				found = true;
			}
			else if (pCode < a[mid].publication_code)
				last = mid -1;
			else if (pCode > a[mid].publication_code)
				first = mid +1;
			count++;
		}
		System.out.println("It took " + count + " iterations to complete the binary search");
		return result;
	}
	
	// Through sequential search, finds if the user inputed passed publication code is in the array. If so, displays how many iterations it takes to find it.
	public static int sequentialPublicationSearch(Publication[] a, int startIndex, int endIndex, long pCode) {
		int count = 1;
		for (int i = 0;i < PublicationArray.length; i++ ) {
			if (PublicationArray[i].publication_code == pCode) {
				System.out.println("It took " + count + " iterations to complete the sequential search");
				return i;
			}
			else
				count++;
		}
		return -1;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("------------------------------------------------------------------"); 
		System.out.println("----------Francesco's Publication Entry Search & Addition---------"); 
		System.out.println("------------------------------------------------------------------"); 
		
		PrintWriter out = new PrintWriter(new FileOutputStream("PublicationData_Output.txt", true));
		
		insertRowsToFile("PublicationData_Output.txt");
		
		// Prints .txt with potentially new entries from above method.
		printFileItems("PublicationData_Output.txt");
		
		PublicationArray = new Publication[publicationSize("PublicationData_Output.txt")];
		
		toArray("PublicationData_Output.txt");
		
		//Prompts user for publication code and uses binary and sequential search
		Scanner keyIn = new Scanner(System.in);
		System.out.println("Enter a publication code: ");
		long userPCode = keyIn.nextLong();
		
		// Searches through array for passed publication code
		binaryPublicationSearch(PublicationArray, 0, PublicationArray.length, userPCode);
		sequentialPublicationSearch(PublicationArray, 0, PublicationArray.length, userPCode);
		
		// Closes scanner and file.
		keyIn.close();
		out.close();
		
		// Serializes array and copies into a .dat file.
		ObjectOutputStream outBinary = null; 
		try {
			outBinary = new ObjectOutputStream(new FileOutputStream("Publications.dat"));
			outBinary.writeObject(PublicationArray);
		}
		catch (FileNotFoundException e) {
			System.out.println("There is no file of that name");
		}
		finally {
			outBinary.close();
		}
		System.out.println("Program now ending.");
		System.exit(0);
	}
}



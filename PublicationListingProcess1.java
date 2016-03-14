// Takes .txt file and copy's to an Array. Detects any duplicates in publication codes and prompts users to fix them, making sure that they do not input a code that already exists. 
// Creates a new array and .txt file with updated, duplicate-free publication listings.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class PublicationListingProcess1 {
	private static Publication[] PublicationArray;
	private static String outputFileName;
	private static String inputFileName = "PublicationData_Input.txt";
	private static boolean correctName;
	public enum PublicationTypes 
		{PUBLICATIONCODE, PUBLICATIONNAME, PUBLICATIONYEAR, 
		PUBLICATIONAUTHORNAME, PUBLICATIONCOST, PUBLICATIONNBPAGES
		}
	
	// Initializes array, copy's content from .txt file into array, corrects any duplicate items in array and creates a new .txt file with updated array
	public static void correctListOfItems(String inputFileName, String outputFileName) throws FileNotFoundException {
			
		PublicationArray = new Publication[publicationSize(inputFileName)];
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
		
		//Finds copies and prompts user for new code.
		Scanner keyIn = new Scanner (System.in);
		int noCopyCount = 0;
		boolean good = false;
		
		while (noCopyCount < 1 ) {
			do {
				noCopyCount = 0;
				if (checkCopy() >= 0) {
					int checkCopynb = checkCopy();
					System.out.println("Publication code duplicate detected in publication listing #" + (checkCopynb+1) + ". Please enter a new publication code: ");
					PublicationArray[checkCopynb].publication_code = keyIn.nextLong();
					
						try {
							if (newCodeNbCheck(checkCopynb) >= 0)
								throw new CopyCodeException("The code number you inputted is already in use.");
							good = true;
						}
						catch (CopyCodeException e) {
							System.out.println(e);
							continue;
						}
				}
				else
					noCopyCount++;
				} while (good == false);
			
			}
		keyIn.close();

		//Copy's array to the new output file.
		PrintWriter out = new PrintWriter(outputFileName);
		
		for (int j = 0; j<PublicationArray.length;j++) {
				out.println(PublicationArray[j].publication_code + " " + PublicationArray[j].publication_name + " " + PublicationArray[j].publication_year + " "
						+ PublicationArray[j].publication_authorname + " " + PublicationArray[j].publication_cost + " " + PublicationArray[j].publication_nbpages);
		}
		out.close();
	}
	

	// Looking for any copies in publication codes
	private static int checkCopy() {
		
		for (int i = 0; i < PublicationArray.length; i++) {
			for (int k = i+1; k < PublicationArray.length; k++) {
				if (PublicationArray[i].publication_code == PublicationArray[k].publication_code) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private static int newCodeNbCheck(int index) {
		for (int i = 0; i < PublicationArray.length; i++) {
			 if (i != index && PublicationArray[i].publication_code == PublicationArray[index].publication_code) {
				 return 0;
				 
			 }
		}
		return -1;
		
	}
	
	// Counts amount of space needed in array
	private static int publicationSize(String inputFileName) throws FileNotFoundException {
		int rowCount = 1, columnCount = 0;
		Scanner read = new Scanner(new FileReader(inputFileName));
		
		while (read.hasNext() == true) {
			if (columnCount < 6) {
				read.next();
				columnCount++;
			}
			else {
				columnCount = 0;
				rowCount++;
			}
		}
		read.close();
		if (rowCount == 0) {
			System.out.println("The file is empty. Program ending.");
			System.exit(0);
		}
		else if (rowCount == 1) {
			System.out.println("This file only has one record. No correcting necessary. Program ending.");
			System.exit(0);
		}
			return rowCount;
	}
	
	// Prints data of called file to the screen
	public static void printFileItems(String inputFileName) throws FileNotFoundException {
		int countColumns = 0;
		
		Scanner read = new Scanner(new FileReader(inputFileName));
		
		while (read.hasNext() == true) {
			if (countColumns < 6) {
				System.out.print(read.next() + " ");
				countColumns++;
			}
			else {
				countColumns = 0;
				System.out.println();
			}
		}
		read.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("------------------------------------------------------------------"); 
		System.out.println("------------Francesco's Publication Duplicate Detection-----------"); 
		System.out.println("------------------------------------------------------------------"); 

		Scanner keyIn = new Scanner(System.in);
		
		//Prompts user for output name and checks to see if it's already taken
		//Assumes user writes a name with .txt extension at end
		do {
			System.out.print("Choose an output file name: ");
			outputFileName = keyIn.next();
		
			File sameName = new File(outputFileName);
			if (sameName.exists() == true) {
				System.out.println("File with that name already exists and is " + sameName.length() + " bytes long, please choose another.");
			}
			else {
				System.out.println("Output file created: " + outputFileName);
				correctName = true;
			}
		} while (correctName == false);
		
		// input stream is publicationdata_input.txt file
		Scanner in = new Scanner (new FileReader("PublicationData_input.txt"));
		in.close();
		
		// output stream is user prompted new file name
		PrintWriter out = new PrintWriter(outputFileName); 
		out.close();
		
		correctListOfItems(inputFileName, outputFileName);
		
		//printFileItems(inputFileName);
		System.out.println();
		System.out.println("There are no duplicate publication codes. \nPrinting out listing: \n");
		printFileItems(outputFileName);
		
		System.out.println("\n\nProgram now ending.");
		keyIn.close();
		System.exit(0);
	}
}

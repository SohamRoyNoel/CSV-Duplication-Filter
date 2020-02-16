package com.entityValidator.validator.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSVReader;

public class Main {

	public static Set<String> detector = new HashSet<String>(); 
	public static List<String> filteredList = new ArrayList<String>(); 
	public static String inputDirectoryPath = "E:\\ME as QA\\CSV-Filter\\Input";
	public static String outputDirectoryPath = "E:\\ME as QA\\CSV-Filter\\Output";
	public static JFrame frame;

	public static void main(String[] args) {

		Instant start = Instant.now();
		try { 

			File[] getArrayOfAllFiles = getListOfFiles(inputDirectoryPath);

			for (int j = 0; j < getArrayOfAllFiles.length; j++) {
				String getAbsolutePath = getArrayOfAllFiles[j].getAbsolutePath();

				int lines = lineCounter(getAbsolutePath)-1;

				File file = new File(getAbsolutePath);
				FileReader filereader = new FileReader(file); 
				CSVReader csvReader = new CSVReader(filereader); 

				String[] nextRecord; 
				int counter = 0;

//				nextRecord = csvReader.readNext();
				// get line number
				String processString = "";
				
				
//				List<String[]> s1 = csvReader.readAll();
//				int leng = s1.size();
//				System.out.println("*************"+leng);
				
				
				while ((nextRecord = csvReader.readNext()) != null) { 
					String s = "";
					int z = 0;
					if(ifExists(nextRecord[0])){
						for (int i = 0; i <= nextRecord.length; i++) {
							z++;
							if (z < nextRecord.length) {
								s = s + (nextRecord[i] + ",");
							}
							if(z == nextRecord.length){
								s = s + nextRecord[i];
							}
						}
						counter++;

						if (lines > counter) {
							processString = s;
							filteredList.add(processString);
						}
						if (lines == counter) {
							processString = s;
							filteredList.add(processString);
						}	
					}

				}
				// write a file to string
				writeToFile(filteredList, file);
				// clear the list
				filteredList.clear();	
				filereader.close();
				csvReader.close();
			}
			Instant end = Instant.now();
			long timeElapsed = Duration.between(start, end).toMillis();
			JOptionPane.showMessageDialog(frame, "All Done! Now Click OK To Reach The Output Files. Execution time : " + timeElapsed + " Millisecond");
			Desktop.getDesktop().open(new File(outputDirectoryPath));
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
	}

	public static boolean ifExists(String csvString) {
		boolean ifSuccess = detector.add(csvString);
		return ifSuccess;
	}

	public static int lineCounter(String f)  {

		int countPage = 0;
		try {
			File file = new File(f);
			FileReader filereader = new FileReader(file);
			CSVReader csvReaders = new CSVReader(filereader); 
			countPage = csvReaders.readAll().size();
			csvReaders.close();
			filereader.close();

		} catch (IOException e) { } 
		return countPage;
	}

	public static void writeToFile(List<String> writableString, File fileAbsolutepath) {

		try {
			String prepareFilename = fileAbsolutepath.getName();
			String locationToBeStored = outputDirectoryPath + "\\"+ prepareFilename;
			FileWriter writer = new FileWriter(locationToBeStored); 
			for(String str: writableString) {
				writer.write(str + System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Returns the list of Files
	public static File[] getListOfFiles(String ResourcePath) {
		File[] fileNames = new File(ResourcePath).listFiles(File::isFile);
		return fileNames;
	}

}

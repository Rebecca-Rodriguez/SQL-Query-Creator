package src;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Creator {
    public static void main(String[] args) {

        // read the file with all the urls inside
        String line;
        ArrayList<String> fileInfo = new ArrayList<String>();
        try {
            File myObj = new File("urls.txt");
            Scanner reader = new Scanner(myObj);
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                fileInfo.add(line);
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            line = null;
        }

        ArrayList<String> variable1 = new ArrayList<String>();
        ArrayList<String> variable2 = new ArrayList<String>();

        // go through each url and save the two parameters
        for (int i = 0; i < fileInfo.size(); i++) {
            String website = fileInfo.get(i);
            int slashCount = 0;
            int startIndex = 0;

            for (int j = 0; j < website.length(); j++) {
                if (website.charAt(j) == '/') {
                    slashCount++;
                }

                if (slashCount == 8) {
                    startIndex = j + 1;
                    break;
                }
            }

            try {
                String secondPart = website.substring(startIndex, website.length());
                variable1.add(secondPart.substring(0, secondPart.indexOf('.')));

                String thirdPart = secondPart.substring(secondPart.indexOf('.') + 1, secondPart.length());

                // check for period first
                if (thirdPart.contains(".")) {
                    variable2.add(thirdPart.substring(0, thirdPart.indexOf(".")));

                }
                else if (thirdPart.contains("?")) {
                    variable2.add(thirdPart.substring(0, thirdPart.indexOf("?")));

                }
            }
            catch(StringIndexOutOfBoundsException e)
            {
                System.out.println("Make sure there are no empty lines in the urls.txt file.");
            }
        }
        

        // create SQL command with parameters
        String sqlFirst = "SELECT PORTAL_URI_SEG1 AS MENUNAME, PORTAL_URI_SEG2 AS COMPONENT FROM PSPRSMDEFN\n"
                + "WHERE\n";

        String sqlSecond = new String();
        for (int k = 0; k < variable1.size(); k++) {
            if (k > 0) {
                sqlSecond += "OR ";
            }
            sqlSecond += "(PORTAL_URI_SEG1 = '" + variable1.get(k) + "' AND PORTAL_URI_SEG2 = '" + variable2.get(k) + "')\n";
        }
        String sqlCommand = sqlFirst + sqlSecond + ";";

        // create and write to the file
        try {
            File myObj = new File("SqlQuery.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created " + myObj.getName());
            }
            else {
                System.out.println("File " + myObj.getName() + " updated.");
            }
            FileWriter writer = new FileWriter("SqlQuery.txt");
            writer.write(sqlCommand);
            writer.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}

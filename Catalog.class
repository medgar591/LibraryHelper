/**
 * Class to create and manage databases used by LibraryHelper
 * 
 * @author Matt Edgar 
 * @version 0.1
 */

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;

public class Catalog
{
    final static String DELIMETER = ",";
    private static String fileToParse;
    private static BufferedReader fileReader = null;
    public static void main (String[]args)
    {
        fileToParse = "sample.csv";
        try
        {
            String line = "";
            fileReader = new BufferedReader(new FileReader(fileToParse));
            
            while ((line = fileReader.readLine()) != null)
            {
                String[] tokens = line.split(DELIMETER);
                for (String token : tokens)
                {
                    System.out.println(token);
                }
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void other() throws FileNotFoundException
    {
        //Get scanner instance
        Scanner scanner = new Scanner(new File("sample.csv"));
         
        //Set the delimiter used in file
        scanner.useDelimiter(",");
         
        //Get all tokens and store them in some data structure
        //I am just printing them
        while (scanner.hasNext())
        {
            System.out.print(scanner.next() + "|");
        }
         
        //Do not forget to close the scanner 
        scanner.close();
    }
    
    public static String getStatus(String name) throws FileNotFoundException
    {
        s = new Scanner(new File("sample.csv"));
        s.useDelimiter(DELIMITER);
        
        String current = "", status = "";
        name = name.toLowerCase();
        while (s.hasNext())
        {
            
            current = s.next();
            System.out.println(name + " " + current);
            if (current.equalsIgnoreCase(name))
            {
                current = s.next();
                System.out.print(current);
                return current;
            }
        }
        return "error";
    }
    
    public Catalog (String name)
    {
        
    }
}

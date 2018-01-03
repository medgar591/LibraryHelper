/**
 * Track and manage the database within the library. Tracks people, fines, and books. Uses CSV Files. 
 *
 * @author Matt Edgar
 * @version 1.0
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;
import javax.swing.JTextPane;
import java.awt.Font;

public class Catalog
{
    private final String PEOPLE = "people.csv";
        //name, teacher(T or S), fines remaining, date fines last paid, book1 id, book1 dueDate, book2 id, book2 dueDate, book3 id (T only), book3 dueDate, book4 id (T only), book4 dueDate
    private final String BOOKS = "books.csv";
        //id #, Title, Author
    private final String DELIMITER = ",";
    private File people = new File(PEOPLE);
    private File books = new File(BOOKS);
    //Main constructor ensures that the files exist before anything else occurs. Also creates a title line on each file to ensure formatting. 
    public Catalog()
    {
        if (!people.exists())
            PowerPoint.infoBox("No " + PEOPLE + " file found. Creating one.");
            try{
                people.createNewFile();
            } catch (IOException e) {
                PowerPoint.infoBox("Error creating " + PEOPLE + " file. Error is fatal to program. Please close out and restart.");
            }
        if (!books.exists())
            PowerPoint.infoBox("No " + BOOKS + " file found. Creating one.");
            try {
                books.createNewFile();
            } catch (IOException e) {
                PowerPoint.infoBox("Error creating " + BOOKS + " file. Error is fatal to program. Please close out and restart.");
            }
    }
    
    //Method to delete all of the databases. Included for testing purposes. Not called ANYWHERE for safety.
    public void deleteDatabases()
    {
        people.delete();
        books.delete();
        if (!books.exists() && !people.exists())
            PowerPoint.infoBox("Databases deleted. Create a new Catalog Object to create new ones.");
        else
            PowerPoint.infoBox("Error deleting databses.");
    }
    
    //Method to add a new person to the PEOPLE database
    public void addPerson(String name, boolean teacher)
    {
        String type;
        Date stamp = new Date();
        if (teacher)
            type = "T";
        else
            type = "S";
        try {
            BufferedWriter typer = new BufferedWriter( new FileWriter(people, true));
            String[] input = {name, type, "0.00", stamp.getTime() + "", "", "", "", "", "", "", "", ""};
            for (String k : input)
            {
               typer.write(k, 0, k.length()); 
               typer.write(44); //ascii for the "," character
            }
            typer.newLine();
            typer.close();
        } catch (IOException e) {
            PowerPoint.infoBox("There was an error adding " + name + " to the database. Please try again. If problems persist, restart LibraryHelper.");
        }
    }
    
    //Method to add a new book to the BOOKS database
    public void addBook(int id, String title, String author)
    {
        String number = "" + id;
        try {
            BufferedWriter typer = new BufferedWriter(new FileWriter(books, true));
            typer.write(number, 0 , number.length());
            typer.write(44); //ascii for the "," character
            typer.write(title, 0, title.length());
            typer.write(44); //ascii for the "," character
            typer.write(author, 0 , author.length());
            typer.newLine();
            typer.close();
        } catch (IOException e) {
            PowerPoint.infoBox("There was an error adding " + title  + " by " + author + " id#" + id + " to the database. Please try again. If problems persist, restart LibraryHelper."); 
        }        
    }
    //Method to obtain and return one of the full databases as an array
    public String[] getList(String source)
    {
        File data;
        if (source.equals(PEOPLE))
            data = people;
        else
            data = books;
        String[] database = {};
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(data));
            int lines = 1;
            while (lnr.readLine() != null)
            {
                lines++;
            }
            lnr.close();
            database = new String[(lines-1)];
            BufferedReader read = new BufferedReader(new FileReader(data));
            for (int i = 0; i < database.length; i++)
            {
                database[i] = read.readLine();
            }
            
        } catch (IOException e) {
            PowerPoint.infoBox("There was an error importing " + data + ". It may be necessary to restart the program.");
        } finally{
            return database;
        }
    }
    
    //Method to republish the catalog. Used to update cells and other changes. 
    public void updateCatalog(String fileName, String[] data)
    {
        try{
            BufferedWriter typer = new BufferedWriter( new FileWriter( new File(fileName)));
            for (String s : data)
            {
                typer.write(s, 0 , s.length());
                typer.newLine();
            }
            typer.close();
        } catch (IOException e) {
            PowerPoint.infoBox("There was an error updating the database.");
        }        
    }
    
    //Method to create a report file and print it.
    public void printReport(String report)
    {
        File reportFile = new File ("report.txt");
        try{
            reportFile.createNewFile();
        } catch (IOException e) {
            PowerPoint.infoBox("An error occured while preparing the report: Creating file");
            return;
        }
        try {
            BufferedWriter typer = new BufferedWriter(new FileWriter(reportFile));
            typer.write(report, 0, report.length());
            typer.close();
        } catch (IOException e) {
            PowerPoint.infoBox("An error occured while preparing the report: Writing to file"); 
        }
        try {
            JTextPane jtp = new JTextPane();
            jtp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            boolean show = true;
            boolean continues = true;
            BufferedReader reader = new BufferedReader( new FileReader(reportFile));
            String total = "";
            String nextLine = "";
            while (continues)
            {
                nextLine = reader.readLine();
                if (nextLine == null)
                {
                    continues = false;
                    continue;
                }
                total += nextLine  + "\n";
            }
            jtp.setText(total);
            jtp.print(null, null, show, null, null, show);
        } catch (java.awt.print.PrinterException | IOException ex) {
            PowerPoint.infoBox("Error Printing.");
        }
        reportFile.delete();
    }
    //Method for testing reading and writing to CSV files
    public static void testMethod()
    {
    }
}

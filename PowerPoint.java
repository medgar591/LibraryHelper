/**
 * Visuals Class for LibraryHelper
 * 
 * @author Matt Edgar 
 * @version 1.0
 */

import javax.swing.JOptionPane;
import java.util.Date;
import java.text.NumberFormat;

public class PowerPoint
{
    //Simplified method for a JOptionPane input dialog box
    public static String queryBox (String query)
    {
        return JOptionPane.showInputDialog(null, query, "LibraryHelper", JOptionPane.QUESTION_MESSAGE);
    }
    
    //Specialized method using a JOptionPane to pull out a double, specifically for money transactions in LibraryHelper such as paying fines
    public static double numberQueryBox(String query)
    {
        String input = " ";
        double number = 0;
        boolean isNum = false;
        do{
            input = JOptionPane.showInputDialog(null, query, "LibraryHelper", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.isEmpty())
                continue;
            isNum = true;
            for (int k = input.length()-1; k >= 0; k--)
            {
                if (!(Character.isDigit(input.charAt(k)) || input.charAt(k) == '.'))
                    isNum = false;                    

            }
        } while (!isNum);
        number = Double.parseDouble(input);
        return number;
    }
    
    //Main menu screen of the program with all of the different commands which can be used 
    public static int mainMenu()
    {
        Object[] options  = { "List People or Books", "Add Person or Book", "Look up Person or Book", "Pay Fines", "Check out book", "Return Books", "Weekly Reports", "Edit Name", "Remove Person or Book", "Helpful Information" };
        return JOptionPane.showOptionDialog( null, "Welcome to the Rockwood Summit Library! What would you like to do?", "LibraryHelper",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
    
    //Method to parse database entries for display. Type 0 is people, Type 1 is books
    public static void listBox(String[] list, int type)
    {
        String total = "";
        int books = 0;
        Date due;
        NumberFormat dollars = NumberFormat.getCurrencyInstance();
        for (String s : list)
        {
            if (s != null && !(s.equals("")))
            {
                String[] line  = s.split(",");
                if (type == 0)
                {
                    String title = "error";
                    total += line[0] + " - ";
                    if (line[1].equalsIgnoreCase("t"))
                        total += "Teacher\n";
                    else total += "Student\n";
                    total += "    Fines: " + dollars.format(Double.parseDouble(line[2])) + "\n";
                    total += "    Books: \n";
                    Catalog catalog = new Catalog();
                    for (int k = 4; k < line.length; k += 2)
                    {
                        if (line[k] != null && !(line[k].equals("")))
                        {
                            for (String t : catalog.getList("books.csv"))
                            {
                                if (t.indexOf(line[k]) == 0)
                                {
                                    title = t.split(",")[1];
                                }
                            }
                            due = new Date(Long.parseLong(line[k+1]));
                            total += "        " + title + " due " + due + "\n";
                        }
                    }
                }
                else 
                {
                    total += line[0] + " - " + line[1] + " by " + line[2] + "\n"; 
                }
            }
        }
        infoBox(total);
        return;
    }
    
    //Simplified JOptionPane command to display a piece of information. 
    public static void infoBox(String info)
    {
        JOptionPane.showMessageDialog(null, info, "LibraryHelper", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Simplified JOptionPane command to ask a yes/no question and format response as a boolean.
    public static boolean yesNoBox(String query)
    {
        int choice = JOptionPane.showConfirmDialog(null, query, "LibraryHelper",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == 0)
            return true;
        else
            return false;
    }
    
    public static int peopleBooksBox(String query, Object[] options)
    {
        return JOptionPane.showOptionDialog( null, query, "LibraryHelper",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
}

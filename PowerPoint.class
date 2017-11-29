/**
 * Visuals Class for LibraryHelper
 * 
 * @author Matt Edgar 
 * @version 0.1
 */

import javax.swing.JOptionPane;

public class PowerPoint
{
    public static String queryBox (String query)
    {
        return JOptionPane.showInputDialog(null, query, "LibraryHelper", JOptionPane.QUESTION_MESSAGE);
    }
    
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
                switch (input.charAt(k))
                {
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                    case '0':
                    case '.':
                        break;
                    default:
                        isNum = false;
                        break;
                }
            }
        } while (!isNum);
        if (isNum)
            number = Double.parseDouble(input);
        else number = 0;
        return number;
    }
    
    public static int mainMenu()
    {
        Object[] options  = { "Add Person", "Add Book", "Check Person", "Pay Fines", "Check out book", "List People", "List Books", "Weekly Report", "Remove Book", "Remove Person" };
        return JOptionPane.showOptionDialog( null, "Welcome to the Library! What would you like to do?", "LibraryHelper",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }
    
    public static void listBox()
    {
    }
    
    public static void infoBox(String info)
    {
        JOptionPane.showMessageDialog(null, info, "LibraryHelper", JOptionPane.INFORMATION_MESSAGE);
    }
}

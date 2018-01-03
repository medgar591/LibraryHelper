/**
 * Main worker class which holds all of the functionality.
 * Program Functions:
 *  -Name School
 *  -List Students and Teachers
 *  -List books
 *  -Edit Student and Teacher Names
 *  -Track issuance of books by person
 *  -Limit number of books teachers can have (4) and how long they can have them (30 days)
 *  -Limit number of books students can have (2) and how long they can have them (10 days)
 *  -Be able to add people and books (Every book gets a different id#)
 *  -Create a weekly report of books issued and time remaining
 *  -Create a weekly report for fines in the library
 *  -Pay fines
 *  {List (People or Books), Add (People or Books), Check(People or Books), Pay fines, Check out books, Return Books, Reports (books or fines), Edit Names(People), Remove People or Books} 
 *  
 *  ~~~~~FINES WILL BE UPDATED WHEN PROGRAM STARTS~~~~~~
 * @author Matt Edgar
 * @version 1.1
 */

import java.util.Date;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
public class Librarian
{
    private int task;
    Catalog catalog;
    public Librarian()
    {
        catalog = new Catalog();
    }
    
    public static void main(String[]args)
    {
        Librarian librarian = new Librarian();
        librarian.coreLoop();
    }
    //Main loop of the program, shows the main menu and runs appropriate method to accomplish user input. Clicking 'X' gives a -1 and quits the program.
    public void coreLoop()
    {
        updateFines();
        //Main loop of the program
        do
        {
            task = PowerPoint.mainMenu();
            switch (task)
            {
                case 0:
                    list();
                    break;
                case 1:
                    add();
                    break;
                case 2:
                    lookup();
                    break;
                case 3:
                    payFines();
                    break;
                case 4:
                    checkOut();
                    break;
                case 5:
                    returnBooks();
                    break;
                case 6:
                    weeklyReport();
                    break;
                case 7:
                    fixName();
                    break;
                case 8:
                    remove();
                    break;
                case 9:
                    info();
                    break;
            }
        } while (task != -1);
        
    }
    
    public void list()
    {
        int choice = -1;
        String[] list;
        
        Object[] options = {"People", "Books"};
        choice = PowerPoint.peopleBooksBox("Would you like to list people or books?", options);
        
        if (choice == 0)
            list = catalog.getList("people.csv");
        else if (choice == 1)
            list = catalog.getList("books.csv");
        else
            list = new String[] {};
        if (choice != -1)
        {
            PowerPoint.listBox(list, choice);
        }
    }
    
    public void add()
    {
        Object[] options = {"Person", "Book"};
        int choice = -1;
        String name;
        String author;
        String[] books = catalog.getList("books.csv");
        String[] people = catalog.getList("people.csv");
        int id;
        boolean isTeacher;
        boolean isUnique = true;
                   
        choice = PowerPoint.peopleBooksBox("Would you like to add a person or a book?", options);            
        if (choice == 0)
        {
            do
            {
                name = PowerPoint.queryBox("What is this person's name? Note: use [First name] [Last name] format please. This is case sensitive.");
                if (name == null)
                    return;
                else
                {
                    for (String s : people)
                    {
                        if (s.indexOf(name) == 0 && s.indexOf(name) + name.length() == s.indexOf(","))
                        {
                            isUnique = false;
                            PowerPoint.infoBox("There already exists a " + name + " within the system.");
                            return;
                        }
                    }
                }
                isTeacher = PowerPoint.yesNoBox("Are they a teacher? Note: clicking x will default to no (a student)");
            } while (name.equals("")|| name.indexOf(",") != -1 || !isUnique);
            catalog.addPerson(name, isTeacher);
        }
        else if (choice == 1)
        {   
            do
            {
                name = PowerPoint.queryBox("What is the title of the book? Note: Please do not use any commas.");
                if (name == null)
                    return;
                author = PowerPoint.queryBox("Who is the author? Note: Use [First name] [Last name] format please.");
                if (author == null)
                    return;
                do
                {
                    id = (int) (Math.random() * 1000000);
                    isUnique = true;
                    for (String s : books)
                    {
                        if ( s.indexOf(id) != -1 )
                            isUnique = false;
                    }
                } while (!isUnique);                
            } while (name.equals("") || author.equals("") || name.indexOf(",") != -1 || author.indexOf(",") != -1);
            catalog.addBook(id, name, author);
        }
    }
    
    public void lookup()
    {
        Object[] options = {"Person", "Book"};
        String input = "";
        int choice = PowerPoint.peopleBooksBox("Would you like to look up a person or a book?", options);
        
        if (choice == 0)
        {
            do
            {
                input = PowerPoint.queryBox("Who are you looking up? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.");
                if (input == null)
                    return;
            } while( input.equals("") || input.indexOf(",") != -1);
            for (String s : catalog.getList("people.csv"))
            {
                if (s.indexOf(input) == 0 && s.indexOf(input) + input.length() == s.indexOf(","))
                {
                    String[] words = {s};
                    PowerPoint.listBox(words, 0);
                    return;
                }
            }
            PowerPoint.infoBox(input + " could not be found in the database.");
            return;
        }
        else if (choice == 1)
        {
            options = new Object[] {"ID", "Title", "Author"};
            do
            {
                choice = PowerPoint.peopleBooksBox("How would you like to look up the book?", options);
                switch (choice)
                {
                    case -1:
                        return;
                    case 0:
                        input = "" + (int) PowerPoint.numberQueryBox("What is the book's ID number?");
                        break;
                    case 1:
                        input = PowerPoint.queryBox("What is the book's title? Note: Titles are not stored with commas.");
                        break;
                    case 2:
                        input = PowerPoint.queryBox("Who is the author? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.");
                        break;
                }
                if (input == null)
                    return;
            }while (input.equals("") || input.indexOf(",") != -1);
            String[] list = catalog.getList("books.csv");
            String[] matches = new String[list.length];
            int tick = 0;
            for (String s : list)
            {
                if (s.indexOf(input) != -1 && (s.indexOf(input) + input.length() == s.indexOf(",",7) || s.indexOf(input) + input.length() == s.length()))
                {
                    matches[tick] = s;
                    tick++;
                }
            }
            if (matches[0] == null)
            {
                PowerPoint.infoBox(input + " could not be found in the database");
                return;
            }
            PowerPoint.listBox(matches, 1);
            return;
        }
        else
            return;
    }
        
    public void payFines()
    {
        String name;
        NumberFormat dollars = NumberFormat.getCurrencyInstance();
        Date payday = new Date();
        String[] list = catalog.getList("people.csv");
        String[] person = new String[12];
        int indexNum = 0;
        boolean found = false;
        do
        {
            name = PowerPoint.queryBox("Who is paying a fine? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.");
            if (name == null)
                return;
        } while( name.equals("") || name.indexOf(",") != -1);
        for (String s : list)
        {
            if (s.indexOf(name) == 0 && s.indexOf(name) + name.length() == s.indexOf(","))
            {
                person = s.split(",");
                found = true;
                break;
            }
            indexNum++;
        }
        if (found)
        {
            double fines = Double.parseDouble(person[2]);
            if (fines == 0)
            {
                PowerPoint.infoBox(name + " has no fines at this time.");
                return;
            }
            double paid = PowerPoint.numberQueryBox(name + " has " + dollars.format(fines) + " in fines. How much are they paying? Note: enter 0 if they do not pay anything.");
            double remaining = fines;
            if (paid == 0)
                return;
            if (paid >= fines)
            {
                remaining = 0.00;
                PowerPoint.infoBox(dollars.format(paid) + " pays off the fine. Please give " + dollars.format(paid - fines) + " in change.");
            }
            else
            {
                remaining = fines - paid;
                PowerPoint.infoBox("Paying " + dollars.format(paid) + " leaves " + dollars.format(remaining) + " of fines remaining.");
            }
            person[2] = "" + remaining;
            person[3] = payday.getTime() + "";
            String total = "";
            for (String s : person)
            {
                total += s + ",";
            }
            if (person.length < 12)
            {
                for (int k = person.length; k < 12; k++)
                    total += ",";
            }
            list[indexNum] = total;
            catalog.updateCatalog("people.csv", list);
            return;
        }
        PowerPoint.infoBox(name + " could not be found in the database.");
    }
    
    public void checkOut()
    {
        String name;
        String[] people = catalog.getList("people.csv");
        String[] books = catalog.getList("books.csv");
        String[] person = new String[12];
        Date due = new Date();
        String type;
        int id;
        int alreadyOut = 0;
        int indexNum = 0;
        boolean found = false;
        boolean out = false;
        
        do
        {
            name = PowerPoint.queryBox("Who is checking out a book? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.");
            if (name == null)
                return;
        } while( name.equals("") || name.indexOf(",") != -1);
        for (String s : people)
        {
            if (s.indexOf(name) == 0 && s.indexOf(name) + name.length() == s.indexOf(","))
            {
                for (int k = 0; k < s.split(",").length; k++)
                    person[k] = s.split(",")[k];
                found = true;
                break;
            }
            indexNum++;
        }
        if (found)
        {
            //first making sure the person can check out another book
            for (int k = 4; k < person.length; k += 2)
            {
                if (person[k] != null && !(person[k].equals("")))
                {
                    alreadyOut++;
                }
            }
            type = person[1];
            if (type.equalsIgnoreCase("s") && alreadyOut >= 2)
            {
                PowerPoint.infoBox(name + " already has " + alreadyOut + " books checked out. Their limit is 2.");
                return;
            }
            else if (type.equalsIgnoreCase("t") && alreadyOut >= 4)
            {
                PowerPoint.infoBox(name + " already has " + alreadyOut + " books checked out. Their limit is 4.");
                return;
            }
            
            //Making sure the book CAN be checked out
            id = (int) PowerPoint.numberQueryBox("What is the ID of the book?");
            found = false;
            for (String s : books)
            {
                if (s.indexOf(""+ id) == 0)
                {
                    found = true;
                }
            }
            for (String s : people)
            {
                if (s.indexOf("," + id + ",") != -1)
                    out = true;
            }
            if (found)
            {
                if (out)
                {
                    PowerPoint.infoBox(id + " is already checked out.");
                    return;
                }

                //Acutally checking the book out from here on.
                else
                {
                    if (type.equalsIgnoreCase("s"))
                        due.setDate(due.getDate() + 10);
                    else if (type.equalsIgnoreCase("t"))
                        due.setDate(due.getDate() + 30);
                    for ( int k = 4; k < person.length; k += 2)
                    {
                        if (person[k] == null || person[k].equals(""))
                        {
                            person[k] = "" + id;
                            person[k+1] = "" + due.getTime();
                            String total = "";
                            for (String s : person)
                            {   
                                if (s!= null && !(s.equals("")))
                                    total += s;
                                total +=",";
                            }
                            people[indexNum] = total;
                            catalog.updateCatalog("people.csv", people);
                            PowerPoint.infoBox(name + " has succesfully checked out " + id);
                            return;
                        }                    
                    }
                }
            }
            else
            {
                PowerPoint.infoBox(id + " could not be found in the database.");
                return;
            }
        }
        else PowerPoint.infoBox(name + " could not be found in the database.");   
    }
    
    public void returnBooks()
    {
        int id = (int) PowerPoint.numberQueryBox("What is the ID of the book being returned?");
        String[] books = catalog.getList("books.csv");
        String[] people = catalog.getList("people.csv");
        boolean isBook = false;
        boolean isOut = false;
        int indexNum = 0;
        String[] person = new String[12];
        String total = "";
        //Checking to see if the book exists
        for (String s : books)
        {
            if (s.indexOf(""+id) == 0)
            {
                isBook = true;
                break;
            }
        }
        if (!isBook)
        {
            PowerPoint.infoBox(id  + " could not be found within the database.");
            return;
        }
        
        //Making sure the book has been checked out
        for (String s : people)
        {
            if (s.indexOf("," + id + ",") != -1)
            {
                isOut = true;
                break;
            }
            indexNum++;
        }
        if (!isOut)
        {
            PowerPoint.infoBox(id + " is not currently checked out.");
            return;
        }
        
        //Actually returning the book.
        for (int k = 0; k < people[indexNum].split(",").length; k++)
        {
            person[k] = people[indexNum].split(",")[k];
        }
        for (int k = 4; k < 12; k++)
        {
            if (person[k].equals(""+id))
            {
                person[k] = "";
                person[k+1] = "";
                isOut = false;
                break;
            }
        }
        if (isOut)
        {
            PowerPoint.infoBox("An error occured while checking out " + id);
            return;
        }
        for (String s : person)
        {
            if (s != null)
                total += s;
            total += ",";
        }
        people[indexNum] = total;
        catalog.updateCatalog("people.csv", people);
        PowerPoint.infoBox(id + " was succesfully returned.");
    }
    
    public void weeklyReport()
    {
        Object[] options = {"Books", "Fines"};
        String[] people = catalog.getList("people.csv");
        String[] books = catalog.getList("books.csv");
        String[] line = new String[12];
        String[] nextLine = new String[4];
        String total = "";
        List<String[]> booksOut = new ArrayList<String[]>();
        List<String[]> finesUnpaid = new ArrayList<String[]>();
        int choice = PowerPoint.peopleBooksBox("Which type of weekly report would you like to print?", options);
        int listIndex = 0;
        NumberFormat dollars = NumberFormat.getCurrencyInstance();
        Date today = new Date();
        total += "Report for the week of ";
        switch (today.getMonth())
                {
                    case 0:
                        total += "Jan ";
                        break;
                    case 1:
                        total += "Feb ";
                        break;
                    case 2:
                        total += "Mar ";
                        break;
                    case 3:
                        total += "Apr ";
                        break;
                    case 4:
                        total += "May ";
                        break;
                    case 5:
                        total += "Jun ";
                        break;
                    case 6:
                        total += "Jul ";
                        break;
                    case 7:
                        total += "Aug ";
                        break;
                    case 8:
                        total += "Sep ";
                        break;
                    case 9:
                        total += "Oct ";
                        break;
                    case 10:
                        total += "Nov ";
                        break;
                    case 11:
                        total += "Dec ";
                        break;                    
                }
                if (today.getDate() < 10)
                    total += "0";
                total += today.getDate() + "\n";
        if (choice == -1)
            return;
        else if (choice == 0) //Books,id, issued to, due
        {
            String[] titles = {String.format("%1$25s", "Title"), String.format("%1$6s","ID"), String.format("%1$25s","Issued To"),String.format("%1$6s", "Due")};
            booksOut.add(titles);
            Date due = new Date();
            String dueDate = "";
            if (people == null || people.length == 0)
            {
                PowerPoint.infoBox("There are no books checked out.");
            }
            for (String s : people)
            {
                if (s == null || s.equals(""))
                {
                   PowerPoint.infoBox("There are no books checked out.");
                   return;
                }
                //Putting all checked out books into a list
                for (int k = 0; k < s.split(",").length; k++)
                {
                    line[k] = s.split(",")[k];
                }
                for (int k = 4; k < line.length; k += 2)
                {
                    if (line[k] != null && !(line[k].equals("")))
                    {
                        nextLine[1] = line[k];
                        nextLine[2] = line[0];
                        nextLine[3] = line[k+1];
                        booksOut.add(nextLine.clone());
                    }
                }
                nextLine = new String[4]; //reset to prevent overwriting things
                line = new String[12]; //reset to prevent people having books that they dont have
            }
            
            //Fixing the formatting of all the entries within the List (except the title line on k = 0)
            for (int k = 1; k < booksOut.size(); k++)
            {
                //Entry 0 - Book Name - Limited to 25 Characters and padded with whitespace                
                for (String s : books)
                {
                    if (s.indexOf(booksOut.get(k)[1]) == 0)
                    {
                        String title = s.split(",")[1];
                        if (title.length() >= 25)
                            title = title.substring(0,25) + ".";
                        else
                            title = String.format("%1$25s", title);
                        booksOut.get(k)[0] = title;
                        break;
                    }
                }
                //Entry 1 - ID# is fine already
                //Entry 2 - Person Name - Limited to 25 Characters and padded with whitespace
                String name = booksOut.get(k)[2];
                if (name.length() >= 25)
                    booksOut.get(k)[2] = name.substring(0,25) + ".";
                else
                    booksOut.get(k)[2] = String.format("%1$25s", name);
                //Entry 3 - Due Date - put into "Mmm Dd" format
                due = new Date (Long.parseLong(booksOut.get(k)[3]));
                switch (due.getMonth())
                {
                    case 0:
                        dueDate += "Jan ";
                        break;
                    case 1:
                        dueDate += "Feb ";
                        break;
                    case 2:
                        dueDate += "Mar ";
                        break;
                    case 3:
                        dueDate += "Apr ";
                        break;
                    case 4:
                        dueDate += "May ";
                        break;
                    case 5:
                        dueDate += "Jun ";
                        break;
                    case 6:
                        dueDate += "Jul ";
                        break;
                    case 7:
                        dueDate += "Aug ";
                        break;
                    case 8:
                        dueDate += "Sep ";
                        break;
                    case 9:
                        dueDate += "Oct ";
                        break;
                    case 10:
                        dueDate += "Nov ";
                        break;
                    case 11:
                        dueDate += "Dec ";
                        break;                    
                }
                if (due.getDate() < 10)
                    dueDate += "0";
                dueDate += due.getDate();
                booksOut.get(k)[3] = dueDate;
                dueDate = ""; //reset to prevent weird date things
            }
            if (booksOut.size() == 1)
            {
                PowerPoint.infoBox("There are no books currently checked out.");
                return;
            }
            for (String[] s : booksOut)
            {
                total += "| " + s[0] + " | " + s[1] + " | " + s[2] + " | " + s[3] + " |\n";
            }
        }
        else //Name, Fines
        {
            nextLine = new String[2];
            String[] titles = {String.format("%1$25s","Name"), String.format("%1$12s","Fines Owed")};
            finesUnpaid.add(titles.clone());
            //creating the list
            if (people == null || people.length == 0)
            {
                PowerPoint.infoBox("There are no fines currently.");
            }
            for (String s : people)
            {
                if (s == null || s.equals(""))
                {
                    PowerPoint.infoBox("There are no fines currently");
                    return;
                }
                for (int k = 0; k < s.split(",").length; k++)
                {
                    line[k] = s.split(",")[k];
                }
                if (Double.parseDouble(line[2]) == 0)
                    continue;
                nextLine[0] = line[0];
                nextLine[1] = String.format("%1$12s",dollars.format(Double.parseDouble(line[2])));
                //Formatting and padding names - 25 characters
                if (nextLine[0].length() >= 25)
                    nextLine[0] = nextLine[0].substring(0,25) + ".";
                else
                    nextLine[0] = String.format("%1$25s", nextLine[0]);
                finesUnpaid.add(nextLine.clone());
                nextLine = new String[2]; //reset for safety
            }
            if (finesUnpaid.size() == 1)
            {                
                PowerPoint.infoBox("There are no fines currently");
                return;                
            }
            for (String[] s : finesUnpaid)
            {
                total += "| " + s[0] + " | " + s[1] + " |\n";
            }
        }
        catalog.printReport(total);
    }
    
    public void fixName()
    {
        String name;
        String newName;
        String[] people = catalog.getList("people.csv");
        String[] person = new String[12];
        boolean found = false;
        int indexNum = 0;
        String total = "";
        do
        {
            name = PowerPoint.queryBox("Whose name needs to be changed? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.");
            if (name == null)
                return;
        } while( name.equals("") || name.indexOf(",") != -1);
        for (String s : people)
        {
            if (s.indexOf(name) == 0 && s.indexOf(name) + name.length() == s.indexOf(","))
            {
                for (int k = 0; k < s.split(",").length; k++)
                    person[k] = s.split(",")[k];
                found = true;
                break;
            }
            indexNum++;
        }
        if (found)
        {
            do
            {
                newName = PowerPoint.queryBox("What should " + name + "'s name be changed to? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.");
                if (newName == null)
                    return;
            } while( newName.equals("") || newName.indexOf(",") != -1);
            person[0] = newName;
            for (String s : person)
            {   
                if (s != null)
                    total += s;
                total += ",";
            }
            people[indexNum] = total;
            catalog.updateCatalog("people.csv", people);
            PowerPoint.infoBox(name + " has succesfully been changed to " + newName);
            return;
        }
        else PowerPoint.infoBox(name + " could not be found in the database.");
    }
    
    public void remove()
    {
        String[] people;
        String[] books = catalog.getList("books.csv");
        String name;
        String[] fixed;
        boolean found = false;
        boolean confirm = false;
        int indexNum = 0;
        int indexNum2 = 0;
        int choice = 0;
        String question = "";
        Object[] options = {"Person","Book"};
        choice = PowerPoint.peopleBooksBox("Are you removing a person or a book?",options);
        String filename;
        if (choice == -1)
            return;
        else if (choice == 0)
        {
            question = "Who is being removed from the database? Note: This is case sensitive and names are stored in [First name] [Last name] format, no commas.";
            people = catalog.getList("people.csv");
            filename = "people.csv";
        }
        else
        {
            question = "What is the ID of the book being removed?";
            people= catalog.getList("books.csv");
            filename = "books.csv";
        }
        fixed = new String[people.length - 1];
        do
        {
            name = PowerPoint.queryBox(question);
            if (name == null)
                return;
        } while( name.equals("") || name.indexOf(",") != -1);
        for (String s : people)
        {
            if (s.indexOf(name) == 0 && s.indexOf(name) + name.length() == s.indexOf(","))
            {
                found = true;
                break;
            }
            indexNum++;
        }
        if (!found)
        {
            PowerPoint.infoBox(name + " could not be found in the database.");
            return;
        }        
        confirm = PowerPoint.yesNoBox("Are you sure that you would like to remove " + name + " from the database? This is permanent and lost data cannot be recovered.");
        if (!confirm)
            return;
        if (choice == 1)
        {
            boolean isOut = false;
            String[] people2 = catalog.getList("people.csv");
            String[] person = new String[12];
            String total = "";
            for (String s : people2)
            {
                if (s.indexOf("," + name + ",") != -1)
                {
                    isOut = true;
                    break;
                }
                indexNum2++;
            }
            if (isOut)
            {
                //Actually returning the book.
                for (int k = 0; k < people2[indexNum2].split(",").length; k++)
                {
                    person[k] = people2[indexNum2].split(",")[k];
                }
                for (int k = 4; k < 12; k++)
                {
                    if (person[k] != null && person[k].equals(name))
                    {
                        person[k] = "";
                        person[k+1] = "";
                        isOut = false;
                        break;
                    }
                }
                if (isOut)
                {
                    PowerPoint.infoBox("An error occured while removing " + name);
                    return;
                }
                for (String s : person)
                {
                    if (s != null)
                    total += s;
                    total += ",";
                }
                people2[indexNum2] = total;
                catalog.updateCatalog("people.csv", people2);               
            }                
        }
        String[] part1 = Arrays.copyOfRange(people, 0, indexNum);
        String[] part2 = Arrays.copyOfRange(people, indexNum + 1, people.length);
        for (int k = 0; k < part1.length; k++)
            fixed[k] = part1[k];
        for (int k = part1.length; k < fixed.length; k++)
            fixed[k] = part2[k - part1.length];
        catalog.updateCatalog(filename, fixed);
        PowerPoint.infoBox(name + " has been succesfully removed from the database");
    }
    
    public void info()
    {
        PowerPoint.infoBox("   This is the LibraryHelper for the Rockwood Summit Library!\n\n-All book IDs are randomly assigned and 6 digits long.\n-Students can check out up to 2 books for 10 days\n" + 
        "-Teachers can check out up to 4 books for 30 days\n-Fines accrue at a rate of $0.05 per day overdue with no upper limit.\n\n                     This program was created by Matt Edgar");
    }
    
    public void updateFines()
    {
        String[] people = catalog.getList("people.csv");
        String[] person = new String[12];
        Date update = new Date();
        Date due = new Date();
        Date last = new Date();
        double fines;
        int booksOut = 0;
        int indexNum = 0;
        int overdue;
        long mOverdue;
        String total = "";
        for (String s : people)
        {
            for (int k = 0; k < s.split(",").length; k++)
            {
                person[k] = s.split(",")[k];
            }
            if (s == null || s.equals(""))
                return;
            fines = Double.parseDouble(person[2]);
            last = new Date(Long.parseLong(person[3]));
            if (last.getYear() == update.getYear() && last.getMonth() == update.getMonth() && last.getDate() == update.getDate())
                continue;
            for (int k = 4; k < person.length; k += 2)
            {
                if (!(person[k] == null || person[k].equals("")))
                {
                    booksOut++;
                }
            }
            if (booksOut == 0)
                continue;
            for (int k = 4; k < person.length; k += 2)
            {
                if (!(person[k] == null || person[k].equals("")))
                {
                    due = new Date(Long.parseLong(person[k+1]));
                    if (update.before(due))
                    {
                        continue;
                    }
                    if (update.getMonth() == due.getMonth()  && update.getDate() == due.getDate() && update.getYear() == due.getYear())
                    {
                        continue;
                    }
                    else
                    {
                        mOverdue = update.getTime() - due.getTime();
                        overdue =(int) (mOverdue / (double) (1000 * 3600 * 24) + 1);
                        fines += 0.05 * overdue;
                        continue;
                    }
                }
            }
            person[2] = "" + fines;
            person[3] = "" + update.getTime();
            for (String k : person)
            {
                if (!(s.equals("")))
                    total += k;
                total += ",";
            }
            people[indexNum] = total;
            total = "";
            indexNum++;
            person = new String[12];
            booksOut = 0;
            continue;
        }
        
        catalog.updateCatalog("people.csv",people);
    }
}

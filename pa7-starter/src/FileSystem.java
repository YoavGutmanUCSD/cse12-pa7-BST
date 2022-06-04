import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

// This class intends to implement a filesystem.
// However, it does not do it well.
// ..
public class FileSystem {

    BST<String, FileData> nameTree;
    BST<String, ArrayList<FileData>> dateTree;

    // Default constructor that creates a new FileSystem object and initializes its instance variable.
    public FileSystem() {
        this.nameTree = new BST<String, FileData>();
        this.dateTree = new BST<String, ArrayList<FileData>>();
    }


    //Constructor that creates a new FileSystem object with the given inputFile that contains the file system information
    public FileSystem(String inputFile) {
        // Add your code here
        this.nameTree = new BST<String, FileData>();
        this.dateTree = new BST<String, ArrayList<FileData>>();
        try {
            File f = new File(inputFile);
            Scanner sc = new Scanner(f);
            // read file line by line and add each one as a FileData object
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                // Add your code here
                String fileName = data[0];
                String fileDir = data[1];
                String fileDate = data[2];
                add(fileName, fileDir, fileDate);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    // This method should create a FileData object with the given file information and 
    // add it to the instance variables of FileSystem. 
    // If there is a duplicate file name, then the FileData with the most recent date should be used. 
    public void add(String name, String dir, String date) {
        if (name == null || dir == null || date == null){
            return;
        }

        // file we wanna add
        FileData fileToAdd = new FileData(name, dir, date);

        // first, check if name is already in nameTree
        // null if not in nameTree, and a file if in
        FileData fileByName = nameTree.get(name);  

        ArrayList<FileData> fileByDate = dateTree.get(date);

        // if the name is not there yet, we always add
        if(fileByName == null) {
            nameTree.put(name, fileToAdd);

            // adding to datetree is hard
            // if there is no date like this, we need to add it anew
            if(fileByDate == null) {
                ArrayList<FileData> fileArrayToAdd = new ArrayList<FileData>();
                fileArrayToAdd.add(fileToAdd);
                //System.out.println(fileArrayToAdd);
                dateTree.put(date, fileArrayToAdd);
                //System.out.println(dateTree.get("2022-04-21"));
                return;
            }
            // date already exists? no problem
            // since it's a different filename, just add
            fileByDate.add(fileToAdd);
            dateTree.replace(date, fileByDate);
            return;
        }

        // tricky, filename is already in.
        // if the date is different and newer, we update
        // we care about dir here too?
        else if (fileByName.lastModifiedDate.compareTo(fileToAdd.lastModifiedDate) < 0) {
            // simply replace with the newer file
            // IF dir is diff
            //if(fileByName.dir.equals())
            nameTree.set(name, fileToAdd);

            // if there is no date like this, we need to add it anew
            // and also remove the file with the older date
            if(fileByDate == null) {
                // adding anew
                ArrayList<FileData> fileArrayToAdd = new ArrayList<FileData>();
                fileArrayToAdd.add(fileToAdd);
                dateTree.put(date, fileArrayToAdd);

                // removing the file with the older date
                ArrayList<FileData> olderFile = dateTree.get(fileByName.lastModifiedDate);
                for(int i = 0; i < olderFile.size(); i++) {
                    if(olderFile.get(i).name.equals(name)){
                        // remove where it's new
                        olderFile.remove(i);
                        // replacing the older file
                        dateTree.replace(fileByName.lastModifiedDate, olderFile);
                        return;
                    }
                }
            }

            // if this date exists, we need to add the file to the already existing date array
            // but still remove the older date
            fileByDate.add(fileToAdd);
            dateTree.replace(date, fileByDate);

            // removing the file with the older date
            ArrayList<FileData> olderFile = dateTree.get(fileByName.lastModifiedDate);
            for(int i = 0; i < olderFile.size(); i++) {
                if(olderFile.get(i).name.equals(name)){
                    // remove where it's new
                    olderFile.remove(i);
                    // replacing the older file
                    dateTree.replace(fileByName.lastModifiedDate, olderFile);
                    return;
                }
            }


        } else {
            nameTree.put(name, fileToAdd);
            //dateTree.put(date, fileToAdd);
        }

    }




    // Given a date (format: yyyy/mm/dd), return an ArrayList of file names that correspond to this date. 
    //This list should have the file names in the order that they were added.
    // If the date given is null, return null.
    public ArrayList<String> findFileNamesByDate(String date) {
        // return null;
        if(date == null) return null;
        ArrayList<String> dateNameList = new ArrayList<String>();
        ArrayList<FileData> dateFileDataList = dateTree.get(date);
        for(int i = 0; i < dateFileDataList.size(); i++){
            dateNameList.add(dateFileDataList.get(i).name);
        }
        return dateNameList;
    }


    // Given a startDate and an endDate (format: yyyy/mm/dd), 
    // return a new FileSystem that contains only the files that are within the range 
    // (startDate is inclusive, endDate is exclusive).
    public FileSystem filter(String startDate, String endDate) {
        // return null;
        FileSystem newFileSystem = new FileSystem();
        String currentDate;
        int[] endDateInt = parseDate(endDate);
        int[] currentDateInt = parseDate(startDate);

        while(!generateDateString(currentDateInt).equals(endDate)){
            System.out.println(generateDateString(currentDateInt));
            currentDate = generateDateString(currentDateInt);
            ArrayList<FileData> allFilesOnDate = dateTree.get(currentDate);
            if(allFilesOnDate != null) {
                for(int i = 0; i < allFilesOnDate.size(); i++){
                    FileData currentData = allFilesOnDate.get(i);
                    newFileSystem.add(
                        currentData.name, currentData.dir, currentData.lastModifiedDate
                        );
                }
            }
            currentDateInt = incrementParsedDate(currentDateInt);
        }
        return newFileSystem;
    }

    // convert a string in format YYYY-MM-DD into an int array like [YYYY, MM, DD]
    private int[] parseDate(String date){
        String[] dateSplit = date.split("-");
        int[] year_month_day = new int[3];
        for(int i = 0; i < year_month_day.length; i++){
            year_month_day[i] = Integer.valueOf(dateSplit[i]);
        }
        return year_month_day;
    }
    // this function is irrelevant basically
    private String generateDateString(int year, int month, int day){
        return String.format("%s/%s/%s", year, month, day);
    }

    // reverses parseDate
    // takes an int array [YYYY, MM, DD] and turns it into YYYY-MM-DD 
    private String generateDateString(int[] dateInfo){
        // if month is a value under 10, add a 0 so it's 0x instead of x.
        if(dateInfo[1] < 10){
            return String.format("%s-0%s-%s", dateInfo[0], dateInfo[1], dateInfo[2]);
        }
        return String.format("%s-%s-%s", dateInfo[0], dateInfo[1], dateInfo[2]);
    }

    // increment date. more involved than you might think.
    // increments month, day, date, according to the.. calendar
    private int[] incrementParsedDate(int[] parsedDate){
        int[] newDate;
        int year = parsedDate[0];
        int month = parsedDate[1];
        int day = parsedDate[2];
        int threshold = daysInMonth(month);
        // if day threshold reached, increment month 
        if (threshold == day){
            // if month threshold reached, increment year and go to january 1st
            if (month == 12){
                return new int[]{year + 1, 1, 1};
            }
            return new int[]{year , month + 1, 1};
        }
        return new int[]{year, month, day+1};
    }

    // some months have 30 days, others have 31, one in particular has 28. 
    // this method will return the proper number of days according to the month
    private int daysInMonth(int month){
        int[] monthsWhere31Days = {1, 3, 5, 7, 8, 10, 12};
        int february = 2;
        if (february == month) {
            return 28;
        }
        for(int i = 0; i < monthsWhere31Days.length; i++){
            if(month == monthsWhere31Days[i]){
                return 31;
            }
        }
        return 30;
    }


    // Give a string wildCard, return a new FileSystem that contains only the files with names that 
    // contain the wildCard string. 
    // Note that this wildcard can be found anywhere in the file name 
    // (if the wild card is test, then test.txt, 
    // thistest.txt and thistest would all be files that should be selected in the filter)
    public FileSystem filter(String wildCard) {
        // return null;
        FileSystem newFileSystem = new FileSystem();
        List<String> datesToExamine = new LinkedList<String>();
        for(String date: dateTree.keys()){
            if(date.contains(wildCard)){
                datesToExamine.add(date);
            }
        }
        for(String date: datesToExamine){
            List<FileData> filesAtDate = dateTree.get(date);
            for(FileData file: filesAtDate){
                newFileSystem.add(file.name, file.dir, file.lastModifiedDate);
            }
        }
        // int[] endDateInt = parseDate(endDate);
        // int[] currentDateInt = parseDate(startDate);
        return newFileSystem;
    }


    // Return a List that contains the name
    // where each entry is formatted as: ": <FileData toString()>"
    public List<String> outputNameTree(){
        // List<String> returnable = new ArrayList<String>();
        // List<String> allNames = nameTree.keys();
        return nameTree.keys();
    }


    // Return a List that contains the datetree
    //  where each entry is formatted as: ": <FileData toString()>"
    public List<String> outputDateTree(){
        List<String> returnable = new ArrayList<String>();
        List<String> allDates = dateTree.keys();
        //System.out.println(allDates);
        // System.out.format("allDates length is %s\n", allDates.size());
        // System.out.format("dateTree size is %s\n", dateTree.size());
        for(int i = 0; i < allDates.size(); i++){
            //System.out.format("i is %s\n", i);
            String key = allDates.get(i);
            ArrayList<FileData> allFiles = dateTree.get(key);
            if(allFiles != null) {
            for(int j = 0; j < allFiles.size(); j++){
                // System.out.format("i is %s\n", i);
                //System.out.println(key);
                String entry = genEntry(key, allFiles.get(j));
                returnable.add(entry);
            }
        }
        }
        return returnable;
    }
    private String genEntry(String key, FileData file){
        return String.format("%s: %s", key, file.toString());
    }


}



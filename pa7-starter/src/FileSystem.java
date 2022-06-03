import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class FileSystem {

    BST<String, FileData> nameTree;
    BST<String, ArrayList<FileData>> dateTree;
    
    // TODO
    public FileSystem() {
        this.nameTree = new BST<String, FileData>();
        this.dateTree = new BST<String, ArrayList<FileData>>();
    }


    // TODO
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

    // TODO test
    public void add(String name, String dir, String date) {
        // add FileData object to both maps depending on what decideAction returns

        // do nothing in this situation
        if(name == null || dir== null || date == null) {
            return;
        }


        // add this file data object

        FileData obj = new FileData(name,dir,date);
        ArrayList<FileData> foreverEver = new ArrayList<FileData>();
        foreverEver.add(obj);

        // flags following the given method 
        boolean containsName = nameTree.keys().contains(name);
        boolean containsDate = dateTree.keys().contains(date);

        // conditions to add
        if(containsName && !containsDate) {
            nameTree.replace(name, obj);
            dateTree.put(date,foreverEver);
        }

        if(!containsName) {
            nameTree.put(name, obj);
            dateTree.put(date, foreverEver);
        }




        
    }



    // TODO test
    public ArrayList<String> findFileNamesByDate(String date) {
        if(date == null) return null;
        ArrayList<String> dateNameList = new ArrayList<String>();
        ArrayList<FileData> dateFileDataList = dateTree.get(date);
        for(int i = 0; i < dateFileDataList.size(); i++){
            dateNameList.add(dateFileDataList.get(i).name);
        }
        return dateNameList;
    }


    // TODO
    public FileSystem filter(String startDate, String endDate) {
        FileSystem newFileSystem = new FileSystem();
        String currentDate;
        int[] endDateInt = parseDate(endDate);
        int[] currentDateInt = parseDate(startDate);
        while(!currentDateInt.equals(endDateInt)){
            currentDate = generateDateString(currentDateInt);
            ArrayList<FileData> allFilesOnDate = dateTree.get(currentDate);
            for(int i = 0; i < allFilesOnDate.size(); i++){
                FileData currentData = allFilesOnDate.get(i);
                newFileSystem.add(currentData.name, currentData.dir, currentData.lastModifiedDate);
            }
            currentDateInt = incrementParsedDate(currentDateInt);
        }
        return newFileSystem;
    }

    // convert a string in format YYYY-MM-DD into an int array like [YYYY, MM, DD]
    private int[] parseDate(String date){
        String[] dateSplit = date.split("/");
        int[] year_month_day = new int[3];
        for(int i = 0; i < year_month_day.length; i++){
            year_month_day[i] = Integer.valueOf(dateSplit[i]);
        }
        return year_month_day;
    }
    // // this function is irrelevant basically
    // private String generateDateString(int year, int month, int day){
    //     return String.format("%s/%s/%s", year, month, day);
    // }

    // reverse parseDate
    private String generateDateString(int[] dateInfo){
        return String.format("%s/%s/%s", dateInfo[0], dateInfo[1], dateInfo[2]);
    }

    // increment date. more involved than you might think.
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
    // // you can use this to account for leap years, but then you'd need to add another argument to daysInMonth
    // private int februaryAccounting(int year){
    //     if(leapYear(year)){
    //         return 29;
    //     }
    //     return 28;
    // }
    // // this is the algorithm for determining a leap year.
    // private boolean leapYear(int year){
    //     if(year % 100 == 0)
    //         return year % 400 == 0;
    //     return year % 4 == 0;
    // }

    // TODO
    public FileSystem filter(String wildCard) {
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


    // TODO
    public List<String> outputNameTree(){
        // List<String> returnable = new ArrayList<String>();
        // List<String> allNames = nameTree.keys();
        return nameTree.keys();
    }


    // TODO
    public List<String> outputDateTree(){
        List<String> returnable = new ArrayList<String>();
        List<String> allDates = dateTree.keys();
        System.out.format("allDates length is %s\n", allDates.size());
        System.out.format("dateTree size is %s\n", dateTree.size());
        for(int i = 0; i < allDates.size(); i++){
            System.out.format("i is %s\n", i);
            String key = allDates.get(i);
            ArrayList<FileData> allFiles = dateTree.get(key);
            for(int j = 0; j < allFiles.size(); j++){
                String entry = genEntry(key, allFiles.get(j));
                returnable.add(entry);
            }
        }
        return returnable;
    }
    private String genEntry(String key, FileData file){
        return String.format("%s: %s", key, file.toString());
    }


}


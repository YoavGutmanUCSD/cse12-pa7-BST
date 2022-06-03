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
        int action = decideAction(name, dir, date);
        FileData file = new FileData(name, dir, date);
        if(action == 2) return;
        if(file == null) System.out.println("Something's in the way");
        addNameMap(file, action);
        addDateMap(file, action);
    }
    private void addNameMap(FileData file, int action){
        switch(action) {
            case 1:
                // replace old
                System.out.format("Replaced current value for %s in nameMap with %s.\n", file.name, file.toString());
                nameTree.replace(file.name, file);
                break;
            case 0:
                // add new
                System.out.format("Put %s in nameMap\n", file.name);
                nameTree.put(file.name, file);
                break;
            // case 2:
            default:
                // indecision
                System.out.format("Chose not to add %s to nameMap.\n", file.name);
                break;
        }
    }
    private void addDateMap(FileData file, int action){
        ArrayList<FileData> dateList; // 
        FileData oldFile;
        switch(action) {
            case 1:
                oldFile = nameTree.get(file.name);
                ArrayList<FileData> oldDateList = dateTree.get(oldFile.lastModifiedDate);
                dateList = dateTree.get(file.lastModifiedDate);
                // System.out.format("\n\n\n[[[[[[[[[[[[[[[[[CASE 1]]]]]]]]]]]]]]]]]\n\n\n");
                // find old
                for(int i = 0; i < oldDateList.size(); i++){
                    // if found, replace old
                    if(dateList.get(i).name.equals(file.name)){
                        dateList.remove(i);
                        break;
                    }
                }
                dateList.add(oldFile);
                dateTree.set(file.lastModifiedDate, dateList);
                System.out.format("Replaced current value for %s in dateMap with %s\n", file.lastModifiedDate, file.toString());
                break;
            case 0:
                // add new ArrayList for the specific date, with only 1 file
                dateList = new ArrayList<FileData>();
                dateList.add(file);
                dateTree.put(file.lastModifiedDate, dateList);
                System.out.format("Put %s in dateMap\n", file.lastModifiedDate);
                break;
            // case 2:
            default:
                // indecision
                System.out.format("Chose not to add %s to dateMap.\n", file.lastModifiedDate);
                break;
        }
    }
    private int decideAction(String name, String dir, String date){
        /* This function gives 0 if the name is different, 1 if the date is different, 2 otherwise.
         * Likely should be renamed as a function
         * According to the truth table in the writeup:
         * Add if the name is different, no matter what.
         * Replace if name is the same, but date is different. 
         * (This means you have to remove from one ArrayList and add to another in dateMap.)
         * Do nothing if only directory changes. I don't understand the logic.
         */
        if(name == null || dir == null || date == null){
            return 2;
        }
        FileData fileInSystem = nameTree.get(name);
        // true if no prior value
        if(fileInSystem == null){
            return 0;
        }

        if(!name.equals(fileInSystem.name)){ 
            return 0;
        }
        if(!date.equals(fileInSystem.lastModifiedDate)){
            return 1;
        }

        // no conditions pass = do nothing
        else {
            return 2;
        }
    }
    // private boolean shouldReplaceDatelist(String name, String dir, String date){

    // }
    // private boolean shouldAdd(String name, String dir, String date){
    //     if(name == null || dir == null || date == null){
    //         return false;
    //     }
    //     FileData fileInSystem = nameTree.get(name);
    //     if(fileInSystem == null){
    //         return true;
    //     }
    //     return false;
    // }


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

        while(!generateDateString(currentDateInt).equals(endDate)){

            if(currentDateInt.equals(parseDate("2025-01-01"))){
                System.out.println("You still have an infinite loop 4head");
                break;
            }


            System.out.println(generateDateString(currentDateInt));
            currentDate = generateDateString(currentDateInt);
            ArrayList<FileData> allFilesOnDate = dateTree.get(currentDate);
            if(allFilesOnDate != null) {
                for(int i = 0; i < allFilesOnDate.size(); i++){
                    FileData currentData = allFilesOnDate.get(i);
                    newFileSystem.add(currentData.name, currentData.dir, currentData.lastModifiedDate);
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
    // // this function is irrelevant basically
    // private String generateDateString(int year, int month, int day){
    //     return String.format("%s/%s/%s", year, month, day);
    // }

    // reverse parseDate
    private String generateDateString(int[] dateInfo){
        // if month is a value under 10, add a 0 so it's 0x instead of x.
        if(dateInfo[1] < 10){
            return String.format("%s-0%s-%s", dateInfo[0], dateInfo[1], dateInfo[2]);
        }
        return String.format("%s-%s-%s", dateInfo[0], dateInfo[1], dateInfo[2]);
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


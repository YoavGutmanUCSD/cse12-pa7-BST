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
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(", ");
                // Add your code here
                String fileName = data[0];
                String fileDir = data[1];
                String fileDate = data[2];
                add(fileName, fileDir, fileDate); // make this work when you can 
                // FileData fileOnLine = new FileData(fileName, fileDir, fileDate);
                // nameTree.put(fileName, fileOnLine);
                // ArrayList<FileData> dateList = dateTree.get(fileDate);
                // if(dateList == null){
                //     dateList = new ArrayList<FileData>();
                //     dateList.add(fileOnLine);
                //     dateTree.put(fileName, dateList);
                // }
                // else {
                //     dateList.add(fileOnLine);
                // }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }


    // TODO
    public void add(String name, String dir, String date) {
        FileData file = new FileData(name, dir, date);
        nameTree.put(name, file);
        ArrayList<FileData> dateList = dateTree.get(date);
        if(dateList == null){
            dateList = new ArrayList<FileData>();
            dateList.add(file);
            dateTree.put(name, dateList);
        }
        else {
            dateList.add(file);
        }
    }
    private int decideAction(String name, String dir, String date){
        // return 0 if add, 1 if modify, 2 if do nothing
        if(name == null || dir == null || date == null){
            return 2;
        }
        FileData fileInSystem = nameTree.get(name);
        if(name.equals(fileInSystem.name)){ // if naem is not the same, add it no matter what
            return 0;
        }
        if(date.equals(fileInSystem.lastModifiedDate)){
            return 2;
        }
        else {
            return 1;
        }
    }


    // TODO
    public ArrayList<String> findFileNamesByDate(String date) {

    }


    // TODO
    public FileSystem filter(String startDate, String endDate) {
 
    }
    
    
    // TODO
    public FileSystem filter(String wildCard) {

    }
    
    
    // TODO
    public List<String> outputNameTree(){

    }
    
    
    // TODO
    public List<String> outputDateTree(){

    }
    

}


public class FileData {

    public String name;
    public String dir;
    public String lastModifiedDate;

    // TOD
    public FileData(String name, String directory, String modifiedDate) {
        this.name = name;
        this.dir = directory;
        this.lastModifiedDate = modifiedDate;
    }

    // TOD
    public String toString() {
        String newString = "{Name: " + name + ", Directory: " + dir +", Modified Date: "+ lastModifiedDate +"}";
        return newString;

    }
}
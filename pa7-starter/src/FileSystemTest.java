import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.*;

public class FileSystemTest {
    FileSystem fileSys;
    @Before
    public void initialize(){
        this.fileSys = new FileSystem();
    }
    @Test
    public void testAddNoExceptions(){
        fileSys.add(".secrets-upon-secrets", "/home", "2022-04-20");
        fileSys.add("NuOrder Shopping List", "/home", "2022-04-20");
        fileSys.add("break_your_pc.sh", "/root", "2022-05-24");
    }
    @Test
    public void testAddFilesSize(){
        fileSys.add(".secrets-upon-secrets", "/home", "2022-04-20");
        fileSys.add("NuOrder Shopping List", "/home", "2022-04-20");
        fileSys.add("break_your_pc.sh", "/root", "2022-05-24");
        List<String> dt = this.fileSys.outputDateTree();
        System.out.println(dt.toString());
        assertEquals(dt.size(), 3);
    }
    @Test
    public void testDecideCorrectAction() {
        BST<String, FileData> nameTree;
        BST<String, ArrayList<FileData>> dateTree;

        nameTree = new BST<String, FileData>();
        dateTree = new BST<String, ArrayList<FileData>>();

        FileData secret = new FileData(".secrets-upon-secrets", "/home", "2022-04-20");
        FileData nuOrder = new FileData("NuOrder Shopping List", "/home", "2022-04-20");
        FileData break_pc = new FileData("break_your_pc.sh", "/root", "2022-05-24");

        FileData secret_diff_name = new FileData(".death-begets-death", "/home", "2022-04-20");
        FileData secret_diff_date = new FileData(".secrets-upon-secrets", "/home", "2022-05-24");
        FileData secret_diff_path = new FileData(".secrets-upon-secrets", "/root", "2022-04-20");

        ArrayList<FileData> four_twenty = new ArrayList<FileData>();
        ArrayList<FileData> five_twenty_four = new ArrayList<FileData>();

        four_twenty.add(nuOrder);
        four_twenty.add(secret);
        five_twenty_four.add(break_pc);
        nameTree.put(".secrets-upon-secrets", secret);
        nameTree.put("NuOrder Shopping List", nuOrder);
        nameTree.put("break_your_pc.sh", break_pc);
        dateTree.put("2022-04-20", four_twenty);
        dateTree.put("2022-05-24", five_twenty_four);
        DecideActionExclusive deci = new DecideActionExclusive(nameTree, dateTree);

        assertEquals(0, deci.decideAction(secret_diff_name));
        assertEquals(1, deci.decideAction(secret_diff_date));
        assertEquals(2, deci.decideAction(secret_diff_path));
    }
    @Test
    public void testFilterDatesConsecutive(){
        System.out.format("Started.\n");
        fileSys.add(".secrets-upon-secrets", "/home", "2022-04-20");
        fileSys.add("NuOrder Shopping List", "/home", "2022-04-20");
        fileSys.add("Copium King-sized", "/home", "2022-04-21"); 
        // fileSys.add("Stalinium King-sized", "/home", "2022-04-21");
        // fileSys.add("Unobtainium King-sized", "/home", "2022-04-21");
        // fileSys.add("break_your_pc.sh", "/root", "2022-05-24");
        // fileSys.add("break_your_electrical_budget.sh", "/root", "2022-05-24");
        // fileSys.add("break_your_morale.sh", "/root", "2022-05-24");
        // fileSys.add("break_your_willpower.sh", "/root", "2022-05-24");
        FileSystem newFileSys = fileSys.filter("2022-04-20", "2022-04-21");
        ArrayList<String> filesBydate = fileSys.findFileNamesByDate("2022-04-20");
        assertArrayEquals(newFileSys.findFileNamesByDate("2022-04-20").toArray(), filesBydate.toArray());

    }
    @Test
    public void testOutputDateTrees(){
        FileData secret = new FileData(".secrets-upon-secrets", "/home", "2022-04-20");
        FileData nuOrder = new FileData("NuOrder Shopping List", "/home", "2022-04-22");
        FileData copium = new FileData("Copium King-sized", "/home", "2022-04-21");
        fileSys.add(".secrets-upon-secrets", "/home", "2022-04-20");
        fileSys.add("NuOrder Shopping List", "/home", "2022-04-22");
        fileSys.add("Copium King-sized", "/home", "2022-04-21"); 
        // ArrayList<String> dateTreeOut = fileSys.outputDateTree();
        Object[] dateTreeOut = fileSys.outputDateTree().toArray();
        Object[] dateTreeExpected = {
            genEntry("2022-04-20", secret),
            genEntry("2022-04-20", nuOrder),
            genEntry("2022-04-21", copium)
        };
        System.out.println(fileSys.outputNameTree().size());
        System.out.println(fileSys.outputDateTree().size());
        for (int i = 0; i<dateTreeOut.length; i++) {
            System.out.println(dateTreeOut[i]);
        }
        // assertArrayEquals(dateTreeOut, dateTreeExpected);
        //assertArrayEquals(dateTreeOut, dateTreeExpected);
    }
    @Test 
    public void testAddDuplicates() {
        fileSys.add(".secrets-upon-secrets", "/home", "2022-04-20");
        fileSys.add(".secrets-upon-secretsz", "/home", "2022-04-20");
        fileSys.add("break_your_pc.sh", "/root", "2022-05-24");
        // fileSys.add("break_your_pc.sh", "/root", "2022-05-25");
        // fileSys.add("break_your_pc.sh", "/root", "2022-05-27");
        // fileSys.add("break_your_pc.sh", "/root", "2022-05-26");
        List<String> dt = this.fileSys.outputDateTree();
        System.out.println(dt.toString());
        assertEquals(dt.size(), 2);
    }

    @Test 
    public void addingStuff() {
        FileData secrets = new FileData(".secrets-upon-secrets", "/home", "2022-01-01");
        fileSys.add(".secrets-upon-secrets", "/home", "2022-01-01");
        fileSys.add(".secrets-upon-secrets", "/home", "2022-01-03");
        fileSys.add(".secrets-upon-secrets", "/home", "2022-01-02");

        // fileSys.add("break_your_pc.sh", "/root", "2022-05-25");
        // fileSys.add("break_your_pc.sh", "/root", "2022-05-27");
        // fileSys.add("break_your_pc.sh", "/root", "2022-05-26");
        List<String> dt = this.fileSys.outputDateTree();
        System.out.println(dt.toString());
        //assertEquals(dt.size(), 2);
        assertEquals(secrets.lastModifiedDate, dt.get(0));
    }


    private String genEntry(String key, FileData file){
        return String.format("%s: %s", key, file.toString());
    }
}

class DecideActionExclusive {
    BST<String, FileData> nameTree;
    BST<String, ArrayList<FileData>> dateTree;

    // TODO
    public DecideActionExclusive(BST nameTree, BST dateTree) {
        this.nameTree = nameTree;
        this.dateTree = dateTree;
    }
    public int decideAction(String name, String dir, String date){
        // return 0 if add, 1 if modify, 2 if do nothing
        if(name == null || dir == null || date == null){
            return 2;
        }
        FileData fileInSystem = nameTree.get(name);
        if(fileInSystem == null){
            return 0;
        }
        if(!name.equals(fileInSystem.name)){ // if naem is not the same, add it no matter what
            return 0;
        }
        if(!date.equals(fileInSystem.lastModifiedDate)){
            return 1;
        }
        else {
            return 2;
        }
    }
    public int decideAction(FileData file){
        return decideAction(file.name, file.dir, file.lastModifiedDate);
    }
}

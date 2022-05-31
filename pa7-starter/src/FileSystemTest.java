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
        assertEquals(dt.size(), 2);
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

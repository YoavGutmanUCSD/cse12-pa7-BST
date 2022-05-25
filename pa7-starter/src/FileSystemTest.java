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
        assertTrue(dt.size() != 0);
    }


}

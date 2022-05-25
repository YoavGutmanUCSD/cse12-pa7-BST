import static org.junit.Assert.*;

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

}

package Test;

import Service.DocReader;
import org.junit.Test;

public class DocReaderTest {

    @Test
    public void test(){
        DocReader docReader = new DocReader("/Users/whynot/Documents/TW/doc");
        try {
            docReader.readDocs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
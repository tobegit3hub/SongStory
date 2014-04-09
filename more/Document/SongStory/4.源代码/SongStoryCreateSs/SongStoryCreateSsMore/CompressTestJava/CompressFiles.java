import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.ArrayList;

public class CompressFiles{

    public static void main(String args[]) throws IOException{

     String firstFileName = "./a.xml";   
     String secondFileName = "./b.jpg";
     String thirdFileName = "../SongStory.mup";

     String zipFileName = "./tobe.ss";

    File firstFile = new File(firstFileName);
    File secondFile = new File(secondFileName);
    File thirdFile = new File(thirdFileName);

     ArrayList<File> files = new ArrayList<File>();
     files.add(firstFile);
     files.add(secondFile);
     files.add(thirdFile);

     byte[] buffer = new byte[4096];
     int bytesRead;

     ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));

     for(int i=0; i<files.size(); ++i){

        File file = files.get(i);
        
        FileInputStream in = new FileInputStream(file);
        
        //ZipEntry entry = new ZipEntry(file.getPath());
        ZipEntry entry = new ZipEntry(file.getName());
        
        out.putNextEntry(entry);

        while((bytesRead = in.read(buffer)) != -1){
            out.write(buffer, 0, bytesRead);
        }

        in.close();
     }

     out.close();
    }


}

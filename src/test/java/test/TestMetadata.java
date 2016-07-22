/*
 * Created on 24.02.2004
 *
 */
package test;

import java.io.File;
import java.util.Iterator;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 * @author J�rgen Weber
 *  
 */
public class TestMetadata
{

    public static void main(String[] args) throws Exception
    {
        File jpegFile = new File("D:/java/jakarta-tomcat-5.0.18/webapps/ROOT/testalbum/CRW_9754.jpg");
        Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
        

        
//      iterate through metadata directories
        Iterator<Directory> directories = metadata.getDirectories().iterator();
        while (directories.hasNext()) {
            Directory directory = (Directory)directories.next();
            // iterate through tags and print to System.out
            Iterator tags = directory.getTags().iterator();
            while (tags.hasNext()) {
                Tag tag = (Tag)tags.next();
                // use Tag.toString()
                System.out.println(tag);
                //System.out.println(tag.getTagName()+" "+tag.getDescription());
            }
        }
    }
}

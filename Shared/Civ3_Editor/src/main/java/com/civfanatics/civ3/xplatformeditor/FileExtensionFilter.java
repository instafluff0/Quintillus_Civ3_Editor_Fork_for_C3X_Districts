package com.civfanatics.civ3.xplatformeditor;

import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.FileView;
/**
 * Implements a FileExtensionFilter.
 * Essentially, this is a re-implementation of the FileExtensionFilter that was
 * added to Java 1.6, but for Java 1.5.
 * @author Andrew
 */
public class FileExtensionFilter extends javax.swing.filechooser.FileFilter implements FilenameFilter {

    String[]extensions;
    String description;

    /**
     * The default constructor.
     *
     * @param extensions - The file extensions that are permissable.
     */
    public FileExtensionFilter(String[] extensions, String description)
    {
        this.extensions = extensions;
        this.description = description;
    }
    /**
     * Whether the given file is accepted by this filter.
     * @param f - The file in question.
     * @return - Whether the file is accepted by this filter.
     */
    public boolean accept(File f)
    {
        String name = f.getName();
        char dot = '.';
        for (int i = name.length() - 1; i > - 1; i--)
        {
            if (f.isDirectory())
            {
                return true;
            }
            if (name.charAt(i) == dot)
            {
                String extension = name.substring(i + 1);
                for (int j = 0; j < extensions.length; j++)
                {
                    if (extensions[j].equals(extension))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean accept(File f, String s) {
        String name = s;
        char dot = '.';
        for (int i = name.length() - 1; i > - 1; i--)
        {
            if (name.charAt(i) == dot)
            {
                String extension = name.substring(i + 1);
                for (int j = 0; j < extensions.length; j++)
                {
                    if (extensions[j].equals(extension))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * @see FileView#getName
     * @return - What type of files are displayed.
     */
    public String getDescription()
    {
        return description;
    }
}

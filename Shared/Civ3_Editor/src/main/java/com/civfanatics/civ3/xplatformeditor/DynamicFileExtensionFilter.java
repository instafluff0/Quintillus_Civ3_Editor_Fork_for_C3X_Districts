/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import java.io.File;

/**
 *
 * @author Andrew
 */
public class DynamicFileExtensionFilter extends FileExtensionFilter {

    String curText = "";

    public void updateText(String newText)
    {
        curText = newText;
    }

    /**
     * The default constructor.
     *
     * @param extensions - The file extensions that are permissable.
     */
    public DynamicFileExtensionFilter(String[] extensions, String description)
    {
        super(extensions, description);
    }

        /**
     * Whether the given file is accepted by this filter.
     * @param f - The file in question.
     * @return - Whether the file is accepted by this filter.
     */
    @Override
    public boolean accept(File f)
    {
        String name = f.getName();
        char dot = '.';
        if (f.isDirectory())
        {
            return true;
        }
        if (!(name.toLowerCase().contains(curText.toLowerCase())))
            return false;
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
}

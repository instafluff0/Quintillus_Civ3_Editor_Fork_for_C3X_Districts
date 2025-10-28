This project is a scenario editor for Civilization III.  For information about the editor itself, see the thread at the CivFanatics forum (https://forums.civfanatics.com/threads/cross-platform-editor-for-conquests-now-available.377188/).  This readme explains technical aspects of the project, in case you'd like to make a contribution.

The editor depends on the Civ3 Shared Components code located at https://bitbucket.org/QuintillusCFC/civ3_shared_components.  If you wish to create your own Civ3-related project and need to read scenario files, but are not creating a scenario editor, you should start there instead.  The shared components have the code for reading and writing to scenario (.biq) files; this repository has the code for the editor that allows creating and modifying scenarios.

This repository is under the MIT license - you can modify it however you want, and do not have to make your changes open source (the shared components repository is Mozilla licensed).  However, sharing your contributions is encouraged!

### How do I get set up? ###

You will need to clone both this project and the Shared Components one with Mercurial, as this one depends on Shared Components.  Then use Maven to build first the Shared Components, and then this project.

The editor has been built using the NetBeans IDE.  While it may work with other IDEs, I have not explored that in detail and do not currently have plans to.

For decompressing BIQ files, the editor relies on an external BIQDecompressor.jar file, which you can download with the editor (available on the Downloads page off this repository).  You need to put it at an appropriate location in your folder structure for it to work with this project.  Let's suppose your main project, which uses Civ3_Shared_Components, is called MyCoolNewProject.  Then, your folder structure would need to look like this to allow decompression to work as expected:


```
#!java

Civ3_Editor
 -> bin
 ----> BIQDecompressor.jar
Civ3_Shared_Components
 -> Everything in this project
editormake.bat

```

With this setup, when you run the editor, the IO.java file will be able to find the decompressor at the proper location to decompress as expected.

Other dependencies should be pulled in by Maven the first time you build the project.

The main class in the project is Main.java.  Key methods are:

 - openBIQ(File file) - This is the method you call to open a BIQ file.  It should open it without issue, and the scenario UI should load.
 - initComponents() - Loads graphics for the scenario after the .biq file is read

However, most of the code is on specific tabs, such as TechTab.java, RULETab.java, etc.  Each major section of a scenario has its own file, and some have additional files as well.

There also is an editormake.bat file located in the Downloads section of this repository, which will build both this project and the shared components, and package them up for distribution if you place it at the same level as the top-level cloned repositories (as pictured above).  If you have 7-zip on your path, that will include putting the compiled code into a .zip file for ease of distribution.

#### Additional Notes ####

Currently, I am using these tools for development:

* Maven 3.5.x
* Java 1.8 for the editor, 1.5 for the shared components.
* NetBeans 8.2
* Mercurial 4.4.x

However, aside from Java, older versions generally interoperate perfectly well.  The one exception is that if you are working on the Legacy branch (which supports systems prior to Windows XP/OS X 10.8), or are working solely with the Shared Components, you will need to use NetBeans 8.1, as 8.2 does not support targeting JDKs below 1.6.

### Contribution guidelines ###

* Writing tests

Currently, there aren't really that many tests, and those that are there aren't in this project yet (though they will likely be moved to it in time).  Tests are generally appreciated.

* Code review

Code will be reviewed for accuracy before being accepted.  Do note that this may take a few days; if so it more likely means I've been pursuing interests than that I don't like the code.

* Other guidelines

Any changes should still leave the project working with my preferred tool set, in particular Maven and NetBeans.  Contributions that make it easier to work with other tools are okay, so long as they don't make it more difficult to work with the existing ones.

### Who do I talk to? ###

Quintillus at the civfanatics.com forums is the main point of contact.  Questions should be submitted to the thread at http://forums.civfanatics.com/showthread.php?t=377188.
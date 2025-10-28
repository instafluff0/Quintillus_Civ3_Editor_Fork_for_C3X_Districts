# README #

The code in this project will let you load up Civilization III scenario files, and then manipulate them how you like.  It's intended for those looking to make Civ3 utilities, but not wishing to write the code to read scenarios from scratch again.

Feel free to use it in your own projects.  If doing so, you should be aware of the following:

* The API is not guaranteed to remain the same, although changes are slow.  I've been moving it towards a better design than it had before, but that is not complete.
* It can open any Civ3 scenario file - including scenarios for the base game or either expansion.  It always saves as scenarios for the most recent expansion.
* The licensing is Mozilla Public License.  This means that you can use it in either open or closed source projects, but if you make changes to this code and make your project that uses it public, you need to make it available publicly, so that they might be added to the main version.

### How do I get set up? ###

The project uses Mercurial for source control, so you'll need it.  Back when I started this project circa 2010, I found it easier to pick up than Git.  The build setup is based on Maven.  I've been using the (free) NetBeans IDE, although it should work with other IDEs that are familiar with Maven as well.  You'll also need to have some BIQ files available locally to test with.

For decompressing BIQ files, the editor relies on an external BIQDecompressor.jar file, which you can download with the editor.  You need to put it at an appropriate location in your folder structure for it to work with this project.  Let's suppose your main project, which uses Civ3_Shared_Components, is called MyCoolNewProject.  Then, your folder structure would need to look like this to allow decompression to work as expected:


```
#!java

MyCoolNewProject
 -> bin
 ----> BIQDecompressor.jar
Civ3_Shared_Components
 -> Everything in this project

```

With this setup, when you run MyCoolNewProject, the IO.java file will be able to find the decompressor at the proper location to decompress as expected.

Other dependencies should be pulled in by Maven the first time you build the project.

The main class in the project is IO.java.  Essentially, an IO object is a BIQ file.  Key methods are:

 - inputBIQ(File file) - This is the method you call to open a BIQ file.  It should open it without issue, and give you an IO object that contains the full BIQ.
 - outputBIQ(File file) - Similarly, this saves a BIQ.  You should add front-end code to detect duplicate files and so forth if you desire that functionality.
 - inputBIQFromScenario(File file) - Though poorly named, this method inputs BIQs from SAV files.  It will search the SAV for relevant BIQ sections.  It does not import any additional data from the SAV file (i.e. anything that changed after the game started).

Once the BIQ is inputted, the BIQ sections will be in a number of lists, such as culture, difficulties, and eras.  You can use these to access the various BIQ sections.  Note that even sections such as RULE that in practice will only appear once are also in lists, since the BIQ format doesn't technically forbid there being more than one.

### Contribution guidelines ###

* Writing tests

Currently, there aren't really that many tests, and those that are there aren't in this project yet (though they will likely be moved to it in time).  Tests are generally appreciated.

* Code review

Code will be reviewed for accuracy before being accepted.  Do note that this may take a few days; if so it more likely means I've been pursuing interests than that I don't like the code.

* Other guidelines

Any changes should still leave the project working with my preferred tool set, in particular Maven and NetBeans.  Contributions that make it easier to work with other tools are okay, so long as they don't make it more difficult to work with the existing ones.

### Who do I talk to? ###

Quintillus at the civfanatics.com forums is the main point of contact.  Questions should be submitted to the thread at http://forums.civfanatics.com/showthread.php?t=377188.
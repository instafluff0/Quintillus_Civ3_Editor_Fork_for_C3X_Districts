# Civ 3 Cross Platform Editor for C3X Districts

This is a fork of the excellent [Civ 3 Cross-Platform Editor by Quintillus](https://forums.civfanatics.com/resources/cross-platform-civ3-editor.15288/), adapted to support [the Districts feature](https://github.com/instafluff0/C3X_Districts/blob/master/README.md) of the popular [C3X mod](https://forums.civfanatics.com/resources/c3x.28759/).

![districts_editor](https://github.com/user-attachments/assets/93ac9f3b-dc9c-49a7-8b08-5b0d77186193)

Output:

<img width="702" height="62" alt="image" src="https://github.com/user-attachments/assets/3d9f8c71-9f30-4460-a8c4-0d7a8fc9b983" />

## Installation

The installation requirements and steps are exactly the same as those described in the [main Cross-Platform Civ3 Editor instructions in Civfanatics](https://forums.civfanatics.com/resources/cross-platform-civ3-editor.15288/). 

The only difference is the location to download the Editor itself, which can be found [here under Releases](https://github.com/instafluff0/Quintillus_Civ3_Editor_Fork_for_C3X_Districts/releases) under the name `conquests_editor_w_districts-1.6.zip`. The Editor and download link will be moved to CivFanatics relatively soon.

## Usage

This adaptation of the Editor changes only 2 things:
1) Support for adding/removing/editing Districts on the `MAP` tab
2) Support for loading & saving `*.c3x.txt` files to store your Districts and load from C3X/Civ

### Editing Districts in the Map
You can find the "Districts mode" `D` button in the top right. Selecting that will enable you to add districts to the map.

<img width="250" height="400" alt="image" src="https://github.com/user-attachments/assets/cfbe6ae9-61e0-48a9-bb05-5f0a8f28c2bf" />

### District Configurations and Images loaded in the Editor

Just like in C3X, the Editor will search for and parse

1) `default.districts_config.txt`  (under `/C3X`)
2) `custom.districts_config.txt`   (optional, under `/C3X`)
3) `scenario.districts_config.txt` (optional, in the same folder as your scenario BIQ file)

If any config files are found, the values override entries from the previous one. The District art image, in turn, is loaded based on your config values (see the [C3X District documentation](https://github.com/instafluff0/C3X_Districts/blob/master/README.md#configuration) for details).

### Saving and Loading Districts

The editor introduces a very simple human-readable file format for saving Districts. From the above example:

<img width="338" height="432" alt="image" src="https://github.com/user-attachments/assets/85d9f32b-401f-440a-8a33-ca883e6bfe10" />

The Editor will save the file with **exactly the same name as your scenario**, and **this is what C3X expects as well**. So if you save a scenario called `my_scenario`, you will see output files in the same folder for both the BIQ and `<scenario>.c3x.txt` files:

<img width="702" height="62" alt="image" src="https://github.com/user-attachments/assets/3d9f8c71-9f30-4460-a8c4-0d7a8fc9b983" />

And that's basically it!

> Why not save directly in the BIQ file? I tried a number of approaches with this, but the BIQ file really is set in stone and not amenable to new sections, at least in my testing (both Civ 3 and the Editor itself broke). On the flip-side, one major benefit of having a human-readable file for this though is how easy it is to interpret and update yourself!

### Running with C3X

In the upcoming release of C3X, Civ 3 will automatically read and add your Districts alongside your BIQ file, **provided they have the same name and are in the same folder**:

<img width="1123" height="475" alt="image" src="https://github.com/user-attachments/assets/e68d58bd-1e1e-42e4-ac2d-011690f4a7d5" />

<img width="698" height="555" alt="image" src="https://github.com/user-attachments/assets/03aab1c0-cde5-49fa-93f0-946b7cc18b14" />

If you don't have a `<scenario>.c3x.txt` file, that's fine too and Districts will not be added. C3X will also notify you of any issues it has in parsing the file, skipping district entries with errors:

<img width="266" height="72" alt="image" src="https://github.com/user-attachments/assets/25b1a8ad-d24f-45f4-b2e2-1ed34a64c842" />

<img width="1239" height="268" alt="image" src="https://github.com/user-attachments/assets/037d6a5d-cbe1-4098-ac23-c11dd8d0aebd" />

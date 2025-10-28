/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.specialty;

/**
 *
 * @author Andrew
 */
public enum SafetyLevel {
    //No safety - no protections against messing up the file.
    //It will be possible to modify a file with SAFETY_NONE, save it, and not
    //be able to reopen it again.
    None,
    //Minimal safety protections - protects against too long of strings going
    //into the next file and making the BIQ unusable (even by the editor).
    //Anything that doesn't make the BIQ technically illegal will be allowed,
    //even if it will not be accepted by Civ3Conquests or Civ3ConquestsEdit.
    //Any file saved with SAFETY_MINIMAL changes should be readable by this
    //editor, although there may be exceptions.
    Minimal,
    //Anything that is known to cause a problem with Civ3Conquests (Windows
    //version 1.22) will be disabled, such as 4 options for world sizes.
    //If something isn't allowed by Firaxis's editor but has not been shown to
    //cause problems in Civ3, it will still be allowed on the chance that a new
    //capability will be uncovered.
    //This level is recommended for those looking for new ways to push the Civ3
    //engine.
    //Files saved with SAFETY_EXPLORATORY should be readable by this editor, but
    //might not be with Civ3ConquestsEdit or Conquests itself.  Note that
    //Civ3Conquests does sometimes open files that it would not create itself.
    Exploratory,
    //At this level, only those settings which are known to be supported by
    //Conquests will be enabled.  This includes some settings that aren't
    //permissable in Firaxis' editor, such as however many difficulty levels
    //you want, and some number of flavors other than 7.
    //The Windows edition of Conquests, version 1.22, will be used for decisions
    //as to what qualifies for this level unless otherwise mentioned, as it is
    //both more common and available for the author to test with.
    //This level is recommended for general-purpose use, especially for those
    //interested in new developments in Civ3 modding possibilities.
    //Files saved with SAFETY_SAFE should be perfectly playable by Conquests,
    //although it is possible Firaxis' editor would complain about them or
    //not be able to properly render all the options (e.g. it couldn't render
    //8 flavors properly, even if Conquests can handle that many).
    Safe,
    //At this level, the capabilities of the editor should be equal to that of
    //the Firaxis editor.
    //This is recommended for those who wish to be sure that their scenarios
    //will not fall victim to any unofficial capability that appears to work
    //not actually working all the time.  It also is recommended for those who
    //wish to be sure they do not use any unofficial abilities that turn out
    //to only work in one version of Conquests (e.g. Windows 1.22).
    //Files saved with SAFETY_FIRAXIS should work with both Conquests and
    //the official Firaxis editor.
    Firaxis,
    //At this level, the editor will attempt to check for problems that may
    //cause an error, but would not be caught by the Firaxis editor.  This
    //includes, for example, errors due to an art file missing.
    //This may or may not be implemented at release.
    //This may be moved to a separate check, as players may wish to check
    //for art files all being present, but also wish to use exploratory features
    //or unofficial but apparently working capabilities.
    Total;
}

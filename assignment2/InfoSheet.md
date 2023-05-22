# CS 1501 Assignment Information Sheet

You must electronically submit an information sheet with
every assignment. Also be sure to submit all materials
following the procedures described in the assignment
description.

Name: Rand Hasan

Assignment #: 2

Source code (.java) file name(s):

AutoComplete.java
AutoCompleteInterface.java
A2Test.java

**Does your program compile without errors?:**
Yes

**If not, what is/are the error(s)?:**
None

**Does your program run without errors?:**
No

**If not, what is/are the error(s) and which parts of your program run correctly?:**
Everything works accept for two instances.  If add() is called, isWord() is false when it should be true.  Also, getNumberOfPredictions() does not always return the correct number of predictions, specifically after advancing.  I've realized that the advance() method is very likely the root of these issues as it iterates through the entire DLB Trie rather than just proceeding from the currentNode pointer.

**Additional comments (including problems and extra credit):**
None

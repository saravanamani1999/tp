# Pranav Venkatram's Project Portfolio Page

## Project Overview

Kolinux is an all-in-one application for NUS freshmen to manage their work and learn more about NUS through its
integrated features such as the Module Manager, Timetable, Event Planner, CAP Calculator, and Bus Route Finder. 
These features are powered by the NUSMods API. Written in Java, Kolinux provides a CLI for users to interact with the app.

## Summary of Contributions

* **Code contributed**: [RepoSense link](https://nus-cs2113-ay2122s1.github.io/tp-dashboard/?search=giterator&sort=groupTitle&sortWithin=title&since=2021-09-25&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=false)

* **Main feature implemented**: Module Manager
  
  * Ability to view module information for any module offered by NUS 
  * Ability to add modules to the module list from a pre-existing repository of all modules offered at NUS.
  * Ability to list all stored modules in the module list.
  * Ability to delete modules from the module list using their module codes.
  * Ability to set grades for each stored module in the module list
  * Ability to load/store the module list from/to a local text file.
  
* **Enhancement added**: Use of a HashMap to store all module details offered by NUS
  
  * What it does: The HashMap enables fast querying of module related data for all features to use. It also allows fast insertion of module data from the NUSmods JSON while building the module repository.
  
  * Justification: Module related information forms the basis of Kolinux. The current architecture involves loading all modules' data from the NUSmods JSON upon startup and querying module data often. Due to the large number of modules offered by NUS, fast querying and loading is essential. Each module's data is stored in an instance of a ModuleDetails object. Each module's code is considered as a key and its associated ModuleDetails instance is considered as a value. These key-value pairs are inserted into the HashMap.
  
  * Highlights: This enhancement changes the way the repository of modules is stored and accessed. Given that all module and timetable related commands interact with module data by specifying module codes, using module codes as keys was an intuitive design decision.
  
    Since the HashMap data structure in Java provides common methods, our methods to insert/query data to/from the module repository are more concise. Further, when querying module data using any invalid module code (key), the HashMap always returns `null`. Thus, only one corner case needs to be accounted for when querying module data, improving testability.
  
* **Enhancement added**: Converting module related data from Unicode to ASCII using the [JUnidecode library](https://github.com/gcardone/junidecode)
  
  * What it does: When loading module data from the NUSmods API, it sometimes contains Unicode characters. The JUnidecode library is used to convert strings containing Unicode characters into their equivalent ASCII representation e.g. `résumé` is converted to `resume`. 
  * Justification: Many modules stored in the NUSmods JSON have descriptions that contain Unicode characters. When users attempt to view such module data, unicode characters are not formatted properly in standard output e.g.  `’` is printed as `??`.  This enhancement ensures uniform formatting for all module related data using ASCII characters so that it can be viewed properly.
  * Highlights: This enhancement involves transliteration i.e. transferring a word from the alphabet of one language to another. This requires associating every character in an alphabet to its equivalent ASCII character. By integrating the JUnidecode library with Kolinux, this transliteration is performed by calling a single method on each string to be converted. Thus, the ModuleDetails code only required slight modification, reducing the risk of regressions.
  
* **Contributions to User Guide**:
  
  * Added documentation for the feature `module` which includes `module add`, `module delete`, `module list`, `module grade` and `module view`.
  * Ensured the language used was consistent with the rest of the UG.
  
* **Contributions to Developer Guide**:
  
  * Added documentation for the design and architecture of the module component.
  * Added documentation for implementation of `add` to and `delete` from module list features.
  * Added instructions for manual testing of all module manager related commands.
  * Added UML diagrams: `ModuleClassDiagram.puml`, `ModuleAddSequenceDiagram.puml`
  
* **Contributions to team-based tasks**:
  * Incorporated [JUnidecode library](https://github.com/gcardone/junidecode)
  * Code Refactoring
  
* **Review/mentoring contributions**:

  - Reviewed pull requests and merged them with the master branch.
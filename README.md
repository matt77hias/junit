# JUnit Test Daemon

Course Design of Software Systems: JUnit Test Daemon

**Team**:
* [Mattias Buelens](http://github.com/MattiasBuelens) (Computer Science)
* [Matthias Moulin](https://github.com/matt77hias) (Computer Science)
* [Ruben Pieters](https://github.com/rubenpieters) (Computer Science)
* [Vital D'haveloose](http://github.com/vital-dhaveloose) (Computer Science)

**Academic Year**: 2013-2014 (1st semester - 1st Master of Science in Engineering: Computer Science)

## About
Automatic test deamon that executes the JUnit tests associated with the part of the codebase that changes during development, in the background. Furthermore, the test deamon notifies the developer of the results of the JUnit tests. Consequently, the developer is informed quickly about the correctness of his code. The project is situated in the context of *continuous integration* which states that a software project should be (re)compiled and tested for each change to its source code repository.

## Use

### Running

1. Run `kuleuven.group2.Main` as main class.
2. Configure your project and choose a policy in the Configuration tab.
3. Click Start to start watching for changes.
4. View the results of each test batch in the Test results tab.
5. Optionally, compose your own custom policy in the Policy composer tab. You can then select your new policy to use in the Configuration tab.

### Testing

The `kuleuven.group2.AllTests` test suite covers the whole project (excluding the original JUnit code).

### Building

This project uses Maven for building.

Eclipse users should right-click the project and choose Configure > Configure as Maven Project.

### Original README

See README.html for the original JUnit README.

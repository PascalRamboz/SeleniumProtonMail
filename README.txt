
---------------------------------------------------------------------------------------------------------------

Author: Pascal RAMBOZ 
Publication date: Wednesday, the 15th of April, 2020 

---------------------------------------------------------------------------------------------------------------
SYSTEMATIC APPROACH TO THE PROBLEM 

The given task as an exercize consisted in selecting a test automation framework, 
write a set of automated tests for the feature described and document the project
into a README file. 

=== Language : JavaSE-1.8 (jre1.8.0_152)

Java has been chosen as a programming language as it is object oriented and very efficient 
once compiled : indeed most java libraries are written in C language. 

The version Java referenced as the JRE system library is JavaSE-1.8 (jre1.8.0_152). 

=== IDE : Mars.2 Release (4.5.2)

Eclipse has been chosen as it is one of the most ubiquitous, multiprojects, intuitive 
to use, convenient (code completion and inspection) and effective Java Integration 
Development Environment. 

The version of Eclipse that was used for this QA project is : Mars.2 Release (4.5.2). 

=== Web automation framework : Selenium Java Client 3.141.59

Selenium WebDriver framework has been selected because it is open source, simple and concise,
works in all major web browsers and is a W3C recommendation. 

=== Unit testing framework : JUnit 4

JUnit has been chosen as the unit testing framework, it enables to use basic 
annotations such as @Before, @Test, @After which defines a general execution workflow. 

=== Screenshot on error 

Everytime on error occurs at runtime a screenshot with time stamp is taken 
and put into screenshots/ folder of java project to ease further technical investigation 
so as to explain what happens and correct java code for future execution. 

=== Compilation 

The project can be executed both as a JUnit test or as a Java application.

=== Xpath considerations 

I developped the code by inspecting target web elements and by looking for the right xpath 
into the google javascript console so as to keep the clearest, simplest 
and the most generic xpath expression as possible. 
For instance, to find left menu element "Folders / Labels" by xpath, it is possible 
to write expressions :
"//span[@id='tour-label-settings']" or "//span[starts-with(@class,'sidebarApp-label')]"
or even "//a[@href='/settings/labels']/span/span[@*='Action']"
But I have prefered to rely on : 
"//*[text()='Folders / Labels']"

---------------------------------------------------------------------------------------------------------------
SOLUTION ARCHITECTURE 

=== Java project

src/protonmail.testing.selenium.SeleniumParentClass.java
src/protonmail.testing.selenium.Stickers.java

drivers/chromedriver.exe
drivers/geckodriver.exe
drivers/IEDriverServer.exe

JRE System Library: JavaSE-1.8 (jre1.8.0_152)

libs/byte-buddy-1.8.15.jar
libs/client-combined-3.141.59.jar
libs/commons-exec-1.3.jar
libs/guava-25.0-jre.jar
libs/hamcrest-core-1.3.jar
libs/junit-4.13.jar
libs/okhttp-3.11.0.jar
libs/okio-1.14.0.jar

screenshots/protonmail.testing.selenium.Stickers_20200416145829.png

=== Class hierarchy 

I have created a parent class called SeleniumParentClass which gathers all generic 
class variables and generic methods. 
Thus any particular child (feature) class like Stickers inherits from 
SeleniumParentClass and use variables and methods which are common to any child class 
deriving from SeleniumParentClass. 

Variables and methods which are specific to a given feature like Stickers 
have been developped into the child class.  

=== Concept 

I gathered Folders and Labels into one single generic meta notion which I naturally 
baptized "Stickers", hence the presence of this term into methods and variables names. 
This "Stickers" idea enables to factor and share some common code embedded into 
methods accessible for both Folders and Labels types. 

---------------------------------------------------------------------------------------------------------------
CODE READABILITY, REUSABILITY AND MAINTAINABILITY

Classical identation managed by Ecplise has been chosen. 
Java code has been refactored to create as many methods as necessary to minimize 
the lines of code amount. 
Methods and variables have been called using classical naming conventions so as 
to use explicit self-supporting terms. 
Some comments were added everywhere it was necessary to ease code reading 
and understanding. 

---------------------------------------------------------------------------------------------------------------

BONUS TASK 1 
The solution developed is available on GitHub onto public repository. 

---------------------------------------------------------------------------------------------------------------

BONUS TASK 2 
A Continuous Integration pipeline has been setup, it works well. 

---------------------------------------------------------------------------------------------------------------

ENHANCEMENTS (If I had more time) 

- I assumed that once the presence of an email subject has been checked into a specific 
stricke then it means the message is right where it belongs, but ideally the presence 
of email body should be checked as well 
- a build automation tool such Maven or Gradle could replace simple classpath 
dependencies declaration 
- a concurrent test execution mechanism could be implemented in order tool
parallelize tests 
- remote webdriver could be used to function with selenium grid 
- values could be stored into a configuration file instead of being hard coded 



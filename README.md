# Assessment A2 - The General Practitioner #

This a portal for patients to view their appointments.
Patients can log in, register as well as create, view and edit appointments.
This project was created in 2022.

## 🙂 Members ##
* Milovan Gveric
* Andrei Constantin
* Filip Fois
* Wenbo Wu

## 📦 Prerequisites ##
* IntelliJ (or another Java IDE)
    *  **IntelliJ is required to modify the GUI**. The GUI is created using **IntelliJ's GUI Designer** tool, which produces a `.form` file that is linked with it's `.java` class of the same name.
* MySQL (version 14.14, Distrib 5.7.36)  
* Java SDK (version 16)
* Maven

## ⚙️ Installation ##
* Unzip the project
* Run/import the `/TheGP_Database_24_Mar_Raw.sql` script with MySQL to create the local database

### ⚠️ Potential errors ###
* When opening the project in an IDE, if there is **an error with the imports, it is likely due to missing Maven**. Follow the steps on this page to fix the issue if encountered: https://stackoverflow.com/a/13640110
* If running `/TheGP_Database_24_Mar_Raw.sql` gives a bug in MySQL, then try running `/TheGP_Database_24_Mar.sql`

## 🛠️ Set up ##

* Open the project with IntelliJ (or the IDE of your choice)  
* Inside the project, open the `/src/main/java/com/group15A/DataAccess/DataAccess.java` file  
* Edit the `DB_USER` and `DB_PASSWORD` values to your local database username and password respectively

## ▶️ Running the program ##
In the IDE of your choice, run the `/src/main/java/com/group15A/Main.java` class

## 📔 Existing data
The database contains some dummy data that you can test the application with.  
Here is a list of patient emails you can use to login (the password for all of the following patients is `Password1*`):

* `dneal@email.com`
* `lhines@email.com`
* `lschaefer@email.com`
* `jelly@email.com`
* `hdrake@email.com`
* `lrice@email.com`
* `bdouglas@email.com`
* `adennis@email.com`
* `acarter@email.com`
* `jedwards@email.com`


# SER517-AgileMetrics
# Google Drive link: [Drive](https://drive.google.com/drive/u/1/folders/1qQd2xZBPPemDE9T6Qrv4r_UXlNUK8Zz_)
# Taiga Backlog Link: [Taiga Backlog](https://tree.taiga.io/project/dhruvimodii-agile-metrics-ser-517/backlog)
# Step by step guide for functionalities: [functionalities guide] (https://docs.google.com/document/d/1ZF-gkPFLlEuamAFUmyAJQvYKOHxFI5aAOYGWXnaJfw0/edit#)

# SER517-Spring23-team1-AgileMetrics Project Setup :
## Requirements :
Java 8

Gradle: 7.2 Version

My SQL: 8.0.26
### Step 1: 
Clone the project from the git hub i.e
https://github.com/dhruvi1610/SER517-Spring23-team1-AgileMetrics

### Step 2: 
Modify the DBCONFIG 
i.e SER517-Spring23-team1-AgileMetrics/src/main/java/edu/asu/cassess/config/DBConfig.java With your DB credentials

Or

create a user with the following credentials

Username:admin

Password: 1234567890

### Step 3: 
Step up the Database

Create a database with the name cassess

Use the created database i.e cassess and run the queries in the SQL file in the database i.e cassess.sql
And set the global variable by running query in the SQL file i.e global.SQL
### Step 4: 
when you are in the Project root folder i.e SER517-Spring23-team1-AgileMetrics Run the following command to build and project and create a war file :
```bash
./gradlew clean build
```
If the above command failes due to OS compactability Run the below command :
```bash
gradlew.bat clean build
```
The deployable war file is available at SER517-Spring23-team1-AgileMetrics/build/libs/ssng-project-1.0-SNAPSHOT.war

We can deploy the war file in the server to run the application: http://<The IP address of the host>/ssng-project-1.0-SNAPSHOT/

On log in page we can login as super user by using the following credentials : 

Username : test@gmail.com

Password : test@123

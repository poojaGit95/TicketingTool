Camel Java Router Project
=========================
This project queries the ticket list from given api and displays on the console.
This project is built using Intellij IDE

**Software Requirements**
1. Java
2. Maven

**How to build**
1. To build this project use
    mvn clean install

**How to run**
1. Update the credentials in Credentials.json
2. Update the subdomain in Credentials.json
3. Compile and run this application using below commands
   mvn exec:java -Dexec.mainClass=com.pooja.tickets.Main

**Note:**
The tool may not be executable if you dont have required credentials and service end-point information. 

**About the Tool:**
This tool implements 2 functions specific to ticketing tool (Service cannot be revealed)
    * Displays details of a user ticket by passing the ticket ID.
    * Displays list of tickets so far registered on the Ticket service website (uses pagination)
    
**1. Main.java:** Class to begin the execution of program

**2. TicketViewer.java:**
Class implements methods to take inputs from user and parse it to the ticketing service to display required
information.

**3. TicketServiceWrapperClient.java:**
Class implements TicketService specific api calls

**4. RestClient.java:**
Class implements HTTP GET method to get the response from Ticketing Service.

package com.pooja.tickets.viewer;

import com.pooja.tickets.exceptions.AuthenticationException;
import com.pooja.tickets.exceptions.ResourceNotFoundException;
import com.pooja.tickets.zendesk.ZendeskWrapperClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Optional;
import java.util.Scanner;

public class TicketViewer {
	private String previousPage = "";
	private String nextPage = "";
	private boolean hasNext = false;
	private int count = 0;
	private int currentSize = 0;
	private Scanner sc;
	private String username;
	private String password;
	private String baseURI;
	private ZendeskWrapperClient zendDestClient;

	public TicketViewer() {
		sc = new Scanner(System.in);
		readCredentialsFromFile();
		zendDestClient = new ZendeskWrapperClient(baseURI, username, password);
	}

	private void readCredentialsFromFile() {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader("src/main/resources/Credentials.json"));
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
			username = (String) jsonObject.get("UserName");
			password = (String) jsonObject.get("Password");
			baseURI = (String) jsonObject.get("URI");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void UserInformationDisplay() {
		System.out.println("Welcome to Ticket Viewer");
		System.out.println("Type \'menu\' to view options or \'quit\' to exit");
	}

	public void UserInputs() {
		String userInputMain = sc.next();
		if (userInputMain.equalsIgnoreCase("menu")) {
			UserInputForMenu();
		} else if (userInputMain.equalsIgnoreCase("quit")) {
			System.out.println("Thank you for using Ticket Viewer \n GoodBye");
			System.exit(0);
		} else {
			System.out.println("Invalid input. \nPlease type correct options");
			System.out.println("\nType \'menu\' to view options or \'quit\' to exit");
			UserInputs();
		}

	}

	public void UserInputForMenu() {
		while (true) {
			System.out.println("Select view options" + "\n * Press 1 to view all tickets "
					+ "\n * Press 2 to view a ticket" + "\n * Type \'quit\' to exit ");

			String userInputForMenu = sc.next();
			switch (userInputForMenu) {
				case "1":
					getInitialTicketList();
					break;
				case "2":
					printTicketDetails();
					break;
				case "quit":
					System.out.println("Thank you for using Ticket Viewer \n GoodBye");
					System.exit(0);
				default:
					System.out.println("Invalid input. \nPlease choose correct options from below");
					UserInputForMenu();
			}
		}
	}

	private void getInitialTicketList() {
		System.out.println("Information of all tickets are as follows");
		getTicketList(Optional.empty());
		count = count + 25;
		navigateTicketPages();
	}

	private void getTicketList(Optional<String> url) {
		try {
			JSONObject response = zendDestClient.getAllTicketsList(url);
			JSONArray tickets = response.getJSONArray("tickets");
			printTicketList(tickets);
			JSONObject links = (JSONObject) response.get("links");
			JSONObject meta = (JSONObject) response.get("meta");
			hasNext = meta.getBoolean("has_more");
			nextPage = links.get("next").toString();
			previousPage = links.get("prev").toString();
		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (AuthenticationException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}


	private void navigateTicketPages() {
		while (true) {
			if (hasNext)
				System.out.println("Type \'next\' to view next tickets");
			if (count - 25 > 0)
				System.out.println("Type \'prev\' to view previous tickets");
			System.out.println("Type \'menu\' to return to previous menu");
			System.out.println("Type \'quit\' to exit");
			System.out.println("count= " + count);
			String pageInput = sc.next();

			switch (pageInput) {
				case "next":
					if (hasNext) {
						getTicketList(Optional.of(nextPage));
						count = count + currentSize;
					} else {
						System.out.println("No more tickets to navigate next.");
					}
					break;
				case "prev":
					if (count - 25 > 0) {
						count = count - currentSize;
						getTicketList(Optional.of(previousPage));
					} else {
						System.out.println("No more tickets to navigate previous.");
					}
					break;
				case "quit":
					System.out.println("Thank you for using Ticket Viewer \n GoodBye");
					System.exit(0);
				case "menu":
					System.out.println("Returning to previous menu.");
					return;
				default:
					System.out.println("Invalid input. \nPlease choose correct options from below");
			}
		}
	}

	private void printTicketList(JSONArray tickets) {
		int first = tickets.getJSONObject(0).getInt("id");
		int last = tickets.getJSONObject(tickets.length() - 1).getInt("id");
		currentSize = last - first + 1;
		System.out.println("size= " + currentSize);
		for (int i = 0; i < tickets.length(); i++) {
			String id = tickets.getJSONObject(i).get("id").toString();
			String date = tickets.getJSONObject(i).get("created_at").toString();
			String subject = tickets.getJSONObject(i).getString("subject");
			System.out.println("Ticket id " + id + " created on " + date + " with subject line " + subject);
		}
	}

	private void printTicketDetails() {
		System.out.println("Enter your ticket number");
		String ticketNumber = sc.next();
		try {
			JSONObject response = zendDestClient.getTicketDetails(ticketNumber);
			System.out.println(response);
			System.out.println("Ticket details:");
			String id = response.getJSONObject("ticket").get("id").toString();
			String date = response.getJSONObject("ticket").get("created_at").toString();
			String subject = response.getJSONObject("ticket").getString("subject");
			String description = response.getJSONObject("ticket").getString("description");
			System.out.println("The ticket with id " + id + " created on " + date);
			System.out.println("Has subject line: " + subject);
			System.out.println("Details: " + description);

		} catch (ResourceNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (AuthenticationException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return;
	}
}

package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject; // W10 Coding Assignment - Select a project> ITEM#1

	// @formatter:off (Week 9 Assignment - ITEM#1a)
	private List<String> operations = List.of(
 
		"1) Add a project", // Week 9 - ITEM#1a
		"2) List projects", // Week 10 - ITEM#1 page 2
		"3) Select a project" // Week 10 - ITEM#2 page 2
	);
	// @formatter:on (Week 9 - ITEM#1a)
	public static void main(String[] args) {

		new ProjectsApp().processUserSelections();
	}

	private void processUserSelections() {
		boolean done = false;

		while (!done) {

			try {
				int selection = getUserSelection();
				switch (selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects(); // W10 Page 1 - ITEM#2
					break;
				case 3:
					selectProject();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e.toString() + "Try again.");
				
  				e.printStackTrace(); // W10 - Modifying the dao section "Test it" - ITEM#7
			}
		}
	}

	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		// Setters and Getters
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();

		System.out.println("\nProjects:");

		projects.forEach(
				project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
		
		
	}

	private void selectProject() { // W10 - Select a project - ITEM#4
		listProjects();
	
		Integer projectId = getIntInput("Enter a project ID to select a project");
	
		// Unselect the last current projected selected
		curProject = null;
	
		curProject = projectService.fetchProjectById(projectId);

	}

	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("\nEnter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}

	private void printOperations() {
		System.out.println();
		System.out.println("These are the available selections. Press the Enter key to quit:");

		operations.forEach(input -> System.out.println("   " + input));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("\nYou are working with a project: " + curProject);
		}
	}
	
	private boolean exitMenu() {
		System.out.println("\nExiting the menu.");
		return true;
	}
	
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number. Try again.");
		}
	}

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number. Try again.");
		}
	}

//	    may need to remove this for the final assignment submission
//	    private Double getDoubleInput(String prompt) {
//		String input = getStringInput(prompt);
//		
//		if(Objects.isNull(input)) {
//			return null;
//		}
//		try {
//			return Double.parseDouble(input);
//		} catch (NumberFormatException e) {
//			throw new DbException(input + " is not a valid number.Try again."); 
//			}
//	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	}
}
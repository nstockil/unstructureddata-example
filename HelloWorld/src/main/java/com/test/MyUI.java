package com.test;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.RadioButtonGroup;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    // Create a list of type Student (which we defined in Student.java)
    ArrayList<StudentEntity> students = new ArrayList<StudentEntity>();
    
    // private String storageConnectionString = "UseDevelopmentStorage=true";
    private String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=[ACCOUNTNAME];AccountKey=[ACCOUNTKEY]";
    private String tableName = "students";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        // define layout
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField studentTextField = new TextField();
        studentTextField.setCaption("Enter student name:");

        final TextField emailTextField = new TextField();
        emailTextField.setCaption("Enter student email:");

        final TextField markTextField = new TextField();
        markTextField.setCaption("Enter student mark:");

        final Label gradeLabel = new Label();

        Button gradeButton = new Button("Grade Student");
        gradeButton.addClickListener(e -> {
            String grade = calculateGrade(markTextField);
            gradeLabel.setValue(studentTextField.getValue() + "'s grade is " + grade);

            // retrieve information from form and create StudentEntity
            String name = studentTextField.getValue();
            String email = emailTextField.getValue();
            int mark = Integer.parseInt(markTextField.getValue());
            StudentEntity student = new StudentEntity(name, mark, grade, email);

            //connect and insert StudentEntity to Table Storage
            try {
                CloudTableClient storageTableClient = getCloudStorageTableClient(); // get client to work with Table Storage
                CloudTable table = storageTableClient.getTableReference(tableName); // connect to the student table
                table.execute(TableOperation.insert(student));                      // Insert the StudentEntity to our table
            } catch (Exception ex) {
                System.err.println("ERROR: " + ex); // Will print out the error to the console
                gradeLabel.setValue(ex.toString()); // Will output the error message to the UI                
            }
        });
        
        String[] gradeOptions = new String[] {"A", "B", "C", "D", "E", "F"};
        RadioButtonGroup<String> gradeOptionRadio = new RadioButtonGroup<String>("Get Students based on Grade");
        gradeOptionRadio.setItems(gradeOptions);

        final Label studentsLabel = new Label();

        Button getStudentByGradeButton = new Button("Get Students");
        getStudentByGradeButton.addClickListener(e -> {
            try {
                CloudTableClient storageTableClient = getCloudStorageTableClient();
                CloudTable table = storageTableClient.getTableReference(tableName);

                String studentList = "<ul>";
                for(StudentEntity student: getStudents(table, gradeOptionRadio.getValue().toString())){
                    studentList += String.format("<li>%s</li>", student.getName()); // add student to list
                }
                studentList += "</ul>";

                studentsLabel.setCaptionAsHtml(true); // tells Vaadin that the caption will contain HTML
                studentsLabel.setCaption(studentList);
            } catch (Exception ex) {
                System.err.println("ERROR: " + ex);   // Will print out the error to the console
            }
        });

        layout.addComponents(studentTextField, emailTextField,
                            markTextField, gradeButton, gradeLabel, gradeOptionRadio, getStudentByGradeButton, studentsLabel);
        
        // set layout
        setContent(layout);
    }

    /**
     * Connects to Cloud Storage Account and returns a Table Client for that Storage Account.
     */
    private CloudTableClient getCloudStorageTableClient() throws Exception{
        CloudStorageAccount storageAccount;
        
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
        } catch (Exception ex) {
            System.err.println("\ngetCloudStorageTableClient(): Unable to get reference to " + storageConnectionString);
            System.err.println("Please confirm the connection string is in the Azure connection string format.");
            throw ex;
        }
        
        return storageAccount.createCloudTableClient();
    }

    /**
     * Queries Table Storage for list of students based on their grades.
     */
    private Iterable<StudentEntity> getStudents(CloudTable table, String grade){
        // create a filter for querying the table: PartitionKey eq grade
        String filter = TableQuery.generateFilterCondition("PartitionKey", TableQuery.QueryComparisons.EQUAL, grade);
        TableQuery<StudentEntity> partitionScanQuery = TableQuery.from(StudentEntity.class).where(filter);
        return table.execute(partitionScanQuery);
    }

	private String calculateGrade(final TextField markTextField) {
		// Calculate grade based on mark
            String grade = "F";
            // Convert the string 'mark' into an integer
            int markInExam = Integer.parseInt(markTextField.getValue());
            if(markInExam >= 80) {
                grade = "A";
            }
            else if(markInExam >= 70) {
                grade = "B";
            }
            else if(markInExam >= 40) {
                grade = "C";
            }
            else {
                grade = "F";
            }
		return grade;
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

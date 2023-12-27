import java.util.Scanner;

public class RangedChoiceQuestion extends Question {
	//Initialize Class variables
	String questionContent;
	int ans = 0;
	
	//Constructor
	public RangedChoiceQuestion(String questionContent) {
		this.questionContent = questionContent;
	}
	
	@Override
	public void displayQuestion() {
		//Initialize variables
		Scanner scanner = new Scanner(System.in);

		
		//Loop until user has selected an integer within the proper range
		while(ans < 1 || ans > 10) {
			//Display question
			System.out.println("In a range between 1 and 10, how strongly do you believe the following?");
			System.out.println(questionContent);
			System.out.println("Your answer: ");
			
			//Get and validate user input in response
			if (scanner.hasNextInt()) {
	            ans = scanner.nextInt();
	        } else {
	            System.out.println("Invalid input. Please enter an integer.");
	            scanner.next();
	        }
			
			//Display error message if entered numbers is outside of proper range
			if(ans < 1 || ans > 10) {
				System.out.println("Invalid input.  Please enter a number between 1 and 10");
			}
		}
	}
	public int getAns() {return ans;}
}

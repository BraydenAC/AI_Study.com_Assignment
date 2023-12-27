import java.util.Scanner;

public class MultiChoiceQuestion extends Question {
	//Initialize Class variables
	String[] questionContent = new String[4];
	String ans = "";
	
	//Constructor
	public MultiChoiceQuestion(String[] questionContent) {
		//Tranferring incoming string array into local questionContent
		for(int x = 0; x < 4; x++) {
			this.questionContent[x] = questionContent[x];
		}
	}
	
	@Override
	public void displayQuestion() {
		//Initialize variables
		Scanner scanner = new Scanner(System.in);
		
		//Loop until user has selected a proper letter
		while(!(ans.equals("a") || ans.equals("b") || ans.equals("c") || ans.equals("d"))) {
			//Display question
			System.out.println("Please enter the letter corresponding to your answer");
			for(int x = 0; x < 4; x++) {
				System.out.println(questionContent[x]);
			}
			
			//Get user input
			System.out.print("Your answer: ");
			System.out.println("");
			ans = scanner.next();
			//Validate user input in response
			if (!(ans.equals("a") || ans.equals("b") || ans.equals("c") || ans.equals("d"))) {
				System.out.println("Invalid input: please enter a, b, c, or d");
			}
		}
	}
	public int getAns() {
	int result = 0;
	switch(ans){
		case "a":
			result = 1;
			break;
		case "b":
			result = 2;
			break;
		case "c":
			result = 3;
			break;
		case "d":
			result = 4;
			break;
		default:
			System.out.println("Error: Multi Choice character incorrect!");
		}
	return result;
	}
}

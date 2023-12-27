import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//AI Study.com Assignment
//Brayden Cloutier
//12/27/2023
public class Main {

	public static void main(String[] args) {
		//Instantiate Variables
		Question[] quizQuestions = new Question[9];
		quizQuestions = buildQuiz();
		Scanner scanner = new Scanner(System.in);
		
		//do questions 1-9
		for(Question x : quizQuestions) {
			x.displayQuestion();
		}
		
		//Print what was predicted
		//Retrieve the information stored in the file
		String[] prevParty1Info = new String[10];
		prevParty1Info = retrievePartyAnswers(System.getProperty("user.dir") + "/Resources/Party1.txt");
		String[] prevParty2Info = new String[10];
		prevParty2Info = retrievePartyAnswers(System.getProperty("user.dir") + "/Resources/Party2.txt");
		String[] prevParty3Info = new String[10];
		prevParty3Info = retrievePartyAnswers(System.getProperty("user.dir") + "/Resources/Party3.txt");
		String[] prevParty4Info = new String[10];
		prevParty4Info = retrievePartyAnswers(System.getProperty("user.dir") + "/Resources/Party4.txt");

		// create a string array identical to the winning party
		String[] winningParty = compareParties(quizQuestions, prevParty4Info, 
								compareParties(quizQuestions, prevParty3Info, 
								compareParties(quizQuestions, prevParty2Info, prevParty1Info)));
		
		//Find which party is identical to the created string array
		if(Arrays.equals(winningParty, prevParty1Info)) {
			System.out.println("Your choices most closly align with the Democratic Party");
		} else if(Arrays.equals(winningParty, prevParty2Info)) {
			System.out.println("Your choices most closly align with Republican Party");
		} else if(Arrays.equals(winningParty, prevParty3Info)) {
			System.out.println("Your choices most closly align with the Libertarian Party");
		} else if(Arrays.equals(winningParty, prevParty4Info)) {
			System.out.println("Your choices most closly align with the Green Party");
		} else {
			System.out.println("Choice Prediction Error");
		}
		
		//Question 10 below
		String q10Ans = "";
		
		//Loop until user has selected a proper letter
		while(!(q10Ans.equals("a") || q10Ans.equals("b") || q10Ans.equals("c") || q10Ans.equals("d"))) {
			//Display question
			System.out.println("Please enter the letter corresponding to which political party you most identify with");
			System.out.println("a) Democrat");
			System.out.println("b) Republican");
			System.out.println("c) Libertarian");
			System.out.println("d) Green");
			System.out.print("Your selection:  ");
			
			//Get user input
			q10Ans = scanner.next();
			//Resource leak prevention
			
			//Validate user input in response
			switch(q10Ans){
				case "a":
					//Add information to Political party 1
					storePartyAnswers(System.getProperty("user.dir") + "/Resources/Party1.txt", prevParty1Info, quizQuestions);
					break;
				case "b":
					//Add information to Political party 2
					storePartyAnswers(System.getProperty("user.dir") + "/Resources/Party2.txt", prevParty2Info, quizQuestions);
					break;
				case "c":
					//Add information to Political party 3
					storePartyAnswers(System.getProperty("user.dir") + "/Resources/Party3.txt", prevParty3Info, quizQuestions);
					break;
				case "d":
					//Add information to Political party 4
					storePartyAnswers(System.getProperty("user.dir") + "/Resources/Party4.txt", prevParty4Info, quizQuestions);
					break;
				default:
					System.out.println("Invalid input: please enter a, b, c, or d");
			}
		}
		scanner.close();
	}
	
	public static String[] retrievePartyAnswers(String path) {
		//Array that information taken from file will be stored in
		String[] storedInfo = new String[10];
		
		//Try statement for catching problems, creates link to read from appropriate file
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
		    //Instantiate incremental variable and string variable for information transferal to storedInfo
			int index = 0;
		    String line;
		    //Loop and read each line at a time up to line 10 or null value, store each in holding array
		    while ((line = reader.readLine()) != null && index < 10) {
		        storedInfo[index++] = line;
		    }
		} catch (IOException e) {e.printStackTrace();}
		return storedInfo;
	}
	
	public static void storePartyAnswers(String path, String[] submitInfo, Question[] answers) {
		//Initialize final submit
		String[] finalSubmit = new String[10];
		int y = Integer.parseInt(submitInfo[9]);
		//For loop to affect each line of the submit info
		for(int num = 0; num < 9; num++) {
			if(answers[num] instanceof MultiChoiceQuestion) {
				//Splits the four options into their own individual strings
				String[] options = submitInfo[num].split(",");
				//Increments the string value of the correct option by one and returns it to a string
				options[answers[num].getAns() - 1] = String.valueOf(Integer.parseInt(options[answers[num].getAns() - 1]) + 1);
				finalSubmit[num] = options[0] + "," + options[1] + "," + options[2] + "," + options[3];
			} else {
				/*Formula used here(due to using integers, all decimals will be discarded; such precision unnecessary):
				 * (x * y + z) / (y + 1)
				 * Where
				 * x = Original averaged number
				 * y = Weight of the original averaged number(how many numbers were added together to make that average)
				 * z = The number to be added in to to make the new average*/
				int x  = Integer.parseInt(submitInfo[num]);
				int z = answers[num].getAns();
				finalSubmit[num] = String.valueOf((x * y + z) / (y + 1));
			}
		}
		
		//Increase party member count tally
		finalSubmit[9] = String.valueOf(y + 1);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			//iterates through array, writing each array element to the file one line at a time
			for (int x = 0; x < 10; x++) {
				writer.write(finalSubmit[x]);
				//If statement keeps file from infinitely increasing in length
				if(x + 1 < finalSubmit.length) {
					writer.newLine();	
				}
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	//Function supporting compareParties; returns how many people answered a specific answer to a specific question
	static float answerTally(String options, int selection) {
		String[] parts = options.split(",");
		return Float.parseFloat(parts[selection - 1]);
	}
	
	//Compares two stored answersets to current user's answerset
	public static String[] compareParties(Question[] currentUser, String[] party1, String[] party2) {
		//Two counters that will be incremented each time a party proves itself a more likely canidate than the other
		int p1Wins = 0;
		int p2Wins = 0;
		
		for(int x = 1; x < 9; x++) {
			//If statement judges what type of handling is nessessary to find the most accurate party option
			if(currentUser[x] instanceof MultiChoiceQuestion) {
				//If statement is true if the percentage of party 1 members picking the same option as the current user is higher
				//than that of party 2
				if(answerTally(party1[x], currentUser[x].getAns()) / Float.parseFloat(party1[9]) > 
				   answerTally(party2[x], currentUser[x].getAns()) / Float.parseFloat(party2[9])) {
					p1Wins++;
				} else {p2Wins++;}
			} else if(currentUser[x] instanceof RangedChoiceQuestion) {
				//If statement is true if the distance between the current user's selection and party 1's average pick
				//is smaller than that of party 2
				if( Math.abs(currentUser[x].getAns() - Integer.parseInt(party1[x])) <
					Math.abs(currentUser[x].getAns() - Integer.parseInt(party2[x]))) {
					p1Wins++;
				} else {p2Wins++;}
			} else {
				System.out.println("Problem with type of element for question #" + (x + 1));
			}
		}
		
		//Return which party had the most accurate answers
		if(p1Wins > p2Wins) {
			return party1;
		} else {
			return party2;
		}
	}
	
	//Instantiates all questions and puts them into an array without cluttering up main
	//	QUESTION PROMPTS GENERATED BY CHATGPT AS THEY ARE IRRELEVENT TO THE GRADE OF THE ASSIGNMENT,
	//	AND NOT VITAL TO THE UNDERLYING OPERATIONS OF THE SYSTEM
	public static Question[] buildQuiz() {
		String qPrompt = "";
		Question[] questionSet = new Question[9];
		String[] optionInput = new String[4];
		//Question 1: MultiChoiceQuestion---
		qPrompt = "What is your stance on gun control";
		optionInput[0] = "a) Implememnt stricter background checks and gun regulations";
		optionInput[1] = "b) Support the right to bear arms with minimal restrictions";
		optionInput[2] = "c) Advocate for armed security measures in public spaces";
		optionInput[3] = "d) Oppose most forms of gun control legislation";
		MultiChoiceQuestion q1 = new MultiChoiceQuestion(optionInput, qPrompt);
		questionSet[0] = q1;
		//Question 2: RangedChoiceQuestion
		RangedChoiceQuestion q2 = new RangedChoiceQuestion("Rate your level of concern about privacy rights in the digital age");
		questionSet[1] = q2;
		//Question 3: MultiChoiceQuestion---
		qPrompt = "How should the government approach healthcare";
		optionInput[0] = "a) Move towards a universal healthcare system";
		optionInput[1] = "b) Emphasize free-market principles with minimal government intervention";
		optionInput[2] = "c) Support private healthcare initiatives and competition";
		optionInput[3] = "d) Repeal the Affordable Care Act and reduce government involvement";
		MultiChoiceQuestion q3 = new MultiChoiceQuestion(optionInput, qPrompt);
		questionSet[2] = q3;
		//Question 4: MultiChoiceQuestion---
		qPrompt = "What is your opinion on immigration policy";
		optionInput[0] = "a) Support a pathway to citizenship for undocumented immigrants";
		optionInput[1] = "b) Enhance border security and enforce existing immigration laws";
		optionInput[2] = "c) Implement a merit-based immigration system";
		optionInput[3] = "d) Advocate for open borders and global migration";
		MultiChoiceQuestion q4 = new MultiChoiceQuestion(optionInput, qPrompt);
		questionSet[3] = q4;
		//Question 5: RangedChoiceQuestion
		RangedChoiceQuestion q5 = new RangedChoiceQuestion("How strongly do you oppose(1) or support(10) government intervention in the economy");
		questionSet[4] = q5;
		//Question 6: MultiChoiceQuestion---
		qPrompt = "What should the goverment's role in education be";
		optionInput[0] = "a) Increase funding for public education and reduce class sizes";
		optionInput[1] = "b) Promote school choice and voucher programs";
		optionInput[2] = "c) Support teacher accountablility measures";
		optionInput[3] = "d) Reduce federal involvement in education and prioritize local control";
		MultiChoiceQuestion q6 = new MultiChoiceQuestion(optionInput, qPrompt);
		questionSet[5] = q6;
		//Question 7: RangedChoiceQuestion
		RangedChoiceQuestion q7 = new RangedChoiceQuestion("How concerned are you about the impact of artificial intelligence on jobs and employment");
		questionSet[6] = q7;
		//Question 8: MultiChoiceQuestion---
		qPrompt = "How should the government address income inequality";
		optionInput[0] = "a) Implement progressive taxation and wealth redistribution";
		optionInput[1] = "b) Lower taxes to encourage economic growth and job creation";
		optionInput[2] = "c) Focus on job training programs and economic mobility";
		optionInput[3] = "d) Let the free market address income disparities without intervention";
		MultiChoiceQuestion q8 = new MultiChoiceQuestion(optionInput, qPrompt);
		questionSet[7] = q8;
		//Question 9: MultiChoiceQuestion---
		qPrompt = "What is your position on criminal justice reform";
		optionInput[0] = "a) Advocate for police reform and community policing initiatives";
		optionInput[1] = "b) Support law and order policies with a focus on strong policing";
		optionInput[2] = "c) Promote rehabilitation and alternative sentencing over incarceration";
		optionInput[3] = "d) Oppose criminal justice reform, emphasizing a tough-on-crime approach";
		MultiChoiceQuestion q9 = new MultiChoiceQuestion(optionInput, qPrompt);
		questionSet[8] = q9;
		
		//Return array of questions
		return questionSet;
	}
}

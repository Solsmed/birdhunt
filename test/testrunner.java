import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Sep 6, 2012.
 */
public class testrunner {
	public static void main(String [] args){
		int games = 0;
		String opponent="";
		try{
			games = Integer.parseInt(args[0]);
		}catch(Exception e){
			System.out.println("invalid use of the tester!");
			System.out.println("testrunner <number of games> <opponent>");
		}
		
		String port = "5020";
		
		System.out.print("|");
		for(int i=0;i<games;i++)
			System.out.print(" ");
		System.out.println("|");
		System.out.print(" ");
		
		PrintStream originalOut = System.out; // To get it back later
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream newOut = new PrintStream(baos);
		System.setOut(newOut);

		Client c;
		String tmp;
		int wins = 0;
		
		for(int i=0;i<games;i++){
			c = new Client();
			String [] str = {"dd2380.csc.kth.se",port,"STANDALONE","practice"};
			c.main(str);
			System.setOut(originalOut);
			tmp = baos.toString();
			if(tmp.contains("good estimation")){
				System.out.print("x");
				wins++;
			}
			else
				System.out.print("-");
			
				baos.reset();
			System.setOut(newOut);
		}
		
		System.out.flush();

		System.setOut(originalOut); // So you can print again
		    System.out.println("\nPlaying against: " + opponent);
		    System.out.println("Number of wins: " + wins + " of " + games + " games, " + (String.format("%.2f percent", (100.0 * wins / games))));
		
	}

}
package course.oop.main;


/*
class TTTDriver2 {

	private static void checkForInvalidLocation(boolean isSelectionValid) {
		if(!isSelectionValid) {
			System.out.println("Invalid location selected by player");
		}
	}
	
	private static void sampleTestCase() {
		Controller ticTacToe = new Controller();
		// 1. create players
		String player_1 = "Ashley";
		String player_2 = "James";
		ticTacToe.createPlayer(player_1, "O", 1);
		ticTacToe.createPlayer(player_2, "X", 2);
		boolean isSelectionValid = true;
		
		// initialize
		// 2. start game
		ticTacToe.startNewGame(2, 0);
		System.out.println(ticTacToe.getGameDisplay());
		
		//play game
		isSelectionValid = ticTacToe.setSelection(0, 0, 1);
		checkForInvalidLocation(isSelectionValid);
		System.out.println(ticTacToe.getGameDisplay());
		isSelectionValid = ticTacToe.setSelection(0, 2, 2);
		checkForInvalidLocation(isSelectionValid);
		System.out.println(ticTacToe.getGameDisplay());
		isSelectionValid = ticTacToe.setSelection(1, 0, 1);
		checkForInvalidLocation(isSelectionValid);
		System.out.println(ticTacToe.getGameDisplay());
		isSelectionValid = ticTacToe.setSelection(1, 2, 2);
		checkForInvalidLocation(isSelectionValid);
		System.out.println(ticTacToe.getGameDisplay());
		isSelectionValid = ticTacToe.setSelection(2, 0, 1);
		checkForInvalidLocation(isSelectionValid);
		System.out.println(ticTacToe.getGameDisplay());
		
		//determine winner
		int winner = ticTacToe.determineWinner();
		if(winner==1) {
			System.out.println(player_1 + " won the game!!");
		}else {
			System.out.println("Failed Test Case");
		}
	}
	
	public static void main(String[] args) {
		sampleTestCase();
	}
	
}
*/
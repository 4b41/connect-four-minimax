import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game {
	
	char[][] board = new char[6][7];
	char human = 'X';
	char ai = 'O';
	int maxDepth = 2;
	int turn = 0;
	boolean input = false;
	
	JFrame window = new JFrame();
	JPanel gamePanel = new JPanel();
	JPanel selectPanel = new JPanel();
	JPanel info = new JPanel();
	JPanel result = new JPanel();
	JButton[] selectColor = new JButton[2];
	JButton[] selectDiff = new JButton[4];
 	JButton playAgain = new JButton();
	JButton[] slot = new JButton[7];
	JLabel[][] chip = new JLabel[6][7];
	JLabel select = new JLabel();
	JLabel winner =  new JLabel();
	JLabel stat = new JLabel();
	
	public Game() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				board[i][j] = '.';
			}
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.createUI();
		game.run();
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (input == true) {
				place(ai, getBestMove());
				turn++;
				updateBoard();
				stat.setText("Player's turn to move!");
				if (turn == 42) {
					endGame('.');
				}
				if (checkWin(ai) == true) {
					endGame(ai);
				}
				input = false;
			}
		}
	}
	
	public void createUI() {
		
		ActionListener menu = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String action = e.getActionCommand();
				
				switch(action) {
				case "easy":
					select.setText("Choose your difficulty! (Current: Easy)");
					maxDepth = 2;
					break;
				case "medium":
					select.setText("Choose your difficulty! (Current: Medium)");
					maxDepth = 4;
					break;
				case "hard":
					select.setText("Choose your difficulty! (Current: Hard)");
					maxDepth = 6;
					break;
				case "expert":
					select.setText("Choose your difficulty! (Current: Expert)");
					maxDepth = 8;
					break;
				case "red":
					stat.setText("Player's turn to move!");
					for (JButton i: slot) {
						i.setVisible(true);
					}
					selectPanel.setVisible(false);
					human = 'X';
					ai = 'O';
					break;
				case "blue":
					stat.setText("AI is thinking!");
					for (JButton i: slot) {
						i.setVisible(true);
					}
					selectPanel.setVisible(false);
					human = 'O';
					ai = 'X';
					input = true;
					break;
				case "retry":
					resetGame();
					break;
				}
			}
		};
		
		
		ActionListener click = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String action = e.getActionCommand();
				if (input == false) {
					if (place(human, Integer.valueOf(action)) == true) {
						undo(Integer.valueOf(action));
						place(human, Integer.valueOf(action));
						turn++;
						updateBoard();
						stat.setText("AI is thinking...");
						if (turn == 42) {
							endGame('.');
						}
						if (checkWin(human) == true) {
							endGame(human);
						} else {
							input = true;
						}
					}
				}
			}
		};
		
		window.setSize(720,800);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setBackground(Color.white);
		window.setLayout(null);
		window.setResizable(false);
		
		selectPanel.setBounds(155,360,410,150);
		selectPanel.setBackground(new Color(206, 206, 206));
		selectPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		window.add(selectPanel);
		
		select.setFont(new Font("Arial", Font.PLAIN, 22));
		select.setText("Choose your difficulty! (Current: Easy)");
		selectPanel.add(select);
		
		info.setBounds(8, 8, 712, 64);
		info.setBackground(new Color(226, 226, 226));
		info.setBorder(BorderFactory.createLineBorder(Color.black));
		window.add(info);
			
		stat.setText("Choose your opponent!");
		info.add(stat);
		
		result.setBounds(262,410,200,80);
		result.setBackground(new Color(206, 206, 206));
		result.setBorder(BorderFactory.createLineBorder(Color.black));
		result.setVisible(false);
		window.add(result);	
		
		winner.setFont(new Font("Arial", Font.PLAIN, 15));
		result.add(winner, BorderLayout.CENTER);
		
		playAgain.setText("Play again?");
		playAgain.setActionCommand("retry");
		playAgain.addActionListener(menu);
		result.add(playAgain);
		
		gamePanel.setBounds(8, 88, 704, 664);
		gamePanel.setBackground(new Color(226, 226, 226));
		gamePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		gamePanel.setLayout(new GridLayout(7,7));
		window.add(gamePanel);
		
		selectDiff[0] = new JButton();
		selectDiff[1] = new JButton();
		selectDiff[2] = new JButton();
		selectDiff[3] = new JButton();
		selectDiff[0].setText("Easy");
		selectDiff[1].setText("Medium");
		selectDiff[2].setText("Hard");
		selectDiff[3].setText("Expert");
		selectDiff[0].addActionListener(menu);
		selectDiff[1].addActionListener(menu);
		selectDiff[2].addActionListener(menu);
		selectDiff[3].addActionListener(menu);
		selectDiff[0].setActionCommand("easy");
		selectDiff[1].setActionCommand("medium");
		selectDiff[2].setActionCommand("hard");
		selectDiff[3].setActionCommand("expert");
		selectPanel.add(selectDiff[0]);
		selectPanel.add(selectDiff[1]);
		selectPanel.add(selectDiff[2]);
		selectPanel.add(selectDiff[3]);
		
		selectColor[0] = new JButton();
		selectColor[1] = new JButton();
		selectColor[0].setText("Red (Player goes first)");
		selectColor[1].setText("Blue (Player goes second)");
		selectColor[0].addActionListener(menu);
		selectColor[1].addActionListener(menu);
		selectColor[0].setActionCommand("red");
		selectColor[1].setActionCommand("blue");
		selectColor[0].setFocusable(false);
		selectColor[1].setFocusable(false);
		selectPanel.add(selectColor[0]);
		selectPanel.add(selectColor[1]);
		
		for(int i = 0; i < 7; i++) {
			slot[i] = new JButton();
			slot[i].setBackground(new Color(220,220,220));
			slot[i].setFocusable(false);
			slot[i].setOpaque(true);
			slot[i].setBorderPainted(false);
			slot[i].addActionListener(click);
			slot[i].setActionCommand("" + i);
			slot[i].setRolloverEnabled(true);
			slot[i].setVisible(false);
			gamePanel.add(slot[i]);
		}
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				chip[i][j] = new JLabel();
				chip[i][j].setBackground(Color.white);
				chip[i][j].setOpaque(true);
				chip[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				gamePanel.add(chip[i][j]);
			}
		}
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public boolean checkValid(int x, int y) {
		if (board[y][x] == '.') {
			return true;
		}
		return false;
	}
	
	public boolean place(char user, int col) {
		boolean placed = false;
		int row = 5;
		if (col >= 0 && col <= 7) {
			if (checkValid(col, 0) == true) {
				while (placed == false) {
					if (checkValid(col, row) == true) {
						board[row][col] = user;
						placed = true;
						return placed;
					} else {
						row--;
					}
				} 
			}
		}
		return placed;
	} 
	
	public void undo(int col) {
		boolean top = false;
		int row = 5;
		while (top == false) {
			if (row == 0 || board[row-1][col] == '.') {
				board[row][col] = '.';
				top = true;
			} else {
				row--;
			}
		}
	}
	
	public boolean checkWin(char user) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] == user) {
					if (checkCardinal(row, col, user) == true) {
						return true;
					}
					if (checkDiagonal(row, col, user) == true) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkCardinal(int row, int col, char user) {
		int tempRow = row + 1, tempCol = col + 1;
		while (tempRow < row + 4 && tempRow < 6) {
			if (board[tempRow][col] == user) {
				tempRow++;
				if (tempRow == row + 4) {
					return true;
				}
			} else {
				break;
			}
		}
		while (tempCol < col + 4 && tempCol < 7) {
			if (board[row][tempCol] == user) {
				tempCol++;	
				if (tempCol == col + 4) {
					return true;
				}
			} else {
				break;
			}
		}
		return false;
	}
	
	public boolean checkDiagonal(int row, int col, char user) {
		int rowInc = row - 1, colInc = col + 1, rowDec = row - 1, colDec = col - 1;
		while (rowInc > row - 4 && colInc < col + 4 && rowInc > 0 && colInc < 7) {
			if (board[rowInc][colInc] == user) {
				rowInc--;
				colInc++;
				if (rowInc == row - 4 && colInc == col + 4) {
					return true;
				}
			} else {
				break;
			}
		}
		while (rowDec > row - 4 && colDec > col - 4 && rowDec >= 0 && colDec >= 0) {
			if (board[rowDec][colDec] == user) {
				rowDec--;
				colDec--;
				if (rowDec == row - 4 && colDec == col - 4) {
					return true;
				}
			} else {
				break;
			}
		}
		return false;
	}
	
	
	public void updateBoard() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (board[i][j] == 'X') {
					chip[i][j].setBackground(Color.red);
				} else if (board[i][j] == 'O') {
					chip[i][j].setBackground(Color.blue);
				} else {
					chip[i][j].setBackground(Color.white);
				}
			}
		}
	}
	
	public void endGame(char user) {
		for (JButton i: slot) {
			i.setVisible(false);
		}
		if (user == human) {
			winner.setText("Player has won the game!");
			stat.setText("Player has won!");
		} else if (user == ai){
			winner.setText("AI has won the game!");
			stat.setText("Ai has won!");
		} else {
			stat.setText("Tie!");
			winner.setText("Game over! Board is full!");
			result.setVisible(true);
		}
		result.setVisible(true);
	}
	
	public void resetGame() {
		turn = 0;
		stat.setText("Player's turn to move!");
		result.setVisible(false);
		selectPanel.setVisible(true);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = '.';
			}
		}
		updateBoard();
	}
	
	public List<Integer> getAvailableMoves(){
		List<Integer> moves = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			if (checkValid(i, 0) == true) {
				moves.add(i);
			}
		}
		return moves;
	}
	
	public int getBestMove() {
		int bestScore = Integer.MAX_VALUE;
		int bestMove = 0;
		
		List<Integer> moves = getAvailableMoves();
		List<Integer> bestMoves = new ArrayList<>();
		
		for (int i = 0; i < moves.size(); i++) {
			place(ai, moves.get(i));
			int score = minimax(0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
			if (score < bestScore) {
				bestScore = score;
				bestMoves.clear();
				bestMoves.add(moves.get(i));
			} else if (score == bestScore) {
				bestMoves.add(moves.get(i));
			}
			undo(moves.get(i));
		}
		int rand = (int) (Math.random()*bestMoves.size());
		bestMove = bestMoves.get(rand);
		return bestMove;
	}
	
	public int minimax(int depth, int alpha, int beta, boolean isMaximizing) {
		int value;
		
		List<Integer> moves = getAvailableMoves();
		
		if (checkWin(ai) == true) {
			return (-20000/(depth+1));
		}
		if (checkWin(human) == true) {
			return (100000/(depth+1));
		}
		
		if (moves.isEmpty()) {
			return 0;
		}
		
		if (depth == maxDepth) {
			int calc = evaluateBoard();
			return calc;
		}
		
		if (isMaximizing) {
			value = Integer.MIN_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				place(human, moves.get(i));
				int score = minimax(depth+1, alpha, beta, false);
				undo(moves.get(i));
				value = Math.max(value, score);
				alpha = Math.max(alpha, value);
				if (beta <= alpha) {
					break;
				}
			}
			return value;
		} else {
			value = Integer.MAX_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				place(ai, moves.get(i));
				int score = minimax(depth+1, alpha, beta, true);
				undo(moves.get(i));
				value = Math.min(value, score);
				beta = Math.min(beta, value);
				if (beta <= alpha) {
					break;
				}
			}
			return value;
		}
	}
	public int evaluateBoard() {
		int score = 0;
		for (int i = 0; i < 6; i++) {
			if (board[i][3] == ai) {
				score -= 10;
			}
			if (board[i][3] == human) {
				score += 10;
			}
			score += evalRow(i);
		}
		for (int i = 0; i < 7; i++) {
			score += evalCol(i);
		}
		for (int i = 0; i < 4; i++) {
			score += evalDiagPos(i);
			score += evalDiagNeg(i);
		}
		return score;
	}
	
	public int evalCol(int col) {
		int count = 0, score = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i+j][col] == human) {
					count = 0;
					break;
				} else if (board[i+j][col] == ai) {
					count++;
				}
			}
			switch (count) {
			case 2:
				score -= 40;
				break;
			case 3:
				score -= 90;
				break;
			case 4:
				score -= 1000;
				break;
			default:
				break;
			}
			count = 0;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i+j][col] == ai) {
					count = 0;
					break;
				} else if (board[i+j][col] == human) {
					count++;
				}
			}
			switch (count) {
			case 2:
				score += 40;
				break;
			case 3:
				score += 90;
				break;
			case 4:
				score += 9000;
				break;
			default:
				break;
			}
			count = 0;
		}
		return score;
	}
	
	public int evalRow(int row) {
		int count = 0, score = 0;
		for (int i = 0 ; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[row][i+j] == human) {
					count = 0;
					break;
				} else if (board[row][i+j] == ai){
					count++;
				}
			}
			switch (count) {
			case 2:
				score -= 40;
				break;
			case 3:
				score -= 90;
				break;
			case 4:
				score -= 1000;
				break;
			default:
				break;
			}
			count = 0;
		}
		for (int i = 0 ; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[row][i+j] == ai) {
					count = 0;
					break;
				} else if (board[row][i+j] == human){
					count++;
				}
			}
			switch (count) {
			case 2:
				score += 40;
				break;
			case 3:
				score += 90;
				break;
			case 4:
				score += 9000;
				break;
			default:
				break;
			}
			count = 0;
		}
		return score;
	}
	
	public int evalDiagPos(int n) {
		int count = 0, score = 0;
		for (int i = 5; i >= 3; i--) {
			for (int j = 0; j < 4; j++) {
				if (board[i-j][n+j] == human) {
					count = 0;
					break;
				} else if (board[i-j][n+j] == ai) {
					count++;
				}
			}
			switch (count) {
			case 2:
				score -= 40;
				break;
			case 3:
				score -= 90;
				break;
			case 4:
				score -= 1000;
				break;
			default:
				break;
			}
			count = 0;
		}
		for (int i = 5; i >= 3; i--) {
			for (int j = 0; j < 4; j++) {
				if (board[i-j][n+j] == ai) {
					count = 0;
					break;
				} else if (board[i-j][n+j] == human) {
					count++;
				}
			}
			switch (count) {
			case 2:
				score += 40;
				break;
			case 3:
				score += 90;
				break;
			case 4:
				score += 9000;
				break;
			default:
				break;
			}
			count = 0;
		}
		return score;
	}
	
	public int evalDiagNeg(int n) {
		int count = 0, score = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i+j][n+j] == human) {
					count = 0;
					break;
				} else if (board[i+j][n+j] == ai) {
					count++;
				}
			}
			switch (count) {
			case 2:
				score -= 40;
				break;
			case 3:
				score -= 90;
				break;
			case 4:
				score -= 1000;
				break;
			default:
				break;
			}
			count = 0;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i+j][n+j] == ai) {
					count = 0;
					break;
				} else if (board[i+j][n+j] == human) {
					count++;
				}
			}
			switch (count) {
			case 2:
				score += 40;
				break;
			case 3:
				score += 90;
				break;
			case 4:
				score += 9000;
				break;
			default:
				break;
			}
			count = 0;
		}
		return score;
	}
}

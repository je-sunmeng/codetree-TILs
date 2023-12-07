import java.io.*;
import java.util.*;



public class Main {
	
	static class Pair {
		int row, col;
		public Pair(int row, int col) {
			this.row = row;
			this.col = col;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("row: ").append(row).append(", col: ").append(col).append("\n");
			return sb.toString();
		}
	}
	
	public static int N, M, K;
	public static int maze[][], nextMaze[][], ans;
	public static int sr, sc, squareSize;
	public static Pair traveler[], exit;
	
	public static void moveAllTraveler() {
		for(int i = 0; i < M; i++) {
			if(traveler[i].row == exit.row && traveler[i].col == exit.col) continue;
			if(traveler[i].row != exit.row) {
				int nr = traveler[i].row;
				int nc = traveler[i].col;
				
				if(exit.row > nr) nr++;
				else nr--;
				
				if(maze[nr][nc] == 0) {
					traveler[i].row = nr;
					traveler[i].col = nc;
					ans++;
					continue;
				}
			}
			if(traveler[i].col != exit.col) {
				int nr = traveler[i].row;
				int nc = traveler[i].col;
				
				if(exit.col > nc) nc++;
				else nc--;
				
				if(maze[nr][nc] == 0) {
					traveler[i].row = nr;
					traveler[i].col = nc;
					ans++;
					continue;
				}
			}
		}
	}
	
	public static void findMinSquare() {
		for(int sz = 2; sz <= N; sz++) {
			for(int r1 = 0; r1 < N-sz+1; r1++) {
				for(int c1 = 0; c1 < N-sz+1; c1++) {
					int r2 = r1 + sz - 1;
					int c2 = c1 + sz - 1;
					// 출구가 네모 안에 있는지 확인
					if(!(r1<=exit.row && exit.row<=r2 && c1<=exit.col && exit.col<=c2)) continue;
					// 사람이 네모 안에 있는지 확인
					boolean isTraveler = false;
					for(int t = 0; t < M; t++) {
						if(r1<=traveler[t].row && traveler[t].row<=r2 && c1<=traveler[t].col && traveler[t].col<=c2) {
							if(traveler[t].row != exit.row || traveler[t].col != exit.col) {
								isTraveler = true;
								break;
							}
						}
					}
					if(isTraveler) {
						sr = r1;
						sc = c1;
						squareSize = sz;
						
						return;
					}
				}
			}
		}
	}
	
	public static void rotateSquare() {
		for(int row = sr; row < sr + squareSize; row++) {
			for(int col = sc; col < sc + squareSize; col++) {
				if(maze[row][col] > 0) maze[row][col]--;
				
				int or = row - sr, oc = col - sc;
				int rr = oc, rc = squareSize - or - 1;
				nextMaze[rr + sr][rc + sc] = maze[row][col];
			}
		}
		
		for(int row = sr; row < sr + squareSize; row++) {
			for(int col = sc; col < sc + squareSize; col++) {
				maze[row][col] = nextMaze[row][col];
			}
		}
	}
	
	public static void rotateTravelerAndExit() {
		for(int i = 0; i < M; i++) {
			int row = traveler[i].row;
			int col = traveler[i].col; 
			
			if(sr <= row && row <= sr+squareSize-1 && sc <= col && col <= sc+squareSize-1) {
				int or = row - sr, oc = col - sc;
				int rr = oc, rc = squareSize - or - 1;
				traveler[i].row = rr + sr;
				traveler[i].col = rc + sc;
			}
		}
		
		int row = exit.row;
		int col = exit.col;
		if(sr <= row && row <= sr+squareSize-1 && sc <= col && col <= sc+squareSize-1) {
			int or = row - sr, oc = col - sc;
			int rr = oc, rc = squareSize - or - 1;
			exit.row = rr + sr;
			exit.col = rc + sc;
		}
	}

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		StringTokenizer stk = new StringTokenizer(str);
		N = Integer.parseInt(stk.nextToken());
		M = Integer.parseInt(stk.nextToken());
		K = Integer.parseInt(stk.nextToken());
		
		maze = new int[N][N];
		nextMaze = new int[N][N];
		traveler = new Pair[M];
		
		for(int i = 0; i < N; i++) {
			str = br.readLine();
			stk = new StringTokenizer(str);
			for(int j = 0; j < N; j++) {
				maze[i][j] = Integer.parseInt(stk.nextToken());
			}
		}
		
		for(int i = 0; i < M; i++) {
			str = br.readLine();
			stk = new StringTokenizer(str);
			int row = Integer.parseInt(stk.nextToken()) - 1;
			int col = Integer.parseInt(stk.nextToken()) - 1;
			traveler[i] = new Pair(row, col);
		}
		
		str = br.readLine();
		stk = new StringTokenizer(str);
		int row = Integer.parseInt(stk.nextToken()) - 1;
		int col = Integer.parseInt(stk.nextToken()) - 1;
		exit = new Pair(row, col);
		
		while(K-- > 0) {
			moveAllTraveler();
			
			boolean isAllEscaped = true;
			for(int i = 0; i < M; i++) {
				if(traveler[i].row != exit.row || traveler[i].col != exit.col) {
					isAllEscaped = false;
					break;
				}
			}
			if(isAllEscaped) break;
			
			findMinSquare();
			
			rotateSquare();
			rotateTravelerAndExit();
		}
		System.out.println(ans);
		System.out.println((exit.row+1) + " " + (exit.col+1));
	}
	
}
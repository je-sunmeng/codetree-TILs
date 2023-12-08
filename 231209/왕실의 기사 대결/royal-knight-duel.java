import java.io.*;
import java.util.*;

public class Main {
	static class Knight {
		int r, c, h, w, k, hp;
		public Knight(int r, int c, int h, int w, int k) {
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
			this.hp = k;
		}
	}
	static class Order {
		int i, d;
		public Order(int i, int d) {
			this.i = i;
			this.d = d;
		}
	}
	
	public static int L, N, Q;
	public static int board[][];
	public static int knightBoard[][];
	public static Knight knights[];
	public static Order orders[];
	public static List<Integer> list = new ArrayList<Integer>();
	public static int[] dr = {-1, 0, 1, 0}, dc = {0, 1, 0, -1};
	
	public static void moveKnight(int start, int d) {
		boolean isMoved[] = new boolean[N+1];
		isMoved[start] = true;
		list.add(start);
		for(int i = 0; i < list.size(); i++) {
//			System.out.println("size: " +list.size());
			int n = list.get(i);
			int row = knights[n].r + dr[d];
			int col = knights[n].c + dc[d];
			int height = knights[n].h;
			int width = knights[n].w;
			for(int r = row; r <= row+height-1; r++) {
				for(int c = col; c <= col+width-1; c++) {
					if(r < 0 || r >= L || c < 0 || c >= L || board[r][c] == 2) {
//						System.out.println("knight: " + n + ", row: " + row + ", col : "+ col);
//						System.out.println("cannot");
						list.clear();
						return;
					}
					if(knightBoard[r][c] != 0 && !isMoved[knightBoard[r][c]] ) {
						list.add(knightBoard[r][c]);
						isMoved[knightBoard[r][c]] = true;
//						System.out.println("add knight: "+ knightBoard[r][c]);
					}
				}
			}
		}
//		System.out.println("fin size: " + list.size());
		knightBoard = new int[L][L];
		for(int i = 0; i < list.size(); i++) {
			int knight = list.get(i);
			Knight thisKight = knights[knight];
			knights[knight].r = thisKight.r + dr[d];
			knights[knight].c = thisKight.c + dc[d];
			for(int row = thisKight.r; row <= thisKight.r+thisKight.h-1; row++) {
				for(int col = thisKight.c; col <= thisKight.c+thisKight.w-1; col++) {
					knightBoard[row][col] = knight;
				}
			}
		}
	}
	
	public static void trap() {
		for(int i = 1; i < list.size(); i++) {
			int n = list.get(i);
			if(knights[n].hp <= 0) continue;
			int r = knights[n].r;
			int c = knights[n].c;
			int h = knights[n].h;
			int w = knights[n].w;
			for(int row = r ; row <= r+h-1; row++) {
				for(int col = c; col <= c+w-1; col++) {
					if(knights[n].hp <= 0) break;
					if(board[row][col] == 1) knights[n].hp--;
				}
			}
			//죽은 기사 처리
			if(knights[n].hp <= 0) {
				for(int row = r ; row <= r+h-1; row++) {
					for(int col = c; col <= c+w-1; col++) {
						knightBoard[row][col] = 0;
					}
				}
			}
		}
		list.clear();
	}
	
	public static int calHP() {
		int sum = 0;
		for(int i = 1; i <= N; i++) {
			Knight knight = knights[i];
			if(knight.hp > 0) {
//				System.out.println("knight: "+i+", k: " +knight.k+", hp: "+knight.hp);
				sum += (knight.k - knight.hp);
			}
		}
		return sum;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = br.readLine();
		StringTokenizer stk = new StringTokenizer(str);
		
		L = Integer.parseInt(stk.nextToken());
		N = Integer.parseInt(stk.nextToken());
		Q = Integer.parseInt(stk.nextToken());
		
		board = new int[L][L];
		knights = new Knight[N+1];
		knightBoard = new int[L][L];
		orders = new Order[Q];
		
		for(int i = 0; i < L; i++) {
			str = br.readLine();
			stk = new StringTokenizer(str);
			for(int j = 0; j < L; j++) {
				board[i][j] = Integer.parseInt(stk.nextToken());
			}
		}
		
		for(int i = 1; i <= N; i++) {
			str = br.readLine();
			stk = new StringTokenizer(str);
			int r = Integer.parseInt(stk.nextToken())-1;
			int c = Integer.parseInt(stk.nextToken())-1;
			int h = Integer.parseInt(stk.nextToken());
			int w = Integer.parseInt(stk.nextToken());
			int k = Integer.parseInt(stk.nextToken());
			knights[i] = new Knight(r, c, h, w, k);
			for(int row = r; row <= r+h-1; row++) {
				for(int col = c; col <= c+w-1; col++) {
					knightBoard[row][col] = i;
				}
			}
		}
		
		for(int j = 0; j < Q; j++) {
			str = br.readLine();
			stk = new StringTokenizer(str);
			int i = Integer.parseInt(stk.nextToken());
			int d = Integer.parseInt(stk.nextToken());
			orders[j] = new Order(i, d);
		}
		
		for(int i = 0; i < Q; i++) {
			Order order = orders[i];
//			System.out.println(order.i +" " + order.d);
			moveKnight(order.i, order.d);
			trap();
		}
		System.out.println(calHP());
	}

}
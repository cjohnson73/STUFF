import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.Random;
import javax.swing.JFrame;

public class Main 
{
	static JFrame frame;
	static Graphics g;
	static BufferStrategy bs;
	static Canvas canvas;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static KeyManager keyList = new KeyManager();
	static double bssf = 0;
	static int numSolves = 1000, numSteps = 20, tsize, w, h, best, sb, bestSin = 0, numSolvers = 40;
	static int[] cubies = new int[48];
	static int[] scramble = new int[50];
	static double[] scores = new double[numSolvers];
	static double[][][] solver = new double[numSolvers][cubies.length][20];//[0,1/7]
	static boolean run = true;
	static int[] solution = new int[20];
	final static String[] moves = {"0", "R", "R2", "R'", "0", "L", "L2", "L'", "0", "F", "F2", "F'", "0", "B", "B2", "B'", "0", "U", "U2", "U'", "0", "D", "D2", "D'"};
	static Random rand = new Random(System.currentTimeMillis());
	public static void main(String[] args)
	{
		init();
		createDisplay();
		for(int k = 0; k<numSolvers; k++)
		{
			for(int i = 0; i<numSteps; i++)
			{
				for(int j = 0; j<cubies.length; j++)
				{
					solver[k][j][i] = rand.nextDouble()/7;
				}
			}
		}
		while(run)
		{
			if(keyList.keyPressed(8))
				run = false;
			for(int k = 0; k<numSolvers; k++)
			{
				scores[k] = 0;
			}
			for(int k = 0; k<numSolvers; k++)
			{
				for(int m = 0; m<numSolves; m++)
				{
					cubies[0] = 1;
					for(int i = 1; i<cubies.length; i++)
						cubies[i] = i/8+1;
					scrambler();
					apply(scramble);
					int bscore = test();
					solve(k);
					apply(solution);
					//for(int i = 0; i<scramble.length; i++)
						//System.out.print(moves[scramble[i]] + " ");
					//System.out.println();
					//for(int i = 0; i<solution.length; i++)
					//{
						//if(solution[i]%4!=0)
							//System.out.print(moves[solution[i]] + " ");
					//}
					//System.out.println();
					int score = test();
					if(score>bscore)
					{
						if(score>bestSin)
							bestSin = score;
						//System.out.println(score);
						scores[k] += score;
					}
				}
				scores[k] /=numSolves;
				if(scores[k]>bssf)
					bssf = scores[k];
			}
			adjust();
			render();
		}
		for(int i = 0; i<numSteps; i++)
		{
			for(int j = 0; j<cubies.length; j++)
			{
				System.out.print(solver[0][j][i] + "\t");
			}
			System.out.println();
		}
		frame.dispose();
	}
	public static int test()
	{
		int j = 0;
		if(cubies[0]==1)
			j++;
		for(int i = 1; i<cubies.length; i++)
		{
			if(cubies[i]==i/8+1)
				j++;
		}
		return j;
	}
	public static void scrambler()
	{
		for(int i = 0; i<scramble.length; i++)
		{
			scramble[i] = rand.nextInt(24);
			if(scramble[i]%4==0)
				scramble[i]+=rand.nextInt(3)+1;
			if((i>0 && let(scramble[i])==let(scramble[i-1]))||(i>1 && let(scramble[i])==let(scramble[i-2]) && let(scramble[i-1])%6==(let(scramble[i])%2==0 ? let(scramble[i])+1: let(scramble[i])-1)%6))
				i--;
		}
	}
	public static void solve(int k)
	{
		for(int i = 0; i<solution.length; i++)
		{
			double temp = 0;
			for(int j = 0; j<cubies.length; j++)
			{
				temp+=solver[k][j][i]*cubies[i];
			}
			solution[i] = (int) Math.floor(temp);
			if(solution[i]>23)
				solution[i] = 23;
		}
	}
	public static void adjust()
	{
		//NOT SO HARD CUZ IM LAZY AND DID GENETIC INSTEAD OF NEURAL NET
		best = 0;
		sb = 0;
		for(int k = 0; k<scores.length; k++)
		{
			if(scores[k]>scores[best])
				best = k;
		}
		for(int k = 0; k<scores.length; k++)
		{
			if(scores[k]>scores[sb] && k!=best)
				sb = k;
		}
		//generate rest of 18 solutions based off of the solver[best][][] and solver[sb][][]
		for(int i = 0; i<numSteps; i++)
		{
			for(int j = 0; j<cubies.length; j++)
			{
				solver[0][j][i] = solver[best][j][i];
				solver[1][j][i] = solver[sb][j][i];
			}
		}
		for(int i = 2; i<numSolvers; i++)
		{
			for(int k = 0; k<numSteps; k++)
			{
				for(int j = 0; j<cubies.length; j++)
				{
					int m = rand.nextInt(101);
					if(m!=50)
						solver[i][j][k] = solver[m%2][j][k];
					else
						solver[i][j][k] = rand.nextDouble()/7;
				}
			}
		}
	}
	public static int let(int a)
	{
		return((a-a%4)/4);
	}
	public static void apply(int[] a)
	{
		for(int i = 0; i<a.length; i++)
		{
			for(int j = 0; j<a[i]%4; j++)
			{
				if(let(a[i])==0)//R
				{
					swap(24,26,31,29);
					swap(25,28,30,27);
					swap(18,2,37,42);
					swap(20,4,35,44);
					swap(47,23,7,32);
				}
				else if(let(a[i])==1)//L
				{
					swap(8,10,15,13);
					swap(9,12,14,11);
					swap(3,19,43,36);
					swap(0,16,40,39);
					swap(5,21,45,34);
				}
				else if(let(a[i])==2)//F
				{
					swap(16,18,23,21);
					swap(17,20,22,19);
					swap(5,24,42,15);
					swap(6,27,41,12);
					swap(7,29,40,10);
				}
				else if(let(a[i])==3)//B
				{
					swap(32,34,39,37);
					swap(33,36,38,35);
					swap(28,1,11,46);
					swap(26,0,13,47);
					swap(31,2,8,45);
				}
				else if(let(a[i])==4)//U
				{
					swap(0,2,7,5);
					swap(1,4,6,3);
					swap(17,9,33,25);
					swap(16,8,32,24);
					swap(18,10,34,26);
				}
				else if(let(a[i])==5)//D
				{
					swap(40,42,47,45);
					swap(41,44,46,43);
					swap(22,30,38,14);
					swap(21,29,37,13);
					swap(23,31,39,15);
				}
			}
		}
	}
	public static void swap(int a, int b, int c, int d)
	{
		int k = cubies[d];
		cubies[d] = cubies[c];
		cubies[c] = cubies[b];
		cubies[b] = cubies[a];
		cubies[a] = k;
	}
	public static void render()
	{
		bs = canvas.getBufferStrategy();
		if(bs == null)
		{
			canvas.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		g.clearRect(0, 0, w, h);
		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  w,  h);
		
		g.setColor(Color.BLACK);
		String S = "Best Sore: " + scores[best];
		String T = "Best Sore So Far: " + bssf;
		String U = "Best Single: " + bestSin;
		g.drawString(S, 0, tsize);
		g.drawString(T, 0, 3*tsize);
		g.drawString(U, 0, 5*tsize);
		//for(int i = 0; i<20; i++)
		//{
		//	String L = "";
		//	for(int j = 0; j<cubies.length; j++)
		//	{
		//		L += ((10000*solver[0][j][i])-(10000*solver[0][j][i])%10)/10000 + "   \t\t   ";
		//	}
		//	g.drawString(L, 0, tsize*i);
		//}
		bs.show();
		g.dispose();
	}
	public static void init()
	{
		tsize = (gd.getDisplayMode().getWidth() <= gd.getDisplayMode().getHeight()) ? gd.getDisplayMode().getWidth()/56 : gd.getDisplayMode().getHeight()/35;
		w = 20*tsize;
		h = 20* tsize;
	}
	public static void createDisplay()
	{
		frame = new JFrame("RNN");
		frame.setSize(w, h);
		frame.addKeyListener(keyList);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(w, h));
		canvas.setMaximumSize(new Dimension(w, h));
		canvas.setMinimumSize(new Dimension(w, h));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
	}
}

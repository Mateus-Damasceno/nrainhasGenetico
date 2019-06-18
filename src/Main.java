import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Main {

	private static final int HashMap = 0;
	private static final int String = 0;
	static int tabuleiroTamanho = 0;
	public static int[][] tabuleiro = null;
	public static int printContador = 0;
	public static int restartContador = 0;
	public static int popSize = 0;
	public static final int MAX_VALUE = Integer.MAX_VALUE;
	public static double taxaDeMutacao = .01;

	public static void main(String[] args) {
		System.out.println("N rainhas usando algoritmo genetico");
		System.out.println("Entre com o tamanho do tabuleiro:");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		tabuleiroTamanho = Integer.parseInt(s);

		System.out.println("Entre com o tamanho da mutacao");
		s = in.nextLine();
		popSize = Integer.parseInt(s);

		System.out.println("Entre com a taxa de mutacao:");
		s = in.nextLine();
		taxaDeMutacao = Double.parseDouble(s);

		in.close();
		tabuleiro = new int[tabuleiroTamanho][tabuleiroTamanho];

		start();

	}

	public static void start() {
		ArrayList<Cromossomo> populacao = new ArrayList<Cromossomo>();
		String[] randStrings = genRandStrings();
		String[] strings = randStrings;
		// determina a funcao fitness de cada cromossomo
		populacao = avaliacao(strings);
		while (true) {
			// ordena a lista baseada na funcao fitness
			Collections.sort(populacao);
			// checa se encontra o criterio
			if (populacao.get(0).getFitness() == 0) {
				System.out.println("solucao encontrada!");
				stringCompara(populacao.get(0).getSeq());
				printTabuleiro();
				break;
			}
			// remove 50% dos mais fraco
						
			// faz o crossover dos outros 50
			populacao = crossOp(populacao);

			if (populacao.size() == 0) {
				System.out.println("solucao nao encontrada");
				break;
			}
			// mutacao randomica
			populacao = mutacao(populacao);

			// avalia(determina o fitness de cada cromossomo
			populacao = avaliacao(getStrings(populacao));
			if (populacao.size() == 0) {
				System.out.println("solucao nao encontrada");
				break;
			}

		}

	}

	public static ArrayList<Cromossomo> mutacao(ArrayList<Cromossomo> populacao) {
		Random rand = new Random();
		int iter = (int) (taxaDeMutacao * populacao.size());
		int n;
		for (int i = 0; i <= iter; i++) {
			n = rand.nextInt(populacao.size());
			populacao.get(n).setSeq(mutacaoRandomica(populacao.get(n).getSeq()));
		}

		return populacao;
	}

	public static String mutacaoRandomica(String s1) {
		Random rand = new Random();
		int n1 = rand.nextInt(s1.length());
		int n2 = rand.nextInt(s1.length());
		while (n2 == n1) {
			n2 = rand.nextInt(s1.length());
		}
		StringBuilder stringSaida = new StringBuilder(s1);
		char temp = s1.charAt(n1);
		stringSaida.setCharAt(n1, s1.charAt(n2));
		stringSaida.setCharAt(n2, temp);
		stringSaida.trimToSize();
		return stringSaida.toString();
	}

	public static ArrayList<Cromossomo> crossOp(ArrayList<Cromossomo> populacao) {
		String[] temp = null;
		Cromossomo c1 = new Cromossomo(null, MAX_VALUE);
		Cromossomo c2 = new Cromossomo(null, MAX_VALUE);
		for (int i = 0; i < populacao.size(); i++) {
			if (populacao.size() - 2 >= (i + 1)) {
				temp = crossOver(populacao.get(i).getSeq(),
						populacao.get(i + 1).getSeq(), 4);
				populacao.remove(i);
				populacao.remove(i + 1);
				c1.setSeq(temp[0]);
				c2.setSeq(temp[1]);
				populacao.add(c1);
				populacao.add(c2);
				c1 = new Cromossomo(null, MAX_VALUE);
				c2 = new Cromossomo(null, MAX_VALUE);
				i++;
			} else {
				break;
			}
		}
		return populacao;
	}

	public static String[] crossOver(String s1, String s2, int pontoDeCrossover) {
		String[] cruzado = new String[2];
		String str1p1 = s1.substring(0, pontoDeCrossover);
		String str1p2 = s1.substring(pontoDeCrossover, s1.length());
		String str2p1 = s2.substring(0, pontoDeCrossover);
		String str2p2 = s2.substring(pontoDeCrossover, s1.length());

		cruzado[0] = str1p1 + str2p2;
		cruzado[1] = str2p1 + str1p2;
		return cruzado;
	}

	public static String[] getStrings(ArrayList<Cromossomo> populacao) {
		String[] saida = new String[populacao.size()];
		for (int i = 0; i < populacao.size(); i++) {
			saida[i] = populacao.get(i).getSeq();
		}
		return saida;
	}

	// faz a avaliacao
	public static ArrayList<Cromossomo> avaliacao(String[] stringAleatoria) {
		ArrayList<Cromossomo> populacao = new ArrayList<Cromossomo>();
		Cromossomo cromo = new Cromossomo(null, tabuleiroTamanho * tabuleiroTamanho);
		for (int i = 0; i < stringAleatoria.length; i++) {
			stringCompara(stringAleatoria[i]);
			cromo.setSeq(stringAleatoria[i]);
			cromo.setFitness(getTabuleiroConflito());
			populacao.add(cromo);
			cromo = new Cromossomo(null, tabuleiroTamanho * tabuleiroTamanho);
		}
		return populacao;
	}

	public static String[] genRandStrings() {
		Random rand = new Random();
		String[] randStrings = new String[popSize];
		int n;
		String myString = "";
		for (int j = 0; j < popSize; j++) {
			for (int i = 0; i < tabuleiroTamanho; i++) {
				n = rand.nextInt(tabuleiroTamanho);
				myString += n;
			}
			randStrings[j] = myString;
			myString = "";
		}
		return randStrings;
	}

	public static boolean solve() {
		return true;
	}

	public static void stringCompara(String cromo) {
		iniciaTabuleiro();
		int pos = 0;
		for (int i = 0; i < cromo.length(); i++) {
			pos = Character.getNumericValue(cromo.charAt(i));
			tabuleiro[i][pos] = i + 1;
		}
		System.out.println(cromo);
	}

	public static int getTabuleiroConflito() {
		int conflitos = 0;
		for (int i = 0; i < tabuleiroTamanho; i++) {
			for (int j = 0; j < tabuleiroTamanho; j++) {
				if (tabuleiro[i][j] > 0) {
					conflitos += getConflito(i, j, i, j);
					break;
				}
			}
		}

		return conflitos;
	}

	public static void iniciaTabuleiro() {
		for (int i = 0; i < tabuleiroTamanho; i++) {
			for (int j = 0; j < tabuleiroTamanho; j++) {
				tabuleiro[i][j] = 0;
			} 
		}
	}

	public static void printTabuleiro() {
		for (int i = 0; i < tabuleiroTamanho; i++) {
			for (int j = 0; j < tabuleiroTamanho; j++) {
				System.out.print(tabuleiro[j][i] + "|");
			} 
			System.out.println();
		}
		System.out.println("----------------");
		printContador++;
	}

	public static void iniciaRainhas() {
		Random rand = new Random();
		for (int i = 0; i < tabuleiroTamanho; i++) {
			colocaRainha(i + 1, i, rand.nextInt(tabuleiroTamanho));
		}

	}

	public static void colocaRainha(int numeroRainhas, int x, int y) {
		tabuleiro[x][y] = numeroRainhas;
	}

	public static void escolheRainha(int numeroRainhas, int x, int y) {
		tabuleiro[x][y] = 0;
	}

	public static int[] encontraRainha(int numeroRainha) {
		int[] result = new int[2];
		for (int i = 0; i < tabuleiroTamanho; i++) {
			for (int j = 0; j < tabuleiroTamanho; j++) {
				if (tabuleiro[i][j] == numeroRainha) {
					result[0] = i;
					result[1] = j;
					return result;
				}
			}
		}
		return null;
	}

	public static int getConflito(int x, int y, int curX, int curY) {

		int count = 0;
		count += buscaAcima(x, y, curX, curY);
		count += buscaBaixo(x, y, curX, curY);
		count += buscaEsquerda(x, y, curX, curY);
		count += buscaDireita(x, y, curX, curY);
		count += buscaNoroeste(x, y, curX, curY);
		count += buscaNordeste(x, y, curX, curY);
		count += buscaSudoeste(x, y, curX, curY);
		count += buscaSudeste(x, y, curX, curY);

		return count;
	}

	public static int buscaAcima(int x, int y, int curX, int curY) {
		for (int i = y - 1; i < tabuleiroTamanho && i >= 0; i--) {
			if (tabuleiro[x][i] > 0 && i != curY && i < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaBaixo(int x, int y, int curX, int curY) {
		for (int i = y + 1; i < tabuleiroTamanho && i >= 0; i++) {
			if (tabuleiro[x][i] > 0 && i != curY && i < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaEsquerda(int x, int y, int curX, int curY) {
		for (int i = x - 1; i < tabuleiroTamanho && i >= 0; i--) {
			if (tabuleiro[i][y] > 0 && i != curX && i < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaDireita(int x, int y, int curX, int curY) {
		for (int i = x + 1; i < tabuleiroTamanho && i >= 0; i++) {
			if (tabuleiro[i][y] > 0 && i != curX && i < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaNoroeste(int x, int y, int curX, int curY) {
		for (int i = x - 1, j = y - 1; i < tabuleiroTamanho && j < tabuleiroTamanho && i >= 0
				&& j >= 0; i--, j--) {
			if (tabuleiro[i][j] > 0 && !(i == curX && j == curY) && i < tabuleiroTamanho
					&& j < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaNordeste(int x, int y, int curX, int curY) {
		for (int i = x + 1, j = y - 1; i < tabuleiroTamanho && j < tabuleiroTamanho && i >= 0
				&& j >= 0; i++, j--) {
			if (tabuleiro[i][j] > 0 && !(i == curX && j == curY) && i < tabuleiroTamanho
					&& j < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaSudoeste(int x, int y, int curX, int curY) {
		for (int i = x - 1, j = y + 1; i < tabuleiroTamanho && j < tabuleiroTamanho && i >= 0
				&& j >= 0; i--, j++) {
			if (tabuleiro[i][j] > 0 && !(i == curX && j == curY) && i < tabuleiroTamanho
					&& j < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

	public static int buscaSudeste(int x, int y, int curX, int curY) {
		for (int i = x + 1, j = y + 1; i < tabuleiroTamanho && j < tabuleiroTamanho && i >= 0
				&& j >= 0; i++, j++) {
			if (tabuleiro[i][j] > 0 && !(i == curX && j == curY) && i < tabuleiroTamanho
					&& j < tabuleiroTamanho) {
				return 1;
			}
		}

		return 0;
	}

}
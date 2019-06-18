import java.util.Collections;
import java.util.Comparator;

public class Cromossomo implements Comparable<Cromossomo> {
	String seq;
	int fitness;

	public Cromossomo(String seq, int fitness) {
		this.seq = seq;
		this.fitness = fitness;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	@Override
	public int compareTo(Cromossomo comparaCromo) {
		int comparefit = ((Cromossomo) comparaCromo).getFitness();
		
		return this.fitness - comparefit;
	}

	

}
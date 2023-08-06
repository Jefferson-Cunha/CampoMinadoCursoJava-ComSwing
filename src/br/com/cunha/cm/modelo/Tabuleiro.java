package br.com.cunha.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

	private final int linhas;
	private final int colunas;
	private final int minas;

	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> obs = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}
	
	public void paraCada(Consumer<Campo> funcao){
		campos.forEach(funcao);
	}
	
	
	
	

	public void registrarObgTab(Consumer<ResultadoEvento> o) {
		obs.add(o);
	}

	private void notificarObsTab(boolean resultado) {
		obs.stream().forEach(obs -> obs.accept(new ResultadoEvento(resultado)));
	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancasdo());
	}

	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	public void abrirCampo(int linha, int coluna) {

		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.abrirC());

	}

	public void MarcaCampo(int linha, int coluna) {
		campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alterarM());
	}

	private void gerarCampos() {
		for (int linha = 0; linha < linhas; linha++) {
			for (int coluna = 0; coluna < colunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObs(this);
				campos.add(campo);

			}
		}

	}

	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		long minasArmadas = 0;

		Predicate<Campo> minado = c -> c.isMinado();

		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minas);
	}

	public void eventoOcorreu(Campo c, CampoEvento e) {
		if (e == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObsTab(false);
		} else if (objetivoAlcancado()) {
			notificarObsTab(true);
		}

	}

	private void mostrarMinas() {
		campos.stream().filter(c -> c.isMinado()).filter(c -> !c.isMarcado()).forEach(c -> c.setAberto(true));

	}

}

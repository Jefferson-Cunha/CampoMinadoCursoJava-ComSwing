package br.com.cunha.cm.modelo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Campo {

	private final int linha;
	private final int coluna;

	private boolean minado = false;
	private boolean aberto = false;
	private boolean marcado = false;

	private List<Campo> vizinhos = new ArrayList<>();
	private Set<CampoObservador> observadores = new LinkedHashSet<>();

	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public void registrarObs(CampoObservador obs) {
		observadores.add(obs);
	}
	
	
	public void notificarObs(CampoEvento evt){
		observadores.stream().forEach(obs -> obs.eventoOcorreu(this, evt));
	}

	// Função para adicionar vizinhos do campo!
	// V -> Abreviação de Verificar!

	boolean adicionarVizinho(Campo v) {

		boolean vLinha = linha != v.linha;
		boolean vColuna = coluna != v.coluna;
		boolean vDiagonal = vLinha && vColuna;

		int deltaLinha = Math.abs(linha - v.linha);
		int deltaColuna = Math.abs(coluna - v.coluna);
		int deltaGeral = deltaLinha + deltaColuna;

		if (deltaGeral == 1 && !vDiagonal) {
			vizinhos.add(v);
			return true;
		} else if (deltaGeral == 2 && vDiagonal) {
			vizinhos.add(v);
			return true;
		} else {
			return false;
		}

	}

	// Gets e Sets da Classe Campo.

	public boolean isMinado() {
		return minado;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if(aberto) {
			notificarObs(CampoEvento.ABRIR);
		}
		
	}

	boolean isAberto() {
		return aberto;
	}

	boolean isFechado() {
		return !aberto;
	}

	boolean isMarcado() {
		return marcado;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	// Função para alterar a marcação do campo!

	public void alterarM() {
		if (!aberto) {
			marcado = !marcado;
		}
		
		if(marcado) {
			notificarObs(CampoEvento.MARCAR);
		} else {
			notificarObs(CampoEvento.DESMARCAR);
		}
		
	}

	// Função para minar campo!

	void minar() {
		if (!minado) {
			minado = !minado;
		}
	}

	// Função para abrir o campo!

	public boolean abrirC() {
		if (!aberto && !marcado) {
			aberto = true;

			if (minado) {
				notificarObs(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);

			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrirC());
			}
			return true;
		} else {
			return false;
		}
	}

	// Função para verificar vizinhança do campo selecionado.

	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}

	boolean objetivoAlcancasdo() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;

		return desvendado || protegido;
	}

	public int minasNaVizinhanca() {
		return (int)vizinhos.stream().filter(v -> v.minado).count();
	}

	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObs(CampoEvento.REINICIAR);
	}

}

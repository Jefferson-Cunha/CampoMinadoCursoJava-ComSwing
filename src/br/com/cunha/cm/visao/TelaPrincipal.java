package br.com.cunha.cm.visao;

import javax.swing.JFrame;

import br.com.cunha.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {

	public TelaPrincipal() {
		Tabuleiro tb = new Tabuleiro(16, 30, 10);		
		add(new PainelTabuleiro(tb));
		setTitle("Campo Minado Com Swing");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {

		new TelaPrincipal();

	}
}

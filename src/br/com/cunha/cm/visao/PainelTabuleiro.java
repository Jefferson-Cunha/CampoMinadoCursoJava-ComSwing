package br.com.cunha.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.cunha.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tb) {

		setLayout(new GridLayout(tb.getLinhas(), tb.getColunas()));

		tb.paraCada(c -> add(new BotaoCampo(c)));

		tb.registrarObgTab(e -> {
			SwingUtilities.invokeLater(() -> {
				if (e.isGanhou()) {
					JOptionPane.showMessageDialog(this, "Parabens você Ganhou!!\nComeçar de novamente?");
				} else {
					JOptionPane.showMessageDialog(this, "Você Perdeu \nComeçar de novamente?");
				}
				
				tb.reiniciar();
				
			});

		});

		

	}

}

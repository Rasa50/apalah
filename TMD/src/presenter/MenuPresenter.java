package presenter;

import model.BenefitDAO;
import view.MenuView;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingUtilities;
import java.util.List;
import model.Benefit;

public class MenuPresenter {
    private MenuView view;
    private BenefitDAO dao;

    public MenuPresenter(MenuView view) {
        this.view = view;
        this.dao = new BenefitDAO();
    }

    public void loadData() {
        List<Benefit> list = dao.getAll();
        DefaultTableModel model = new DefaultTableModel(new String[]{"User", "Skor", "Meleset", "Sisa Peluru"}, 0);
        for (Benefit b : list) {
            model.addRow(new Object[]{b.getUsername(), b.getSkor(), b.getPeluruMeleset(), b.getPeluruAkhir()});
        }
        SwingUtilities.invokeLater(() -> {
            view.setTableModel(model);
        });
    }

    public void onPlayClicked() {
        String user = view.getUsername();
        if (!user.isEmpty()) {
            view.getMainFrame().setPlayerName(user);
            dao.upsertUser(user);
            view.startGame();
        } else {
            javax.swing.JOptionPane.showMessageDialog(view, "Pilot name is required!");
        }
    }
}
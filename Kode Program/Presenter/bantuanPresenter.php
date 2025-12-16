<?php
require_once __DIR__ . '/../model/bantuanModel.php';
require_once 'interface.php';

// Implementasi Presenter
class BantuanPresenter implements IBantuanPresenter {
    private $model;
    private $view; 

    public function __construct($view) {
        $this->model = new BantuanModel();
        $this->view = $view; 
    }

    // Metode untuk menangani semua input POST
    public function handleActions() {
        if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action'])) {
            $action = $_POST['action'];

            if ($action == 'simpan') {
                $data = [
                    'id' => $_POST['id'],
                    'donatur' => $_POST['donatur'],
                    'isibantuan' => $_POST['isibantuan'],
                    'tanggalmasuk' => $_POST['tanggalmasuk'],
                    'nilai' => $_POST['nilai'],
                    'daerahsalur' => $_POST['daerahsalur']
                ];
                $this->handleInputBantuan($data);
            } elseif ($action == 'ubah_masuk') {
                $this->handleUbahMasuk($_POST['ubah_id'], $_POST['ubah_nilai'], $_POST['ubah_status']);
            } elseif ($action == 'hapus_masuk') {
                $this->handleHapusMasuk($_POST['hapus_id']);
            } elseif ($action == 'salur') {
                $this->handleSalur($_POST['salur_id'], $_POST['salur_status']);
            } elseif ($action == 'ubah_salur') {
                $this->handleUbahSalur($_POST['ubah_salur_id'], $_POST['ubah_salur_status']);
            }
            header("Location: index.php");
            exit;
        }
    }

    public function handleInputBantuan($data) {
        if (empty($data['id']) || empty($data['donatur']) || empty($data['tanggalmasuk'])) {
            $this->view->displayMessage("ID, Donatur, dan Tanggal Masuk wajib diisi.", "error");
            return;
        }

        $result = $this->model->tambahBantuanMasuk(
            $data['id'], 
            $data['donatur'], 
            $data['isibantuan'], 
            $data['tanggalmasuk'], 
            $data['nilai'], 
            $data['daerahsalur']
        );

        if ($result) {
            $this->view->displayMessage("Data bantuan berhasil dimasukkan.", "success");
        } else {
            $this->view->displayMessage("Gagal memasukkan data bantuan. ID mungkin sudah ada.", "error");
        }
    }
    
    public function handleUbahMasuk($id, $nilai, $status) {
        $result = $this->model->ubahBantuanMasuk($id, $nilai, $status);
        
        if ($result) {
            $this->view->displayMessage("Data bantuan masuk berhasil diubah.", "success");
        } else {
            $this->view->displayMessage("Gagal mengubah data atau status tidak valid.", "error");
        }
    }
    
    public function handleHapusMasuk($id) {
        $result = $this->model->hapusBantuanMasuk($id);
        
        if ($result) {
            $this->view->displayMessage("Data bantuan masuk berhasil dihapus.", "success");
        } else {
            $this->view->displayMessage("Gagal menghapus data.", "error");
        }
    }
    
    public function handleSalur($id, $statusSalur) {
        if (!in_array($statusSalur, ['masuk', 'verifikasi', 'tersalur', 'hilang'])) {
             $this->view->displayMessage("Status penyaluran tidak valid.", "error");
             return;
        }
        
        $result = $this->model->salurkanBantuan($id, $statusSalur);
        
        if ($result) {
            $this->view->displayMessage("Bantuan dengan ID {$id} berhasil disalurkan dan dipindahkan.", "success");
        } else {
            $this->view->displayMessage("Gagal menyalurkan/memindahkan bantuan. Cek log.", "error");
        }
    }
    
    public function handleUbahSalur($id, $status) {
        $result = $this->model->ubahStatusBantuanSalur($id, $status);
        
        if ($result) {
            $this->view->displayMessage("Status bantuan tersalur berhasil diubah.", "success");
        } else {
            $this->view->displayMessage("Gagal mengubah status atau status tidak valid.", "error");
        }
    }

    public function getSemuaData() {
        return [
            'masuk' => $this->model->getAllBantuanMasuk(),
            'salur' => $this->model->getAllBantuanSalur()
        ];
    }
}
?>
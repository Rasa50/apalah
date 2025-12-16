<?php
require_once __DIR__ . '/../config/database.php';

class BantuanModel
{
    private $conn;

    public function __construct()
    {
        $database = new Database();
        $this->conn = $database->getConnection();
    }

    // --- FUNGSI TAMBAH ---

    public function tambahBantuanMasuk($id, $donatur, $isi, $tanggal, $nilai, $daerah)
    {
        // Status default saat menambahkan adalah 'masuk'
        $status = 'masuk';
        $stmt = $this->conn->prepare("INSERT INTO tbantuanmasuk (id, donatur, isibantuan, tanggalmasuk, nilai, daerahsalur, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
        $nilai = number_format($nilai, 2, '.', '');
        
        $stmt->bind_param("sssssss", $id, $donatur, $isi, $tanggal, $nilai, $daerah, $status);
        
        $result = $stmt->execute();
        $stmt->close();
        return $result;
    }

    // --- FUNGSI Read ---

    public function getAllBantuanMasuk()
    {
        $result = $this->conn->query("SELECT * FROM tbantuanmasuk");
        return $result->fetch_all(MYSQLI_ASSOC);
    }

    public function getAllBantuanSalur()
    {
        $result = $this->conn->query("SELECT * FROM tbantuansalur");
        return $result->fetch_all(MYSQLI_ASSOC);
    }
    
    // --- FUNGSI Update pada Bantuan Masuk ---

    public function ubahBantuanMasuk($id, $nilai, $status)
    {
        // Pastikan status yang diperbolehkan hanya 'masuk', 'verifikasi', 'tersalur', 'hilang'' di tabel ini.
        if (!in_array($status, ['masuk', 'verifikasi', 'tersalur', 'hilang'])) {
            return false;
        }

        $stmt = $this->conn->prepare("UPDATE tbantuanmasuk SET nilai = ?, status = ? WHERE id = ?");
        $nilai = number_format($nilai, 2, '.', '');
        $stmt->bind_param("dss", $nilai, $status, $id);
        
        $result = $stmt->execute();
        $stmt->close();
        return $result;
    }
    
    // --- FUNGSI Delete pada Bantuan Masuk ---
    
    public function hapusBantuanMasuk($id)
    {
        $stmt = $this->conn->prepare("DELETE FROM tbantuanmasuk WHERE id = ?");
        $stmt->bind_param("s", $id);
        $result = $stmt->execute();
        $stmt->close();
        return $result;
    }
    
    // --- FUNGSI SALUR/PINDAH DATA (Move/Update) ---
    
    public function salurkanBantuan($id, $statusSalur) 
    {
        // 1. Ambil data dari tbantuanmasuk
        $stmt_select = $this->conn->prepare("SELECT * FROM tbantuanmasuk WHERE id = ?");
        $stmt_select->bind_param("s", $id);
        $stmt_select->execute();
        $result_select = $stmt_select->get_result();
        $data = $result_select->fetch_assoc();
        $stmt_select->close();

        if (!$data) {
            return false; // Data tidak ditemukan
        }
        
        $this->conn->begin_transaction();
        $success = false;
        
        try {
            $stmt_insert = $this->conn->prepare("INSERT INTO tbantuansalur (id, donatur, isibantuan, tanggalmasuk, nilai, daerahsalur, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            $stmt_insert->bind_param("sssdsis", $data['id'], $data['donatur'], $data['isibantuan'], $data['tanggalmasuk'], $data['nilai'], $data['daerahsalur'], $statusSalur);
            
            if (!$stmt_insert->execute()) {
                throw new Exception("Gagal memasukkan ke tbantuansalur");
            }
            $stmt_insert->close();
            $stmt_delete = $this->conn->prepare("DELETE FROM tbantuanmasuk WHERE id = ?");
            $stmt_delete->bind_param("s", $id);
            
            if (!$stmt_delete->execute()) {
                throw new Exception("Gagal menghapus dari tbantuanmasuk");
            }
            $stmt_delete->close();
            $this->conn->commit();
            $success = true;
            
        } catch (Exception $e) {
            $this->conn->rollback();
            $success = false;
        }

        return $success;
    }
    
    // --- FUNGSI Update Status pada Bantuan Salur ---
    
    public function ubahStatusBantuanSalur($id, $status)
    {
        if (!in_array($status, ['masuk', 'verifikasi', 'tersalur', 'hilang'])) {
            return false;
        }
        
        $stmt = $this->conn->prepare("UPDATE tbantuansalur SET status = ? WHERE id = ?");
        $stmt->bind_param("ss", $status, $id);
        $result = $stmt->execute();
        $stmt->close();
        return $result;
    }
}
?>
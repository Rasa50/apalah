<?php
function renderBantuanSalurTable($dataSalur) {
    echo "<h2>Bantuan Diproses Penyaluran</h2>";
    
    if (empty($dataSalur)) {
        echo "<p>Tidak ada data bantuan.</p>";
        return;
    }

    echo '<table border="1" cellpadding="10" cellspacing="0">';
    echo '<tr><th>Id</th><th>Donatur</th><th>Tanggal Masuk</th><th>Nilai</th><th>Daerah Penyaluran</th><th>Deskripsi</th><th>Status</th><th>Aksi</th></tr>';
    
    foreach ($dataSalur as $data) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars($data['id']) . '</td>';
        echo '<td>' . htmlspecialchars($data['donatur']) . '</td>';
        echo '<td>' . htmlspecialchars($data['tanggalmasuk']) . '</td>';
        echo '<td>' . (int)($data['nilai']) . '</td>';
        echo '<td>' . htmlspecialchars($data['daerahsalur']) . '</td>';
        echo '<td>' . htmlspecialchars($data['isibantuan']) . '</td>';
        
        // Form untuk Ubah Status Salur (tersalur atau hilang)
        echo '<form method="POST" action="index.php">';
        echo '<input type="hidden" name="action" value="ubah_salur">';
        echo '<input type="hidden" name="ubah_salur_id" value="' . htmlspecialchars($data['id']) . '">';
        
        echo '<td>';
        echo '<select name="ubah_salur_status">';
        $statuses = ['masuk', 'verifikasi', 'tersalur', 'hilang'];
        foreach ($statuses as $s) {
            $selected = ($s == $data['status']) ? 'selected' : '';
            echo "<option value='{$s}' {$selected}>{$s}</option>";
        }
        echo '</select>';
        echo '</td>';
        echo '<td><button type="submit">Ubah</button></td>';
        echo '</form>';
        echo '</tr>';
    }
    echo '</table>';
}

function renderInputForm() {
    $today = date('Y-m-d');
    echo "<h2>Bantuan Kemanusiaan</h2>";
    echo '<form method="POST" action="index.php" style="margin-bottom: 30px; border: 1px solid #eee; padding: 20px;">
        <input type="hidden" name="action" value="simpan">
        <p>ID: <input type="text" name="id" required></p>
        <p>Donatur: <input type="text" name="donatur" required></p>
        <p>Isi Bantuan: <textarea name="isi_bantuan"></textarea></p>
        <p>Tanggal Masuk: <input type="date" name="tanggalmasuk" required value="' . $today . '"></p>
        <p>Nilai: <input type="number" step="0.01" name="nilai"></p>
        <p>Daerah Penyaluran: <input type="text" name="daerahsalur"></p>
        <button type="submit">Simpan</button>
    </form>';
}
?>

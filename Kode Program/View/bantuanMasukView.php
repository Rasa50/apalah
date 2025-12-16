<?php
function renderBantuanMasukTable($dataMasuk) {
    echo "<h2>Bantuan Masuk</h2>";
    
    if (empty($dataMasuk)) {
        echo "<p>Tidak ada data bantuan masuk.</p>";
        return;
    }

    echo '<table border="1" cellpadding="10" cellspacing="0">';
    echo '<tr><th>Id</th><th>Donatur</th><th>Tanggal Masuk</th><th>Nilai</th><th>Daerah Penyaluran</th><th>Isi Bantuan</th><th>Status</th><th colspan="3">Aksi</th></tr>';
    
    foreach ($dataMasuk as $data) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars($data['id']) . '</td>';
        echo '<td>' . htmlspecialchars($data['donatur']) . '</td>';
        echo '<td>' . htmlspecialchars($data['tanggalmasuk']) . '</td>';
        
        // Form untuk Ubah Nilai dan Status
        echo '<form method="POST" action="index.php">';
        echo '<input type="hidden" name="action" value="ubah_masuk">';
        echo '<input type="hidden" name="ubah_id" value="' . htmlspecialchars($data['id']) . '">';
        echo '<td><input type="number" step="0.01" name="ubah_nilai" value="' . (int)($data['nilai']) . '"></td>';
        echo '<td>' . htmlspecialchars($data['daerahsalur']) . '</td>';
        echo '<td>' . htmlspecialchars($data['isibantuan']) . '</td>';
        echo '<td>';
        echo '<select name="ubah_status">';
        $statuses = ['masuk', 'verifikasi', 'tersalur', 'hilang'];
        foreach ($statuses as $s) {
            $selected = ($s == $data['status']) ? 'selected' : '';
            echo "<option value='{$s}' {$selected}>{$s}</option>";
        }
        echo '</select>';
        echo '</td>';
        echo '<td><button type="submit">Ubah</button></td>';
        echo '</form>';
        
        // Form untuk Hapus
        echo '<form method="POST" action="index.php" onsubmit="return confirm(\'Yakin hapus data ini?\');">';
        echo '<input type="hidden" name="action" value="hapus_masuk">';
        echo '<input type="hidden" name="hapus_id" value="' . htmlspecialchars($data['id']) . '">';
        echo '<td><button type="submit">Hapus</button></td>';
        echo '</form>';

        // Form untuk Salur
        echo '<form method="POST" action="index.php">';
        echo '<input type="hidden" name="action" value="salur">';
        echo '<input type="hidden" name="salur_id" value="' . htmlspecialchars($data['id']) . '">';
        echo '<input type="hidden" name="salur_status" value="tersalur">';
        echo '<td><button type="submit">Salur</button></td>';
        echo '</form>';
        
        echo '</tr>';
    }
    echo '</table>';
}
?>
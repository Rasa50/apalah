<?php
// Pastikan path ke file lain benar
require_once 'presenter/interface.php'; 
require_once 'bantuanMasukView.php';
require_once 'bantuanSalurView.php';

class IndexView implements IBantuanView {
    private $message = [];

    public function displayMessage($message, $type) {
        $this->message[] = ['text' => $message, 'type' => $type];
    }
    
    public function renderPage($data) {
        ?>
        <!DOCTYPE html>
        <html lang="id">
        <head>
            <meta charset="UTF-8">
            <title>Bantuan Kemanusiaan</title>
            <link rel="stylesheet" href="style.css"> 
        </head>
        <body>
            <div class="container">
                <h1>Manajemen Bantuan Kemanusiaan</h1>
                <?php
                if (!empty($this->message)) {
                    foreach ($this->message as $msg) {
                        echo "<div class='message {$msg['type']}'>{$msg['text']}</div>";
                    }
                }
                
                // 1. Tampilkan Formulir Input
                renderInputForm();
                
                // 2. Tampilkan Tabel Bantuan Masuk
                renderBantuanMasukTable($data['masuk']);
                
                // 3. Tampilkan Tabel Bantuan Salur
                renderBantuanSalurTable($data['salur']);
                ?>
            </div>
        </body>
        </html>
        <?php
    }
}
?>
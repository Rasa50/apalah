<?php
// Mengimpor file utama View dan Presenter
require_once 'view/indexView.php';
require_once 'presenter/bantuanPresenter.php';

// 1. Inisialisasi View
$view = new IndexView();

// 2. Inisialisasi Presenter (Presenter memegang View)
$presenter = new BantuanPresenter($view);

// 3. Tangani Aksi POST (Simpan, Ubah, Salur, Hapus)
// Note: handleActions akan melakukan redirect/exit setelah selesai
$presenter->handleActions();

// 4. Ambil Data Terbaru
$allData = $presenter->getSemuaData();

// 5. Render Halaman Utama
$view->renderPage($allData);
?>
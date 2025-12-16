<?php
// Interface untuk View
interface IBantuanView {
    public function displayMessage($message, $type);
    public function renderPage($data);
}

// Interface untuk Presenter
interface IBantuanPresenter {
    public function handleActions();
    public function handleInputBantuan($data);
    public function handleUbahMasuk($id, $nilai, $status);
    public function handleHapusMasuk($id);
    public function handleSalur($id, $statusSalur);
    public function handleUbahSalur($id, $status);
    public function getSemuaData();
}
?>
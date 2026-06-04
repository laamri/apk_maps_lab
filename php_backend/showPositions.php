<?php
header('Content-Type: application/json; charset=utf-8');

include_once __DIR__ . '/service/PositionService.php';

try {
    $ps = new PositionService();
    echo json_encode(["positions" => $ps->getAll()]);
} catch(Exception $e) {
    echo json_encode(["positions" => [], "error" => $e->getMessage()]);
}
?>
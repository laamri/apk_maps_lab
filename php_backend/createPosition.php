<?php
header('Content-Type: application/json; charset=utf-8');

include_once __DIR__ . '/service/PositionService.php';
include_once __DIR__ . '/classe/Position.php';

$latitude = $_POST['latitude'] ?? null;
$longitude = $_POST['longitude'] ?? null;
$date = $_POST['date'] ?? null;
$imei = $_POST['imei'] ?? null;

if ($latitude === null || $longitude === null || $date === null || $imei === null) {
    echo json_encode(["status" => "Error", "message" => "Missing parameters"]);
    exit;
}

try {
    $ss = new PositionService();
    $ss->create(new Position(null, $latitude, $longitude, $date, $imei));
    echo json_encode(["status" => "OK"]);
} catch(Exception $e) {
    echo json_encode(["status" => "Error", "message" => $e->getMessage()]);
}
?>
<?php
include_once __DIR__ . '/../dao/IDao.php';
include_once __DIR__ . '/../classe/Position.php';
include_once __DIR__ . '/../connexion/Connexion.php';

class PositionService implements IDao {
    private $connexion;

    public function __construct() {
        $this->connexion = new Connexion();
    }

    public function create($position) {
        $sql = "INSERT INTO position(latitude, longitude, date, imei) VALUES (?, ?, ?, ?)";
        $stmt = $this->connexion->getConn()->prepare($sql);
        $stmt->execute([
            $position->getLatitude(),
            $position->getLongitude(),
            $position->getDate(),
            $position->getImei()
        ]);
        return true;
    }

    public function getAll() {
        $query = "select * from position";
        $req = $this->connexion->getConn()->prepare($query);
        $req->execute();
        return $req->fetchAll(PDO::FETCH_ASSOC);
    }

    public function update($obj) {}
    public function delete($obj) {}
    public function getById($id) {}
}
?>
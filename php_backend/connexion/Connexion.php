<?php
class Connexion {
    private $conn;

    public function __construct() {
        $host = 'localhost';
        $db   = 'localisation';
        $user = 'root';
        $pw   = '';
        try {
            $this->conn = new PDO("mysql:host=$host;dbname=$db;charset=utf8", $user, $pw);
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            die("Erreur de connexion : " . $e->getMessage());
        }
    }

    public function getConn() {
        return $this->conn;
    }
}
?>
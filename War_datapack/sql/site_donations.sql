DROP TABLE IF EXISTS `site_donations`;
CREATE TABLE `site_donations` (
  `protocolo` int(10) NOT NULL AUTO_INCREMENT,
  `account` varchar(30) NOT NULL,
  `personagem` int(11) DEFAULT NULL,
  `quant_coins` int(10) NOT NULL,
  `coins_bonus` int(10) NOT NULL DEFAULT 0,
  `coins_entregues` int(10) NOT NULL DEFAULT 0,
  `valor` decimal(11,2) NOT NULL,
  `price` decimal(11,2) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `metodo_pgto` varchar(50) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `status_real` varchar(40) DEFAULT NULL,
  `data` int(11) NOT NULL,
  `ultima_alteracao` int(11) DEFAULT NULL,
  `transaction_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`protocolo`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10061 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
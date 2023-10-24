DROP TABLE IF EXISTS `site_balance`;
CREATE TABLE `site_balance` (
  `account` varchar(16) NOT NULL,
  `saldo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
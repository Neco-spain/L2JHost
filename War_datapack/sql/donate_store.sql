DROP TABLE IF EXISTS `donate_store`;
CREATE TABLE `donate_store` (
  `obj_Id` int(10) unsigned NOT NULL DEFAULT 0,
  `order_number` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `char_name` varchar(35) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pais` varchar(255) NOT NULL,
  `ddd` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`order_number`)
) ENGINE=InnoDB AUTO_INCREMENT=268481056 DEFAULT CHARSET=latin1;
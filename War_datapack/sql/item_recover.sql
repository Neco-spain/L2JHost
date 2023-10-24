DROP TABLE IF EXISTS `item_recover`;
CREATE TABLE `item_recover` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `object_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `enchant_level` int(11) NOT NULL,
  `item_count` int(11) NOT NULL DEFAULT 1,
  `item_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `object_id_index` (`object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=latin1;

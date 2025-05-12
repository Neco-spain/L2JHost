/*
MySQL Data Transfer
Source Host: localhost
Source Database: l2jdev
Target Host: localhost
Target Database: l2jdev
Date: 05/08/2023 17:26:17
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for balance
-- ----------------------------
DROP TABLE IF EXISTS `balance`;
CREATE TABLE `balance` (
  `from_class` smallint(6) NOT NULL DEFAULT 0,
  `to_class` smallint(6) NOT NULL DEFAULT 0,
  `mod_val` smallint(6) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `balance` VALUES ('109', '-1', '-20');
INSERT INTO `balance` VALUES ('109', '110', '0');
INSERT INTO `balance` VALUES ('110', '109', '0');
INSERT INTO `balance` VALUES ('103', '110', '15');
INSERT INTO `balance` VALUES ('110', '-1', '0');
INSERT INTO `balance` VALUES ('109', '103', '-3');
INSERT INTO `balance` VALUES ('109', '97', '0');
INSERT INTO `balance` VALUES ('92', '-1', '0');
INSERT INTO `balance` VALUES ('103', '-1', '-10');
INSERT INTO `balance` VALUES ('109', '94', '-5');
INSERT INTO `balance` VALUES ('92', '94', '-6');
INSERT INTO `balance` VALUES ('94', '-1', '0');
INSERT INTO `balance` VALUES ('102', '-1', '-25');
INSERT INTO `balance` VALUES ('94', '102', '-10');
INSERT INTO `balance` VALUES ('106', '-1', '-20');
INSERT INTO `balance` VALUES ('112', '-1', '-20');
INSERT INTO `balance` VALUES ('99', '-1', '-20');
INSERT INTO `balance` VALUES ('96', '-1', '-20');
INSERT INTO `balance` VALUES ('93', '-1', '-10');
INSERT INTO `balance` VALUES ('105', '-1', '0');
INSERT INTO `balance` VALUES ('113', '-1', '-30');
INSERT INTO `balance` VALUES ('116', '-1', '-20');
INSERT INTO `balance` VALUES ('107', '-1', '-20');
INSERT INTO `balance` VALUES ('118', '-1', '-20');
INSERT INTO `balance` VALUES ('100', '-1', '-20');
INSERT INTO `balance` VALUES ('88', '-1', '-25');
INSERT INTO `balance` VALUES ('91', '-1', '-20');
INSERT INTO `balance` VALUES ('97', '-1', '-20');
INSERT INTO `balance` VALUES ('114', '-1', '-30');
INSERT INTO `balance` VALUES ('108', '-1', '-10');
INSERT INTO `balance` VALUES ('111', '-1', '-20');
INSERT INTO `balance` VALUES ('117', '-1', '-20');
INSERT INTO `balance` VALUES ('101', '-1', '-35');
INSERT INTO `balance` VALUES ('104', '-1', '-20');
INSERT INTO `balance` VALUES ('89', '-1', '-25');
INSERT INTO `balance` VALUES ('98', '-1', '-30');
INSERT INTO `balance` VALUES ('115', '-1', '-20');
INSERT INTO `balance` VALUES ('110', '88', '-20');
INSERT INTO `balance` VALUES ('109', '93', '-20');
INSERT INTO `balance` VALUES ('103', '94', '12');
INSERT INTO `balance` VALUES ('94', '103', '10');
INSERT INTO `balance` VALUES ('110', '103', '5');
INSERT INTO `balance` VALUES ('110', '94', '5');
INSERT INTO `balance` VALUES ('94', '110', '4');
INSERT INTO `balance` VALUES ('95', '-1', '0');
INSERT INTO `balance` VALUES ('95', '108', '8');
INSERT INTO `balance` VALUES ('95', '97', '50');
INSERT INTO `balance` VALUES ('94', '97', '25');
INSERT INTO `balance` VALUES ('103', '97', '2');
INSERT INTO `balance` VALUES ('109', '92', '0');
INSERT INTO `balance` VALUES ('92', '109', '0');
INSERT INTO `balance` VALUES ('109', '102', '-20');
INSERT INTO `balance` VALUES ('102', '109', '-20');
INSERT INTO `balance` VALUES ('103', '90', '15');
INSERT INTO `balance` VALUES ('103', '106', '15');
INSERT INTO `balance` VALUES ('103', '99', '25');
INSERT INTO `balance` VALUES ('94', '106', '15');
INSERT INTO `balance` VALUES ('94', '90', '15');
INSERT INTO `balance` VALUES ('94', '99', '15');
INSERT INTO `balance` VALUES ('95', '106', '15');
INSERT INTO `balance` VALUES ('95', '90', '15');
INSERT INTO `balance` VALUES ('95', '99', '15');
INSERT INTO `balance` VALUES ('109', '106', '10');
INSERT INTO `balance` VALUES ('109', '90', '-20');
INSERT INTO `balance` VALUES ('109', '99', '15');
INSERT INTO `balance` VALUES ('109', '95', '5');
INSERT INTO `balance` VALUES ('94', '92', '-4');
INSERT INTO `balance` VALUES ('92', '103', '-4');
INSERT INTO `balance` VALUES ('103', '92', '3');
INSERT INTO `balance` VALUES ('92', '97', '15');
INSERT INTO `balance` VALUES ('103', '89', '0');
INSERT INTO `balance` VALUES ('88', '98', '10');
INSERT INTO `balance` VALUES ('90', '95', '-10');

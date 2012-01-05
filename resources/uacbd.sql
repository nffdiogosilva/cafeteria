/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50509
 Source Host           : localhost
 Source Database       : uacbd

 Target Server Type    : MySQL
 Target Server Version : 50509
 File Encoding         : utf-8

 Date: 01/05/2012 17:44:44 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `Admins`
-- ----------------------------
DROP TABLE IF EXISTS `Admins`;
CREATE TABLE `Admins` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) NOT NULL,
  `username` varchar(10) NOT NULL,
  `password` varchar(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `Admins`
-- ----------------------------
BEGIN;
INSERT INTO `Admins` VALUES ('1', 'Administrador', 'superadmin', '12345678');
COMMIT;

-- ----------------------------
--  Table structure for `Alunos`
-- ----------------------------
DROP TABLE IF EXISTS `Alunos`;
CREATE TABLE `Alunos` (
  `id` int(10) unsigned NOT NULL,
  `nome` varchar(60) NOT NULL,
  `morada` int(10) unsigned NOT NULL,
  `telefone` char(9) NOT NULL,
  `email` varchar(30) NOT NULL,
  `bolsa` tinyint(1) NOT NULL DEFAULT '0',
  `curso` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_alunos_curso` (`curso`),
  KEY `fk_alunos_morada` (`morada`),
  CONSTRAINT `fk_alunos_curso` FOREIGN KEY (`curso`) REFERENCES `Cursos` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_alunos_morada` FOREIGN KEY (`morada`) REFERENCES `Moradas` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `Cursos`
-- ----------------------------
DROP TABLE IF EXISTS `Cursos`;
CREATE TABLE `Cursos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;

-- ----------------------------
--  Records of `Cursos`
-- ----------------------------
BEGIN;
INSERT INTO `Cursos` VALUES ('13', 'Arquitectura (Preparatórios)'), ('1', 'Biologia'), ('2', 'Ciclo Básico de Medicina'), ('4', 'Ciências Agrárias'), ('3', 'Ciências Biológicas e da Saúde'), ('14', 'Ciências da Engenharia Civil'), ('5', 'Ciências da Nutrição (Preparatórios)'), ('15', 'Ciências de Engenharia - Engenharia Civil, Engenharia Mecâni'), ('6', 'Ciências Farmacêuticas (Preparatórios)'), ('27', 'Comunicação Social e Cultura'), ('18', 'Economia'), ('11', 'Educação Básica'), ('7', 'Energias Renováveis'), ('31', 'Enfermagem'), ('16', 'Engenharia Civil'), ('8', 'Engenharia e Gestão do Ambiente'), ('29', 'Engenharia Electrotécnica e de Computadores'), ('17', 'Engenharia Mecânica'), ('21', 'Estudos Europeus e Política Internacional'), ('22', 'Filosofia e Cultura Portuguesa'), ('19', 'Gestão'), ('9', 'Guias da Natureza'), ('23', 'História'), ('30', 'Informática - Redes e Multimédia'), ('10', 'Medicina Veterinária (Preparatórios)'), ('24', 'Património Cultural'), ('12', 'Psicologia'), ('28', 'Relações Públicas e Comunicação'), ('25', 'Serviço Cultural'), ('26', 'Sociologia'), ('20', 'Turismo');
COMMIT;

-- ----------------------------
--  Table structure for `Moradas`
-- ----------------------------
DROP TABLE IF EXISTS `Moradas`;
CREATE TABLE `Moradas` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rua` varchar(60) NOT NULL,
  `nr` varchar(15) NOT NULL,
  `cod_postal` char(8) NOT NULL,
  `localidade` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
--  Table structure for `histAlunos`
-- ----------------------------
DROP TABLE IF EXISTS `histAlunos`;
CREATE TABLE `histAlunos` (
  `id` int(10) unsigned NOT NULL,
  `nome` varchar(60) NOT NULL,
  `morada` int(10) unsigned NOT NULL,
  `telefone` char(9) NOT NULL,
  `email` varchar(30) NOT NULL,
  `bolsa` tinyint(1) NOT NULL DEFAULT '0',
  `curso` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_alunos_curso` (`curso`),
  KEY `fk_alunos_morada` (`morada`),
  CONSTRAINT `fk_alunos_curso0` FOREIGN KEY (`curso`) REFERENCES `Cursos` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_alunos_morada0` FOREIGN KEY (`morada`) REFERENCES `Moradas` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;

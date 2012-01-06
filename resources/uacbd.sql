SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `uacbd` ;
CREATE SCHEMA IF NOT EXISTS `uacbd` DEFAULT CHARACTER SET latin1 ;
USE `uacbd` ;

-- -----------------------------------------------------
-- Table `uacbd`.`Moradas`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Moradas` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `rua` VARCHAR(60) NOT NULL ,
  `nr` VARCHAR(15) NOT NULL ,
  `cod_postal` CHAR(8) NOT NULL ,
  `localidade` VARCHAR(30) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `uacbd`.`Admins`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Admins` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `nome` VARCHAR(60) NOT NULL ,
  `username` VARCHAR(10) NOT NULL ,
  `password` VARCHAR(15) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `uacbd`.`Cursos`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Cursos` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `nome` VARCHAR(70) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `nome` (`nome` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `uacbd`.`Alunos`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Alunos` (
  `id` INT UNSIGNED NOT NULL ,
  `nome` VARCHAR(60) NOT NULL ,
  `morada` INT UNSIGNED NOT NULL ,
  `telefone` CHAR(9) NOT NULL ,
  `email` VARCHAR(30) NOT NULL ,
  `bolsa` TINYINT(1) NOT NULL DEFAULT 0 ,
  `curso` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) ,
  INDEX `fk_alunos_curso` (`curso` ASC) ,
  INDEX `fk_alunos_morada` (`morada` ASC) ,
  CONSTRAINT `fk_alunos_curso`
    FOREIGN KEY (`curso` )
    REFERENCES `uacbd`.`Cursos` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_alunos_morada`
    FOREIGN KEY (`morada` )
    REFERENCES `uacbd`.`Moradas` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `uacbd`.`histAlunos`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`histAlunos` (
  `id` INT UNSIGNED NOT NULL ,
  `nome` VARCHAR(60) NOT NULL ,
  `morada` INT UNSIGNED NOT NULL ,
  `telefone` CHAR(9) NOT NULL ,
  `email` VARCHAR(30) NOT NULL ,
  `bolsa` TINYINT(1) NOT NULL DEFAULT 0 ,
  `curso` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) ,
  INDEX `fk_histalunos_curso` (`curso` ASC) ,
  INDEX `fk_histalunos_morada` (`morada` ASC) ,
  CONSTRAINT `fk_histalunos_curso`
    FOREIGN KEY (`curso` )
    REFERENCES `uacbd`.`Cursos` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_histalunos_morada`
    FOREIGN KEY (`morada` )
    REFERENCES `uacbd`.`Moradas` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- ----------------------------
--  Records of `Cursos`
-- ----------------------------
BEGIN;
INSERT INTO `uacbd`.`Cursos` (`nome`) VALUES 
('Arquitectura (Preparatórios)'),
('Biologia'),
('Ciclo Básico de Medicina'),
('Ciências Agrárias'),
('Ciências Biológicas e da Saúde'),
('Ciências da Engenharia Civil'),
('Ciências da Nutrição (Preparatórios)'),
('Ciências de Engenharia - Engenharia Civil, Engenharia Mecânica'),
('Ciências Farmacêuticas (Preparatórios)'),
('Comunicação Social e Cultura'),
('Economia'),
('Educação Básica'),
('Energias Renováveis'),
('Enfermagem'),
('Engenharia Civil'),
('Engenharia e Gestão do Ambiente'),
('Engenharia Electrotécnica e de Computadores'),
('Engenharia Mecânica'),
('Estudos Europeus e Política Internacional'),
('Filosofia e Cultura Portuguesa'),
('Gestão'),
('Guias da Natureza'),
('História'),
('Informática - Redes e Multimédia'),
('Medicina Veterinária (Preparatórios)'),
('Património Cultural'),
('Psicologia'),
('Relações Públicas e Comunicação'),
('Serviço Cultural'),
('Sociologia'),
('Turismo');
COMMIT;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `uacbd` ;
CREATE SCHEMA IF NOT EXISTS `uacbd` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `uacbd` ;

-- -----------------------------------------------------
-- Table `uacbd`.`Moradas`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Moradas` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `rua` VARCHAR(60) NOT NULL ,
  `nr` VARCHAR(15) NOT NULL ,
  `cod_postal` CHAR(8) NOT NULL ,
  `localidade` VARCHAR(30) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `uacbd`.`Admins`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Admins` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `nome` VARCHAR(60) NOT NULL ,
  `username` VARCHAR(10) NOT NULL ,
  `password` VARCHAR(15) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `uacbd`.`Cursos`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Cursos` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `nome` VARCHAR(60) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `nome` (`nome` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `uacbd`.`Alunos`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `uacbd`.`Alunos` (
  `id` INT NOT NULL ,
  `nome` VARCHAR(60) NOT NULL ,
  `morada` INT NOT NULL ,
  `telefone` CHAR(9) NULL ,
  `email` VARCHAR(30) NOT NULL ,
  `bolsa` TINYINT(1) NOT NULL DEFAULT 0 ,
  `curso` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) ,
  INDEX `fk_alunos_curso` (`curso` ASC) ,
  INDEX `fk_alunos_morada` (`morada` ASC) ,
  CONSTRAINT `fk_alunos_curso0`
    FOREIGN KEY (`curso` )
    REFERENCES `uacbd`.`Cursos` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_alunos_morada0`
    FOREIGN KEY (`morada` )
    REFERENCES `uacbd`.`Moradas` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

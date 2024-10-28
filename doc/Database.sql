-- MySQL Script generated by MySQL Workbench
-- Mon Oct 28 10:30:01 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema personal_finances
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema personal_finances
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `personal_finances` DEFAULT CHARACTER SET utf8 ;
USE `personal_finances` ;

-- -----------------------------------------------------
-- Table `personal_finances`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`user` (
  `uid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `password` VARCHAR(512) NOT NULL,
  `status` ENUM('Active', 'Inactive', 'Blocked') NOT NULL DEFAULT 'Active',
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_finances`.`accountType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`accountType` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_finances`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`account` (
  `idAccount` INT NOT NULL AUTO_INCREMENT,
  `uid` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `accountType_id` INT NOT NULL,
  `initialAmount` DOUBLE NULL,
  `updatedAmount` DOUBLE NULL,
  `createAt` DATETIME NULL DEFAULT NOW(),
  `status` ENUM('Active', 'Inactive') NULL DEFAULT 'Active',
  PRIMARY KEY (`idAccount`),
  INDEX `fk_Account_user_idx` (`uid` ASC) VISIBLE,
  INDEX `fk_account_accountType1_idx` (`accountType_id` ASC) VISIBLE,
  CONSTRAINT `fk_Account_user`
    FOREIGN KEY (`uid`)
    REFERENCES `personal_finances`.`user` (`uid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_account_accountType1`
    FOREIGN KEY (`accountType_id`)
    REFERENCES `personal_finances`.`accountType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_finances`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`category` (
  `idCategory` INT NOT NULL AUTO_INCREMENT,
  `user_uid` INT NOT NULL,
  `Name` VARCHAR(60) NOT NULL,
  `createAt` DATETIME NOT NULL DEFAULT NOW(),
  `categoryType` ENUM('Income', 'Expense') NOT NULL DEFAULT 'Expense',
  PRIMARY KEY (`idCategory`),
  INDEX `fk_category_user1_idx` (`user_uid` ASC) VISIBLE,
  CONSTRAINT `fk_category_user1`
    FOREIGN KEY (`user_uid`)
    REFERENCES `personal_finances`.`user` (`uid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_finances`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`transaction` (
  `idtransaction` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `value` DOUBLE NULL,
  `notes` TEXT NULL,
  `date` DATE NULL,
  `createAt` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`idtransaction`),
  INDEX `fk_transaction_account1_idx` (`account_id` ASC) VISIBLE,
  INDEX `fk_transaction_category1_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_account1`
    FOREIGN KEY (`account_id`)
    REFERENCES `personal_finances`.`account` (`idAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `personal_finances`.`category` (`idCategory`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_finances`.`categoryType`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`categoryType` (
  `idCategoryType` INT NOT NULL AUTO_INCREMENT,
  `categoryTypecol` VARCHAR(45) NULL,
  PRIMARY KEY (`idCategoryType`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `personal_finances`.`transfer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personal_finances`.`transfer` (
  `idtransfer` INT NOT NULL AUTO_INCREMENT,
  `fromIdAccount` INT NOT NULL,
  `toIdAcoount` INT NOT NULL,
  `value` DOUBLE NULL,
  `notes` TEXT NULL,
  `date` DATE NULL,
  `createAt` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`idtransfer`),
  INDEX `fk_transfer_account1_idx` (`fromIdAccount` ASC) VISIBLE,
  INDEX `fk_transfer_account2_idx` (`toIdAcoount` ASC) VISIBLE,
  CONSTRAINT `fk_transfer_account1`
    FOREIGN KEY (`fromIdAccount`)
    REFERENCES `personal_finances`.`account` (`idAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transfer_account2`
    FOREIGN KEY (`toIdAcoount`)
    REFERENCES `personal_finances`.`account` (`idAccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

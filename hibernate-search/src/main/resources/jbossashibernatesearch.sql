-- phpMyAdmin SQL Dump
-- version 3.5.8.1
-- http://www.phpmyadmin.net
--
-- Host: 127.2.16.2:3306
-- Generation Time: Jul 24, 2013 at 06:07 PM
-- Server version: 5.1.69
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `jbossashibernatesearch`
--

-- --------------------------------------------------------

--
-- Table structure for table `Feed`
--

CREATE TABLE IF NOT EXISTS `Feed` (
  `id` int(11) NOT NULL,
  `author` varchar(1000) DEFAULT NULL,
  `copyright` varchar(1000) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `link` varchar(1000) DEFAULT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FeedEntry`
--

CREATE TABLE IF NOT EXISTS `FeedEntry` (
  `feedEntryId` int(11) NOT NULL,
  `author` varchar(1000) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `feedId` int(11) NOT NULL,
  `title` varchar(1000) DEFAULT NULL,
  `uri` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`feedEntryId`),
  KEY `FK140725546932A588` (`feedId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `SEQUENCE_TABLE`
--

CREATE TABLE IF NOT EXISTS `SEQUENCE_TABLE` (
  `SEQ_NAME` varchar(255) NOT NULL,
  `SEQ_COUNT` int(11) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `SEQUENCE_TABLE`
--

INSERT INTO `SEQUENCE_TABLE` (`SEQ_NAME`, `SEQ_COUNT`) VALUES
('FEED_SEQ', 1),
('FEED_ENTRY_SEQ', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

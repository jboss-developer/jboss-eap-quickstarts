-- phpMyAdmin SQL Dump
-- version 3.5.8.1
-- http://www.phpmyadmin.net
--
-- Host: 127.2.16.2:3306
-- Generation Time: Jul 24, 2013 at 06:07 PM
-- Server version: 5.1.69
-- PHP Version: 5.3.3

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `jbossashibernatesearch`
--

--DROP TABLE IF EXISTS FeedEntry;
--DROP TABLE IF EXISTS Feed;
--DROP TABLE IF EXISTS SEQUENCE_TABLE;

-- --------------------------------------------------------

--
-- Table structure for table SEQUENCE_TABLE
--

--CREATE TABLE SEQUENCE_TABLE(
--  SEQ_NAME varchar(255) NOT NULL,
--  SEQ_COUNT int DEFAULT NULL,
--  PRIMARY KEY (SEQ_NAME)
--);

-- --------------------------------------------------------

--
-- Table structure for table `Feed`
--


--CREATE TABLE Feed (
--  id int NOT NULL,
--  author varchar(1000) DEFAULT NULL,
--  copyright varchar(1000) DEFAULT NULL,
--  description varchar(2000) DEFAULT NULL,
--  link varchar(1000) DEFAULT NULL,
--  title varchar(1000) DEFAULT NULL,
--  url varchar(500) DEFAULT NULL,
--  PRIMARY KEY (id)
--);

ALTER TABLE Feed ALTER COLUMN author TYPE varchar(1000);
ALTER TABLE Feed ALTER COLUMN copyright TYPE varchar(1000);
ALTER TABLE Feed ALTER COLUMN link TYPE varchar(1000);
ALTER TABLE Feed ALTER COLUMN description TYPE varchar(1000);
ALTER TABLE Feed ALTER COLUMN title TYPE varchar(1000);
ALTER TABLE Feed ALTER COLUMN url TYPE varchar(1000);

-- --------------------------------------------------------

--
-- Table structure for table FeedEntry
--

--CREATE TABLE FeedEntry (
--  feedEntryId int NOT NULL,
--  author varchar(1000) DEFAULT NULL,
--  description varchar(5000) DEFAULT NULL,
--  feedId int NOT NULL references Feed(id),
--  title varchar(1000) DEFAULT NULL,
--  uri varchar(500) DEFAULT NULL,
--  PRIMARY KEY (feedEntryId)
--);

ALTER TABLE FeedEntry ALTER COLUMN author TYPE varchar(1000);
ALTER TABLE FeedEntry ALTER COLUMN description TYPE varchar(5000);
ALTER TABLE FeedEntry ALTER COLUMN title TYPE varchar(1000);
ALTER TABLE FeedEntry ALTER COLUMN uri TYPE varchar(500);

--
-- Dumping data for table 'SEQUENCE_TABLE'
--

INSERT INTO SEQUENCE_TABLE (SEQ_NAME, SEQ_COUNT) VALUES ('FEED_SEQ', 7);
INSERT INTO SEQUENCE_TABLE (SEQ_NAME, SEQ_COUNT) VALUES ('FEED_ENTRY_SEQ', 1);


INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (1, NULL, 'All rights reserved. Users may download and print extracts of content from this website for their own personal and non-commercial use only. Republication or redistribution of Reuters content, including by framing or similar means, is expressly prohibited without the prior written consent of Reuters. Reuters and the Reuters sphere logo are registered trademarks or trademarks of the Reuters group of companies around the world. © Reuters 2013', 'Reuters.com is your source for breaking news, business, financial and investing news, including personal finance and stocks. Reuters is the leading global provider of news, financial information and technology solutions to the world''s media, financial institutions, businesses and individuals.', 'http://www.reuters.com', 'Reuters: World News', 'http://feeds.reuters.com/Reuters/worldNews');
INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (2, NULL, 'All rights reserved. Users may download and print extracts of content from this website for their own personal and non-commercial use only. Republication or redistribution of Reuters content, including by framing or similar means, is expressly prohibited without the prior written consent of Reuters. Reuters and the Reuters sphere logo are registered trademarks or trademarks of the Reuters group of companies around the world. © Reuters 2013', 'Reuters.com is your source for breaking news, business, financial and investing news, including personal finance and stocks. Reuters is the leading global provider of news, financial information and technology solutions to the world''s media, financial institutions, businesses and individuals.', 'http://www.reuters.com', 'Reuters: Sports News', 'http://feeds.reuters.com/reuters/sportsNews');
INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (3, NULL, 'Copyright: (C) British Broadcasting Corporation, see http://news.bbc.co.uk/2/hi/help/rss/4498287.stm for terms and conditions of reuse.', 'The latest stories from the Home section of the BBC News web site.', 'http://www.bbc.co.uk/news/#sa-ns_mchannel=rss&ns_source=PublicRSS20-sa', 'BBC News - Home', 'http://feeds.bbci.co.uk/news/rss.xml');
INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (4, NULL, '(c)Cricinfo', 'Visit Cricinfo.com for up-to-the-minute cricket news, breaking cricket news, live cricket commentary, ball-by-ball commentary, cricket video, cricket audio and features.', 'http://www.espncricinfo.com', 'Cricket news from ESPN Cricinfo.com', 'http://www.espncricinfo.com/rss/content/story/feeds/0.xml');
INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (5, NULL, NULL, 'Latest cricket news from around the cricketing world', 'http://sports.ndtv.com', 'Cricket - News | NDTVSports.com', 'http://feeds.feedburner.com/ndtv/qJNd');
INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (6, NULL, NULL, 'Latest cricket news from around the cricketing world', 'http://sports.ndtv.com', 'Cricket - News | NDTVSports.com', 'http://feeds.feedburner.com/ndtv/qJNd?format=xml');
INSERT INTO feed (id, author, copyright, description, link, title, url) VALUES (7, NULL, NULL, 'The Latest Soccer News from around the globe.', 'http://www.soccernews.com', 'SoccerNews.com - The Latest Soccer & Transfer News ', 'http://feeds.feedburner.com/soccernewsfeed?format=xml');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

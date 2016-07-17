--
-- Database: `onionCrawler`
--

-- --------------------------------------------------------

--
-- Table structure for table `bannedDomains`
--

CREATE TABLE IF NOT EXISTS `bannedDomains` (
  `hostMd5Sum` int(11) NOT NULL,
  `added` datetime NOT NULL,
  `comment` text COLLATE utf8_bin NOT NULL,
  UNIQUE KEY `hostMd5Sum` (`hostMd5Sum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `onionHosts`
--

CREATE TABLE IF NOT EXISTS `onionHosts` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(24) COLLATE utf8_bin NOT NULL,
  `lastChecked` datetime NOT NULL,
  `lastOnline` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `host` (`host`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `robotsTxt`
--

CREATE TABLE IF NOT EXISTS `robotsTxt` (
  `host` varchar(255) CHARACTER SET utf8 NOT NULL,
  `port` int(5) unsigned NOT NULL,
  `content` text CHARACTER SET utf8,
  `updated` datetime NOT NULL,
  UNIQUE KEY `host` (`host`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `toCrawl`
--

CREATE TABLE IF NOT EXISTS `toCrawl` (
  `URL` varchar(255) COLLATE utf8_bin NOT NULL,
  `lastAction` datetime DEFAULT NULL,
  `attempts` int(3) unsigned NOT NULL,
  UNIQUE KEY `URL` (`URL`),
  KEY `lastAction` (`lastAction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;